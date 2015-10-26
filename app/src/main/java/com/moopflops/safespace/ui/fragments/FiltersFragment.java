package com.moopflops.safespace.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.moopflops.safespace.R;
import com.moopflops.safespace.engine.CarParkManager;
import com.moopflops.safespace.engine.CarParkRatingTask;
import com.moopflops.safespace.engine.model.CarPark;
import com.moopflops.safespace.engine.model.RatedCarPark;
import com.moopflops.safespace.ui.adapters.CarParkAdapter;

import java.util.List;

/**
 * Created by patrickc on 24/10/15.
 */
public class FiltersFragment extends Fragment {

    private CarParkAdapter mAdapter;
    ProgressBar mProgress;

    public static FiltersFragment newInstance() {
        return new FiltersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filters, container, false);

        mProgress = (ProgressBar) rootView.findViewById(R.id.progress);

        final RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.car_park_list);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        CarParkManager.getInstance().getCarParks(new CarParkManager.CarParkCallbacks() {
            @Override
            public void onSuccess(List<CarPark> carParks) {
                CarParkRatingTask task = new CarParkRatingTask(new CarParkRatingTask.CarParkRatingListener() {
                    @Override
                    public void onComplete(List<RatedCarPark> ratedCarParks) {
                        mProgress.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mAdapter = new CarParkAdapter(ratedCarParks);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });

                CarPark[] cpArray = new CarPark[carParks.size()];
                carParks.toArray(cpArray);

                task.execute(cpArray);
            }

            @Override
            public void onFail(Throwable t) {

            }
        });



        return rootView;
    }
}
