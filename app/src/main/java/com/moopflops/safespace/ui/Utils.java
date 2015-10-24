package com.moopflops.safespace.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.moopflops.safespace.R;

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

    public static int getCrimeColour(Context context, int severity){
        int appColour = context.getResources().getColor(R.color.goodGreen);
        int dangerColour = context.getResources().getColor(R.color.dangerRed);

        float ratio = severity/100f;

        int red = (int) (Color.red(appColour) * ratio + Color.red(dangerColour) * (1 - ratio));
        int green = (int) (Color.green(appColour) * ratio + Color.green(dangerColour) * (1 - ratio));
        int blue = (int) (Color.blue(appColour) * ratio + Color.blue(dangerColour) * (1 - ratio));

        return Color.rgb(red, green, blue);
    }

}
