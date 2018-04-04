package com.shakepoint.web.api.data.dto.response;

public class PurchaseCodeResponse {
    private String imageUrl;
    private String productName;
    private String machineName;
    private String purchaseDate;

    public PurchaseCodeResponse() {
    }

    public PurchaseCodeResponse(String imageUrl, String productid, String machineName, String purchaseDate) {
        this.imageUrl = imageUrl;
        this.productName = productid;
        this.machineName = machineName;
        this.purchaseDate = purchaseDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productid) {
        this.productName = productid;
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

    public PurchaseCodeResponse(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageurl) {
        this.imageUrl = imageurl;
    }


}
