package com.shakepoint.web.api.core.service;

import com.shakepoint.integration.jms.client.handler.JmsHandler;
import com.shakepoint.web.api.core.repository.MachineRepository;
import com.shakepoint.web.api.core.repository.ProductRepository;
import com.shakepoint.web.api.core.repository.PurchaseRepository;
import com.shakepoint.web.api.core.repository.UserRepository;
import com.shakepoint.web.api.core.service.email.EmailAsyncSender;
import com.shakepoint.web.api.core.service.security.AuthenticatedUser;
import com.shakepoint.web.api.core.service.security.RequestPrincipal;
import com.shakepoint.web.api.core.shop.PayWorksClientService;
import com.shakepoint.web.api.core.util.TransformationUtils;
import com.shakepoint.web.api.data.dto.request.ConfirmPurchaseRequest;
import com.shakepoint.web.api.data.dto.request.UserProfileRequest;
import com.shakepoint.web.api.data.dto.response.MachineSearch;
import com.shakepoint.web.api.data.dto.response.ProductDTO;
import com.shakepoint.web.api.data.dto.response.PurchaseCodeResponse;
import com.shakepoint.web.api.data.entity.Product;
import com.shakepoint.web.api.data.entity.VendingMachine;
import com.shakepoint.web.api.data.entity.VendingMachineProductStatus;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class ShopRestServiceImpl implements ShopRestService {


    @Inject
    private UserRepository userRepository;

    @Inject
    private MachineRepository machineRepository;

    @Inject
    private PurchaseRepository purchaseRepository;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private JmsHandler jmsHandler;

    @Inject
    private EmailAsyncSender emailSender;

    @Inject
    private PayWorksClientService payWorksClientService;

    @Inject
    @AuthenticatedUser
    private RequestPrincipal authenticatedUser;

    private final Logger LOG = Logger.getLogger(getClass());
    private static final String RETRY_UPLOAD_QUEUE_NAME = "retry_file_upload";

    @Override
    public Response searchMachine(double longitude, double latitude) {
        //get all machines
        List<VendingMachine> machines = machineRepository.getMachines();
        LOG.info(String.format("Found %d registered machines", machines.size()));
        MachineSearch search = new MachineSearch();
        double distance = 1000000; // high distance to get accurate results
        String[] array = null;
        int currentIndex = 0;
        for (int i = 0; i < machines.size(); i++) {
            if (machines.get(i).getLocation() == null || machines.get(i).getLocation().isEmpty())
                continue;
            array = machines.get(i).getLocation().split(",");
            double long1 = Double.parseDouble(array[0]);
            double lat1 = Double.parseDouble(array[1]);
            //get distance
            double tmpDistance = distance(lat1, long1, latitude, longitude);
            if (tmpDistance < distance) {
                distance = tmpDistance;
                currentIndex = i;
            }

        }
        VendingMachine machine = machines.get(currentIndex);
        LOG.info(String.format("Got machine %s from distance search", machine.getName()));
        search.setMachineId(machine.getId());
        search.setMachineName(machine.getName());
        search.setDistance(distance);
        return Response.ok(search).build();
    }

    @Override
    public Response getMachineProducts(String machineId) {
        if (machineId == null || machineId.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<VendingMachineProductStatus> productsStatus = machineRepository.getMachineProducts(machineId);
        List<Product> products = new ArrayList();
        for (VendingMachineProductStatus p : productsStatus) {
            products.add(p.getProduct());
        }
        List<ProductDTO> productsDTO = TransformationUtils.createProducts(products);
        return Response.ok(productsDTO).build();
    }

    @Override
    public Response getActiveQrCodes(String machineId) {
        //get user id
        List<PurchaseCodeResponse> page = TransformationUtils.createPurchaseCodes(purchaseRepository.getAuthorizedPurchases(authenticatedUser.getId(), machineId));
        return Response.ok(page).build();
    }

    @Override
    public Response confirmPurchase(ConfirmPurchaseRequest request) {
        return null;
    }

    @Override
    public Response getUserPurchases() {
        return null;
    }

    @Override
    public Response getUserProfile() {
        return null;
    }

    @Override
    public Response saveProfile(UserProfileRequest request) {
        return null;
    }

    @Override
    public Response searchMachinesByName(String machineName) {
        return null;
    }

    @Override
    public Response getProductDetails(String productId) {
        return null;
    }

    @Override
    public Response getAvailablePurchaseForMachine(String productId, String machineId) {
        return null;
    }


    /**
     * Get distance in mts from 2 points
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @param
     * @return
     */
    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        return (dist);
    }


    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts decimal degrees to radians						 :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts radians to decimal degrees						 :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
