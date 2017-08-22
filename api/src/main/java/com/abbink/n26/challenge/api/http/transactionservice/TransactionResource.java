package com.abbink.n26.challenge.api.http.transactionservice;

import static com.abbink.n26.challenge.api.utils.Constants.BASE_PATH;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.extern.slf4j.Slf4j;

import com.abbink.n26.challenge.api.http.JsonTransaction;
import com.abbink.n26.challenge.common.jersey.aop.OverrideInputType;
import com.abbink.n26.challenge.service.TransactionService;

@Slf4j
@Singleton
@Path(BASE_PATH + "transactions")
public class TransactionResource {
    @Inject private TransactionService transactionService;

    @POST
//	@Consumes(MediaType.APPLICATION_JSON)
    @OverrideInputType(MediaType.APPLICATION_JSON)
    public Response post(JsonTransaction transaction) {
        log.trace("POST {}, {}", transaction.getAmount(), transaction.getTimestamp());
        try {
            transactionService.add(transaction.toTransaction());
        } catch (TransactionService.TransactionExpiredError e) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.CREATED).build();
    }

}
