package com.example.sinav1.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeiboHttpUtils {

	/**
	 * 发送条纯文本新微博
	 */
	public static final String SEND_ONLY_TEXT = "https://api.weibo.com/2/statuses/update.json";
	
	public static final String CHARSET_UTF8 = "utf-8";
	public static final String HTTP_POST = "POST";
	public static final String HTTP_GET = "GET";
	public static final int CONNECT_TIMEOUT = 8000;

	/**
	 * 发布一条纯文本的新微博
	 * 
	 * @param content
	 *            要发布的微博文本内容，必须做URLencode，内容不超过140个汉字。
	 * @param token
	 *            采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得。
	 * @param Longitude
	 *            经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0。
	 * @param Latitude
	 *            纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0。
	 * @param visible
	 *            微博的可见性，0：所有人能看，1：仅自己可见，2：密友可见，3：指定分组可见，默认为0。
	 */
	public static String sendOnlyText(String content, String token, float Longitude, float Latitude, int visible) {

		HttpURLConnection conn = null;
		OutputStream os = null;
		InputStream is =null ;
		StringBuilder result = new StringBuilder();
		try {
			URL url = new URL(SEND_ONLY_TEXT);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(HTTP_POST);
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setDoOutput(true);
			conn.setDoInput(true);

			String parmas = "access_token=" + token + "&status=" + content + "&lat=" + Latitude + "&long=" + Longitude;

			os = conn.getOutputStream();
			os.write(parmas.getBytes(CHARSET_UTF8));
			os.flush();
			
			System.out.println(conn.getResponseCode());
			if (conn.getResponseCode() == 200) {
				System.out.println("发送成功");
				is=conn.getInputStream();
				int len=0;
				byte[] buff=new byte[1024];
				while((len=is.read(buff))!=-1){
					result.append(new String(buff,0,len));
				}
			}
			
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}
		
	
		return result.toString();
	}
	
	
	
	/**
	 * 通过HTTP的GET请求方式访问新浪微博后台
	 * @param token
	 * @return
	 */
	public static String sinaHttpGet(String urlStr){
		
		HttpURLConnection conn = null;
		InputStream is =null ;
		StringBuilder sBuilder=null;
		
		try {
			URL url=new URL(urlStr);

			conn=(HttpURLConnection) url.openConnection();
			conn.setRequestMethod(HTTP_GET);
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setDoInput(true);
			
			System.out.println("responsecode="+conn.getResponseCode());
			if(conn.getResponseCode()==200){
				
				System.out.println("连接成功");
				is=conn.getInputStream();
				int len=0;
				byte[] buff = new byte[1024];
				sBuilder=new StringBuilder();
				while((len=is.read(buff))!=-1){
					sBuilder.append(new String(buff,0,len));
				}

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(sBuilder.toString());
		
		if(sBuilder==null){
			return null ;
		}
		System.out.println(sBuilder.toString());
		return sBuilder.toString();
	}

}
