package com.fptuni.capstone.pgss.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TrungTNM on 3/23/2017.
 */

public class CheckCodePackage {
    @SerializedName("CarParkId")
    private int carParkId;
    @SerializedName("Username")
    private String username;
    @SerializedName("TransactionCode")
    private String transactionCode;

    public CheckCodePackage() {
    }

    public int getCarParkId() {
        return carParkId;
    }

    public void setCarParkId(int carParkId) {
        this.carParkId = carParkId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }
}
