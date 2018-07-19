package com.shakepoint.web.api.resource.mapper;

import com.shakepoint.web.api.data.dto.response.ErrorResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class StandardExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Ha ocurrido un error inesperado, intenta de nuevo"))
                .build();
    }
}
