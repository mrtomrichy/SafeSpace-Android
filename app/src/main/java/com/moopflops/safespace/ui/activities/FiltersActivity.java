package com.moopflops.safespace.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.moopflops.safespace.R;
import com.moopflops.safespace.ui.Utils;
import com.moopflops.safespace.ui.fragments.FiltersFragment;

/**
 * Created by patrickc on 24/10/15.
 */
public class FiltersActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filters_activity);

        Utils.addFragment(this, FiltersFragment.newInstance());


    }
}
