package com.moopflops.safespace.engine.model;

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

    public String getRating(){
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
}
