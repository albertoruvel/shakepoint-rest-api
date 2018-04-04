package com.shakepoint.web.api.data.dto.response;

public class UnauthorizedResponse {
	private String message; 
	private int status;
	public UnauthorizedResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
