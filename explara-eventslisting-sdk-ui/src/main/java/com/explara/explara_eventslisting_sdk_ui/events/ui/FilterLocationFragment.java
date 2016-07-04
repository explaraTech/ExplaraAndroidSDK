package com.explara.explara_eventslisting_sdk_ui.events.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.explara.explara_eventslisting_sdk.events.EventsManger;
import com.explara.explara_eventslisting_sdk.events.dto.CityNamesResponseDto;
import com.explara.explara_eventslisting_sdk_ui.R;
import com.explara.explara_eventslisting_sdk_ui.common.EventBaseFragment;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Log;

/**
 * Created by ananthasooraj on 1/18/16.
 */
public class FilterLocationFragment extends EventBaseFragment {

    private static final String TAG = FilterLocationFragment.class.getSimpleName();
    private ListView mCityNameList;
    CityFilterItemAdapter mCityFilterItemAdapter;
    private String mSegmentName;

    CollectionEventFragment.CityNameClickListener mCityNameClickListener;

    public static FilterLocationFragment newInstance(String segmentName) {
        FilterLocationFragment filterLocationFragment = new FilterLocationFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString(ConstantKeys.UrlKeys.SEGMENT, segmentName);
        filterLocationFragment.setArguments(bundle);
        return filterLocationFragment;
    }

    @Override
    public void refresh() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_window_layout, container, false);
        initViews(view);
        extractArguments();

        return view;

    }

    private void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mSegmentName = args.getString(ConstantKeys.UrlKeys.SEGMENT, "");
            Log.d(TAG, "" + mSegmentName);
        }
    }

    private void initViews(View view) {
        mCityNameList = (ListView) view.findViewById(R.id.list_view);

        initializeListeners();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        downloadCityNames(mSegmentName);
    }

    private void downloadCityNames(String segmentName) {
        EventsManger.getInstance().downloadCityNames(getActivity(), new EventsManger.CityNamesResponseListener() {
            @Override
            public void onCityResponseSuccessListener(CityNamesResponseDto cityNamesResponseDto) {
                Log.d(TAG, "" + cityNamesResponseDto.status);

                if (getActivity() != null) {
                    mCityFilterItemAdapter = new CityFilterItemAdapter(getContext(), 0, cityNamesResponseDto.cities, mCityNameClickListener);
                    mCityNameList.setAdapter(mCityFilterItemAdapter);
                }
            }

            @Override
            public void onCityResponseFailureListener(VolleyError volleyError) {
                if (getActivity() != null) {
                    Log.d(TAG, "" + volleyError);
                    volleyError.printStackTrace();
                }
            }
        }, segmentName);
    }

    public void initializeListeners() {
        mCityNameClickListener = new CollectionEventFragment.CityNameClickListener() {
            @Override
            public void onCityNameClickListener(String cityName) {
                Log.d(TAG, "CITY======" + cityName);
                loadCategoryBasedOnCity(cityName);
            }
        };
    }

    public void loadCategoryBasedOnCity(String cityName) {
        ((EventsFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container)).downloadCategories(cityName);
    }


}
