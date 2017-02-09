package com.core;

import com.core.dbService.entities.User;
import com.core.dbService.services.GroupService;
import com.core.dbService.services.UserService;
import com.google.gson.Gson;
import com.core.dbService.entities.Loan;
import com.core.dbService.services.LoanService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public String getLoan(@PathParam("id") int id) {
        LoanService loanService = new LoanService();
        Loan loan = loanService.getLoanById(id);
        if (loan == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        loanService.stop();
        return "{\"loan\": " + new Gson().toJson(loan) + "}";
    }

    @GET
    @Path("{id: \\d+}/payer")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLoanPayer(@PathParam("id") int id) {
        LoanService loanService = new LoanService();
        User user = loanService.getLoanPayer(id);
        if (user == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        loanService.stop();
        return "{\"payer\": " + new Gson().toJson(user) + "}";
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addLoan(@QueryParam("group") int id, @QueryParam("payer") int payer, @QueryParam("sum") double sum) {
        GroupService groupService = new GroupService();
        List<User> users = groupService.getGroupUsers(id);
        LoanService loanService = new LoanService();
        Integer loanId = loanService.addLoanAndUsers(users, payer, sum);
        if (loanId == null)
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        groupService.stop();
        loanService.stop();
        return Response.ok().build();
    }

    @GET
    @Path("{id: \\d+}/participants")
    @Produces(MediaType.APPLICATION_JSON)
    public String getGroupUsers(@PathParam("id") int id) {
        LoanService loanService = new LoanService();
        UserService userService = new UserService();
        if (!loanService.checkLoanExists(id))
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        List<User> users = loanService.getLoanUsers(id);
        if (users == null || users.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        String response = "{\"users\": [";
        int i = 0;
        for (User user : users) {
            i++;
            response = (i == users.size() ? response.concat(user.toString()) : response.concat(user.toString() + ", "));
        }
        response = response.concat("]}");
        loanService.stop();
        userService.stop();
        return response;
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
