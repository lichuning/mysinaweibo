package com.example.sinav1.fragment;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.app.SherlockFragment;
import com.example.sinav1.MainActivity;
import com.example.sinav1.R;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class IndexFragment extends SherlockFragment implements OnPageChangeListener,TabListener{
	
	public static final int WRITE_WEIBO = 6 ;
	public static final int ME = 7 ;
	ActionBar actionBar ;
	ViewPager viewPager;
	private Fragment[] fragmentGroup = new Fragment[] {MyPageFragment.getInstance(), new PublicPageFragment() };

	public IndexFragment(ActionBar actionBar) {
		this.actionBar=actionBar;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.index_fragment, null);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// 设置导航标签
		actionBar.addTab(actionBar.newTab().setText("我的首页").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("公共首页").setTabListener(this));
		// 获得ViewPager对象
		viewPager = (ViewPager) view.findViewById(R.id.view_pager);
		// 设置适配器
		viewPager.setAdapter(new MyViewPagerAdapter(getActivity().getSupportFragmentManager()));
		// ViewPager对象注册监听
		viewPager.setOnPageChangeListener(this);
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.write_weibo:
			((MainActivity)getActivity()).changeFragment(WRITE_WEIBO);
			break;

		case R.id.me:
			break;
			
		case android.R.id.home:
			((MainActivity)getActivity()).getSlidingMenu().showMenu();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		getSherlockActivity().getSupportMenuInflater().inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		if(!hidden){
			// 设置标题
			actionBar.setTitle("首页");
			// 设置actionBar模式
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			
		}
		super.onHiddenChanged(hidden);
	}

	// 适配器
	class MyViewPagerAdapter extends FragmentPagerAdapter {

		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {

			return fragmentGroup[pos];
		}

		@Override
		public int getCount() {
			return fragmentGroup.length;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int pos) {
		actionBar.getTabAt(pos).select();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if(viewPager!=null){
			viewPager.setCurrentItem(tab.getPosition());
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

}
