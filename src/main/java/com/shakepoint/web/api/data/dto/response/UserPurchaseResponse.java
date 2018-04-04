package com.shakepoint.web.api.data.dto.response;

public class UserPurchaseResponse {

    private String id;
    private double total;
    private String productName;
    private String machineName;
    private String purchaseDate;

    public UserPurchaseResponse(String id, double total, String productName, String machineName, String purchaseDate) {
        this.id = id;
        this.total = total;
        this.productName = productName;
        this.machineName = machineName;
        this.purchaseDate = purchaseDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
