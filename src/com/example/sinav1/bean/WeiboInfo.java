package com.example.sinav1.bean;

public class WeiboInfo {
	
	private String text ;
	private User user ;
	private String source ;
	private String created_at ;
	private String thumbnail_pic ;
	
	
	public String getThumbnail_pic() {
		return thumbnail_pic;
	}
	public void setThumbnail_pic(String thumbnail_pic) {
		this.thumbnail_pic = thumbnail_pic;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public WeiboInfo(String text, User user, String source,String created_at) {
		super();
		this.text = text;
		this.user = user;
		this.source = source;
		this.created_at=created_at;
	}
	public WeiboInfo() {
		super();
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
