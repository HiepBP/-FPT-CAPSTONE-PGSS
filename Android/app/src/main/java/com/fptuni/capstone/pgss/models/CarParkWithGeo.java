package com.fptuni.capstone.pgss.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TrungTNM on 2/15/2017.
 */

public class CarParkWithGeo {
    @SerializedName("Carpark")
    private CarPark carPark;
    @SerializedName("Geo")
    private Geo geo;

    public CarParkWithGeo() {
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
}
