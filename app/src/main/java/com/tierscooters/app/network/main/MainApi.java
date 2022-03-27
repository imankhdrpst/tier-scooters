package com.tierscooters.app.network.main;

import com.tierscooters.app.models.ApiResponse;
import com.tierscooters.app.models.Vehicle;
import com.tierscooters.app.network.ServicePath;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface MainApi {

    // get locations
    @GET(ServicePath.LOCAIONS)
    Flowable<ApiResponse<Vehicle>> getVehicleLocations();
}