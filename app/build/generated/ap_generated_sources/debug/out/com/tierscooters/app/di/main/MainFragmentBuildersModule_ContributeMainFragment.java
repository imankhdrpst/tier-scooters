package com.tierscooters.app.di.main;

import com.tierscooters.app.ui.main.main.MainFragment;
import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module(
  subcomponents = MainFragmentBuildersModule_ContributeMainFragment.MainFragmentSubcomponent.class
)
public abstract class MainFragmentBuildersModule_ContributeMainFragment {
  private MainFragmentBuildersModule_ContributeMainFragment() {}

  @Binds
  @IntoMap
  @ClassKey(MainFragment.class)
  abstract AndroidInjector.Factory<?> bindAndroidInjectorFactory(
      MainFragmentSubcomponent.Factory builder);

  @Subcomponent
  public interface MainFragmentSubcomponent extends AndroidInjector<MainFragment> {
    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<MainFragment> {}
  }
}
