package com.tierscooters.app.di.main;

import androidx.lifecycle.ViewModel;

import com.tierscooters.app.di.ViewModelKey;
import com.tierscooters.app.ui.main.MainViewModel;
import com.tierscooters.app.ui.main.main.MainPageViewModel;
import com.tierscooters.app.ui.main.vehicles.VehiclesViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    public abstract ViewModel bindMainViewModel(MainViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(VehiclesViewModel.class)
    public abstract ViewModel bindVehiclesViewModel(VehiclesViewModel viewModel);


    @Binds
    @IntoMap
    @ViewModelKey(MainPageViewModel.class)
    public abstract ViewModel bindMainPageViewModel(MainPageViewModel viewModel);


}




