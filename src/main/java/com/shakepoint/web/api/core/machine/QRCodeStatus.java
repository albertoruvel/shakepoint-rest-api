package com.shakepoint.web.api.core.machine;

public enum QRCodeStatus {
    AUTHORIZED(1), CASHED(2), NOT_VALID(9);
    int value;

    QRCodeStatus(int value) {
        this.value = value;
    }

    public QRCodeStatus get(int value) {
        switch (value) {
            case 0:
                return AUTHORIZED;
            case 1:
                return CASHED;
            default:
                return NOT_VALID;
        }
    }

    public int getValue(QRCodeStatus status) {
        return status.getValue();
    }

    public int getValue() {
        return value;
    }
}
