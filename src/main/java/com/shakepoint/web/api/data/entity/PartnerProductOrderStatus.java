package com.shakepoint.web.api.data.entity;

public enum PartnerProductOrderStatus {
    REQUESTED (1),
    ON_HOLD(2),
    PROCESSING(3),
    COMPLETED(4);

    int value;
    PartnerProductOrderStatus(int value){
        this.value = value;
    }
}
