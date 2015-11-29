package com.example.sinav1.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.sinav1.R;
import com.example.sinav1.bean.User;
import com.example.sinav1.bean.WeiboInfo;
import com.example.sinav1.db.SinaDB;
import com.example.sinav1.util.AccessTokenKeeper;
import com.example.sinav1.util.Constants;
import com.example.sinav1.util.ImageOptions;
import com.example.sinav1.util.JsonParse;
import com.example.sinav1.util.WeiboHttpUtils;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyPageFragment extends Fragment {

	private PullToRefreshListView listView;
	private List<WeiboInfo> dataList = new ArrayList<WeiboInfo>();
	private TextView username;
	private TextView weiboText;
	private TextView publishTime;
	private TextView weiboRes;
	private ImageView header;
	private ImageView pictrue;
	private MyAdapter adapter;
	private int currentPage = 1 ;

	private static MyPageFragment myPageFragment = new MyPageFragment();

	private MyPageFragment() {
	}

	public static MyPageFragment getInstance() {
		return myPageFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(R.layout.mypage_fragment, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// ��ȡListView����
		listView = (PullToRefreshListView) view.findViewById(R.id.listview_mypage);
		// ����ģʽΪboth:�ȿ��������ֿ�������
		listView.setMode(Mode.BOTH);
		// ��������������
		adapter = new MyAdapter();
		// ע������ˢ���¼�����
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				// Update the LastUpdatedLabel
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// ���ø÷�������΢����ҳ
				updateWeibo();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("���ظ���");
				loadMore();
			}

		});

		ListView actualListView = listView.getRefreshableView();
		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);
		// ��listview���������
		actualListView.setAdapter(adapter);

		// ��ʼ����ҳ
		init();
		

	}
	/*
	 * �÷���������ʼ���ҵ���ҳ����
	 * */
	private void init() {
		// �ȴ����ݿ�����json����,������ݿ�û��������ͨ�������ȡ����
		String content=SinaDB.getInstance(getActivity()).query();
		if(content==null){
			updateWeibo();
			
		}else{
			dataList=JsonParse.parse(content);
			adapter.notifyDataSetChanged();
		}
		
	}

	/*
	 * ������
	 * */
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup arg2) {

			if (convertView == null) {
				// ���벼���ļ�
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.index_listview_item, null);
			}
			username = (TextView) convertView.findViewById(R.id.tv_username);
			weiboText = (TextView) convertView.findViewById(R.id.tv_text);
			weiboRes = (TextView) convertView.findViewById(R.id.tv_resource);
			header = (ImageView) convertView.findViewById(R.id.iv_header);
			publishTime=(TextView) convertView.findViewById(R.id.tv_time);
			pictrue= (ImageView) convertView.findViewById(R.id.iv_showpic);

			username.setText(dataList.get(pos).getUser().getUsername());
			weiboText.setText(dataList.get(pos).getText());
			weiboRes.setText(dataList.get(pos).getSource());
			publishTime.setText(dataList.get(pos).getCreated_at());
			//  ����ͷ��
			ImageLoader.getInstance().displayImage(dataList.get(pos).getUser().getProfileImageURL(), header,ImageOptions.getInstance().getOptions());
			// ������ͼ
			String picUrl=null ;
			if((picUrl=dataList.get(pos).getThumbnail_pic())!=null){
				System.out.println("url=="+picUrl);
				ImageLoader.getInstance().displayImage(picUrl, pictrue,ImageOptions.getInstance().getOptions());
				pictrue.setVisibility(View.VISIBLE);
			}
			return convertView;
		}

	}

	/*
	 * �����ҵ���ҳ
	 */
	public void updateWeibo() {
		// ͨ���첽�̴߳������л�ȡ����
		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... arg0) {
				String token = AccessTokenKeeper.readAccessToken(getActivity()).getToken();
				String urlStr = Constants.HOME_TIMELINE + token;
				return WeiboHttpUtils.sinaHttpGet(urlStr);
			}

			@Override
			protected void onPostExecute(String result) {
				if(result==null){
					Toast.makeText(getActivity(), "��ȡ�������ʧ��", Toast.LENGTH_SHORT).show();
				
				}else{
					
					// ����ձ�������,�ٽ����������ص�����json���ݴ浽���ݿ�
					SinaDB.getInstance(getActivity()).deleteAll();
					SinaDB.getInstance(getActivity()).save(result);
					
					// �����������ص����ݽ��н���
					List<WeiboInfo> tempList = JsonParse.parse(result);
					// dataList.addAll(0, tempList);
					dataList = tempList;
					// �������ݼ�
					adapter.notifyDataSetChanged();
					
					//
					Toast.makeText(getActivity(), "������" + tempList.size() + "��΢��", Toast.LENGTH_SHORT).show();
				}
				// ����ˢ�����
				listView.onRefreshComplete();
			}

		}.execute();

	}

	/*
	 *	������������ 
	 **/
	public void loadMore() {
		// ͨ���첽�̴߳������л�ȡ����
		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... arg0) {
				String token = AccessTokenKeeper.readAccessToken(getActivity()).getToken();
				String urlStr = Constants.HOME_TIMELINE + token+"&page="+(++currentPage);
				return WeiboHttpUtils.sinaHttpGet(urlStr);
			}

			@Override
			protected void onPostExecute(String result) {
				if(result==null){
					Toast.makeText(getActivity(), "��ȡ�������ʧ��", Toast.LENGTH_SHORT).show();
				
				}else{
					// �����������ص����ݽ��н���
					List<WeiboInfo> tempList = JsonParse.parse(result);
					 dataList.addAll(tempList);
					// �������ݼ�
					adapter.notifyDataSetChanged();
				}
				// �����������
				listView.onRefreshComplete();
			}

		}.execute();
	}

}
