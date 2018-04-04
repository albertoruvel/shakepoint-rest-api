package com.shakepoint.web.api.data.dto.response;

public class MachineSearch {
	private String machineId; 
	private String machineName;
	private double distance;

	public MachineSearch() {
		super();
	}

	public MachineSearch(String machineId, String machineName, double distance) {
		this.machineId = machineId;
		this.machineName = machineName;
		this.distance = distance;
	}

	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public String getMachineName() {
		return machineName;
	}
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getDistance() {
		return distance;
	}
}
