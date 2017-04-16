package com.fptuni.capstone.pgss.network;

import com.fptuni.capstone.pgss.models.Area;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by TrungTNM on 3/12/2017.
 */

public class AreaPackage {
    @SerializedName("result")
    private List<Area> result;
    @SerializedName("success")
    private boolean success;

    public AreaPackage() {
    }

    public List<Area> getResult() {
        return result;
    }

    public void setResult(List<Area> result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
