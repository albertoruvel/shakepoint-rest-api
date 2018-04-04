package com.shakepoint.web.api.data.dto.response.admin;

import java.util.List;
import java.util.Map;

public class PerMachineValues {
	private String fromDate; 
	private String toDate; 
	
	private String[] range;
	
	private Map<String, List<Double>> values; 
	
	public PerMachineValues() {
		super();
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String[] getRange() {
		return range;
	}

	public void setRange(String[] range) {
		this.range = range;
	}

	public Map<String, List<Double>> getValues() {
		return values;
	}

	public void setValues(Map<String, List<Double>> values) {
		this.values = values;
	}
	
	
	
}
