package com.tierscooters.app.di.main;

import com.tierscooters.app.ui.main.main.MainFragment;
import com.tierscooters.app.ui.main.vehicles.VehiclesFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract MainFragment contributeMainFragment();

    @ContributesAndroidInjector
    abstract VehiclesFragment contributePostsFragment();

}
