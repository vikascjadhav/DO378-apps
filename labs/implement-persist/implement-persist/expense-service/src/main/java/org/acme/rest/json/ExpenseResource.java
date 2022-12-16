package org.acme.rest.json;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
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

@Path("/expenses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExpenseResource {

    @Inject
    ExpenseService service;

    @GET
    public List<Expense> list() {
        return service.list();
    }

    @POST
    public Expense create(final Expense expense) {

        Expense newExpense = Expense.of(expense.name, expense.paymentMethod, expense.amount.toString());
        service.create(newExpense);

        return newExpense;
    }

    @DELETE
    @Path("{uuid}")
    public List<Expense> delete(@PathParam("uuid") final UUID uuid) {
        boolean deleted = service.delete(uuid);

        if (!deleted) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return service.list();
    }

    @PUT
    public void update(final Expense expense) {
        service.update(expense);
    }
}