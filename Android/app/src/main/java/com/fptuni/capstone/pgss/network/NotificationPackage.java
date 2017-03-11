package com.fptuni.capstone.pgss.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TrungTNM on 3/11/2017.
 */

public class NotificationPackage {
    @SerializedName("username")
    private String username;
    @SerializedName("car_park_id")
    private int carParkId;
    @SerializedName("car_park_name")
    private String carParkName;
    @SerializedName("lot_id")
    private int lotId;
    @SerializedName("lot_name")
    private String lotName;

    public NotificationPackage() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCarParkId() {
        return carParkId;
    }

    public void setCarParkId(int carParkId) {
        this.carParkId = carParkId;
    }

    public String getCarParkName() {
        return carParkName;
    }

    public void setCarParkName(String carParkName) {
        this.carParkName = carParkName;
    }

    public int getLotId() {
        return lotId;
    }

    public void setLotId(int lotId) {
        this.lotId = lotId;
    }

    public String getLotName() {
        return lotName;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }
}
