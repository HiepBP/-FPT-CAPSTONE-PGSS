package com.fptuni.capstone.pgss.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TrungTNM on 2/28/2017.
 */

public class MobilePubnubPackage {
    @SerializedName("username")
    private String username;
    @SerializedName("lot_name")
    private String lotName;
    @SerializedName("hub_name")
    private String hubName;

    public MobilePubnubPackage() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLotName() {
        return lotName;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }

    public String getHubName() {
        return hubName;
    }

    public void setHubName(String hubName) {
        this.hubName = hubName;
    }
}
