package com.shakepoint.web.api.data.dto.response.admin;

import java.util.List;

public class MachineProductCountItem {
    private String machineId;
    private String machineName;
    private String[] products;
    private List<Integer> data;

    public MachineProductCountItem() {
    }

    public MachineProductCountItem(String machineName, String[] products, List<Integer> data) {
        this.machineName = machineName;
        this.products = products;
        this.data = data;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String[] getProducts() {
        return products;
    }

    public void setProducts(String[] products) {
        this.products = products;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }
}
