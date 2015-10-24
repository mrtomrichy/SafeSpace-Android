package com.moopflops.safespace.ui;

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

}
