package com.explara.explara_ticketing_sdk_ui.tickets.ui;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.explara.explara_ticketing_sdk.tickets.dto.PackageDesc;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara_core.utils.AppUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ananthasooraj on 9/4/15.
 */

public class PackagesItemAdapter extends RecyclerView.Adapter<PackagesItemAdapter.ViewHolder> {

    private List<PackageDesc> mList;
    private String mSelectedSessionDate;
    private PackagesByDateFragment.PackageSelectedListener mPackageSelectedListener;

    public PackagesItemAdapter(List<PackageDesc> sessionsList, PackagesByDateFragment.PackageSelectedListener mPackageSelectedListener, String mSelectedSessionDate) {
        mList = sessionsList;
        this.mPackageSelectedListener = mPackageSelectedListener;
        this.mSelectedSessionDate = mSelectedSessionDate;
    }

    @Override
    public PackagesItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView, mPackageSelectedListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PackageDesc packageDesc = this.mList.get(position);
        holder.packageNameTextView.setText(Html.fromHtml(packageDesc.name));
        holder.packageStartDateTextView.setText(String.format("%s, %s", getPackageDate(packageDesc.startDate), packageDesc.startTime));
        holder.packageEndDateTextView.setText(String.format("%s, %s", getPackageDate(packageDesc.endDate), packageDesc.endTime));
        holder.itemView.setTag(packageDesc.id);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView packageNameTextView;
        TextView packageStartDateTextView;
        TextView packageEndDateTextView;
        PackagesByDateFragment.PackageSelectedListener packageSelectedListener;

        public ViewHolder(View itemView, PackagesByDateFragment.PackageSelectedListener mPackageSelectedListener) {
            super(itemView);
            this.packageSelectedListener = mPackageSelectedListener;
            packageNameTextView = (TextView) itemView.findViewById(R.id.package_name_text);
            packageStartDateTextView = (TextView) itemView.findViewById(R.id.package_startdate);
            packageEndDateTextView = (TextView) itemView.findViewById(R.id.package_enddate);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            packageSelectedListener.onPackageSelected((String) v.getTag());
        }
    }

    private String getPackageDate(String packageDate) {
        String inputFormat = "yyyy-MM-dd";
        //String outputFormat = "EEEE, dd MMM, yyyy";
        String outputFormat = "dd MMM yyyy";
        String finalDateString = "";
//        String timeInputFormat = "HH:mm";
//        String timeOutputFormat = "hh:mm a";
//        String finalTimeString = "";
//        String tag = "eventTime";
        SimpleDateFormat formatter = new SimpleDateFormat(inputFormat, Locale.US);
        String startDateString = AppUtility.getText(packageDate);

        try {
            Date startDate = formatter.parse(startDateString);
            SimpleDateFormat newFormat = new SimpleDateFormat(outputFormat, Locale.US);
            finalDateString = newFormat.format(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

      /*  SimpleDateFormat formatter1 = new SimpleDateFormat(timeInputFormat);
        String startTimeString = AppUtility.getText(eventDetail.getStartTime());
        try {
            Date startTime = formatter1.parse(startTimeString);
            SimpleDateFormat newFormat = new SimpleDateFormat(timeOutputFormat);
            finalTimeString = newFormat.format(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalDateString + " " + finalTimeString;*/
        return finalDateString;
    }
}