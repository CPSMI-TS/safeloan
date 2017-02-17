package com.core.controllers;

import com.core.dbService.entities.User;
import com.core.dbService.services.GroupService;
import com.core.dbService.services.UserService;
import com.core.jsonRequests.JsonAddLoanRequest;
import com.google.gson.Gson;
import com.core.dbService.entities.Loan;
import com.core.dbService.services.LoanService;
import com.sun.jersey.api.uri.UriBuilderImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by t.konst on 04.02.2017.
 */
@Path("loans")
public class Loans {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getLoans() {
        LoanService loanService = new LoanService();
        Loan loan;
        String response = "{\"loans\": [";
        for (int i = 1; i <= 3; i++) {
            loan = loanService.getLoanById(i);
            if (loan != null) {
                response = (i == 3 ? response.concat(loan.toString()) : response.concat(loan.toString() + ", "));
            } else {
                i--;
            }
        }
        response = response.concat("]}");
        loanService.stop();
        return response;
    }

    @GET
    @Path("{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLoan(@PathParam("id") int id) {
        LoanService loanService = new LoanService();
        Loan loan = loanService.getLoanById(id);
        if (loan == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        loanService.stop();
        return Response.ok(loan.toString(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("{id: \\d+}/payer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLoanPayer(@PathParam("id") int id) {
        LoanService loanService = new LoanService();
        User payer = loanService.getLoanPayer(id);
        if (payer == null) {
            loanService.stop();
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        loanService.stop();
        return Response.ok(payer.toString(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/add_plain")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addLoanPlain(@QueryParam("group") int id, @QueryParam("payer") int payer, @QueryParam("sum") double sum) {
        GroupService groupService = new GroupService();
        ArrayList<User> users = groupService.getGroupUsers(id);
        if (!groupService.checkGroupExists(id)) {
            groupService.stop();
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else Users.checkUserExists(payer);
        groupService.stop();
        if (!Users.isGroupMember(payer, id))
            return Response.status(Response.Status.PRECONDITION_FAILED).build();
        LoanService loanService = new LoanService();
        Integer loanId = loanService.addLoanAndUsers(users, payer, sum);
        Loan loan = loanService.getLoanById(loanId);
        loanService.stop();
        if (loanId == null)
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        return Response.ok(loan.toString(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addLoan(String loanRequest) {
        Gson gson = new Gson();
        JsonAddLoanRequest request = gson.fromJson(loanRequest, JsonAddLoanRequest.class);
        ArrayList<User> users = request.getLoanUsers();
        LoanService loanService = new LoanService();
        User payer = request.getPayer();
        Double sum = request.getSum();
        if (users == null || users.isEmpty() || payer == null || sum < 10.0) {
            loanService.stop();
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        Integer loanId = loanService.addLoanUsersPicture(users, payer.getUserId(), sum, request.getUrl(), request.getReturnDate());
        Loan loan = loanService.getLoanById(loanId);
        loanService.stop();
        if (loan == null)
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        return Response.created(URI.create("/" + loanId)).entity(loan.toString()).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("{id: \\d+}/participants")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGroupUsers(@PathParam("id") int id) {
        LoanService loanService = new LoanService();
        UserService userService = new UserService();
        if (!loanService.checkLoanExists(id)) {
            loanService.stop();
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        List<User> users = loanService.getLoanUsers(id);
        if (users == null || users.isEmpty()) {
            loanService.stop();
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        loanService.stop();
        userService.stop();
        return Response.ok(users.toString(), MediaType.APPLICATION_JSON).build();
    }

/*todo: авторизация решит проблему проверки прав юзера на эти действия
    @GET
    @Path("{id: \\d+}/return")
    @Produces(MediaType.APPLICATION_JSON)
    public String returnLoan(@PathParam("id") int id, @QueryParam("user") String login) {
        LoanService loanService = new LoanService();
        boolean success = loanService.changeState(LoanState.WAITING_CONFIRMATION, id);
        if (success == false) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Loan loan = loanService.getLoanById(id);
        loanService.stop();
        return "{\"loan\": " + new Gson().toJson(loan) + "}";
    }

    @GET
    @Path("{id: \\d+}/confirm")
    @Produces(MediaType.APPLICATION_JSON)
    public String confirmReturn(@PathParam("id") int id, @QueryParam("payer") String login) {
        LoanService loanService = new LoanService();
        boolean success = loanService.changeState(LoanState.WAITING_CONFIRMATION, id);
        if (success == false) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Loan loan = loanService.getLoanById(id);
        loanService.stop();
        return "{\"loan\": " + new Gson().toJson(loan) + "}";
    }
*/
}
