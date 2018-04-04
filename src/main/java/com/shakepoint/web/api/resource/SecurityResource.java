package com.shakepoint.web.api.resource;

import com.shakepoint.web.api.core.service.security.AuthenticationService;
import com.shakepoint.web.api.data.dto.request.SignInRequest;
import com.shakepoint.web.api.data.dto.request.SignupRequest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("security")
public class SecurityResource {

    @Inject
    private AuthenticationService authenticationService;

    @Path("signIn")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signIn(SignInRequest request){
        return authenticationService.authenticate(request);
    }

    @POST
    @Path("signUp")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUp(SignupRequest request){
        return authenticationService.signUp(request);
    }
}
