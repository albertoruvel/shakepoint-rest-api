package com.shakepoint.web.api.core.service.security;

public enum SecurityRole {
    ADMIN("ADMIN"), MEMBER("MEMBER"), PARTNER("PARTNER"), TRAINER("TRAINER"), ALL("ALL");

    String value;

    SecurityRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static SecurityRole fromString(String v) {
        if (v.equals(ADMIN.value)) {
            return ADMIN;
        } else if (v.equals(MEMBER.value)) {
            return MEMBER;
        } else if (v.equals(PARTNER.value)) {
            return PARTNER;
        } else if (v.equals(TRAINER.value)) {
            return TRAINER;
        } else {
            return ALL;
        }
    }

}
