package com.core.dbService.entities;

import com.google.gson.Gson;

import javax.persistence.*;

/**
 * Created by t.konst on 21.01.2017.
 */

@Entity(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Integer user_id;

    @Column(name = "user_login", unique = true, updatable = false, nullable = false, length = 20)
    private String user_login;

    @Column(name = "user_password", nullable = false, length = 25)
    private String password;

    @Column(name = "user_debt", nullable = false)
    private Double debt;

    public User() {
    }

    public User(String login, String password, Double debt) {
        this.user_login = login;
        this.password = password;
        this.debt = debt;
    }

    public Integer getUserId() {
        return user_id;
    }

    public String getUserLogin() {
        return user_login;
    }

    public String getPassword() {
        return password;
    }

    public Double getDebt() {
        return debt;
    }

    public void setUserLogin(String user_login) {
        this.user_login = user_login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDebt(Double debt) {
        this.debt = debt;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
