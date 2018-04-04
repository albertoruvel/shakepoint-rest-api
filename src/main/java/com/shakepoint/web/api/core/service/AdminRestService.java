package com.shakepoint.web.api.core.service;

import com.shakepoint.web.api.data.dto.request.admin.NewMachineRequest;
import com.shakepoint.web.api.data.dto.request.admin.NewProductRequest;
import com.shakepoint.web.api.data.dto.request.admin.NewTechnicianRequest;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.core.Response;

public interface AdminRestService {
    public Response getProducts();

    public Response createNewProduct(MultipartFormDataInput multipart);

    public Response getTechnicians();

    public Response createNewTechnician(NewTechnicianRequest dto);


    public Response getMachines();

    public Response createNewMachine(NewMachineRequest dto);

    public Response getMachineProducts(String machineId);

    public Response getMachineProductsContent(String machineId);

    public Response updateTechnicianMachine(String techId, String machineId, int option);

    public Response addMachineProduct(String machineId, String productId, int slotNumber);

    public Response deleteMachineProduct(String id);

    public Response getPartnerMachines(String techId);

    public Response getTechnicianMachinesContent(String technicianId);

    public Response getShakepointUsers();

    public Response getIndexContent();

    public Response getIndexPerMachineValues(String from, String to);

    public Response getTotalIncomeValues(String from, String to);

    public Response deleteMediaContent();

    public Response writePayWorksMode(String mode);
}
