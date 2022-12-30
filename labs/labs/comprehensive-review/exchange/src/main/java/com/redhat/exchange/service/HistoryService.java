package com.redhat.exchange.service;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.exchange.dto.Exchange;
import com.redhat.exchange.dto.ExchangeRequest;

import java.util.List;

// Path on the service we're calling
@Path("/")
@RegisterRestClient
public interface HistoryService {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    List<Exchange> getCurrencyExchangeRates(ExchangeRequest body);
}
