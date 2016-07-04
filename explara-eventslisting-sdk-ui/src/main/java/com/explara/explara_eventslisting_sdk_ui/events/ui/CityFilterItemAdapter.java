package com.explara.explara_eventslisting_sdk_ui.events.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.explara.explara_eventslisting_sdk.events.dto.CityNamesResponseDto;
import com.explara.explara_eventslisting_sdk_ui.R;

import java.util.List;

/**
 * Created by ananthasooraj on 1/16/16.
 */
public class CityFilterItemAdapter extends ArrayAdapter<CityNamesResponseDto.Cities> {

    private List<CityNamesResponseDto.Cities> mCityNamesResponseDtoList;

    CollectionEventFragment.CityNameClickListener mFragmentListener;

    public CityFilterItemAdapter(Context context, int resource, List<CityNamesResponseDto.Cities> cityNamesResponseDtoList, CollectionEventFragment.CityNameClickListener cityAdapterListener) {
        super(context, resource, cityNamesResponseDtoList);
        mCityNamesResponseDtoList = cityNamesResponseDtoList;
        mFragmentListener = cityAdapterListener;
    }

    public class ViewHolder {
        private TextView mCityName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CityNamesResponseDto.Cities cities = mCityNamesResponseDtoList.get(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.popup_window_item_layout, parent, false);

            viewHolder.mCityName = (TextView) convertView.findViewById(R.id.city_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mCityName.setText(cities.cityName);


        viewHolder.mCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentListener.onCityNameClickListener(cities.cityName);
            }
        });

        return convertView;

    }

    @Override
    public int getCount() {
        return mCityNamesResponseDtoList.size();
    }
}
