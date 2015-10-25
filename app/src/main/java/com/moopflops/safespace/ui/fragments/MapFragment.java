package com.moopflops.safespace.ui.fragments;

import android.content.Context;
import android.location.Location;
import android.media.Rating;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.moopflops.safespace.R;
import com.moopflops.safespace.engine.CarParkManager;
import com.moopflops.safespace.engine.CarParkRatingTask;
import com.moopflops.safespace.engine.Constants;
import com.moopflops.safespace.engine.CrimeManager;
import com.moopflops.safespace.engine.RatingUtils;
import com.moopflops.safespace.engine.model.CarPark;
import com.moopflops.safespace.engine.model.Crime;
import com.moopflops.safespace.engine.model.RatedCarPark;
import com.moopflops.safespace.ui.Utils;
import com.moopflops.safespace.ui.components.SlidyView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrickc on 24/10/15.
 */
public class MapFragment extends Fragment {

    SupportMapFragment mMapFragment;
    GoogleMap mGoogleMap;
    SlidyView mSlidyView;

    List<RatedCarPark> mRatedCarParks = new ArrayList<>();
    List<MapMarker> mMapMarkers = new ArrayList<>();

    Location mMyLocation;
    LatLng mPinLocation;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        setHasOptionsMenu(true);

        mSlidyView = (SlidyView) rootView.findViewById(R.id.slidy_view);

        mMapFragment = new SupportMapFragment();
        mMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                mGoogleMap.setMyLocationEnabled(true);
                carParkMapSetup();
            }
        });

        Utils.addChildFragment(this, mMapFragment, R.id.map_container);

        return rootView;
    }

    private void showMyLocationData(){
        if(mMyLocation != null){
            mSlidyView.setMyLocationData(RatingUtils.getRatingForLocation(new com.moopflops.safespace.engine.model.Location(mMyLocation.getLatitude(), mMyLocation.getLongitude()), CrimeManager.getInstance().getCrimes()));
        }
    }

    private void showPinData(){
        if(mPinLocation != null){
            mSlidyView.setPinLocation(RatingUtils.getRatingForLocation(new com.moopflops.safespace.engine.model.Location(mPinLocation.latitude, mPinLocation.longitude), CrimeManager.getInstance().getCrimes()));
        }
    }

    private void carParkMapSetup(){
        mMyLocation = mGoogleMap.getMyLocation();
        showMyLocationData();
        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                mMyLocation = mGoogleMap.getMyLocation();
                showMyLocationData();
                return false;
            }
        });

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (MapMarker mapMarker : mMapMarkers) {
                    if (mapMarker.marker.equals(marker)) {
                        mSlidyView.setEnabled(true);
                        mSlidyView.setData(mapMarker.ratedCarPark);
                        VisibleRegion visibleRegion = mGoogleMap.getProjection().getVisibleRegion();
                        int viewHeight = mMapFragment.getView().getMeasuredHeight();
                        double latSpan = visibleRegion.latLngBounds.northeast.latitude - visibleRegion.latLngBounds.southwest.latitude;
                        double latOffset = (latSpan * 0.5) * (1 - (viewHeight - mSlidyView.getVisibleHeight()) / viewHeight);
                        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(mapMarker.ratedCarPark.carPark.latitude - latOffset, mapMarker.ratedCarPark.carPark.longitude), mGoogleMap.getCameraPosition().zoom)));
                        return true;
                    }
                }
                return false;
            }
        });

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mSlidyView.isOpen()) {
                    mSlidyView.close();
                }
            }
        });
        mGoogleMap.setPadding(32, 32, 32, 32);
        setMapBounds(Constants.getDefaultBounds());
        getCrimes();
    }

    private void addMapPins(){
        mMapMarkers.clear();
        for(RatedCarPark ratedCarPark : mRatedCarParks){
            mMapMarkers.add(new MapMarker(ratedCarPark));
        }
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
                CarParkRatingTask task = new CarParkRatingTask(new CarParkRatingTask.CarParkRatingListener() {
                    @Override
                    public void onComplete(List<RatedCarPark> ratedCarParks) {
                        mRatedCarParks = ratedCarParks;
                        addMapPins();
                    }
                });
                CarPark[] carParkArray = new CarPark[carParks.size()];
                carParks.toArray(carParkArray);

                task.execute(carParkArray);
                //double left = Double.MAX_VALUE, right = -Double.MAX_VALUE, top = Double.MAX_VALUE, bottom = -Double.MAX_VALUE;
//                for (CarPark carPark : carParks) {

//                    if (carPark.latitude < left) {
//                        left = carPark.latitude;
//                    }
//
//                    if (carPark.latitude > right) {
//                        right = carPark.latitude;
//                    }
//
//                    if (carPark.longitude > bottom) {
//                        bottom = carPark.longitude;
//                    }
//
//                    if (carPark.longitude < top) {
//                        top = carPark.longitude;
//                    }
//                }

//                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(left, bottom), new LatLng(right, top)), 10));
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

    public class MapMarker {
        public Marker marker;
        public RatedCarPark ratedCarPark;

        public MapMarker(RatedCarPark carPark){
            ratedCarPark = carPark;
            marker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(ratedCarPark.carPark.latitude, ratedCarPark.carPark.longitude))
                    .icon(Utils.getIcon(getContext(), RatingUtils.getColour(getContext(), ratedCarPark.rating), RatingUtils.getRating(ratedCarPark.rating))));
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pin_toggle, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_drop_pin:
                for(MapMarker mapMarker : mMapMarkers){
                    mapMarker.marker.remove();
                }
                mGoogleMap.clear();

                Toast.makeText(getContext(), "Tap the map to drop a pin", Toast.LENGTH_LONG).show();

                mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        for(MapMarker mapMarker : mMapMarkers){
                            mapMarker.marker.remove();
                        }
                        mGoogleMap.clear();
                        mGoogleMap.addMarker(new MarkerOptions().position(latLng));
                        mPinLocation = latLng;
                        showPinData();
                    }
                });

                break;
        }
        return false;
    }

}
