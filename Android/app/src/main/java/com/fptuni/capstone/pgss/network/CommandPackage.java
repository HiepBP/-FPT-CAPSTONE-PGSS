package com.fptuni.capstone.pgss.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TrungTNM on 3/11/2017.
 */

public class CommandPackage {
    public static final String COMMAND_RESERVE = "reserve";
    public static final String COMMAND_CANCEL = "cancel";
    public static final String COMMAND_CHECK_IN = "checkin";

    @SerializedName("username")
    private String username;
    @SerializedName("car_park_id")
    private int carParkId;
    @SerializedName("command")
    private String command;
    @SerializedName("duration")
    private int duration;
    @SerializedName("amount")
    private int amount;
    @SerializedName("lot_id")
    private int lotId;
    @SerializedName("transaction_id")
    private int transactionId;

    public CommandPackage() {
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

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getLotId() {
        return lotId;
    }

    public void setLotId(int lotId) {
        this.lotId = lotId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
}
