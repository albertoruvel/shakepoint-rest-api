package com.shakepoint.web.api.core.service.security.impl;

import com.github.roar109.syring.annotation.ApplicationProperty;
import com.shakepoint.web.api.core.repository.UserRepository;
import com.shakepoint.web.api.core.service.email.EmailSender;
import com.shakepoint.web.api.core.service.email.Template;
import com.shakepoint.web.api.core.service.security.*;
import com.shakepoint.web.api.core.util.ShakeUtils;
import com.shakepoint.web.api.core.util.TransformationUtils;
import com.shakepoint.web.api.data.dto.request.SignInRequest;
import com.shakepoint.web.api.data.dto.request.SignupRequest;
import com.shakepoint.web.api.data.dto.response.AuthenticationResponse;
import com.shakepoint.web.api.data.dto.response.ForgotPasswordResponse;
import com.shakepoint.web.api.data.dto.response.ValidateForgotPasswordTokenResponse;
import com.shakepoint.web.api.data.entity.User;
import com.shakepoint.web.api.data.entity.UserPassword;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SecurityServiceImpl implements SecurityService {

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

    @Inject
    @AuthenticatedUser
    private RequestPrincipal authenticatedUser;

    @Override
    public Response authenticate(SignInRequest request) {
        //check first if is admin
        if (request.getEmail().equalsIgnoreCase(adminEmail)) {
            //check password
            if (request.getPassword().equals(adminPassword)) {
                //create a response with pre-defined token
                return Response.ok(new AuthenticationResponse("Welcome your majesty", adminToken, true, SecurityRole.ADMIN.getValue())).build();
            } else {
                return Response.ok(new AuthenticationResponse("Invalid credentials "))
                        .build();
            }
        }
        //check user
        User user = userRepository.getUserByEmail(request.getEmail());
        if (user != null) {
            //check pass
            String password = cryptoService.encrypt(request.getPassword());
            if (password.equals(user.getPassword())) {
                //generate a new token
                String token = ShakeUtils.getNextSessionToken();
                //check role
                final String role = user.getRole();
                SecurityRole securityRole = SecurityRole.fromString(role);
                //save it
                userRepository.saveUserToken(user.getId(), token);
                return Response.ok(new AuthenticationResponse("Welcome to Shakepoint", token, true, securityRole.getValue()))
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

    @Override
    public Response forgotPassword(String email) {
        if (email == null || email.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ForgotPasswordResponse("No email was provided")).build();
        }
        //get user by email
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ForgotPasswordResponse("The provided email is invalid")).build();
        }

        //create a new token
        final String forgotPasswordToken = ShakeUtils.getNextSessionToken();
        //check if user already tried to restore password
        UserPassword userPassword = userRepository.getUserPassword(authenticatedUser.getId());
        if (userPassword == null) {
            //create a new one
            userPassword = new UserPassword();
            userPassword.setExpirationDate(createNewPasswordExpirationToken());
            userPassword.setToken(forgotPasswordToken);
            userPassword.setUserId(authenticatedUser.getId());
            userRepository.updateUserPassword(userPassword);
        } else {
            userPassword.setToken(forgotPasswordToken);
            userPassword.setExpirationDate(createNewPasswordExpirationToken());
            userRepository.updateUserPassword(userPassword);
        }

        //send an email
        Map<String, Object> emailParams = new HashMap<String, Object>();
        emailParams.put("forgotPasswordToken", userPassword.getToken());
        emailParams.put("userName", user.getName());
        emailSender.sendEmail(user.getEmail(), Template.FORGOT_PASSWORD_REQUEST, emailParams);

        return Response.ok(new ForgotPasswordResponse("La petición se ha enviado correctamente")).build();
    }

    @Override
    public Response validateForgotPasswordToken(String token) throws Exception {
        if (token == null || token.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .build();
        }
        //check token
        UserPassword userPassword = userRepository.getUserPasswordByToken(token);
        if (userPassword == null) {
            return Response.ok(new ValidateForgotPasswordTokenResponse(false)).build();
        } else {
            //check expiration
            Date expirationDate = ShakeUtils.DATE_FORMAT.parse(userPassword.getExpirationDate());
            Date currentDate = new Date();
            if (currentDate.getTime() >= expirationDate.getTime()) {
                //expired
                return Response.ok(new ValidateForgotPasswordTokenResponse(false)).build();
            } else {
                //get minutes between dates
                Integer minutes = ((int)expirationDate.getTime() / 60000) - ((int)currentDate.getTime() / 60000);
                if (minutes > 60) {
                    return Response.ok(new ValidateForgotPasswordTokenResponse(false)).build();
                }else {
                    return Response.ok(new ValidateForgotPasswordTokenResponse(true)).build();
                }
            }
        }
    }

    private String createNewPasswordExpirationToken() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        Date date = calendar.getTime();
        String expirationDate = ShakeUtils.DATE_FORMAT.format(date);
        return expirationDate;
    }
}
