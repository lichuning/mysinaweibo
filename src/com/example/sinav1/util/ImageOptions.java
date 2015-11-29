package com.example.sinav1.util;

import com.example.sinav1.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.graphics.Bitmap;

public class ImageOptions {
	private static ImageOptions imageOptions ;
	DisplayImageOptions options ;
	
	private ImageOptions(){
		options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.icon_120)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.icon_120) // 设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
				.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				// 设置图片加入缓存前，对bitmap进行设置
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				.displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
				.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
				.build();// 构建完成
	}
	
	public static synchronized ImageOptions getInstance(){
		if(imageOptions==null){
			imageOptions=new ImageOptions();
		}
		
		return imageOptions ;
	}

	
	public  DisplayImageOptions getOptions() {
		
		return options ;
	}

}
