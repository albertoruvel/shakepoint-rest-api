package com.shakepoint.web.api.resource;

import com.shakepoint.web.api.core.service.PartnerRestService;
import com.shakepoint.web.api.core.service.security.AllowedUsers;
import com.shakepoint.web.api.core.service.security.RequestPrincipal;
import com.shakepoint.web.api.core.service.security.Secured;
import com.shakepoint.web.api.core.service.security.SecurityRole;
import com.shakepoint.web.api.data.dto.request.partner.CreateTrainerRequest;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("partner")
@AllowedUsers(securityRoles = { SecurityRole.PARTNER, SecurityRole.ADMIN })
@Secured
public class PartnerResource {

    @Inject
    private PartnerRestService partnerRestService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("createTrainer")
    public Response doSomethingWithRequestPrincipal(CreateTrainerRequest request){
        return partnerRestService.createTrainer(request);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("trainers")
    public Response getTrainers() {
        return partnerRestService.getTrainers();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("searchTrainers")
    public Response searchTrainers(@QueryParam("email") String email) {
        return partnerRestService.searchTrainers(email);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("convertUser")
    public Response convertUser(@QueryParam("memberId") String memberId, @QueryParam("convertType")String convertType) {
        return partnerRestService.convertUser(memberId, convertType);
    }

}
