package com.example.sinav1.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.sinav1.bean.User;
import com.example.sinav1.bean.WeiboInfo;

import android.text.TextUtils;

public class JsonParse {

	public static List<WeiboInfo> parse(String str) {
		
		if(str==null || TextUtils.isEmpty(str)){
			return null ;
		}

		List<WeiboInfo> list = new ArrayList<WeiboInfo>();

		try {
			JSONObject jsonObj = new JSONObject(str);
			JSONArray jsonArr = jsonObj.getJSONArray("statuses");

			for (int i = 0; i < jsonArr.length(); i++) {

				JSONObject obj = (JSONObject) jsonArr.get(i);
				WeiboInfo weiboObj = new WeiboInfo();
				// 微博正文
				weiboObj.setText(obj.getString("text"));
				
				// 微博配图
//				System.out.println(obj.toString());
				JSONArray picArr=(JSONArray) obj.get("pic_urls");
				System.out.println(i+"->"+picArr.toString());
				if(picArr.length()>0)
					weiboObj.setThumbnail_pic(((JSONObject) picArr.get(0)).getString("thumbnail_pic"));
				/*for (int j = 0; j < picArr.length(); j++) {
					JSONObject picObj=(JSONObject) picArr.get(j);
					weiboObj.setThumbnail_pic(picObj.getString("thumbnail_pic"));
				}*/
				
				// 发布时间
				String []date=obj.getString("created_at").split(" ");
				weiboObj.setCreated_at(date[3]);

				// 微博来源,将HTML转成纯文本
				if (obj.getString("source").length() > 0) {
					String astring = obj.getString("source").substring(obj.getString("source").indexOf('>') + 1,
							obj.getString("source").lastIndexOf('<'));
					weiboObj.setSource(astring);
				}
				// 解析用户
				User user = new User();
				JSONObject jsonUser = obj.getJSONObject("user");
				user.setUsername(jsonUser.getString("name"));
				user.setProfileImageURL(jsonUser.getString("profile_image_url"));

				weiboObj.setUser(user);

				list.add(weiboObj);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static String parseID(String str) {

		String result = null;
		try {

			JSONObject jsonObj = new JSONObject(str);

			result = jsonObj.getString("uid");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	public static User parseUser(String str) {

		User user = null;
		try {

			JSONObject jsonObj = new JSONObject(str);
			user=new User();
			user.setUsername(jsonObj.getString("name"));
			user.setLocation(jsonObj.getString("location"));
			user.setProfileImageURL(jsonObj.getString("profile_image_url"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return user;

	}
}
