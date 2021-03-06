package com.moopflops.safespace.engine;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.moopflops.safespace.engine.model.CarPark;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import co.uk.rushorm.core.RushCore;
import co.uk.rushorm.core.RushSearch;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by tom on 24/10/2015.
 */
public class CarParkManager {

  private static List<CarPark> mCarParks;

  private static CarParkManager mInstance;

  private CarParkManager(){
  }

  public static CarParkManager getInstance(){
    if(mInstance == null){
      mInstance = new CarParkManager();
    }
    return mInstance;
  }

  public interface CarParkCallbacks {
    void onSuccess(List<CarPark> carParks);
    void onFail(Throwable t);
  }

  public void getCarParks(CarParkCallbacks callbacks) {
    if(mCarParks != null) {
      callbacks.onSuccess(mCarParks);
    } else {
      List<CarPark> carParks = new RushSearch().find(CarPark.class);
      if(carParks.size() > 0){
        mCarParks = carParks;
        callbacks.onSuccess(carParks);
      }else {
        makeCarParkRequest(0, callbacks, new ArrayList<CarPark>());
      }
    }
  }

  private void removeOutOfScopeCarParks(List<CarPark> carParks) {
    LatLngBounds bounds = Constants.getDefaultBounds();
    Iterator it = carParks.iterator();
    CarPark c;

    while(it.hasNext()) {
      c = (CarPark) it.next();
      LatLng lng = new LatLng(c.getLocation().latitude, c.getLocation().longitude);
      if(!bounds.contains(lng)) it.remove();
    }
  }

  // This method is recursive for fucks sake why :'(
  private void makeCarParkRequest(final int currentPageNum, final CarParkCallbacks callback, final List<CarPark> carParks) {
    RetrofitClient.tfgm().carParks(currentPageNum, 20).enqueue(new Callback<List<CarPark>>() {
      @Override
      public void onResponse(Response<List<CarPark>> response, Retrofit retrofit) {
        carParks.addAll(response.body());

        if(response.body().size() == 20) {
          makeCarParkRequest(currentPageNum + 1, callback, carParks);
        } else {
          removeOutOfScopeCarParks(carParks);

          RushCore.getInstance().save(carParks);
          mCarParks = carParks;
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
