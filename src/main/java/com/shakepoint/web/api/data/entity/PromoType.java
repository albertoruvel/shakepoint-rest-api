package com.shakepoint.web.api.data.entity;

public enum PromoType {
    OPEN("open"), TRAINER("trainer"), SEASON("season"), BIRTHDATE("birthdate");

    String value;
    PromoType(String value) {
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
