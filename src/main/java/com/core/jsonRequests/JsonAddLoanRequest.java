package com.core.jsonRequests;

import com.core.dbService.entities.User;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by t.konst on 13.02.2017.
 */
public class JsonAddLoanRequest {

    private ArrayList<User> loanUsers;

    private User payer;

    private Double sum;

    public JsonAddLoanRequest(ArrayList<User> loanUsers, User payer, Double sum) {
        this.loanUsers = loanUsers;
        this.payer = payer;
        this.sum = sum;
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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
