package com.fptuni.capstone.pgss.network;

import com.fptuni.capstone.pgss.models.ParkingLot;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by TrungTNM on 3/12/2017.
 */

public class ParkingLotPackage {
    @SerializedName("result")
    private List<ParkingLot> result;
    @SerializedName("success")
    private boolean success;

    public ParkingLotPackage() {
    }

    public List<ParkingLot> getResult() {
        return result;
    }

    public void setResult(List<ParkingLot> result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
