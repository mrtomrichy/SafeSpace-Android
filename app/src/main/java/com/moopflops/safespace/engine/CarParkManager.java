package com.moopflops.safespace.engine;

import com.moopflops.safespace.engine.model.CarPark;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by tom on 24/10/2015.
 */
public class CarParkManager {

  public interface CarParkCallbacks {
    void onSuccess(List<CarPark> carParks);
    void onFail(Throwable t);
  }

  public static void getCarParks(CarParkCallbacks callbacks) {
    makeCarParkRequest(0, callbacks, new ArrayList<CarPark>());
  }

  // This method is recursive for fucks sake why :'(
  private static void makeCarParkRequest(final int currentPageNum, final CarParkCallbacks callback, final List<CarPark> carParks) {
    RetrofitClient.tfgm().carParks(currentPageNum, 20).enqueue(new Callback<List<CarPark>>() {
      @Override
      public void onResponse(Response<List<CarPark>> response, Retrofit retrofit) {
        carParks.addAll(response.body());

        if(response.body().size() == 20) {
          makeCarParkRequest(currentPageNum + 1, callback, carParks);
        } else {
          callback.onSuccess(carParks);
        }
      }

      @Override
      public void onFailure(Throwable t) {
        callback.onFail(t);
      }
    });
  }
}
