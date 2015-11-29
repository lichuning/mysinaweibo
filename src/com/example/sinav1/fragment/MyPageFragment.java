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
		// 获取ListView对象
		listView = (PullToRefreshListView) view.findViewById(R.id.listview_mypage);
		// 设置模式为both:既可以下拉又可以上拉
		listView.setMode(Mode.BOTH);
		// 创建适配器对象
		adapter = new MyAdapter();
		// 注册下拉刷新事件监听
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				// Update the LastUpdatedLabel
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// 调用该方法更新微博首页
				updateWeibo();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("加载更多");
				loadMore();
			}

		});

		ListView actualListView = listView.getRefreshableView();
		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);
		// 向listview添加适配器
		actualListView.setAdapter(adapter);

		// 初始化首页
		init();
		

	}
	/*
	 * 该方法用来初始化我的首页数据
	 * */
	private void init() {
		// 先从数据库中拿json数据,如果数据库没有数据则通过网络获取数据
		String content=SinaDB.getInstance(getActivity()).query();
		if(content==null){
			updateWeibo();
			
		}else{
			dataList=JsonParse.parse(content);
			adapter.notifyDataSetChanged();
		}
		
	}

	/*
	 * 适配器
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
				// 充入布局文件
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
			//  加载头像
			ImageLoader.getInstance().displayImage(dataList.get(pos).getUser().getProfileImageURL(), header,ImageOptions.getInstance().getOptions());
			// 加载配图
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
	 * 更新我的首页
	 */
	public void updateWeibo() {
		// 通过异步线程从网络中获取数据
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
					Toast.makeText(getActivity(), "获取网络更新失败", Toast.LENGTH_SHORT).show();
				
				}else{
					
					// 先清空表中数据,再将服务器返回的最新json数据存到数据库
					SinaDB.getInstance(getActivity()).deleteAll();
					SinaDB.getInstance(getActivity()).save(result);
					
					// 将服务器返回的数据进行解析
					List<WeiboInfo> tempList = JsonParse.parse(result);
					// dataList.addAll(0, tempList);
					dataList = tempList;
					// 更新数据集
					adapter.notifyDataSetChanged();
					
					//
					Toast.makeText(getActivity(), "更新了" + tempList.size() + "条微博", Toast.LENGTH_SHORT).show();
				}
				// 下拉刷新完成
				listView.onRefreshComplete();
			}

		}.execute();

	}

	/*
	 *	上拉加载数据 
	 **/
	public void loadMore() {
		// 通过异步线程从网络中获取数据
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
					Toast.makeText(getActivity(), "获取网络更新失败", Toast.LENGTH_SHORT).show();
				
				}else{
					// 将服务器返回的数据进行解析
					List<WeiboInfo> tempList = JsonParse.parse(result);
					 dataList.addAll(tempList);
					// 更新数据集
					adapter.notifyDataSetChanged();
				}
				// 上拉加载完成
				listView.onRefreshComplete();
			}

		}.execute();
	}

}
