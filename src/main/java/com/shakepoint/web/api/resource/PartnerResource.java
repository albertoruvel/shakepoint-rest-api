package com.shakepoint.web.api.resource;

import com.shakepoint.web.api.core.service.security.AllowedUsers;
import com.shakepoint.web.api.core.service.security.RequestPrincipal;
import com.shakepoint.web.api.core.service.security.Secured;
import com.shakepoint.web.api.core.service.security.SecurityRole;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("partner")
@AllowedUsers(securityRoles = { SecurityRole.PARTNER, SecurityRole.ADMIN })
@Secured
public class PartnerResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void doSomethingWithRequestPrincipal(){
        //Logger.getLogger(getClass()).info(requestPrincipal);
    }

}
