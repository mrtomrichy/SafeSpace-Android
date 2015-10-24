package com.moopflops.safespace.engine.model;

import com.google.gson.annotations.SerializedName;

import co.uk.rushorm.core.RushObject;

/**
 * Created by tom on 24/10/2015.
 */
public class Crime extends RushObject {
  @SerializedName("persistent_id")
  public String persistentId;
  @SerializedName("month")
  public String month;
  @SerializedName("context")
  public String context;
  @SerializedName("location_type")
  public String locationType;
  @SerializedName("location")
  public Location location;
}
