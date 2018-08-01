package com.shakepoint.web.api.core.service;

import com.shakepoint.integration.jms.client.handler.JmsHandler;
import com.shakepoint.web.api.core.repository.UserRepository;
import com.shakepoint.web.api.core.service.email.EmailAsyncSender;
import com.shakepoint.web.api.core.service.email.Template;
import com.shakepoint.web.api.core.service.security.AuthenticatedUser;
import com.shakepoint.web.api.core.service.security.CryptoService;
import com.shakepoint.web.api.core.service.security.RequestPrincipal;
import com.shakepoint.web.api.core.util.ShakeUtils;
import com.shakepoint.web.api.core.util.TransformationUtils;
import com.shakepoint.web.api.data.dto.request.partner.CreateTrainerRequest;
import com.shakepoint.web.api.data.dto.response.partner.Trainer;
import com.shakepoint.web.api.data.entity.PartnerTrainer;
import com.shakepoint.web.api.data.entity.User;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartnerRestServiceImpl implements PartnerRestService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private EmailAsyncSender emailSender;

    @Inject
    private JmsHandler jmsHandler;

    @Inject
    private CryptoService cryptoService;

    @Inject
    @AuthenticatedUser
    private RequestPrincipal requestPrincipal;

    @Override
    public Response createTrainer(CreateTrainerRequest request) {
        //validate
        if (request == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        User user = TransformationUtils.createUserFromTrainerRequest(request, ShakeUtils.getNextSessionToken(), cryptoService);
        userRepository.addShakepointUser(user);

        User partner = userRepository.get(requestPrincipal.getId());

        //create relationships
        PartnerTrainer partnerTrainer = new PartnerTrainer();
        partnerTrainer.setPartner(partner);
        partnerTrainer.setTrainer(user);
        userRepository.addPartnerTrainer(partnerTrainer);

        Map<String, Object> emailParams = new HashMap<>();
        emailParams.put("trainerName", user.getName());
        emailParams.put("token", user.getAccessToken());

        //send email to trainer with welcome and confirmation link
        emailSender.sendEmail(user.getEmail(), Template.NEW_TRAINER, emailParams);

        return Response.ok().build();
    }

    @Override
    public Response getTrainers() {
        //get all users that belongs to partner
        List<User> trainers = userRepository.getTrainersForPartner(requestPrincipal.getId());
        //create dto's
        List<Trainer> trainersDto = TransformationUtils.createTrainers(trainers);
        return Response.ok(trainersDto).build();
    }
}