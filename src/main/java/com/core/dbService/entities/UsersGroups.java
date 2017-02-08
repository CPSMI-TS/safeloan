package com.core.dbService.entities;

import com.google.gson.Gson;

import javax.persistence.*;

/**
 * Created by t.konst on 21.01.2017.
 */

@Entity(name = "users_groups")
public class UsersGroups {
    @Id
    @Column(name = "users_groups_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer users_groups_id;

    //// TODO: 22.01.2017 foreign key constraint
    //@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    //@JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @Column(name = "user_id", nullable = false)
    private Integer user_id;

    //// TODO: 22.01.2017 foreign key constraint
    //@OneToOne(targetEntity = Group.class, fetch = FetchType.EAGER)
    //@JoinColumn(name = "group_id", referencedColumnName = "group_id")
    @Column(name = "group_id", nullable = false)
    private Integer group_id;

    public UsersGroups() {
    }

    public UsersGroups(Integer user_id, Integer group_id) {
        this.user_id = user_id;
        this.group_id = group_id;
    }

    public Integer getUsersGroupsId() {
        return users_groups_id;
    }

    public Integer getUserId() {
        return user_id;
    }

    public Integer getGroupId() {
        return group_id;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
