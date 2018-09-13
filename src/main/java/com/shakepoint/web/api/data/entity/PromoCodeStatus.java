package com.shakepoint.web.api.data.entity;

public enum PromoCodeStatus {
    ACTIVE(1), INACTIVE(0);
    int value;
    PromoCodeStatus(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
