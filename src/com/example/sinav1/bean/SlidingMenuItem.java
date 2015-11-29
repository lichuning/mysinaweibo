package com.example.sinav1.bean;

public class SlidingMenuItem {

	private String title ;
	private int imageIcon ;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getImageIcon() {
		return imageIcon;
	}
	public void setImageIcon(int imageIcon) {
		this.imageIcon = imageIcon;
	}
	public SlidingMenuItem(String title, int imageIcon) {
		super();
		this.title = title;
		this.imageIcon = imageIcon;
	}
	
	
}
