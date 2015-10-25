package com.moopflops.safespace.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.moopflops.safespace.R;
import com.moopflops.safespace.engine.Constants;
import com.moopflops.safespace.ui.Utils;

/**
 * Created by patrickc on 25/10/15.
 */
public class StreetViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filters_activity);

        LatLng position = getIntent().getParcelableExtra(Constants.LAT_LONG);

        StreetViewPanoramaOptions options = new StreetViewPanoramaOptions();
        options.position(position);

        Utils.addFragment(this, SupportStreetViewPanoramaFragment.newInstance(options));
    }
}
