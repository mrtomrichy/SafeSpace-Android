package com.moopflops.safespace.engine;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by tom on 24/10/2015.
 */
public class Constants {
  public static final double DEFAULT_TOP_LEFT_LATITUDE = 53.545612;
  public static final double DEFAULT_TOP_LEFT_LONGITUDE = -2.392273;
  public static final double DEFAULT_BOTTOM_LEFT_LATITUDE = 53.398070;
  public static final double DEFAULT_BOTTOM_LEFT_LONGITUDE = -2.392273;
  public static final double DEFAULT_BOTTOM_RIGHT_LATITUDE = 53.398070;
  public static final double DEFAULT_BOTTOM_RIGHT_LONGITUDE = -2.112122;
  public static final double DEFAULT_TOP_RIGHT_LATITUDE = 53.545612;
  public static final double DEFAULT_TOP_RIGHT_LONGITUDE = -2.112122;

  public static final double ZOOM_LEFT = 53.4688;
  public static final double ZOOM_RIGHT = 53.4889;
  public static final double ZOOM_BOTTOM = -2.23021;
  public static final double ZOOM_TOP = -2.25745;

  public static final String DEFAULT_BOUNDS_BITCHES = String.format("%f,%f:%f,%f:%f,%f:%f,%f",  DEFAULT_TOP_LEFT_LATITUDE,
                                                                                                DEFAULT_TOP_LEFT_LONGITUDE,
                                                                                                DEFAULT_BOTTOM_LEFT_LATITUDE,
                                                                                                DEFAULT_BOTTOM_LEFT_LONGITUDE,
                                                                                                DEFAULT_BOTTOM_RIGHT_LATITUDE,
                                                                                                DEFAULT_BOTTOM_RIGHT_LONGITUDE,
                                                                                                DEFAULT_TOP_RIGHT_LATITUDE,
                                                                                                DEFAULT_TOP_RIGHT_LONGITUDE);
    public static final String LAT_LONG = "lat_long";

    public static LatLngBounds getDefaultBounds() {
    return new LatLngBounds(new LatLng(DEFAULT_BOTTOM_LEFT_LATITUDE, DEFAULT_BOTTOM_LEFT_LONGITUDE),
                            new LatLng(DEFAULT_TOP_RIGHT_LATITUDE, DEFAULT_TOP_RIGHT_LONGITUDE));
  }

  public static LatLngBounds getDefaultMapZoom() {
    return new LatLngBounds(new LatLng(ZOOM_LEFT, ZOOM_TOP), new LatLng(ZOOM_RIGHT, ZOOM_BOTTOM));
  }
}
