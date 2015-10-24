package com.moopflops.safespace.engine.model;

import com.google.gson.annotations.SerializedName;

import co.uk.rushorm.core.RushObject;

/**
 * Created by tom on 24/10/2015.
 */
public class CarPark extends RushObject {
  @SerializedName("Id")
  public int id;
  @SerializedName("Name")
  public String name;
  @SerializedName("State")
  public String state;
  @SerializedName("LastUpdated")
  public String lastUpdated;
  @SerializedName("Latitude")
  public double latitude;
  @SerializedName("Longitude")
  public double longitude;
  @SerializedName("SCN")
  public String scn;
  @SerializedName("Capacity")
  public int capacity;
  @SerializedName("SpacesNow")
  public int spacesNow;
  @SerializedName("PredictedSpaces30Mins")
  public int spaces30;
  @SerializedName("PredictedSpaces60Mins")
  public int spaces60;

  public Location getLocation() {
    return new Location(latitude, longitude);
  }

}
