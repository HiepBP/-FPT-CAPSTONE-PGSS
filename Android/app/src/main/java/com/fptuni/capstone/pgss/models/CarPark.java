package com.fptuni.capstone.pgss.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by TrungTNM on 2/15/2017.
 */

public class CarPark implements Serializable {
    @SerializedName("Id")
    private int id;
    @SerializedName("Name")
    private String name;
    @SerializedName("Address")
    private String address;
    @SerializedName("Phone")
    private String phone;
    @SerializedName("Email")
    private String email;
    @SerializedName("Description")
    private String description;
    @SerializedName("Lat")
    private String lat;
    @SerializedName("Lon")
    private String lon;
    @SerializedName("Active")
    private boolean active;
    private int availableLot;
    private double awayDistance;
    private String fromTarget;

    public CarPark() {
        setFromTarget("You");
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getAvailableLot() {
        return availableLot;
    }

    public void setAvailableLot(int availableLot) {
        this.availableLot = availableLot;
    }

    public double getAwayDistance() {
        return awayDistance;
    }

    public void setAwayDistance(double awayDistance) {
        this.awayDistance = awayDistance;
    }

    public String getFromTarget() {
        return fromTarget;
    }

    public void setFromTarget(String fromTarget) {
        this.fromTarget = fromTarget;
    }
}
