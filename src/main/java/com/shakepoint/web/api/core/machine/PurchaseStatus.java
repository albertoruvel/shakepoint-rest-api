package com.shakepoint.web.api.core.machine;

public enum PurchaseStatus {
    PRE_AUTH(199),
    AUTHORIZED(69),
    CASHED(999);


    int value;

    PurchaseStatus(int value) {
        this.value = value;
    }

    public int getValue(PurchaseStatus status) {
        return Integer.parseInt(status.toString());
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
