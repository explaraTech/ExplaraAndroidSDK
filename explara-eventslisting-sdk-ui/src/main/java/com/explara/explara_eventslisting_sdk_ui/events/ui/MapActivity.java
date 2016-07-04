package com.explara.explara_eventslisting_sdk_ui.events.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.explara.explara_eventslisting_sdk_ui.R;
import com.explara_core.common.BaseWithOutNavActivity;
import com.explara_core.utils.FragmentHelper;

/**
 * Created by anudeep on 25/10/15.
 */
public class MapActivity extends BaseWithOutNavActivity {


    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String VENUE_NAME = "venue_name";
    double latitude;
    double longitude;
    String venueName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        latitude = extras.getDouble(LATITUDE);
        longitude = extras.getDouble(LONGITUDE);
        venueName = extras.getString(VENUE_NAME);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void addContentFragment() {
        displayBackButton();
        removeContainerPadding();
        FragmentHelper.replaceContentFragment(this, R.id.fragment_container, EventMapsFragment.newInstance(latitude, longitude, venueName), EventMapsFragment.TAG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setToolBarTitle() {
        //super.setToolBarTitle();
        if (!TextUtils.isEmpty(venueName)) {
            getSupportActionBar().setTitle(venueName);
        } else super.setToolBarTitle();
    }
}
