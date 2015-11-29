package com.example.sinav1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		new Thread(){

			@Override
			public void run() {

				SystemClock.sleep(2000);
				startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
				finish();
			}
			
		}.start();
	}
}
