package com.shakepoint.web.api.resource;

import com.shakepoint.web.api.core.service.AdminRestService;
import com.shakepoint.web.api.core.service.security.AllowedUsers;
import com.shakepoint.web.api.core.service.security.Secured;
import com.shakepoint.web.api.core.service.security.SecurityRole;
import com.shakepoint.web.api.data.dto.request.admin.CreatePromoCodeRequest;
import com.shakepoint.web.api.data.dto.request.admin.CreateTrainerRequest;
import com.shakepoint.web.api.data.dto.request.admin.NewMachineRequest;
import com.shakepoint.web.api.data.dto.request.admin.NewTechnicianRequest;
import com.shakepoint.web.api.data.dto.request.admin.TogglePromoCodeStatusRequest;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("admin")
@AllowedUsers(securityRoles = {SecurityRole.ADMIN})
@Secured
public class AdminResource {

    @Inject
    private AdminRestService adminRestService;

    @Path("getProducts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts() {
        return adminRestService.getProducts();
    }

    @Path("createProduct")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewProduct(MultipartFormDataInput multipart) {
        return adminRestService.createNewProduct(multipart);
    }

    @GET
    @Path("getPartners")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPartners() {
        return adminRestService.getTechnicians();
    }

    @POST
    @Path("createPartner")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewTechnician(NewTechnicianRequest dto) {
        return adminRestService.createNewTechnician(dto);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getVendings")
    public Response getMachines() {
        return adminRestService.getMachines();
    }

    @Path("createVending")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewMachine(NewMachineRequest dto) {
        return adminRestService.createNewMachine(dto);
    }

    @GET
    @Path("getVendingProducts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMachineProducts(@QueryParam("machineId") String machineId) {
        return adminRestService.getMachineProducts(machineId);
    }

    @GET
    @Path("getVendingProductsContent")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMachineProductsContent(@QueryParam("machineId") String machineId) {
        return adminRestService.getMachineProductsContent(machineId);
    }

    @PUT
    @Path("updatePartnerMachine")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTechnicianMachine(@QueryParam("partnerId") String techId,
                                            @QueryParam("machineId") String machineId,
                                            @QueryParam("option") int option) {
        return adminRestService.updateTechnicianMachine(techId, machineId, option);
    }

    @POST
    @Path("addVendingProduct")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMachineProduct(@QueryParam("machineId") String machineId,
                                      @QueryParam("productId") String productId,
                                      @QueryParam("slotNumber") int slotNumber) {
        return adminRestService.addMachineProduct(machineId, productId, slotNumber);
    }

    @POST
    @Path("deleteVendingProduct")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMachineProduct(@QueryParam("vendingProductId") String id) {
        return adminRestService.deleteMachineProduct(id);
    }

    @GET
    @Path("getPartnerMachines")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPartnerMachines(@QueryParam("partnerId") String techId) {
        return adminRestService.getPartnerMachines(techId);
    }

    @Path("getPartnerMachinesContent")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTechnicianMachinesContent(@QueryParam("machineId") String technicianId) {
        return adminRestService.getTechnicianMachinesContent(technicianId);
    }

    @GET
    @Path("getUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getShakepointUsers() {
        return adminRestService.getShakepointUsers();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getIndexContent")
    public Response getIndexContent() {
        return adminRestService.getIndexContent();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getPerVendingContent")
    public Response getIndexPerMachineValues(@QueryParam("machineId") String from, @QueryParam("machineId") String to) {
        return adminRestService.getIndexPerMachineValues(from, to);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getTotalIncomeValues")
    public Response getTotalIncomeValues(@QueryParam("machineId") String from, @QueryParam("machineId") String to) {
        return adminRestService.getTotalIncomeValues(from, to);
    }

    @DELETE
    @Path("deleteMediaContent")
    public Response deleteMediaContent() {
        return adminRestService.deleteMediaContent();
    }

    @POST
    @Path("writePayWorksPayMode")
    public Response writePayWorksMode(@QueryParam("machineId") String mode) {
        return adminRestService.writePayWorksMode(mode);
    }

    @POST
    @Path("createPromoCode")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOpenPromoCode(CreatePromoCodeRequest request) {
        return adminRestService.createOpenPromoCode(request);
    }

    @GET
    @Path("getAllTrainers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTrainers() {
        return adminRestService.getAllTrainers();
    }


    @GET
    @Path("getActivePromos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivePromos() {
        return adminRestService.getActivePromos();
    }

    @POST
    @Path("createTrainer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createYTrainer(CreateTrainerRequest request) {
        return adminRestService.createTrainer(request);
    }

    @POST
    @Path("togglePromoCodeStatus")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response togglePromoCodeStatus(TogglePromoCodeStatusRequest request) {
        return adminRestService.togglePromoCodeStatus(request);
    }

    @POST
    @Path("deactivatePromoCode")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deactivatePromoCode(@QueryParam("promoCodeId")String promoId) {
        return adminRestService.deactivatePromoCode(promoId);
    }
}

