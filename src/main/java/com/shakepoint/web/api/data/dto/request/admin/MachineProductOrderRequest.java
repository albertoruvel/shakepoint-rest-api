package com.shakepoint.web.api.data.dto.request.admin;

import com.shakepoint.web.api.data.dto.response.admin.ProductLevelDescription;

import java.util.List;

public class MachineProductOrderRequest {

    private List<ProductLevelDescription> products;
    private String machineId;

    public MachineProductOrderRequest() {
    }

    public MachineProductOrderRequest(List<ProductLevelDescription> products, String machineId) {
        this.products = products;
        this.machineId = machineId;
    }

    public List<ProductLevelDescription> getProducts() {
        return products;
    }

    public void setProducts(List<ProductLevelDescription> products) {
        this.products = products;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }
}
