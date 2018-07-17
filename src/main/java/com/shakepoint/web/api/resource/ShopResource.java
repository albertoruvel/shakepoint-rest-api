package com.shakepoint.web.api.resource;

import com.shakepoint.web.api.core.service.ShopRestService;
import com.shakepoint.web.api.core.service.security.AllowedUsers;
import com.shakepoint.web.api.core.service.security.Secured;
import com.shakepoint.web.api.core.service.security.SecurityRole;
import com.shakepoint.web.api.data.dto.request.ConfirmPurchaseRequest;
import com.shakepoint.web.api.data.dto.request.UserProfileRequest;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("shop")
@AllowedUsers(securityRoles = {SecurityRole.ALL})
@Secured
public class ShopResource {

    @Inject
    private ShopRestService shopRestService;


    @Path("productDetails")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductDetails(@QueryParam("productId") String productId) {
        return shopRestService.getProductDetails(productId);
    }

    @Path("searchMachine")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMachine(@QueryParam("longitude") Double longitude, @QueryParam("latitude") Double latitude) {
        return shopRestService.searchMachine(longitude, latitude);
    }

    @Path("searchMachineByName")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMachineByName(@QueryParam("name") String machineName) {
        return shopRestService.searchMachinesByName(machineName);
    }

    @Path("getProducts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMachineProducts(@QueryParam("machineId") String machineId) {
        return shopRestService.getMachineProducts(machineId);
    }

    @Path("getAuthorizedPurchases")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthorizedPurchasesO(@QueryParam("machineId") String machineId) {
        return shopRestService.getActiveQrCodes(machineId);
    }

    @Path("confirmPurchase")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response confirmPurchase(ConfirmPurchaseRequest request) {
        return shopRestService.confirmPurchase(request);
    }

    @Path("getAvailablePurchaseForMachine")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailablePurchaseForMachine(@QueryParam("machineId") String machineId, @QueryParam("productId") String productId) {
        return shopRestService.getAvailablePurchaseForMachine(productId, machineId);
    }

    @Path("getPurchases")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPurchases() {
        return shopRestService.getUserPurchases();
    }

    @Path("profile")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfile() {
        return shopRestService.getUserProfile();
    }

    @Path("saveProfile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response saveProfile(UserProfileRequest request) {
        return shopRestService.saveProfile(request);
    }
}