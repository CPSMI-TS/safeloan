package com.core.dbService.entities;

import com.google.gson.Gson;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by t.konst on 22.01.2017.
 */

@Entity(name = "loan")
public class Loan {
    @Id
    @Column(name = "loan_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer loan_id;

    //// TODO: 22.01.2017 normal foreign key constraint
    //@JoinColumn(name = "user_id")
    @Column(name = "loan_payer", nullable = false)
    private Integer loan_payer;

    //todo: add precision
    @Column(name = "loan_sum", nullable = false)
    private Double loan_sum;

    @Column(name = "users_count", nullable = false)
    private Integer users_count;

    @Column(name = "loan_state")
    private Integer loan_state;

    @Column(name = "loan_description", nullable = true, length = 100)
    private String loan_description;

    @Temporal(TemporalType.DATE)
    @Column(name = "loan_date")
    private Date loan_date;

    @Temporal(TemporalType.DATE)
    @Column(name = "return_date")
    private Date return_date;

    @Column(name = "loan_url", length = 255)
    private String loan_url;

    @Column(name = "extended")
    private Integer extended;


    public Loan() {
    }

    public Loan(Integer payer) {
        this.loan_payer = payer;
        this.extended = 1;
        this.loan_sum = 0.0;
        this.loan_state = 0;
        //this.loan_description = description;
    }

    public Loan(Integer payer, Double sum, Integer usersCount) {
        this.loan_payer = payer;
        this.extended = 0;
        this.loan_sum = sum;
        this.loan_state = 0;
        this.users_count = usersCount;
        //this.loan_description = description;
    }

    public Loan(Integer payer, Double sum, Integer usersCount, String url) {
        this.loan_payer = payer;
        this.extended = 0;
        this.loan_sum = sum;
        this.loan_state = 0;
        this.users_count = usersCount;
        this.loan_url = url;
    }

    public Loan(Integer payer, Double sum, Integer usersCount, String url, Date returnDate) {
        this.loan_payer = payer;
        this.extended = 0;
        this.loan_sum = sum;
        this.loan_state = 0;
        this.users_count = usersCount;
        this.loan_url = url;
        this.return_date = returnDate;
    }

    public Integer getLoanId() {
        return loan_id;
    }

    public Integer getPayer() {
        return loan_payer;
    }

    public Double getLoanSum() {
        return loan_sum;
    }

    public Integer getUsersCount() {
        return users_count;
    }

    public Integer getLoanState() {
        return loan_state;
    }

    public String getLoanDescription() {
        return loan_description;
    }

    public Date getLoanDate() {
        return loan_date;
    }

    public String getLoanUrl() {
        return loan_url;
    }

    public Integer getExtended() {
        return extended;
    }

    public void setPayer(Integer payer) {
        this.loan_payer = payer;
    }

    public void setLoanSum(Double sum) {
        this.loan_sum = sum;
    }

    public void setUsersCount(Integer users_count) {
        this.users_count = users_count;
    }

    public void setLoanState(Integer loan_state) {
        this.loan_state = loan_state;
    }

    public void setLoanDescription(String loan_description) {
        this.loan_description = loan_description;
    }

    public void setLoanDate(Date loan_date) {
        this.loan_date = loan_date;
    }

    public Date getReturnDate() {
        return return_date;
    }

    public void setReturnDate(Date return_date) {
        this.return_date = return_date;
    }

    public void setLoanUrl(String loan_url) {
        this.loan_url = loan_url;
    }

    public void setExtended(Integer extended) {
        this.extended = extended;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
