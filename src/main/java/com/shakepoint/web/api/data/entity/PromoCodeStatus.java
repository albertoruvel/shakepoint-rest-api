package com.shakepoint.web.api.data.entity;

public enum PromoCodeStatus {
    ACTIVE(1), INACTIVE(0);
    int value;
    PromoCodeStatus(int value){
        this.value = value;
    }

    public static PromoCodeStatus fromValue(Integer value) {
        switch(value) {
            case 0:
                return INACTIVE;
            case 1:
                return ACTIVE;
            default:
                return null;
        }
    }

    public int getValue() {
        return value;
    }
}
