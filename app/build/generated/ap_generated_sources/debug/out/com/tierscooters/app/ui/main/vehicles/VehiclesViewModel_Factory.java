// Generated by Dagger (https://dagger.dev).
package com.tierscooters.app.ui.main.vehicles;

import com.tierscooters.app.network.main.MainApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import javax.inject.Provider;

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class VehiclesViewModel_Factory implements Factory<VehiclesViewModel> {
  private final Provider<MainApi> mainApiProvider;

  public VehiclesViewModel_Factory(Provider<MainApi> mainApiProvider) {
    this.mainApiProvider = mainApiProvider;
  }

  @Override
  public VehiclesViewModel get() {
    return newInstance(mainApiProvider.get());
  }

  public static VehiclesViewModel_Factory create(Provider<MainApi> mainApiProvider) {
    return new VehiclesViewModel_Factory(mainApiProvider);
  }

  public static VehiclesViewModel newInstance(MainApi mainApi) {
    return new VehiclesViewModel(mainApi);
  }
}
