package com.example.sinav1.bean;

public class User {

	private String username ;
	private String profileImageURL ;
	private String id ;
	private String location ;
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User() {
		super();
	}

	public User(String username, String profileImageURL) {
		super();
		this.username = username;
		this.profileImageURL = profileImageURL;
	}

	public String getProfileImageURL() {
		return profileImageURL;
	}

	public void setProfileImageURL(String profileImageURL) {
		this.profileImageURL = profileImageURL;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", profileImageURL=" + profileImageURL + ", id=" + id + "]";
	}
	
	
}
