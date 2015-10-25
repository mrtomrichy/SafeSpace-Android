package com.moopflops.safespace.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.moopflops.safespace.R;
import com.moopflops.safespace.ui.components.CarParkMapPin;

/**
 * Created by patrickc on 24/10/15.
 */
public class Utils {


    public static void addFragment(AppCompatActivity activity, Fragment fragment){
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
    }

    public static void addChildFragment(Fragment host, Fragment fragment, int resId){
        host.getChildFragmentManager().beginTransaction().replace(resId, fragment).commit();
    }

//    public static int getCrimeColour(int severity){
//        float ratio = severity/100f;
//
//        int first;
//        int second;
//
//        if(ratio > 0.5f){
//            first = okay;
//            second = danger;
//        } else{
//            first = good;
//            second = okay;
//        }
//
//        int red = (int) (Color.red(first) * ratio + Color.red(second) * (1 - ratio));
//        int green = (int) (Color.green(first) * ratio + Color.green(second) * (1 - ratio));
//        int blue = (int) (Color.blue(first) * ratio + Color.blue(second) * (1 - ratio));
//
//        return Color.rgb(red, green, blue);
//    }

    public static BitmapDescriptor getIcon(Context context, int colour, String rating){
        View view = new CarParkMapPin(context, colour, rating);

        int width = (int)context.getResources().getDimension(R.dimen.map_pin_width);
        int height = (int) context.getResources().getDimension(R.dimen.map_pin_height);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.measure(width, height);
        view.layout(0, 0, width, height);
        view.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }



}
