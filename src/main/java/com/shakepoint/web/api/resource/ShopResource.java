package com.shakepoint.web.api.resource;

import com.shakepoint.web.api.core.service.ShopRestService;
import com.shakepoint.web.api.core.service.security.AllowedUsers;
import com.shakepoint.web.api.core.service.security.Secured;
import com.shakepoint.web.api.core.service.security.SecurityRole;
import com.shakepoint.web.api.data.dto.request.ConfirmPurchaseRequest;
import com.shakepoint.web.api.data.dto.request.ContactRequest;
import com.shakepoint.web.api.data.dto.request.FcmTokenRequest;
import com.shakepoint.web.api.data.dto.request.UserProfileRequest;
import com.shakepoint.web.api.data.dto.request.ValidatePromoCodeRequest;

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
    public Response getProductDetails(@QueryParam("productId") String productId, @QueryParam("vendingId") String vendingId, @QueryParam("slot") int slotNumber) {
        return shopRestService.getProductDetails(productId, vendingId, slotNumber);
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

    @POST
    @Path("validatePromoCode")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validatePromoCode(ValidatePromoCodeRequest request) {
        return shopRestService.validatePromoCode(request);
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
    public Response getAvailablePurchaseForMachine(@QueryParam("machineId") String machineId, @QueryParam("productId") String productId, @QueryParam("slot") Integer productSlot) {
        return shopRestService.getAvailablePurchaseForMachine(productId, machineId, productSlot);
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

    @Path("setNotificationsEnabled")
    @POST
    public Response setNotificationsEnabled(@QueryParam("enabled") boolean enabled) {
        return shopRestService.setNotificationsEnabled(enabled);
    }

    @Path("setEmailsEnabled")
    @POST
    public Response setEmailsEnabled(@QueryParam("enabled") boolean enabled) {
        return shopRestService.setEmailsEnabled(enabled);
    }

    @Path("deactivate")
    @POST
    public Response deactivateUser() {
        return shopRestService.deactivateUser();
    }

    @Path("activate")
    @POST
    public Response activateUser() {
        return shopRestService.activateUser();
    }

    @Path("contact")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response contact(ContactRequest request) {
        return shopRestService.contact(request);
    }

    @Path("setFcmToken")
    @POST
    public Response setFcmToken(FcmTokenRequest request) {
        return shopRestService.setFcmToken(request);
    }
}
