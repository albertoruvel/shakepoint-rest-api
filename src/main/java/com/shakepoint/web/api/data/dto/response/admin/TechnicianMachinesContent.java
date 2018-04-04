package com.shakepoint.web.api.data.dto.response.admin;

import java.util.List;

public class TechnicianMachinesContent {
	private Technician technician;
	private List<SimpleMachine> allMachines;
	private List<SimpleMachine> asignedMachines;
	public TechnicianMachinesContent() {
		super();
	}
	public Technician getTechnician() {
		return technician;
	}
	public void setTechnician(Technician technician) {
		this.technician = technician;
	}
	public List<SimpleMachine> getAllMachines() {
		return allMachines;
	}
	public void setAllMachines(List<SimpleMachine> allMachines) {
		this.allMachines = allMachines;
	}
	public List<SimpleMachine> getAsignedMachines() {
		return asignedMachines;
	}
	public void setAsignedMachines(List<SimpleMachine> asignedMachines) {
		this.asignedMachines = asignedMachines;
	} 
	
}
