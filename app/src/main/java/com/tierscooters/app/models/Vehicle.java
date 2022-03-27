package com.tierscooters.app.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

public class Vehicle implements ClusterItem {
    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("attributes")
    @Expose
    public VehicleAttributes attributes;

    @Override
    public LatLng getPosition() {
        return new LatLng(attributes.lat, attributes.lng);
    }

    @Override
    public String getTitle() {
        return type + "-" + id;
    }

    @Override
    public String getSnippet() {
        return "snippet";
    }

    public class VehicleAttributes
    {
        @SerializedName("batteryLevel")
        @Expose
        public int batteryLevel;

        @SerializedName("lat")
        @Expose
        public double lat;

        @SerializedName("lng")
        @Expose
        public double lng;

        @SerializedName("maxSpeed")
        @Expose
        public int maxSpeed;

        @SerializedName("vehicleType")
        @Expose
        public String vehicleType;

        @SerializedName("hasHelmetBox")
        @Expose
        public boolean hasHelmetBox;

    }
}
