package com.fptuni.capstone.pgss.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by TrungTNM on 2/15/2017.
 */

public class GetCoordinatePackage {
    @SerializedName("result")
    private List<CarParkAdvancePackage> result;
    @SerializedName("success")
    private boolean success;

    public GetCoordinatePackage() {
    }

    public List<CarParkAdvancePackage> getResult() {
        return result;
    }

    public void setResult(List<CarParkAdvancePackage> result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
