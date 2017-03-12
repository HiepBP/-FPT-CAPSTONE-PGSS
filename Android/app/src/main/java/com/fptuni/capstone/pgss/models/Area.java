package com.fptuni.capstone.pgss.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TrungTNM on 3/12/2017.
 */

public class Area {
    @SerializedName("Id")
    private int id;
    @SerializedName("Name")
    private String name;
    @SerializedName("EmptyAmount")
    private int emptyAmount;

    public Area() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEmptyAmount() {
        return emptyAmount;
    }

    public void setEmptyAmount(int emptyAmount) {
        this.emptyAmount = emptyAmount;
    }
}
