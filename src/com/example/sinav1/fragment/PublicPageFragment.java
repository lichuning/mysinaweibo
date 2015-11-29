package com.example.sinav1.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.sinav1.R;
import com.example.sinav1.bean.WeiboInfo;
import com.example.sinav1.fragment.MyPageFragment.MyAdapter;
import com.example.sinav1.util.AccessTokenKeeper;
import com.example.sinav1.util.Constants;
import com.example.sinav1.util.ImageOptions;
import com.example.sinav1.util.JsonParse;
import com.example.sinav1.util.WeiboHttpUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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

public class PublicPageFragment extends Fragment {

	private PullToRefreshListView listView ;
	private List<WeiboInfo> dataList = new ArrayList<WeiboInfo>();
	private TextView username ;
	private TextView weiboText ;
	private TextView weiboRes ;
	private ImageView header ;
	private MyAdapter adapter ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.publicpage_fragment, null);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// ��ȡListView����
		listView=(PullToRefreshListView) view.findViewById(R.id.listview_public);
		// ��������������
		adapter=new MyAdapter();
		
		// Need to use the Actual ListView when registering for Context Menu
		ListView actualListView=listView.getRefreshableView();
		registerForContextMenu(actualListView);
		
		// ��listview���������
		actualListView.setAdapter(adapter);
		
		// ����ˢ���¼�����
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
			
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// ִ�и��첽�����΢��
				new UpdateAsync().execute();
			}
		});
		
//		new UpdateAsync().execute();
	}
	
	/*
	 *  ���첽���������¹�����ҳ΢����Ϣ
	 * */
	class UpdateAsync extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... arg0) {
			String token=AccessTokenKeeper.readAccessToken(getActivity()).getToken();
			String urlStr=Constants.PUBLIC_TIMELINE+token;
			return WeiboHttpUtils.sinaHttpGet(urlStr);
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result==null){
				Toast.makeText(getActivity(), "����ʧ��", Toast.LENGTH_SHORT).show();
			
			}else{
				// �����������ص����ݽ��н���
				dataList=JsonParse.parse(result);
				// �������ݼ�
				adapter.notifyDataSetChanged();
				// ����ˢ�����
				listView.onRefreshComplete();
			}
		}
		
	}
	
	class MyAdapter extends BaseAdapter{

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
		public View getView(int pos, View contentView, ViewGroup arg2) {
			if(contentView==null){
				// ���벼���ļ�
				contentView=LayoutInflater.from(getActivity()).inflate(R.layout.index_listview_item, null);
			}
			username=(TextView) contentView.findViewById(R.id.tv_username);
			weiboText=(TextView) contentView.findViewById(R.id.tv_text);
			weiboRes=(TextView) contentView.findViewById(R.id.tv_resource);
			header=(ImageView) contentView.findViewById(R.id.iv_header);
			
			username.setText(dataList.get(pos).getUser().getUsername());
			weiboText.setText(dataList.get(pos).getText());
			weiboRes.setText(dataList.get(pos).getSource());
			ImageLoader.getInstance().displayImage(dataList.get(pos).getUser().getProfileImageURL(), header,ImageOptions.getInstance().getOptions());
			
			return contentView;
		}
		
	}
}
