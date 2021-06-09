// Generated by Dagger (https://dagger.dev).
package com.aman.foodordering;

import dagger.MembersInjector;
import dagger.android.DaggerApplication_MembersInjector;
import dagger.android.DispatchingAndroidInjector;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class FoodApplication_MembersInjector implements MembersInjector<FoodApplication> {
  private final Provider<DispatchingAndroidInjector<Object>> androidInjectorProvider;

  public FoodApplication_MembersInjector(
      Provider<DispatchingAndroidInjector<Object>> androidInjectorProvider) {
    this.androidInjectorProvider = androidInjectorProvider;
  }

  public static MembersInjector<FoodApplication> create(
      Provider<DispatchingAndroidInjector<Object>> androidInjectorProvider) {
    return new FoodApplication_MembersInjector(androidInjectorProvider);}

  @Override
  public void injectMembers(FoodApplication instance) {
    DaggerApplication_MembersInjector.injectAndroidInjector(instance, androidInjectorProvider.get());
  }
}
