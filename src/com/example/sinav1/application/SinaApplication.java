package com.example.sinav1.application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;

public class SinaApplication extends Application {
	DisplayImageOptions options; 
	@Override
	public void onCreate() {
		super.onCreate();
		ImageLoaderConfiguration.Builder config=new ImageLoaderConfiguration.Builder(getApplicationContext());
		config.threadPriority(Thread.NORM_PRIORITY-2);
		// ������ͼƬ�Ķ��ֳߴ����ڴ���
		config.denyCacheImageMultipleSizesInMemory();
		// �������ʱ���URI������MD5
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		// ���û����С
		config.diskCacheSize(50*1024*1024);
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs();
		
		// ��ʼ��ImageLoader
		ImageLoader.getInstance().init(config.build());
		
		
	}
}
