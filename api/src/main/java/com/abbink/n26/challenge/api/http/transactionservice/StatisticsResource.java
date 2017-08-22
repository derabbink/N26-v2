package com.abbink.n26.challenge.api.http.transactionservice;

import com.abbink.n26.challenge.api.http.JsonStatistics;
import com.abbink.n26.challenge.service.TransactionService;
import com.abbink.n26.challenge.service.data.Statistics;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.abbink.n26.challenge.api.utils.Constants.BASE_PATH;

@Slf4j
@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Path(BASE_PATH + "statistics")
public class StatisticsResource {
    @Inject
    private TransactionService transactionService;

    @GET
    public JsonStatistics get() {
        Statistics stats = transactionService.getStatistics();
        return new JsonStatistics(
                stats.getAvg(),
                stats.getCount(),
                stats.getMax(),
                stats.getMin(),
                stats.getSum());
    }
}
