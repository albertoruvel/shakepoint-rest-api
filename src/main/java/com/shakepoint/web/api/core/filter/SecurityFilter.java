package com.shakepoint.web.api.core.filter;

import com.github.roar109.syring.annotation.ApplicationProperty;
import com.shakepoint.web.api.core.repository.UserRepository;
import com.shakepoint.web.api.core.service.security.AllowedUsers;
import com.shakepoint.web.api.core.service.security.RequestPrincipal;
import com.shakepoint.web.api.core.service.security.Secured;
import com.shakepoint.web.api.core.service.security.SecurityRole;
import com.shakepoint.web.api.data.dto.response.AuthenticationResponse;
import com.shakepoint.web.api.data.entity.User;
import org.apache.log4j.Logger;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;

@Secured
@Provider
public class SecurityFilter implements ContainerRequestFilter {

    private final Logger log = Logger.getLogger(getClass());

    @Inject
    private UserRepository userRepository;

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.admin.token", type = ApplicationProperty.Types.SYSTEM)
    private String adminToken;

    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        //authenticate user here...
        ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) containerRequestContext
                .getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
        //get invoked method
        Method method = methodInvoker.getMethod();
        //get token
        String auth = containerRequestContext.getHeaderString("Authorization");
        //check if the incoming request is from the admin token
        if(auth != null && auth.equals(adminToken)){
            //let it pass..
            return;
        }
        //first check if the class checks if is annotated with @AllowedUsers
        if (method.getDeclaringClass().isAnnotationPresent(AllowedUsers.class)) {
            //validate
            validate(method.getDeclaringClass().getAnnotation(AllowedUsers.class), auth, containerRequestContext);
        } else if (method.isAnnotationPresent(AllowedUsers.class)) {
            validate(method.getAnnotation(AllowedUsers.class), auth, containerRequestContext);
        }
    }

    private void validate(AllowedUsers annotation, String token, ContainerRequestContext crc) {
        SecurityRole[] allowedRoles = annotation.securityRoles();
        //validate token
        if (token == null || token.isEmpty()) //no token content
        {
            abort(crc);
        } else if (!validateToken(allowedRoles, token)) //token have not been successfully validated
        {
            abort(crc);
        }

    }

    private boolean validateToken(SecurityRole[] roles, String token) {
        //get user by token
        User user = null;
        try {
            user = userRepository.findUserByToken(token);
            if (user != null) {
                for (SecurityRole role : roles) {
                    if (SecurityRole.fromString(user.getRole()) == role) {
                        //the user has role and has been authenticated successfully
                        //push security context
                        ResteasyProviderFactory.pushContext(RequestPrincipal.class,
                                new RequestPrincipal(user.getId()));
                        return true;
                    }
                }
                return false;
            } else {
                return false;
            }
        } catch (Exception ex) {
            log.error("Error finding user: " + ex.getMessage());
            return false;
        }
    }

    private void abort(ContainerRequestContext cxt) {
        cxt.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity(new AuthenticationResponse("Unauthorized"))
                .type(MediaType.APPLICATION_JSON)
                .build());
    }
}
