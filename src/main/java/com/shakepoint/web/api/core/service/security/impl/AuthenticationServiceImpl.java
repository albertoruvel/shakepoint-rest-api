package com.shakepoint.web.api.core.service.security.impl;

import com.github.roar109.syring.annotation.ApplicationProperty;
import com.shakepoint.web.api.core.repository.UserRepository;
import com.shakepoint.web.api.core.service.email.EmailSender;
import com.shakepoint.web.api.core.service.email.Template;
import com.shakepoint.web.api.core.service.security.AuthenticationService;
import com.shakepoint.web.api.core.service.security.CryptoService;
import com.shakepoint.web.api.core.util.ShakeUtils;
import com.shakepoint.web.api.core.util.TransformationUtils;
import com.shakepoint.web.api.data.dto.request.SignInRequest;
import com.shakepoint.web.api.data.dto.request.SignupRequest;
import com.shakepoint.web.api.data.dto.response.AuthenticationResponse;
import com.shakepoint.web.api.data.entity.User;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationServiceImpl implements AuthenticationService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private CryptoService cryptoService;

    @Inject
    private EmailSender emailSender;

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.admin.user", type = ApplicationProperty.Types.SYSTEM)
    private String adminEmail;

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.admin.pass", type = ApplicationProperty.Types.SYSTEM)
    private String adminPassword;

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.admin.token", type = ApplicationProperty.Types.SYSTEM)
    private String adminToken;

    @Override
    public Response authenticate(SignInRequest request) {
        //check first if is admin
        if (request.getEmail().equalsIgnoreCase(adminEmail)){
            //check password
            if (request.getPassword().equals(adminPassword)){
                //create a response with pre-defined token
                return Response.ok(new AuthenticationResponse("Welcome your majesty", adminToken, true)).build();
            }else {
                return Response.ok(new AuthenticationResponse("Invalid credentials ", null, false))
                        .build();
            }
        }
        //check user
        User user = userRepository.getUserByEmail(request.getEmail());
        if (user != null) {
            //check pass
            if (cryptoService.encrypt(request.getPassword()).equals(user.getPassword())) {
                //generate a new token
                String token = ShakeUtils.getNextSessionToken();
                //save it
                userRepository.saveUserToken(user.getId(), token);
                return Response.ok(new AuthenticationResponse("Welcome to Shakepoint", token, true))
                        .build();
            } else {
                return Response.ok(new AuthenticationResponse("Invalid credentials"))
                        .build();
            }
        } else {
            return Response.ok(new AuthenticationResponse("Invalid credentials"))
                    .build();
        }
    }

    @Override
    public Response signUp(SignupRequest request) {
        AuthenticationResponse ar = new AuthenticationResponse();
        //check if  user has already been signed up with the same email
        if (userRepository.userExists(request.getEmail())) {
            ar.setAuthenticationToken("N/A");
            ar.setMessage("This email is already registered on shakepoint");
            ar.setSuccess(false);
            return Response.status(Response.Status.BAD_REQUEST).entity(ar).build();
        } else {
            //get the user
            final String encryptedPass = cryptoService.encrypt(request.getPassword());
            User user = TransformationUtils.getUser(request, encryptedPass);
            userRepository.addShakepointUser(user);
            ar.setAuthenticationToken(ShakeUtils.getNextSessionToken());
            ar.setMessage("User signed up successfully");
            ar.setSuccess(true);
            //Send Email
            final Map<String, Object> parameters = new HashMap<String, Object>(1);
            parameters.put("name", user.getName());
            emailSender.sendEmail(request.getEmail(), Template.SIGN_UP, parameters);
            return Response.ok(ar).build();
        }
    }
}
