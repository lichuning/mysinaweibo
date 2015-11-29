package com.example.sinav1.fragment;

import com.example.sinav1.MainActivity;
import com.example.sinav1.R;
import com.example.sinav1.bean.SlidingMenuItem;
import com.example.sinav1.bean.User;
import com.example.sinav1.util.AccessTokenKeeper;
import com.example.sinav1.util.ImageOptions;
import com.example.sinav1.util.JsonParse;
import com.example.sinav1.util.WeiboHttpUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SlidingFragment extends Fragment implements OnItemClickListener {

	private ListView listView;
	private ImageView imgHeader;
	private TextView textUsername;
	private TextView textLocation;
	private String token;

	private SlidingMenuItem[] data = new SlidingMenuItem[] { new SlidingMenuItem("��ҳ", R.drawable.slidingmenu_home),
			new SlidingMenuItem("���Ż���", R.drawable.slidingmenu_hot),
			new SlidingMenuItem("����", R.drawable.slidingmenu_search),
			new SlidingMenuItem("�ݸ�", R.drawable.slidingmenu_sending_queue),
			new SlidingMenuItem("����", R.drawable.slidingmenu_theme),
			new SlidingMenuItem("����", R.drawable.slidingmenu_settings) };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.sliding_fragment, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		listView = (ListView) view.findViewById(R.id.list_view);

		imgHeader = (ImageView) view.findViewById(R.id.current_user_header);
		textUsername = (TextView) view.findViewById(R.id.current_user_name);
		textLocation = (TextView) view.findViewById(R.id.current_user_location);

		// ��ȡ����
		token = AccessTokenKeeper.readAccessToken(getActivity()).getToken();
		// ��ȡ��ǰ��¼�û�
		try {
			new GetUserTask().execute("https://api.weibo.com/2/account/get_uid.json?access_token=" + token);
		} catch (Exception e) {
			Toast.makeText(getActivity(), "���粻����!", Toast.LENGTH_LONG).show();
		}

		BaseAdapter adapter = new BaseAdapter() {

			@Override
			public View getView(int pos, View view, ViewGroup arg2) {

				// ���ز߻��˵�Item��ʽ
				view = View.inflate(getActivity(), R.layout.sliding_list_item, null);
				// ����������
				ImageView img = (ImageView) view.findViewById(R.id.sliding_item_img);
				TextView title = (TextView) view.findViewById(R.id.sliding_item_title);
				// �����������
				img.setImageBitmap(BitmapFactory.decodeResource(getResources(), data[pos].getImageIcon()));
				title.setText(data[pos].getTitle());

				return view;
			}

			@Override
			public long getItemId(int arg0) {
				return 0;
			}

			@Override
			public Object getItem(int arg0) {
				return null;
			}

			@Override
			public int getCount() {
				return data.length;
			}
		};
		// ����������
		listView.setAdapter(adapter);
		// ע�����
		listView.setOnItemClickListener(this);
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {

		if (getActivity() instanceof MainActivity) {
			MainActivity activity = (MainActivity) getActivity();
			activity.changeFragment(pos);
		}
	}

	/*
	 * ���첽��������ȡ��ǰ��¼�û�����Ϣ
	 */
	class GetUserTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... paramas) {

			return WeiboHttpUtils.sinaHttpGet(paramas[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (result == null) {
//				Toast.makeText(getActivity(), "����ʧ��", Toast.LENGTH_SHORT).show();

			} else {
				String uid = JsonParse.parseID(result);

				new AsyncTask<String, Void, String>() {
					@Override
					protected String doInBackground(String... paramas) {

						return WeiboHttpUtils.sinaHttpGet(paramas[0]);
					}

					@Override
					protected void onPostExecute(String result) {
						// TODO Auto-generated method stub
						if(result!=null){
							User user=JsonParse.parseUser(result);
							textUsername.setText(user.getUsername());
							textLocation.setText(user.getLocation());
							ImageLoader.getInstance().displayImage(user.getProfileImageURL(), imgHeader,ImageOptions.getInstance().getOptions());
						}
					}

				}.execute("https://api.weibo.com/2/users/show.json?access_token=" + token + "&uid=" + uid);
			}
		}

	}
}
