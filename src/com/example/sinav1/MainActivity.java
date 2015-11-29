package com.example.sinav1;

import com.actionbarsherlock.app.ActionBar;
import com.example.sinav1.fragment.HotFragment;
import com.example.sinav1.fragment.IndexFragment;
import com.example.sinav1.fragment.SearchFragment;
import com.example.sinav1.fragment.SendQueueFragment;
import com.example.sinav1.fragment.SettingFragment;
import com.example.sinav1.fragment.SlidingFragment;
import com.example.sinav1.fragment.ThemeFragment;
import com.example.sinav1.fragment.WriteWeiboFragment;
import com.example.sinav1.util.AccessTokenKeeper;
import com.example.sinav1.util.Constants;
import com.example.sinav1.util.JsonParse;
import com.example.sinav1.util.WeiboHttpUtils;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;
public class MainActivity extends SlidingFragmentActivity   {

	private AuthInfo mAuthInfo ;
	private SsoHandler mSsoHandler ;
	private Oauth2AccessToken mAccessToken ;
	private ActionBar actionBar;
	private Fragment[] slidingfragments ;
	private int currentPos;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(!AccessTokenKeeper.readAccessToken(this).isSessionValid()){
			mAuthInfo =new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
			
			mSsoHandler=new SsoHandler(this, mAuthInfo);
			
			mSsoHandler.authorizeWeb(new AuthListener());
		}	
		
		
		// 设置主窗体布局
		setContentView(R.layout.activity_main);
		// 设置侧滑菜单布局
		setBehindContentView(R.layout.sliding_layout);
		// 获得ActionBar对象,
		actionBar = getSupportActionBar();
		// 设置向上按钮
		actionBar.setDisplayHomeAsUpEnabled(true);

		// 初始化slidingfragments
		slidingfragments = new Fragment[] { 
				new IndexFragment(actionBar), 
				new HotFragment(actionBar),
				new SearchFragment(actionBar),
				new SendQueueFragment(actionBar),
				new ThemeFragment(actionBar),
				new SettingFragment(actionBar),
				new WriteWeiboFragment(actionBar)
				};

		//将fragment添加到主窗体容器中
		for (int i = 0; i < slidingfragments.length; i++) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.main_container, slidingfragments[i])
					.hide(slidingfragments[i])
					.commit();
		}
		// 设置主窗体默认显示的fragment
		getSupportFragmentManager().beginTransaction().show(slidingfragments[0]).commit();

		// 将SlidingFragment加入到策划菜单布局的容器中
		getSupportFragmentManager().beginTransaction()
				.add(R.id.sliding_container, new SlidingFragment())
				.commit();

		// 获得侧滑菜单对象并设置其属性
		getSlidingMenu().setBehindWidth(350);

	}

	public void changeFragment(int pos) {
		// 主窗体页面切换
		if (pos < slidingfragments.length && currentPos != pos) {
			getSupportFragmentManager().beginTransaction().show(slidingfragments[pos])
					.hide(slidingfragments[currentPos]).commit();

			getSlidingMenu().showContent();
			currentPos = pos;
		}
	}
	
	class AuthListener implements WeiboAuthListener {
        
        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
           System.out.println("ok");
            if (mAccessToken.isSessionValid()) {
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(MainActivity.this, mAccessToken);
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = getString(R.string.weibosdk_demo_toast_auth_failed);
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

		@Override
		public void onCancel() {
			Toast.makeText(MainActivity.this, "onCancel", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			
			Toast.makeText(MainActivity.this, "WeiboException:"+e, Toast.LENGTH_LONG).show();
			System.out.println(e);
		}

 }
	
	@Override
	public void onBackPressed() {
		
		if(currentPos==6||currentPos==7){
			this.changeFragment(0);
		}else{
			new AlertDialog.Builder(this)
				.setMessage("是否退出微一微博?")
				.setNegativeButton("是", new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						finish();
					}
				})
				.setPositiveButton("否", null)
				.show();
		}
	}
	


}
