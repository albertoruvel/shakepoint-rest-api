package com.shakepoint.web.api.core.service;

import com.shakepoint.web.api.data.dto.request.partner.CreateTrainerRequest;

import javax.ws.rs.core.Response;

public interface PartnerRestService {

    public Response createTrainer(CreateTrainerRequest request);

    public Response getTrainers();
}
