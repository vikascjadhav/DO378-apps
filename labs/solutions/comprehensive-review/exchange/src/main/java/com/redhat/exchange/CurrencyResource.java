package com.redhat.exchange;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.exchange.service.CurrencyService;

import java.util.List;

@Path("/currencies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CurrencyResource {

    @ConfigProperty(name = "feature.external_currencies", defaultValue = "false")
    boolean externalCurrencies;

    @Inject
    @RestClient
    CurrencyService currencies;

    @Inject
    io.micrometer.core.instrument.MeterRegistry registry;

    @GET
    @Fallback(fallbackMethod = "getDefaultCurrencyNames")
    @Timeout
    public List<String> getCurrencyNames() {
        if (externalCurrencies)
            return currencies.getCurrencyNames();
        else
            return getDefaultCurrencyNames();
    }

    public String getCurrencySign(final String currencyName){
        registry.counter("currencysign.count").increment();

        if (externalCurrencies) 
            return currencies.getCurrency(currencyName).sign;
         else 
            return getDefaultCurrencySign(currencyName);
    }

    public List<String> getDefaultCurrencyNames() {
        return List.of("EUR", "USD", "JPY");
    }

    public String getDefaultCurrencySign(final String currencyName) {
        switch (currencyName){
            case "EUR": return "€";
            case "USD": return "$";
            case "JPY": return "¥";
        }
        return null;
    }
}
