package com.fptuni.capstone.pgss.interfaces;

import com.fptuni.capstone.pgss.models.ParkingLot;
import com.fptuni.capstone.pgss.network.ParkingLotPackage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by TrungTNM on 3/12/2017.
 */

public interface ParkingLotClient {

    @GET("/api/ParkingLots/GetParkingLotsByAreaId/{areaId}")
    Call<ParkingLotPackage> getParkingLotByAreaId(
            @Path("areaId") int id
    );

    @POST("/api/ParkingLots/UpdateStatus")
    Call<ParkingLotPackage> updateStatus(
            @Body ParkingLot lot
    );

    @POST("/api/ParkingLots/UpdateName")
    Call<ParkingLotPackage> updateName(
            @Body ParkingLot lot
    );
}
