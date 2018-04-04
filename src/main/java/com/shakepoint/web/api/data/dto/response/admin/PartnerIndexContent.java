package com.shakepoint.web.api.data.dto.response.admin;

import java.util.List;

public class PartnerIndexContent {
    private PerMachineValues perMachineValues;
    private String lastSignin;
    private int alertedMachines;
    private List<MachineProductCountItem> machines;
    private String [] range;
    private Technician partner;

    public PerMachineValues getPerMachineValues() {
        return perMachineValues;
    }

    public void setPerMachineValues(PerMachineValues perMachineValues) {
        this.perMachineValues = perMachineValues;
    }

    public String getLastSignin() {
        return lastSignin;
    }

    public void setLastSignin(String lastSignin) {
        this.lastSignin = lastSignin;
    }

    public int getAlertedMachines() {
        return alertedMachines;
    }

    public void setAlertedMachines(int alertedMachines) {
        this.alertedMachines = alertedMachines;
    }

    public List<MachineProductCountItem> getMachines() {
        return machines;
    }

    public void setMachines(List<MachineProductCountItem> machines) {
        this.machines = machines;
    }

    public Technician getPartner() {
        return partner;
    }

    public void setPartner(Technician partner) {
        this.partner = partner;
    }

    public String[] getRange() {
        return range;
    }

    public void setRange(String[] range) {
        this.range = range;
    }


}
