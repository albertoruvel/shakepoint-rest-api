package com.shakepoint.web.api.resource;

import com.shakepoint.web.api.core.service.security.SecurityService;
import com.shakepoint.web.api.data.dto.request.ResetPasswordRequest;
import com.shakepoint.web.api.data.dto.request.SignInRequest;
import com.shakepoint.web.api.data.dto.request.SignupRequest;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("security")
public class SecurityResource {

    @Inject
    private SecurityService securityService;

    @Path("signIn")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signIn(SignInRequest request) {
        return securityService.authenticate(request);
    }

    @POST
    @Path("signUp")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUp(SignupRequest request) {
        return securityService.signUp(request);
    }

    @POST
    @Path("forgotPassword")
    @Produces(MediaType.APPLICATION_JSON)
    public Response forgotPassword(@QueryParam("email") String email) {
        return securityService.forgotPassword(email);
    }

    @POST
    @Path("resetPassword")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetPassword(ResetPasswordRequest resetPasswordRequest) {
        return securityService.resetPassword(resetPasswordRequest);
    }

    @POST
    @Path("validateForgotPasswordToken")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateForgotPasswordToken(@QueryParam("token") String token) throws Exception {
        return securityService.validateForgotPasswordToken(token);
    }
}
