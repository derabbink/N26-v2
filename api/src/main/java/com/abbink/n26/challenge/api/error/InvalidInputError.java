package com.abbink.n26.challenge.api.error;

import javax.ws.rs.core.Response.Status;

import com.abbink.n26.challenge.common.error.WebAppError;

public class InvalidInputError extends WebAppError {

    public InvalidInputError() {
        super(100, "Invalid input.", Status.NOT_ACCEPTABLE);
    }

    public InvalidInputError(Throwable cause) {
        super(100, "Invalid input.", Status.NOT_ACCEPTABLE, cause);
    }
}
