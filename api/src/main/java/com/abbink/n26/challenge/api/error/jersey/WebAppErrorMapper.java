package com.abbink.n26.challenge.api.error.jersey;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.abbink.n26.challenge.api.error.JsonError;
import com.abbink.n26.challenge.common.error.WebAppError;
import com.abbink.n26.challenge.common.error.jersey.SpecializedExceptionMapper;

public class WebAppErrorMapper implements SpecializedExceptionMapper<WebAppError> {

    @Override
    public Response toResponse(WebAppError exception, HttpHeaders headers, HttpServletRequest request) {
        return Response
                .status(exception.getResponse().getStatus())
                .entity(new JsonError(exception.getCode(), exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
