package com.moopflops.safespace.engine;

import com.moopflops.safespace.engine.model.CarPark;
import com.moopflops.safespace.engine.model.Crime;
import com.moopflops.safespace.engine.model.CrimeCategory;
import com.moopflops.safespace.engine.model.Force;

import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;

/**
 * Created by tom on 24/10/2015.
 */
public class RetrofitClient {
  private final static String API_BASE = "https://data.police.uk/api/";
  private final static String TFGM_BASE = "http://opendata.tfgm.com/api/";
  public final static String DEV_KEY = "DevKey: 9338ecd0-34c5-476c-bceb-28bb10e9242a";
  public final static String APP_KEY = "AppKey: 3bc49dee-5343-4093-ad1c-d4871feb587c";
  private static PoliceApi policeClient;
  private static TFGMApi tfgmApi;
  private RetrofitClient(){
    setupRetrofit();
  }
  public static PoliceApi police(){
    if(policeClient == null){
      new RetrofitClient();
    }
    return policeClient;
  }
  public static TFGMApi tfgm(){
    if(tfgmApi == null){
      new RetrofitClient();
    }
    return tfgmApi;
  }
  private void setupRetrofit() {
    Retrofit retrofitPolice = new Retrofit.Builder().baseUrl(API_BASE).addConverterFactory(GsonConverterFactory.create()).build();
    policeClient = retrofitPolice.create(PoliceApi.class);
    Retrofit retrofitTfgm = new Retrofit.Builder().baseUrl(TFGM_BASE).addConverterFactory(GsonConverterFactory.create()).build();
    tfgmApi = retrofitTfgm.create(TFGMApi.class);
  }
  public interface PoliceApi{
    @GET("crime-categories")
    Call<List<CrimeCategory>> crimeCategories(@Query("date") String date);
    @GET("crimes-no-location")
    Call<List<Crime>> crimesNoLocation(@Query("category") String category, @Query("force") String force, @Query("date") String date);
    @GET("forces")
    Call<List<Force>> forces();
    @GET("crimes-street/vehicle-crime")
    Call<List<Crime>> vehicleCrime(@Query("poly") String poly, @Query("date") String date);
  }
  public interface TFGMApi{
    @Headers({DEV_KEY, APP_KEY, "Content-Type: application/json"})
    @GET("Carparks")
    Call<List<CarPark>> carParks(@Query("pageIndex") int index, @Query("pageSize") int size);
  }
}
