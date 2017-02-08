package com.core.dbService.entities;

import com.google.gson.Gson;

import javax.persistence.*;

/**
 * Created by t.konst on 23.01.2017.
 */

@Entity(name = "loan_users")
public class LoanUsers {

    @Id
    @Column(name = "loan_users_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer loan_users_id;

    //@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    //@JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @Column(name = "user_id", nullable = false)
    private Integer user_id;

    //@OneToOne(targetEntity = Loan.class, fetch = FetchType.EAGER)
    //@JoinColumn(name = "loan_id", referencedColumnName = "loan_id")
    @Column(name = "loan_id", nullable = false)
    private Integer loan_id;

    @Column(name = "loan_user_state", nullable = false)
    private Integer loan_user_state;

    @Column(name = "loan_user_share", nullable = false)
    private Double loan_user_share;

    @Column(name = "is_payer", nullable = false)
    private Integer is_payer;

    public LoanUsers() {
    }

    public LoanUsers(Integer user_id, Integer loan_id, Integer loan_user_state, Integer is_payer) {
        this.user_id = user_id;
        this.loan_id = loan_id;
        if (loan_user_state == null) {
            this.loan_user_state = 0;
        } else {
            this.loan_user_state = loan_user_state;
        }
        this.is_payer = is_payer;
    }

    public LoanUsers(Integer user_id, Integer loan_id, Integer loan_user_state, Double loan_user_share, Integer is_payer) {
        this.user_id = user_id;
        this.loan_id = loan_id;
        if (loan_user_state == null) {
            this.loan_user_state = 0;
        } else {
            this.loan_user_state = loan_user_state;
        }
        this.loan_user_share = loan_user_share;
        this.is_payer = is_payer;
    }

    public Integer getUsersLoansId() {
        return loan_users_id;
    }

    public Integer getUserId() {
        return user_id;
    }

    public Integer getLoanId() {
        return loan_id;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
