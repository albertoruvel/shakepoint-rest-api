package com.shakepoint.web.api.data.internal;

public enum PartnerConversionType {
    MEMBER_TO_TRAINER("mtt"), TRAINER_TO_MEMBER("ttm");

    String value;
    PartnerConversionType(String value) {
        this.value = value;
    }

    public static PartnerConversionType fromString(String val){
        if (MEMBER_TO_TRAINER.value.equalsIgnoreCase(val)) {
            return MEMBER_TO_TRAINER;
        } else if (TRAINER_TO_MEMBER.value.equalsIgnoreCase(val)){
            return TRAINER_TO_MEMBER;
        } else {
            return null;
        }
    }
}
