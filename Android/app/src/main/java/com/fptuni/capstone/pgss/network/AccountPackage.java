package com.fptuni.capstone.pgss.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by TrungTNM on 2/22/2017.
 */

public class AccountPackage {
    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("obj")
    private List<String> objs;

    public AccountPackage() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getObjs() {
        return objs;
    }

    public void setObjs(List<String> objs) {
        this.objs = objs;
    }
}
