package com.moopflops.safespace.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.moopflops.safespace.R;
import com.moopflops.safespace.ui.Utils;
import com.moopflops.safespace.ui.components.CarParkMapPin;

/**
 * Created by patrickc on 24/10/15.
 */
public class FiltersFragment extends Fragment {


    public static FiltersFragment newInstance() {
        return new FiltersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filters, container, false);

        LinearLayout test = (LinearLayout) rootView.findViewById(R.id.test);

//        test.addView(new CarParkMapPin(getContext(), Utils.getCrimeColour(getContext(), 100), 100), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        test.addView(new CarParkMapPin(getContext(), Utils.getCrimeColour(getContext(), 0), 20), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        test.addView(new CarParkMapPin(getContext(), Utils.getCrimeColour(getContext(), 10), 1), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        test.addView(new CarParkMapPin(getContext(), Utils.getCrimeColour(getContext(), 20), 1), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        test.addView(new CarParkMapPin(getContext(), Utils.getCrimeColour(getContext(), 30), 1), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        test.addView(new CarParkMapPin(getContext(), Utils.getCrimeColour(getContext(), 40), 1), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        test.addView(new CarParkMapPin(getContext(), Utils.getCrimeColour(getContext(), 50), 1), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return rootView;
    }
}
