package com.shakepoint.web.api.data.dto.request;

public class UserProfileRequest {
	private double height; 
	private double weight; 
	private String birthday;
	public UserProfileRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	} 
	
}
