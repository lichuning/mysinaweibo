package com.example.sinav1.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeiboHttpUtils {

	/**
	 * ���������ı���΢��
	 */
	public static final String SEND_ONLY_TEXT = "https://api.weibo.com/2/statuses/update.json";
	
	public static final String CHARSET_UTF8 = "utf-8";
	public static final String HTTP_POST = "POST";
	public static final String HTTP_GET = "GET";
	public static final int CONNECT_TIMEOUT = 8000;

	/**
	 * ����һ�����ı�����΢��
	 * 
	 * @param content
	 *            Ҫ������΢���ı����ݣ�������URLencode�����ݲ�����140�����֡�
	 * @param token
	 *            ����OAuth��Ȩ��ʽΪ���������������Ȩ��ʽ����Ҫ�˲�����OAuth��Ȩ���á�
	 * @param Longitude
	 *            ���ȣ���Ч��Χ��-180.0��+180.0��+��ʾ������Ĭ��Ϊ0.0��
	 * @param Latitude
	 *            γ�ȣ���Ч��Χ��-90.0��+90.0��+��ʾ��γ��Ĭ��Ϊ0.0��
	 * @param visible
	 *            ΢���Ŀɼ��ԣ�0���������ܿ���1�����Լ��ɼ���2�����ѿɼ���3��ָ������ɼ���Ĭ��Ϊ0��
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
				System.out.println("���ͳɹ�");
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
	 * ͨ��HTTP��GET����ʽ��������΢����̨
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
				
				System.out.println("���ӳɹ�");
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
