package com.example.sinav1.fragment;

import com.actionbarsherlock.app.ActionBar;
import com.example.sinav1.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingFragment extends Fragment   {

	private ActionBar actionBar ;
	
	public SettingFragment(ActionBar actionBar) {
		this.actionBar=actionBar;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.setting_fragment, null);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		
		if(!hidden){
			actionBar.setTitle("…Ë÷√");
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		}
		super.onHiddenChanged(hidden);
	}

	
	
	
	
}
