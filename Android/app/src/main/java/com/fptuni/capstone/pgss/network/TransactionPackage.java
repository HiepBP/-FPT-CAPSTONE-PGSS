package com.fptuni.capstone.pgss.network;

import com.fptuni.capstone.pgss.models.Transaction;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by TrungTNM on 3/12/2017.
 */

public class TransactionPackage {
    @SerializedName("result")
    private List<Transaction> result;
    @SerializedName("success")
    private boolean success;

    public TransactionPackage() {
    }

    public List<Transaction> getResult() {
        return result;
    }

    public void setResult(List<Transaction> result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
