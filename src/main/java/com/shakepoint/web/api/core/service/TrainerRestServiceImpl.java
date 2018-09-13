package com.shakepoint.web.api.core.service;

import com.shakepoint.web.api.core.repository.PromoCodeRepository;
import com.shakepoint.web.api.core.repository.UserRepository;
import com.shakepoint.web.api.core.service.security.AuthenticatedUser;
import com.shakepoint.web.api.core.service.security.RequestPrincipal;
import com.shakepoint.web.api.core.util.TransformationUtils;
import com.shakepoint.web.api.data.entity.PromoCode;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

public class TrainerRestServiceImpl implements TrainerRestService {

    @Inject
    private PromoCodeRepository promoCodeRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    @AuthenticatedUser
    private RequestPrincipal authenticatedUser;

    private Logger log = Logger.getLogger(getClass());

    @Override
    public Response getTrainerAssignedPromos() {
        //get all promo codes for user
        List<PromoCode> promoCodes = promoCodeRepository.getTrainerPromoCodes(authenticatedUser.getId());
        log.info(String.format("Got %d promo codes for trainer", promoCodes.size()));
        return Response.ok(TransformationUtils.createPromoCodes(promoCodes)).build();
    }
}
