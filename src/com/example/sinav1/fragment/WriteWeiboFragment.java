package com.example.sinav1.fragment;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.sinav1.MainActivity;
import com.example.sinav1.R;
import com.example.sinav1.util.AccessTokenKeeper;
import com.example.sinav1.util.WeiboHttpUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WriteWeiboFragment extends SherlockFragment implements OnClickListener{

	
	private ActionBar actionBar ;
	private EditText weiboContent ;
	private ImageView messageEmotion ;
	private ImageView composeMentionbutton ;
	private ImageView composeTrendbutton ;
	private TextView wordNumber ;
	
	
	public  WriteWeiboFragment( ActionBar actionBar ) {
		this.actionBar=actionBar;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.write_weibo_fragment, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		weiboContent=(EditText) view.findViewById(R.id.weibo_content);
		messageEmotion=(ImageView) view.findViewById(R.id.message_emotion);
		composeMentionbutton=(ImageView) view.findViewById(R.id.compose_mentionbutton);
		composeTrendbutton=(ImageView) view.findViewById(R.id.compose_trendbutton);
		wordNumber=(TextView) view.findViewById(R.id.word_number);
		
		weiboContent.addTextChangedListener(new MyTextWatcher());
		messageEmotion.setOnClickListener(this);
		composeMentionbutton.setOnClickListener(this);
		composeTrendbutton.setOnClickListener(this);
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.send_button:
			//发送一条微博
			new AsyncTask<String, String, String>() {
				@Override
				protected String doInBackground(String... params) {
					String token=AccessTokenKeeper.readAccessToken(getActivity()).getToken();
					String result=WeiboHttpUtils.sendOnlyText(weiboContent.getText().toString(), token, 0, 0, 1);
					return result;
				}

				@Override
				protected void onPostExecute(String result) {
					//更新我的首页
					MyPageFragment.getInstance().updateWeibo();
					//跳转回首页
					Toast.makeText(getActivity(), "发布成功!", Toast.LENGTH_LONG).show();
					((MainActivity)getActivity()).changeFragment(0);
				}
				
				
			}.execute();
			
			break;

		case android.R.id.home:
			((MainActivity)getActivity()).changeFragment(0);
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		getSherlockActivity().getSupportMenuInflater().inflate(R.menu.write_weibo, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		
		if(!hidden){
			actionBar.setTitle("写微博");
			actionBar.setIcon(R.drawable.toolbar_compose_highlighted);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		}
		super.onHiddenChanged(hidden);
	}
	
	class MyTextWatcher implements TextWatcher{
		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			wordNumber.setText(""+s.toString().length());
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.message_emotion:
			
			break;
			
		case R.id.compose_mentionbutton:
			
			break;
			
		case R.id.compose_trendbutton:
	
			break;
		}
		
	}
	
	
}
