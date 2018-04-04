package com.shakepoint.web.api.core.service.email;

public enum Template {
    SIGN_UP("successfully-signup","Registro en Shakepoint"),
    SUCCESSFUL_PURCHASE("success-purchase","Su compra ha sido procesada correctamente"),
    NEW_PARTNER_ORDER_REQUEST_CLIENT("new_partner_order", "Petición Shakepoint"),
    NEW_PARTNER_ORDER_REQUEST_ADMIN("new_partner_order_admin", "Solicitud Shakepoint");;

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
