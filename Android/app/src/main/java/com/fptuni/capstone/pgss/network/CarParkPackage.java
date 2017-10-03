package com.fptuni.capstone.pgss.network;

import com.fptuni.capstone.pgss.models.CarPark;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by TrungTNM on 3/12/2017.
 */

public class CarParkPackage {
    @SerializedName("result")
    private List<CarPark> result;
    @SerializedName("success")
    private boolean success;

    public CarParkPackage() {
    }

    public List<CarPark> getResult() {
        return result;
    }

    public void setResult(List<CarPark> result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
