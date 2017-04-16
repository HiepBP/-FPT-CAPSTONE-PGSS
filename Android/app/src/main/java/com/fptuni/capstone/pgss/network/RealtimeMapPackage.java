package com.fptuni.capstone.pgss.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TrungTNM on 3/11/2017.
 */

public class RealtimeMapPackage {
    @SerializedName("car_park_id")
    private int id;
    @SerializedName("available_lot")
    private int availableLot;

    public RealtimeMapPackage() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAvailableLot() {
        return availableLot;
    }

    public void setAvailableLot(int availableLot) {
        this.availableLot = availableLot;
    }
}
