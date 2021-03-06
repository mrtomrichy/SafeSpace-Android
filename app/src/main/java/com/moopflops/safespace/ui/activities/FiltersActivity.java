package com.moopflops.safespace.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.moopflops.safespace.R;
import com.moopflops.safespace.ui.Utils;
import com.moopflops.safespace.ui.fragments.FiltersFragment;

/**
 * Created by patrickc on 24/10/15.
 */
public class FiltersActivity  extends AppCompatActivity {

    FiltersFragment mFiltersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filters_activity);

        Toolbar tool = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(tool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        mFiltersFragment = FiltersFragment.newInstance();

        Utils.addFragment(this, mFiltersFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.car_park_filters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.most_spaces) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
