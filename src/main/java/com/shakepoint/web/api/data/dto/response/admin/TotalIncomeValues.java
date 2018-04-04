package com.shakepoint.web.api.data.dto.response.admin;

import java.util.List;

public class TotalIncomeValues {
	private String[] range; 
	private List<Double> values;
	public TotalIncomeValues() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String[] getRange() {
		return range;
	}
	public void setRange(String[] range) {
		this.range = range;
	}
	public List<Double> getValues() {
		return values;
	}
	public void setValues(List<Double> values) {
		this.values = values;
	}
	
	
}
