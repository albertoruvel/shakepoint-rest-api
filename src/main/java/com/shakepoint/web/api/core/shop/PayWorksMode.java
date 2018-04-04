package com.shakepoint.web.api.core.shop;

public enum PayWorksMode {
    PRD ("PRD", "Modo producción"),
    AUT("AUT", "Modo de prueba autorizando siempre"),
    DEC("DEC", "Modo de prueba declinando siempre"),
    RND("RND", "Modo de prueba con autorizacion aleatoria");

    PayWorksMode(String value, String desc){
        this.value = value;
        this.description = desc;
    }
    String value;
    String description;

    public String getDescription(){
        return this.description;
    }

    public String getValue(){
        return this.value;
    }

    public static PayWorksMode get(String mode) {
        if (mode.equals(PRD.value)){
            return PRD;
        }else if (mode.equals(AUT.value)){
            return AUT;
        }else if (mode.equals(DEC.value)){
            return DEC;
        } else return RND;
    }
}
