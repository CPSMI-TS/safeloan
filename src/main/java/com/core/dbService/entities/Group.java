package com.core.dbService.entities;

import com.google.gson.Gson;

import javax.persistence.*;

/**
 * Created by t.konst on 21.01.2017.
 */
@Entity(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "group_id", unique = true, nullable = false)
    private Integer group_id;

    @Column(name = "group_name", nullable = false, unique = true, length = 50)
    private String group_name;

    @Column(name = "group_type", nullable = false, length = 20)
    private String group_type;

    public Group() {

    }

    public Group(String group_name, String group_type) {
        this.group_name = group_name;
        this.group_type = group_type;
    }

    public Integer getGroupId() {
        return group_id;
    }

    public String getGroupName() {
        return group_name;
    }

    public String getGroupType() {
        return group_type;
    }

    public void setGroupId(Integer group_id) {
        this.group_id = group_id;
    }

    public void setGroupName(String group_name) {
        this.group_name = group_name;
    }

    public void setGroupType(String group_type) {
        this.group_type = group_type;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
