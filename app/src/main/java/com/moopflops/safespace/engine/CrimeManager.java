package com.moopflops.safespace.engine;

import com.moopflops.safespace.engine.model.Crime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.uk.rushorm.core.RushSearch;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by tom on 24/10/2015.
 */
public class CrimeManager {

  public interface CrimeCallback {
    void onSuccess(List<Crime> crimes);
    void onFail(Throwable t);
    void onProgressUpdate(int progress);
  }

  public static void getVehicleCrimesDefault(CrimeCallback callback){
    getVehicleCrimes(2015, 10, 12, callback);
  }

  public static void getVehicleCrimes(int year, int month, int monthCount, CrimeCallback callback) {
    Calendar c = Calendar.getInstance();
    c.set(year, month, 0);

    List<Crime> crimes = new RushSearch().find(Crime.class);
    if(crimes.size() > 0) {
      callback.onSuccess(crimes);
    } else {
      makeVehicleCrimeRequest(c, 0, monthCount, new ArrayList<Crime>(), callback);
    }
  }

  private static void makeVehicleCrimeRequest(final Calendar c, final int currentMonthCount, final int monthCount, final List<Crime> crimes, final CrimeCallback callback) {
    if(currentMonthCount == monthCount) {
      callback.onSuccess(crimes);
      return;
    }

    callback.onProgressUpdate((int) (((double) currentMonthCount / (double) monthCount) * 100.0));

    String month = String.format("%4d-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1);

    // Get already saved crimes
    List<Crime> savedCrimes = new RushSearch().whereEqual("month", month).find(Crime.class);
    if(savedCrimes.size() > 0) {
      crimes.addAll(savedCrimes);
      c.add(Calendar.MONTH, -1);
      makeVehicleCrimeRequest(c, currentMonthCount+1, monthCount, crimes, callback);
    }


    RetrofitClient.police().vehicleCrime(Constants.DEFAULT_BOUNDS_BITCHES, month).enqueue(new Callback<List<Crime>>() {
      @Override
      public void onResponse(Response<List<Crime>> response, Retrofit retrofit) {
        if(response.body() == null || response.body().size() == 0) {
          c.add(Calendar.MONTH, -1);
          makeVehicleCrimeRequest(c, currentMonthCount, monthCount, crimes, callback);
        } else {
          crimes.addAll(response.body());
          c.add(Calendar.MONTH, -1);
          makeVehicleCrimeRequest(c, currentMonthCount+1, monthCount, crimes, callback);
        }
      }

      @Override
      public void onFailure(Throwable t) {
        callback.onFail(t);
      }
    });
  }
}
