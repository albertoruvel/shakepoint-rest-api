package com.shakepoint.web.api.core.service.security;

public class RequestPrincipal {
    private String id;

    public RequestPrincipal() {
    }

    public RequestPrincipal(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
