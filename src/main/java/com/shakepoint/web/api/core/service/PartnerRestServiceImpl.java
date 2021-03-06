package com.shakepoint.web.api.core.service;

import com.shakepoint.integration.jms.client.handler.JmsHandler;
import com.shakepoint.web.api.core.repository.UserRepository;
import com.shakepoint.web.api.core.service.email.EmailAsyncSender;
import com.shakepoint.web.api.core.service.email.Template;
import com.shakepoint.web.api.core.service.security.AuthenticatedUser;
import com.shakepoint.web.api.core.service.security.CryptoService;
import com.shakepoint.web.api.core.service.security.RequestPrincipal;
import com.shakepoint.web.api.core.service.security.SecurityRole;
import com.shakepoint.web.api.core.util.ShakeUtils;
import com.shakepoint.web.api.core.util.TransformationUtils;
import com.shakepoint.web.api.data.dto.request.partner.CreateTrainerRequest;
import com.shakepoint.web.api.data.dto.response.partner.Trainer;
import com.shakepoint.web.api.data.entity.TrainerInformation;
import com.shakepoint.web.api.data.entity.User;
import com.shakepoint.web.api.data.internal.PartnerConversionType;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Date;
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

    private static final Logger log = Logger.getLogger(PartnerRestServiceImpl.class);

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
        TrainerInformation trainerInformation = new TrainerInformation();
        trainerInformation.setTrainerUser(user.getId());
        trainerInformation.setPartner(partner.getId());
        trainerInformation.setRegistrationDate(ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.format(new Date()));
        trainerInformation.setActive(true);
        userRepository.createTrainerInformation(trainerInformation);

        Map<String, Object> emailParams = new HashMap<>();
        emailParams.put("trainerName", user.getName());
        emailParams.put("gymName", partner.getName());
        emailParams.put("email", user.getEmail());
        emailParams.put("pass", request.getPassword());
        emailParams.put("token", user.getAccessToken());

        //send email to trainer with welcome and confirmation link
        if (user.isEmailsEnabled()) {
            emailSender.sendEmail(user.getEmail(), Template.NEW_TRAINER, emailParams);
        }

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

    @Override
    public Response searchTrainers(String email) {
        if (email == null || email.isEmpty()) {
            return Response.ok(Collections.emptyList()).build();
        }
        //search trainers
        List<User> members = userRepository.searchMembersByEmail(email);
        //transform
        List<Trainer> trainers = TransformationUtils.createTrainers(members);
        return Response.ok(trainers).build();
    }

    @Override
    public Response convertUser(String memberId, String convertType) {
        //get member
        User user = userRepository.get(memberId);

        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        //convert convert type
        PartnerConversionType type = PartnerConversionType.fromString(convertType);
        if (type == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        //change role to user
        switch (type) {
            case MEMBER_TO_TRAINER:
                user.setRole(SecurityRole.TRAINER.toString());
                //create a new relationship between partner and member
                TrainerInformation trainerInformation = new TrainerInformation();
                trainerInformation.setRegistrationDate(ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.format(new Date()));
                trainerInformation.setPartner(requestPrincipal.getId());
                trainerInformation.setTrainerUser(user.getId());
                userRepository.createTrainerInformation(trainerInformation);
                //TODO: send email with welcome here
                break;
            case TRAINER_TO_MEMBER:
                user.setRole(SecurityRole.MEMBER.toString());
                //delete trainer information
                TrainerInformation existingTrainerInformation = userRepository.getTrainerInfoForPartner(memberId, requestPrincipal.getId());
                if (existingTrainerInformation == null) {
                    log.warn(String.format("No existing trainer information for user %s, bad request", user.getName()));
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                existingTrainerInformation.setActive(false);
                userRepository.updateTrainerInformation(existingTrainerInformation);
                //TODO: send email? Yes, is a question
                break;
        }
        userRepository.updateUser(user);

        return Response.ok().build();
    }
}
