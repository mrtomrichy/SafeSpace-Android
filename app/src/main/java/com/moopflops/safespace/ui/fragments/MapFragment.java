package com.moopflops.safespace.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    private static final LatLngBounds manchesterHype = new LatLngBounds(new LatLng(53.398070, -2.392273), new LatLng(53.545612, -2.112122));

    SupportMapFragment mMapFragment;
    GoogleMap mGoogleMap;

    List<RatedCarPark> mCarParks = new ArrayList<>();
    List<Crime> mCrimes = new ArrayList<>();

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
                setUpMap();
            }
        });

        Utils.addChildFragment(this, mMapFragment, R.id.map_container);

        return rootView;
    }


    private void addMapPins(){
        for(RatedCarPark ratedCarPark : mCarParks){
            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(ratedCarPark.carPark.latitude, ratedCarPark.carPark.longitude)))
                    .setIcon(Utils.getIcon(getContext(), ratedCarPark.rating, ratedCarPark.getRating()));
        }
    }

    private void setUpMap(){
        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(manchesterHype, 0));
                mGoogleMap.setOnCameraChangeListener(null);
            }
        });
        getCrimes();
    }

    private void getCrimes() {
        CrimeManager.getVehicleCrimesDefault(new CrimeManager.CrimeCallback() {
            @Override
            public void onSuccess(List<Crime> crimes) {
                mCrimes = crimes;
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
        CarParkManager.getCarParks(new CarParkManager.CarParkCallbacks() {
            @Override
            public void onSuccess(List<CarPark> carParks) {
                for(CarPark carPark : carParks){
                    mCarParks.add(new RatedCarPark(carPark, RatingUtils.getRatingForLocation(carPark.getLocation(), mCrimes)));
                }
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
//        } catch (ClassCastException e) {
//            throw new ClassCastException("Implement NavDrawerCallbacks Yo!");
//        }
    }

    public void toggleTraffic() {
        mGoogleMap.setTrafficEnabled(!mGoogleMap.isTrafficEnabled());
    }

    public void toggleSatellite() {
        mGoogleMap.setMapType(mGoogleMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL ? GoogleMap.MAP_TYPE_HYBRID : GoogleMap.MAP_TYPE_NORMAL);
    }
}
