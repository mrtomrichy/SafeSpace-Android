package com.moopflops.safespace.engine;

import android.os.AsyncTask;

import com.moopflops.safespace.engine.model.CarPark;
import com.moopflops.safespace.engine.model.RatedCarPark;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 25/10/2015.
 */
public class CarParkRatingTask extends AsyncTask<CarPark, Void, List<RatedCarPark>> {

  public interface CarParkRatingListener {
    void onComplete(List<RatedCarPark> ratedCarParks);
  }

  private CarParkRatingListener mListener;

  public CarParkRatingTask(CarParkRatingListener listener) {
    mListener = listener;
  }

  @Override
  protected List<RatedCarPark> doInBackground(CarPark... carParks) {
    List<RatedCarPark> ratedCarParks = new ArrayList<>();
    for(CarPark carPark : carParks) {
      ratedCarParks.add(new RatedCarPark(carPark, RatingUtils.getRatingForLocation(carPark.getLocation(), CrimeManager.getInstance().getCrimes())));
    }
    return ratedCarParks;
  }

  @Override
  protected void onPostExecute(List<RatedCarPark> ratedCarParks) {
    super.onPostExecute(ratedCarParks);
    mListener.onComplete(ratedCarParks);
  }
}
