package com.shakepoint.web.api.data.dto.response.admin;

public class AdminIndexContent {
    private int activeMachines;
    private int alertedMachines;
    private int registeredTechnicians;
    private double todayTotal;
    private PerMachineValues perMachineValues;
    private TotalIncomeValues totalIncomeValues;
    private String[] modes;


    public AdminIndexContent() {
        super();
    }


    public int getActiveMachines() {
        return activeMachines;
    }


    public void setActiveMachines(int activeMachines) {
        this.activeMachines = activeMachines;
    }


    public int getAlertedMachines() {
        return alertedMachines;
    }


    public void setAlertedMachines(int alertedMachines) {
        this.alertedMachines = alertedMachines;
    }


    public int getRegisteredTechnicians() {
        return registeredTechnicians;
    }


    public void setRegisteredTechnicians(int registeredTechnicians) {
        this.registeredTechnicians = registeredTechnicians;
    }


    public double getTodayTotal() {
        return todayTotal;
    }


    public void setTodayTotal(double todayTotal) {
        this.todayTotal = todayTotal;
    }


    public PerMachineValues getPerMachineValues() {
        return perMachineValues;
    }


    public void setPerMachineValues(PerMachineValues perMachineValues) {
        this.perMachineValues = perMachineValues;
    }


    public TotalIncomeValues getTotalIncomeValues() {
        return totalIncomeValues;
    }


    public void setTotalIncomeValues(TotalIncomeValues totalIncomeValues) {
        this.totalIncomeValues = totalIncomeValues;
    }

    public String[] getModes() {
        return modes;
    }

    public void setModes(String[] modes) {
        this.modes = modes;
    }
}
