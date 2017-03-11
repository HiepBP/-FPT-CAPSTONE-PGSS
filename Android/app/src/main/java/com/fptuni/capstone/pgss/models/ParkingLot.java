package com.fptuni.capstone.pgss.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TrungTNM on 3/11/2017.
 */

public class ParkingLot {
    @SerializedName("Id")
    private int id;
    @SerializedName("Name")
    private String name;

    public ParkingLot() {
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
}
