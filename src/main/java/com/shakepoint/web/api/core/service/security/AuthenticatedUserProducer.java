package com.shakepoint.web.api.core.service.security;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

@RequestScoped
public class AuthenticatedUserProducer {

    @Produces
    @RequestScoped
    @AuthenticatedUser
    private RequestPrincipal principal;

    public void handleAuthenticatedUserEvent(@Observes @AuthenticatedUser String userId){
        this.principal = new RequestPrincipal(userId);
    }
}
