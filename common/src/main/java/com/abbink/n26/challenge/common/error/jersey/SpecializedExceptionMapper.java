package com.abbink.n26.challenge.common.error.jersey;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

public interface SpecializedExceptionMapper<E extends Throwable> {

    Response toResponse(E exception, HttpHeaders headers, HttpServletRequest request);

}
