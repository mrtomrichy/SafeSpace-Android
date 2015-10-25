package com.moopflops.safespace.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.moopflops.safespace.R;
import com.moopflops.safespace.engine.CarParkManager;
import com.moopflops.safespace.engine.Constants;
import com.moopflops.safespace.engine.CrimeManager;
import com.moopflops.safespace.engine.RatingUtils;
import com.moopflops.safespace.engine.model.CarPark;
import com.moopflops.safespace.engine.model.Crime;
import com.moopflops.safespace.engine.model.RatedCarPark;
import com.moopflops.safespace.ui.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrickc on 24/10/15.
 */
public class MapFragment extends Fragment {

    SupportMapFragment mMapFragment;
    GoogleMap mGoogleMap;

    List<RatedCarPark> mRatedCarParks = new ArrayList<>();

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapFragment = new SupportMapFragment();
        mMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                mGoogleMap.setMyLocationEnabled(true);
                setUpMap();
            }
        });

        Utils.addChildFragment(this, mMapFragment, R.id.map_container);

        return rootView;
    }


    private void addMapPins(){
        for(RatedCarPark ratedCarPark : mRatedCarParks){
            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(ratedCarPark.carPark.latitude, ratedCarPark.carPark.longitude)))
                    .setIcon(Utils.getIcon(getContext(), ratedCarPark.getColour(getContext()), ratedCarPark.getRating()));
        }
    }

    private void setUpMap(){
        mGoogleMap.setPadding(32, 32, 32, 32);
        setMapBounds(Constants.getDefaultBounds());
        getCrimes();
    }

    private void setMapBounds(final LatLngBounds bounds){
        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
                mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {

                    }
                });
            }
        });
    }

    private void getCrimes() {
        CrimeManager.getInstance().getVehicleCrimesDefault(new CrimeManager.CrimeCallback() {
            @Override
            public void onSuccess(List<Crime> crimes) {
                setUpCarParks();
            }

            @Override
            public void onFail(Throwable t) {

            }

            @Override
            public void onProgressUpdate(int progress) {

            }
        });
    }

    private void setUpCarParks(){
        CarParkManager.getInstance().getCarParks(new CarParkManager.CarParkCallbacks() {
            @Override
            public void onSuccess(List<CarPark> carParks) {
                double left = Double.MAX_VALUE, right = -Double.MAX_VALUE, top = Double.MAX_VALUE, bottom = -Double.MAX_VALUE;
                for (CarPark carPark : carParks) {
                    mRatedCarParks.add(new RatedCarPark(carPark, RatingUtils.getRatingForLocation(carPark.getLocation(), CrimeManager.getInstance().getCrimes())));

                    if (carPark.latitude < left) {
                        left = carPark.latitude;
                    }

                    if (carPark.latitude > right) {
                        right = carPark.latitude;
                    }

                    if (carPark.longitude > bottom) {
                        bottom = carPark.longitude;
                    }

                    if (carPark.longitude < top) {
                        top = carPark.longitude;
                    }
                }

//                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(left, bottom), new LatLng(right, top)), 10));
                addMapPins();
            }


            @Override
            public void onFail(Throwable t) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        try {
//            mCallback = (MapCallback) getContext();
//        } catch (ClassCastException e) {
//            throw new ClassCastException("Implement MapCallbacks Yo!");
//        }
    }

    public void toggleTraffic() {
        mGoogleMap.setTrafficEnabled(!mGoogleMap.isTrafficEnabled());
    }

    public void toggleSatellite() {
        mGoogleMap.setMapType(mGoogleMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL ? GoogleMap.MAP_TYPE_HYBRID : GoogleMap.MAP_TYPE_NORMAL);
    }

    public void heatMap() {
        //TODO HEAT MAP
    }
}
