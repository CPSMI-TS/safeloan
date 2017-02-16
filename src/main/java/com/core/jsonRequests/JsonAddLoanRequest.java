package com.core.jsonRequests;

import com.core.dbService.entities.Loan;
import com.core.dbService.entities.User;
import com.core.dbService.services.GroupService;
import com.core.dbService.services.LoanService;
import com.core.dbService.services.UserService;
import com.google.gson.Gson;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by t.konst on 13.02.2017.
 */
public class JsonAddLoanRequest {

    private ArrayList<User> loanUsers;

    private User payer;

    private Double sum;

    private String url;

    private Date returnDate;

    public JsonAddLoanRequest(ArrayList<User> loanUsers, User payer, Double sum, String url, Date returnDate) {
        this.loanUsers = loanUsers;
        this.payer = payer;
        this.sum = sum;
        this.url = url;
        this.returnDate = returnDate;
    }

    public ArrayList<User> getLoanUsers() {
        return loanUsers;
    }

    public void setLoanUsers(ArrayList<User> loanUsers) {
        this.loanUsers = loanUsers;
    }

    public User getPayer() {
        return payer;
    }

    public void setPayer(User payer) {
        this.payer = payer;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /*public static void main(String[] args) throws ParseException {
        UserService userService = new UserService();
        GroupService groupService = new GroupService();
        SimpleDateFormat dateformat2 = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String strdate2 = "02-04-2013 11:35:42";
            Date newdate = dateformat2.parse(strdate2);
            System.out.println(newdate);
        System.out.println(new JsonAddLoanRequest(groupService.getGroupUsers(6), userService.getUserById(11), 45.0, "google.com/pict.png", newdate));
        userService.stop();
        groupService.stop();
        LoanService loanService = new LoanService();
        Loan loan = loanService.getLoanById(34);
        System.out.println(loan.getLoanSum());
        loanService.stop();
    }*/

}
