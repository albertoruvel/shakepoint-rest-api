package com.shakepoint.web.api.data.fcm;

public enum FcmMessageType {
    TENTH_PURCHASE(1);

    int value;
    FcmMessageType(int value) {
        this.value = value;
    }
}
