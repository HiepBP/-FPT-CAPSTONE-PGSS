package com.fptuni.capstone.pgss.network;

import com.fptuni.capstone.pgss.models.Transaction;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TrungTNM on 3/23/2017.
 */

public class CheckCodePackage {
    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("obj")
    private Transaction transaction;

    public CheckCodePackage() {
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

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
