package com.moopflops.safespace.engine.model;

import android.content.Context;

import com.moopflops.safespace.R;

/**
 * Created by patrickc on 24/10/15.
 */
public class RatedCarPark {

    public RatedCarPark(CarPark carPark, int rating){
        this.carPark = carPark;
        this.rating = rating;
    }

    public CarPark carPark;
    public int rating;


}
