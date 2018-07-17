package com.shakepoint.web.api.core.service;

import com.shakepoint.web.api.data.dto.request.ConfirmPurchaseRequest;
import com.shakepoint.web.api.data.dto.request.UserProfileRequest;

import javax.ws.rs.core.Response;

public interface ShopRestService {
    public Response searchMachine(double longitude, double latitude);

    public Response getMachineProducts(String machineId);

    public Response getActiveQrCodes(String machineId);

    public Response confirmPurchase(ConfirmPurchaseRequest request);

    public Response getUserPurchases();

    public Response getUserProfile();

    public Response saveProfile(UserProfileRequest request);

    public Response searchMachinesByName(String machineName);

    public Response getProductDetails(String productId);

    public Response getAvailablePurchaseForMachine(String productId, String machineId);
}