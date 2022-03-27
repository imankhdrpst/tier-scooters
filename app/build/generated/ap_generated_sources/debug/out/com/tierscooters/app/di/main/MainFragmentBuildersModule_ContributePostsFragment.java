package com.tierscooters.app.di.main;

import com.tierscooters.app.ui.main.vehicles.VehiclesFragment;
import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module(
  subcomponents =
      MainFragmentBuildersModule_ContributePostsFragment.VehiclesFragmentSubcomponent.class
)
public abstract class MainFragmentBuildersModule_ContributePostsFragment {
  private MainFragmentBuildersModule_ContributePostsFragment() {}

  @Binds
  @IntoMap
  @ClassKey(VehiclesFragment.class)
  abstract AndroidInjector.Factory<?> bindAndroidInjectorFactory(
      VehiclesFragmentSubcomponent.Factory builder);

  @Subcomponent
  public interface VehiclesFragmentSubcomponent extends AndroidInjector<VehiclesFragment> {
    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<VehiclesFragment> {}
  }
}
