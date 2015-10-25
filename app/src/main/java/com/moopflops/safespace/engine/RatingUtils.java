package com.moopflops.safespace.engine;

import android.content.Context;

import com.moopflops.safespace.R;
import com.moopflops.safespace.engine.model.Crime;
import com.moopflops.safespace.engine.model.Location;

import java.util.List;

/**
 * Created by tom on 24/10/2015.
 */
public class RatingUtils {

  private static final int MIN_CRIME_RADIUS = 250; // Radius in m
  private static final double RATING_SCALE_FACTOR = 100;

  public static float getDistance(Location l1, Location l2) {
    float results[] = new float[1];
    android.location.Location.distanceBetween(l1.latitude, l1.longitude, l2.latitude, l2.longitude, results);

    return results[0];
  }

  public static int getCrimesInArea(Location location, List<Crime> crimes) {
    int count = 0;
    for(Crime c : crimes) {
      if(getDistance(c.location, location) < MIN_CRIME_RADIUS) { // Crimes within 500m
        count++;
      }
    }

    return count;
  }

  public static int getRatingForLocation(Location location, List<Crime> crimes) {
    int crimesInArea = getCrimesInArea(location, crimes);

    return (int) (Math.exp(-crimesInArea * (RATING_SCALE_FACTOR / crimes.size())) * 100);
  }

  public static String getRating(int rating){
    if(rating > 90){
      return "A+";
    } else if (rating > 80){
      return "A";
    } else if (rating > 70){
      return "B";
    } else if (rating > 60){
      return "C";
    } else if (rating > 50){
      return "D";
    } else if (rating > 40){
      return "E";
    } else {
      return "F";
    }
  }

  public static int getColour(Context context, int rating){
    if(rating > 80){
      return context.getResources().getColor(R.color.goodGreen);
    } else if (rating > 60){
      return context.getResources().getColor(R.color.yellow);
    } else if (rating > 40){
      return context.getResources().getColor(R.color.orange);
    } else {
      return context.getResources().getColor(R.color.red);
    }
  }
}
