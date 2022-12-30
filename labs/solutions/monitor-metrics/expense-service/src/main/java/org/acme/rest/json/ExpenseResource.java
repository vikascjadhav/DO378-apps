package org.acme.rest.json;

import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.time.StopWatch;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.function.Supplier;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Tags;

@Path("/expenses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExpenseResource {
    @Inject
    public ExpenseService expenseService;

    @Inject
    public MeterRegistry registry;

    private Counter listCounter;
    private Supplier<List<Expense>> listTimer;

    private final StopWatch stopWatch = StopWatch.createStarted();

    @PostConstruct
    public void initMeters() {
        registry.gauge("timeToLastExpenseList", 
                Tags.of("description", "Time from the last time expenses list was retrieved"),
                stopWatch, StopWatch::getTime);
        listCounter = registry.counter("callsToExpenseList",
                "description", "How many requests to list expenses have happened.");
        listTimer = registry.timer("expenseListTimer", 
                "description", "How long it takes to list expenses.")
                .wrap((Supplier<List<Expense>>) expenseService::list);
    }

    @GET
    public List<Expense> list() {
        listCounter.increment();
        stopWatch.reset();
        stopWatch.start();
        return listTimer.get();
    }

    @POST
    @Transactional
    public Expense create(final Expense expense) {
        registry.counter("callsToExpenseCreate").increment();
        return registry.timer("expenseCreateTimer").wrap((Supplier<Expense>) () -> expenseService.create(expense)).get();
    }

    @DELETE
    @Path("{uuid}")
    @Transactional
    public List<Expense> delete(@PathParam("uuid") UUID uuid) {
        return expenseService.delete(uuid);
    }

    @PUT
    @Transactional
    public void update(Expense expense) {
        if (expenseService.exists(expense.uuid)) {
            expenseService.update(expense);
        } else {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }
}