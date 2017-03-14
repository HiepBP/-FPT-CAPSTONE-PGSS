package com.fptuni.capstone.pgss.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by TrungTNM on 3/7/2017.
 */

public class Transaction {
    @SerializedName("Id")
    private int id;
    private String username;
    @SerializedName("CarParkId")
    private int carParkId;
    @SerializedName("CarPark")
    private CarPark carPark;
    @SerializedName("ParkingLotId")
    private int lotId;
    @SerializedName("TransactionDate")
    private Date date;
    @SerializedName("Status")
    private int status;
    @SerializedName("Amount")
    private double amount;
    @SerializedName("ParkingLot")
    private ParkingLot lot;

    public Transaction() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getLotId() {
        return lotId;
    }

    public void setLotId(int lotId) {
        this.lotId = lotId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public ParkingLot getLot() {
        return lot;
    }

    public void setLot(ParkingLot lot) {
        this.lot = lot;
    }

    public CarPark getCarPark() {
        return carPark;
    }

    public void setCarPark(CarPark carPark) {
        this.carPark = carPark;
    }
}
