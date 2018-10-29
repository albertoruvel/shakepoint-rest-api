package com.shakepoint.web.api.data.entity;

public enum PromoType {
    OPEN(2, "Abierto"),
    OPEN_ALL(1, "Abierto para todos los productos"),
    TRAINER(3, "Entrenadores"),
    SEASON(5, "Por temporada"),
    SEASON_ALL(4, "Por temporada todos"),
    BIRTHDATE(6, "Cumpleaños"),
    EARNED(7, "Premio"),;

    int value;
    String clientValue;

    PromoType(int value, String s) {
        this.value = value;
        this.clientValue = s;
    }

    @Override
    public String toString(){
        return clientValue;
    }
    public static PromoType fromValue(int value) {
        switch (value) {
            case 1:
                return OPEN_ALL;
            case 2:
                return OPEN;
            case 3:
                return TRAINER;
            case 4:
                return SEASON_ALL;
            case 5:
                return SEASON;
            case 6:
                return BIRTHDATE;
            case 7:
                return EARNED;
                default:
                    return null;
        }
    }

    public int getValue() {
        return this.value;
    }
}
