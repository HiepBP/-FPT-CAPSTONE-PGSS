package com.fptuni.capstone.pgss.interfaces;

import com.fptuni.capstone.pgss.network.CarParkPackage;
import com.fptuni.capstone.pgss.network.GetCoordinatePackage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("/api/CarParks/GetCoordinateNearestCarParkByRange/{lat}/{lon}/{numberOfCarPark}")
    Call<GetCoordinatePackage> getCoordinateNearestCarParkByRange(
            @Path("lat") double lat,
            @Path("lon") double lon,
            @Path("numberOfCarPark") int numberOfCarPark,
            @Query("range") double range
    );

    @GET("/api/CarParks/GetCarParksByUsername/{username}")
    Call<CarParkPackage> getCarParkByUsername(
            @Path("username") String username
    );
}
