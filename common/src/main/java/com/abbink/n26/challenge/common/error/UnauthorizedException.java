package com.abbink.n26.challenge.common.error;

import javax.ws.rs.core.Response.Status;

public class UnauthorizedException extends WebAppError {

    public UnauthorizedException(int code, String message) {
        super(code, message, Status.UNAUTHORIZED);
    }

    public UnauthorizedException(int code, String message, Throwable cause) {
        super(code, message, Status.UNAUTHORIZED, cause);
    }
}
