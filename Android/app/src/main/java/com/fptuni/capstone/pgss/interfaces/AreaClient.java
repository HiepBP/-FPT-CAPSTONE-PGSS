package com.fptuni.capstone.pgss.interfaces;

import com.fptuni.capstone.pgss.network.AreaPackage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by TrungTNM on 3/12/2017.
 */

public interface AreaClient {

    @GET("/api/Areas/GetAreasByCarParkid/{carParkId}")
    Call<AreaPackage> getAreaByCarParkId(
            @Path("carParkId") int id
    );
}
