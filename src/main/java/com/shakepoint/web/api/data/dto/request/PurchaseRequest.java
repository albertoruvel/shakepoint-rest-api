package com.shakepoint.web.api.data.dto.request;

public class PurchaseRequest {
	private String machineId; 
	private String productId;
	private double price;
	public PurchaseRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	
}
