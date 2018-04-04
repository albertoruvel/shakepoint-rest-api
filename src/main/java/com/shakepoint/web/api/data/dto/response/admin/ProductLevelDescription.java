package com.shakepoint.web.api.data.dto.response.admin;

public class ProductLevelDescription {
    private String productId;
    private String productName;
    private int requestedQuantity = 0;
    private String imageUrl;
    private boolean alerted;

    public ProductLevelDescription(boolean alerted, String productId, String productName, int requestedQuantity, String imageUrl) {
        this.alerted = alerted;
        this.productId = productId;
        this.productName = productName;
        this.requestedQuantity = requestedQuantity;
        this.imageUrl = imageUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    public void setRequestedQuantity(int requestedQuantity) {
        this.requestedQuantity = requestedQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isAlerted() {
        return alerted;
    }

    public void setAlerted(boolean alerted) {
        this.alerted = alerted;
    }
}
