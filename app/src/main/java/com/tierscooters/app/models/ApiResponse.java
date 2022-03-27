package com.tierscooters.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse<T> {

    @SerializedName("data")
    @Expose
    private List<T> data = null;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> items) {
        data = items;
    }
}
