package com.fptuni.capstone.pgss.network;

import com.fptuni.capstone.pgss.models.CarPark;
import com.fptuni.capstone.pgss.models.Geo;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TrungTNM on 2/15/2017.
 */

public class CarParkAdvancePackage {
    @SerializedName("Carpark")
    private CarPark carPark;
    @SerializedName("EmptyAmount")
    private int availableLot;
    @SerializedName("Distance")
    private double distance;
    @SerializedName("Geo")
    private Geo geo;

    public CarParkAdvancePackage() {
    }

    public CarPark getCarPark() {
        return carPark;
    }

    public void setCarPark(CarPark carPark) {
        this.carPark = carPark;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public int getAvailableLot() {
        return availableLot;
    }

    public void setAvailableLot(int availableLot) {
        this.availableLot = availableLot;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
