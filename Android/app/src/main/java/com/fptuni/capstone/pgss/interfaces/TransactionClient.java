package com.fptuni.capstone.pgss.interfaces;

import com.fptuni.capstone.pgss.network.TransactionPackage;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by TrungTNM on 3/11/2017.
 */

public interface TransactionClient {

    @POST("/api/Transactions/GetTransactionByUsername")
    Call<TransactionPackage> getTransactionByUsername(
            @Query("username") String username
    );
}
