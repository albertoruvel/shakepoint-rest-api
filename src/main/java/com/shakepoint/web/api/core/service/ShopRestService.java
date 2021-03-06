package com.shakepoint.web.api.core.service;

import com.shakepoint.web.api.data.dto.request.ConfirmPurchaseRequest;
import com.shakepoint.web.api.data.dto.request.ContactRequest;
import com.shakepoint.web.api.data.dto.request.FcmTokenRequest;
import com.shakepoint.web.api.data.dto.request.UserProfileRequest;
import com.shakepoint.web.api.data.dto.request.ValidatePromoCodeRequest;

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

    public Response getProductDetails(String productId, String vendingId, int slotNumber);

    public Response getAvailablePurchaseForMachine(String productId, String machineId, Integer slot);

    public Response validatePromoCode(ValidatePromoCodeRequest request);

    public Response deactivateUser();

    public Response activateUser();

    public Response setNotificationsEnabled(boolean enabled);

    public Response setEmailsEnabled(boolean enabled);

    public Response contact(ContactRequest request);

    public Response setFcmToken(FcmTokenRequest request);
}
