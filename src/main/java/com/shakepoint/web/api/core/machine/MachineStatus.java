package com.shakepoint.web.api.core.machine;

public enum MachineStatus {
    OFF(0), ON(1), IDLE(2), FAIL(3), NOT_VALID(-1);

    int value;

    MachineStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static MachineStatus get(int value) {
        switch (value) {
            case 0:
                return OFF;
            case 1:
                return ON;
            case 2:
                return IDLE;
            case 3:
                return FAIL;
            default:
                return NOT_VALID;
        }
    }
}
