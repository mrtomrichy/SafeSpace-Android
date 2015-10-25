package com.moopflops.safespace.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moopflops.safespace.R;
import com.moopflops.safespace.engine.CarParkManager;
import com.moopflops.safespace.engine.CrimeManager;
import com.moopflops.safespace.engine.model.CarPark;
import com.moopflops.safespace.engine.model.Crime;
import com.moopflops.safespace.ui.components.SelectableTextView;

import java.util.List;

/**
 * Created by patrickc on 24/10/15.
 */
public class NavigationDrawerFragment extends Fragment{

    NavDrawerCallbacks mListener;
    private DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;
    private ActionBarDrawerToggle mDrawerToggle;

    private SelectableTextView mTrafficText;
    private SelectableTextView mSatelliteText;
    private SelectableTextView mHeatMapText;

    private TextView mTotalSpacesText;
    private TextView mTotalCrimesText;

    int spaces = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        rootView.findViewById(R.id.satellite_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                mListener.satellite();
            }
        });

        mTrafficText = (SelectableTextView) rootView.findViewById(R.id.traffic_button);
        mSatelliteText = (SelectableTextView) rootView.findViewById(R.id.satellite_button);
        mHeatMapText = (SelectableTextView) rootView.findViewById(R.id.heat_map);

        mTotalCrimesText = (TextView) rootView.findViewById(R.id.total_crimes);
        mTotalSpacesText = (TextView) rootView.findViewById(R.id.total_spaces);

        mSatelliteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                mSatelliteText.toggleSelected();
                mListener.satellite();
            }
        });

        mTrafficText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                mTrafficText.toggleSelected();
                mListener.traffic();
            }
        });

        mHeatMapText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                mHeatMapText.toggleSelected();
                mListener.heatMap();
            }
        });

        return rootView;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout){
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, R.string.open, R.string.close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0);
            }
        };

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);


        setTotalCrimes();
        setTotalSpaces();
    }

    public void setTotalSpaces(){
        CarParkManager.getInstance().getCarParks(new CarParkManager.CarParkCallbacks() {
            @Override
            public void onSuccess(List<CarPark> carParks) {
                for (CarPark carPark : carParks) {
                    spaces += carPark.spacesNow;
                }
                mTotalSpacesText.setText("Total Spaces: " + spaces);
            }

            @Override
            public void onFail(Throwable t) {

            }
        });
    }

    public void setTotalCrimes(){
        CrimeManager.getInstance().getVehicleCrimesDefault(new CrimeManager.CrimeCallback() {
            @Override
            public void onSuccess(List<Crime> crimes) {
                mTotalCrimesText.setText("Total Crimes: " + crimes.size());
            }

            @Override
            public void onFail(Throwable t) {

            }

            @Override
            public void onProgressUpdate(int progress) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (NavDrawerCallbacks) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Implement NavDrawerCallbacks Yo!");
        }
    }

    public boolean drawerIsOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && drawerIsOpen()) {
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawers();
    }

    public interface NavDrawerCallbacks{
        void satellite();
        void traffic();
        void heatMap();
    }
}
