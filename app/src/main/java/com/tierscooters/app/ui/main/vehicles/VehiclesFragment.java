package com.tierscooters.app.ui.main.vehicles;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.maps.android.clustering.ClusterManager;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.tierscooters.app.BaseFragment;
import com.tierscooters.app.R;
import com.tierscooters.app.models.Vehicle;
import com.tierscooters.app.ui.main.Resource;

import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;

public class VehiclesFragment extends BaseFragment {

    private static final String TAG = "VehiclesFragment";
    private VehiclesViewModel viewModel;
    private GoogleMap googleMap;
    private ClusterManager<Vehicle> clusterManager;
    private FusedLocationProviderClient fusedLocationClient;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RxPermissions rxPermissions;
    private Location myLatestLocation = null;
    private LinearLayout sheetLayout;
    private BottomSheetBehavior bottomSheet;
    private Vehicle nearestVehicle = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_posts, container, false).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this, providerFactory).get(VehiclesViewModel.class);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        sheetLayout = view.findViewById(R.id.bottom_sheet);
        bottomSheet = BottomSheetBehavior.from(sheetLayout);

        view.findViewById(R.id.btn_view_on_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nearestVehicle != null && googleMap != null) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(nearestVehicle.attributes.lat, nearestVehicle.attributes.lng), 15));
                }
            }
        });
        view.findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nearestVehicle != null && googleMap != null) {
                    refresh();
                }
            }
        });

        bottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);

        rxPermissions = new RxPermissions(this.getActivity());

        checkPermission();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            compositeDisposable.add(rxPermissions
                    .request(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean granted) throws Throwable {
                            if (!granted) {
                                ((TextView)getView().findViewById(R.id.txt_bottom_title)).setText("Permission is not granted...");
                            } else {

                                checkEnabledGPS();
                            }
                        }
                    }));
        } else {
            checkEnabledGPS();
        }
    }

    private void checkEnabledGPS() {
        LocationManager service = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean isEnabled =
                service.isProviderEnabled(LocationManager.GPS_PROVIDER) || service.isProviderEnabled(
                        LocationManager.NETWORK_PROVIDER);

        if (!isEnabled) {
            ((TextView)getView().findViewById(R.id.txt_bottom_title)).setText("GPS is not enabled on this device...");

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        } else {
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    myLatestLocation = location;
                                } else {
                                    ((TextView) (getView().findViewById(R.id.txt_bottom_title))).setText("You location is not available");
                                }
                            }
                        });

                initMap();
                subscribeObservers();
            }

        }
    }

    private void initMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map_fragment);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                VehiclesFragment.this.googleMap = googleMap;
                clusterManager = new ClusterManager<Vehicle>(getActivity(), googleMap);
                googleMap.setOnCameraIdleListener(clusterManager);
                googleMap.setOnMarkerClickListener(clusterManager);
                clusterManager.setRenderer(new TierClusterRenderer(getActivity(), googleMap, clusterManager));

            }
        });
    }

    private void subscribeObservers() {
        viewModel.queryVehicles().observe(getViewLifecycleOwner(), new Observer<Resource<List<Vehicle>>>() {
            @Override
            public void onChanged(Resource<List<Vehicle>> listResource) {
                if (listResource != null) {
                    switch (listResource.status) {
                        case LOADING:
                            ((TextView) (getView().findViewById(R.id.txt_bottom_title))).setText("Loading vehicles information ...");
                            getView().findViewById(R.id.btn_view_on_map).setVisibility(View.INVISIBLE);
                            break;
                        case ERROR:
                            ((TextView) (getView().findViewById(R.id.txt_bottom_title))).setText("Error in getting vehicles, check you connection");
                            break;
                        case UPDATED:
                        case SUCCESS:
                            getView().findViewById(R.id.btn_view_on_map).setVisibility(View.VISIBLE);

                            googleMap.clear();

                            for (Vehicle item : listResource.data) {
                                clusterManager.addItem(item);
                            }
                            nearestVehicle = null;
                            if (myLatestLocation != null) {
                                nearestVehicle = findTheNearestVehicle(listResource.data);
                                if (nearestVehicle != null) {
                                    LatLng coordinate = new LatLng(nearestVehicle.attributes.lat, nearestVehicle.attributes.lng);
                                    CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                                            coordinate, 9);
                                    googleMap.animateCamera(location);
                                    ((TextView) getView().findViewById(R.id.txt_bottom_title))
                                            .setText("Nearest vehicle");
                                    ((TextView) getView().findViewById(R.id.txt_bottom_description))
                                            .setText(nearestVehicle.type + " - " + nearestVehicle.id + "\n" + "Type : " + nearestVehicle.attributes.vehicleType
                                                    + "\nMaximum Speed : " + nearestVehicle.attributes.maxSpeed + "\nBattery Level : " + nearestVehicle.attributes.batteryLevel
                                                    + "\nHas Helmet Box : " + (nearestVehicle.attributes.hasHelmetBox ? "YESY" : "NO"));

                                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(nearestVehicle.attributes.lat, nearestVehicle.attributes.lng)));
                                }
                            }
                            break;
                    }
                }
            }
        });
    }

    private void refresh() {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(googleMap.getCameraPosition().target, 9));

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                myLatestLocation = location;
                viewModel.updateLatestData();
            }
        });
    }


    private Vehicle findTheNearestVehicle(List<Vehicle> vehicles) {
        double distance = 0;
        Vehicle result = null;
        for (Vehicle item : vehicles) {
            double dist = calculationByDistance(new LatLng(myLatestLocation.getLatitude(), myLatestLocation.getLongitude()), new LatLng(item.attributes.lat, item.attributes.lng));
            if (distance == 0 || dist < distance) {
                distance = dist;
                result = item;
            }
        }
        return result;
    }

    public double calculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

}




















