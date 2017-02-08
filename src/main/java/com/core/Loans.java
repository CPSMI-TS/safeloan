package com.core;

import com.core.dbService.services.GroupService;
import com.google.gson.Gson;
import com.core.dbService.entities.Loan;
import com.core.dbService.services.LoanService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

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

}
