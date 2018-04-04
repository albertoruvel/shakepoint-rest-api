package com.shakepoint.web.api.core.service.security;

import com.shakepoint.web.api.data.dto.request.SignInRequest;
import com.shakepoint.web.api.data.dto.request.SignupRequest;

import javax.ws.rs.core.Response;

public interface AuthenticationService {
    public Response authenticate(SignInRequest request);
    public Response signUp(SignupRequest request);
}
