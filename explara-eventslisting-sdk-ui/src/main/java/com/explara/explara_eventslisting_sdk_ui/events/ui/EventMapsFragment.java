package com.explara.explara_eventslisting_sdk_ui.events.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.explara.explara_eventslisting_sdk_ui.R;
import com.explara.explara_eventslisting_sdk_ui.common.EventBaseFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by anudeep on 25/10/15.
 */
public class EventMapsFragment extends EventBaseFragment {
    public static final String TAG = EventMapsFragment.class.getSimpleName();

    private GoogleMap mGooglemap;
    double longitude;
    double latitude;
    String venueName;

    public static EventMapsFragment newInstance(double latitude, double longitude, String venueName) {
        Bundle bundle = new Bundle(3);
        bundle.putDouble(MapActivity.LATITUDE, latitude);
        bundle.putDouble(MapActivity.LONGITUDE, longitude);
        bundle.putString(MapActivity.VENUE_NAME, venueName);
        EventMapsFragment eventMapsFragment = new EventMapsFragment();
        eventMapsFragment.setArguments(bundle);
        return eventMapsFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_scree_layout, container, false);
        extractArguments();
        return view;
    }

    @Override
    public void refresh() {

    }

    private void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            latitude = args.getDouble(MapActivity.LATITUDE);
            longitude = args.getDouble(MapActivity.LONGITUDE);
            venueName = args.getString(MapActivity.VENUE_NAME);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupGoogleMapView();
    }

    public void setupGoogleMapView() {
        try {
            if (mGooglemap == null) {
                mGooglemap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
            }
            mGooglemap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mGooglemap.getUiSettings().setZoomControlsEnabled(true);
            mGooglemap.getUiSettings().setMyLocationButtonEnabled(true);
            mGooglemap.setMyLocationEnabled(true);
            mGooglemap.setBuildingsEnabled(true);
            getView().findViewById(R.id.map).setVisibility(View.VISIBLE);

            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(venueName);

            mGooglemap.addMarker(marker);

            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(12).build();

            mGooglemap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        } catch (Exception e) {
            e.printStackTrace();
            getView().findViewById(R.id.map).setVisibility(View.GONE);
        }
    }
}
