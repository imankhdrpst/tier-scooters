package com.tierscooters.app.ui.main.vehicles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.tierscooters.app.models.ApiResponse;
import com.tierscooters.app.models.Vehicle;
import com.tierscooters.app.network.main.MainApi;
import com.tierscooters.app.ui.main.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;

public class VehiclesViewModel extends ViewModel {

    private MediatorLiveData<Resource<List<Vehicle>>> vehiclesLiveData;
    private List<Vehicle> vehicleList = new ArrayList<>();

    // inject
    private final MainApi mainApi;

    @Inject
    public VehiclesViewModel(MainApi mainApi) {
        this.mainApi = mainApi;
    }


    public void updateLatestData()
    {
        queryVehicles();
    }

    public LiveData<Resource<List<Vehicle>>> queryVehicles() {
        if (vehiclesLiveData == null) {
            vehiclesLiveData = new MediatorLiveData<>();
        }
        vehiclesLiveData.setValue(Resource.loading(vehicleList));

        final LiveData<Resource<List<Vehicle>>> source = LiveDataReactiveStreams.fromPublisher(

                mainApi.getVehicleLocations().onErrorReturn(new io.reactivex.functions.Function<Throwable, ApiResponse<Vehicle>>() {
                    @Override
                    public ApiResponse<Vehicle> apply(Throwable throwable) throws Exception {
                        ApiResponse<Vehicle> res = new ApiResponse<Vehicle>();
                        return res;
                    }
                }).map(new io.reactivex.functions.Function<ApiResponse<Vehicle>, Resource<List<Vehicle>>>() {
                    @Override
                    public Resource<List<Vehicle>> apply(ApiResponse<Vehicle> response) throws Exception {
                        if (response == null || response.getData() == null )
                            return Resource.error("Error in getting vehicles locations", null);
                        else return Resource.success(response.getData());
                    }
                })

                        .subscribeOn(Schedulers.io())
        );

        vehiclesLiveData.addSource(source, new Observer<Resource<List<Vehicle>>>() {
            @Override
            public void onChanged(Resource<List<Vehicle>> listResource) {
                vehiclesLiveData.setValue(listResource);
                vehiclesLiveData.removeSource(source);
            }
        });

        return vehiclesLiveData;

    }

}



















