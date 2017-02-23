package com.fptuni.capstone.pgss.interfaces;

import com.fptuni.capstone.pgss.models.Account;
import com.fptuni.capstone.pgss.network.AccountPackage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by TrungTNM on 2/22/2017.
 */

public interface AccountClient {
    @POST("/api/Account/Login")
    Call<AccountPackage> login(@Body Account account);

    @POST("/api/Account/Register")
    Call<AccountPackage> register(@Body Account account);
}
