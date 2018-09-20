package com.shakepoint.web.api.core.service.email;

public enum Template {
    OPEN_PROMO_CODE_CREATED("open-promo-code-created", "Código de descuento!"),
    SIGN_UP("successfully-signup","Registro en Shakepoint"),
    SUCCESSFUL_PROMO_PURCHASE("success-promo-purchase", "Compra procesada con éxito"),
    EARNED_DRINK_DISCOUNT("earned-drink-discount", "Te has ganado un descuento!"),
    SUCCESSFUL_PURCHASE("success-purchase","Su compra ha sido procesada correctamente"),
    USER_BIRTHDATE_PROMO_CODE("birthdate-promo-code", "Es el mes de tu cumpleaños!"),
    NEW_TRAINER("trainer-welcome", "Bienvenido!"),
    TRAINER_PROMO_CODE_CREATED("trainer-promo-code-created", "Promociones para entrenadores"),
    NEW_PARTNER_WELCOME("partner-welcome", "Bienvenido!!"),
    NEW_PARTNER_ORDER_REQUEST_CLIENT("new_partner_order", "Petición Shakepoint"),
    FORGOT_PASSWORD_REQUEST("forgot_password_request", "Reestablecer contraseña"),
    NEW_PARTNER_ORDER_REQUEST_ADMIN("new_partner_order_admin", "Solicitud Shakepoint"),
    PASSWORD_RESET_SUCCESS("password_reset_success", "Reseteo de contraseña");

    Template(final String templateName, final String subject ){
        this.templateName = templateName;
        this.subject = subject;
    }

    private String templateName;
    private String subject;

    public String getTemplateName(){
        return this.templateName;
    }

    public String getSubject(){
        return this.subject;
    }
}
