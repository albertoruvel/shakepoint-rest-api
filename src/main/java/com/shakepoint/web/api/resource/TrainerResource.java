package com.shakepoint.web.api.resource;

import com.shakepoint.web.api.core.service.TrainerRestService;
import com.shakepoint.web.api.core.service.security.AllowedUsers;
import com.shakepoint.web.api.core.service.security.Secured;
import com.shakepoint.web.api.core.service.security.SecurityRole;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("trainer")
@AllowedUsers(securityRoles = {SecurityRole.TRAINER})
@Secured
public class TrainerResource {

    @Inject
    private TrainerRestService trainerRestService;

    @GET
    @Path("getAssignedPromos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrainerAssignedPromos() {
        return trainerRestService.getTrainerAssignedPromos();
    }

}
