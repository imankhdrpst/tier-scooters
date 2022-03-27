package com.tierscooters.app.ui.main.main;

import androidx.lifecycle.ViewModel;

import com.tierscooters.app.network.main.MainApi;

import javax.inject.Inject;

public class MainPageViewModel extends ViewModel {

    // inject
    private final MainApi mainApi;

    @Inject
    public MainPageViewModel(MainApi mainApi) {
        this.mainApi = mainApi;
    }

}



















