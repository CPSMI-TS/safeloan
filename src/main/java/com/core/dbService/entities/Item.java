package com.core.dbService.entities;

import com.google.gson.Gson;

import javax.persistence.*;

/**
 * Created by t.konst on 22.01.2017.
 */
@Entity(name = "item")
public class Item {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer item_id;

    //// TODO: 22.01.2017 foreign key constraint
    @JoinColumn(name = "loan_id")
    @Column(name = "item_loan", nullable = false)
    private Integer item_loan;

    @Column(name = "item_cost", nullable = false)
    private Double item_cost;

    @Column(name = "item_name", nullable = false, length = 50)
    private String item_name;

    @Column(name = "item_category", length = 20)
    private String item_category;

    public Item() {
    }

    public Item(Integer item_loan, String item_name, Double item_cost, String item_category) {
        this.item_loan = item_loan;
        this.item_cost = item_cost;
        this.item_name = item_name;
        this.item_category = item_category;
    }

    public Item(String item_name, Double item_cost, String item_category) {
        this.item_cost = item_cost;
        this.item_name = item_name;
        this.item_category = item_category;
    }

    public Integer getItemId() {
        return item_id;
    }

    public Integer getItemLoan() {
        return item_loan;
    }

    public String getItemName() {
        return item_name;
    }

    public String getItemCategory() {
        return item_category;
    }

    public Double getItemCost() {
        return item_cost;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
