package com.tierscooters.app.di;

import com.tierscooters.app.di.main.MainFragmentBuildersModule;
import com.tierscooters.app.di.main.MainModule;
import com.tierscooters.app.di.main.MainScope;
import com.tierscooters.app.di.main.MainViewModelsModule;
import com.tierscooters.app.ui.main.MainActivity;
import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module(
  subcomponents = ActivityBuildersModule_ContributeMainActivity.MainActivitySubcomponent.class
)
public abstract class ActivityBuildersModule_ContributeMainActivity {
  private ActivityBuildersModule_ContributeMainActivity() {}

  @Binds
  @IntoMap
  @ClassKey(MainActivity.class)
  abstract AndroidInjector.Factory<?> bindAndroidInjectorFactory(
      MainActivitySubcomponent.Factory builder);

  @Subcomponent(
    modules = {MainFragmentBuildersModule.class, MainViewModelsModule.class, MainModule.class}
  )
  @MainScope
  public interface MainActivitySubcomponent extends AndroidInjector<MainActivity> {
    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<MainActivity> {}
  }
}
