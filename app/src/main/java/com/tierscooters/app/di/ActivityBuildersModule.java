package com.tierscooters.app.di;

import com.tierscooters.app.di.main.MainFragmentBuildersModule;
import com.tierscooters.app.di.main.MainModule;
import com.tierscooters.app.di.main.MainScope;
import com.tierscooters.app.di.main.MainViewModelsModule;
import com.tierscooters.app.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {


    @MainScope
    @ContributesAndroidInjector(
            modules = {MainFragmentBuildersModule.class, MainViewModelsModule.class, MainModule.class}
    )
    abstract MainActivity contributeMainActivity();


}
