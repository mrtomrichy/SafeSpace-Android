package com.moopflops.safespace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;
import com.moopflops.safespace.engine.Constants;
import com.moopflops.safespace.ui.Utils;
import com.moopflops.safespace.ui.activities.FiltersActivity;
import com.moopflops.safespace.ui.activities.StreetViewActivity;
import com.moopflops.safespace.ui.fragments.MapFragment;
import com.moopflops.safespace.ui.fragments.NavigationDrawerFragment;

public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavDrawerCallbacks, MapFragment.MapFragmentCallbacks{

    NavigationDrawerFragment mNavDrawerFragment;
    MapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mMapFragment = MapFragment.newInstance();
        Utils.addFragment(this, mMapFragment);
        mNavDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filters) {
            startActivity(new Intent(getApplicationContext(), FiltersActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed(){
        if(mNavDrawerFragment.drawerIsOpen()){
            mNavDrawerFragment.closeDrawer();
        } else {
            finish();
        }
    }

    @Override
    public void satellite() {
        mMapFragment.toggleSatellite();
    }

    @Override
    public void traffic() {
        mMapFragment.toggleTraffic();
    }

    @Override
    public void heatMap() {
        mMapFragment.heatMap();
    }

    @Override
    public void viewStreetView(LatLng position) {
        startActivity(new Intent(this, StreetViewActivity.class).putExtra(Constants.LAT_LONG, position));
    }
}
