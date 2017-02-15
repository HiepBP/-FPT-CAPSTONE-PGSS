package com.fptuni.capstone.pgss.interfaces;

import com.fptuni.capstone.pgss.network.GetCoordinatePackage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by TrungTNM on 2/15/2017.
 */

public interface CarParkClient {
    @GET("/api/CarParks/GetCoordinateNearestCarPark/{lat}/{lon}/{numberOfCarPark}")
    Call<GetCoordinatePackage> getCoordinateNearestCarPark(
            @Path("lat") double lat,
            @Path("lon") double lon,
            @Path("numberOfCarPark") int numberOfCarPark
    );
}
