package com.explara.explara_eventslisting_sdk.events.dto;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ajeetkumar on 16/07/15.
 */
public class Filter {

    @SerializedName("free")
    private String free;

    @SerializedName("popular")
    private String popular;

    @SerializedName("thisMonth")
    private String thisMonth;

    @SerializedName("today")
    private String today;

    @SerializedName("latest")
    private String latest;

    @SerializedName("thisWeekend")
    private String thisWeekend;

    @SerializedName("thisWeek")
    private String thisWeek;

    @SerializedName("price")
    public String price;

    @SerializedName("priceRange")
    public int priceRange;

    @SerializedName("pageView")
    public String pageView;


    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public String getPopular() {
        return popular;
    }


    public void setPopular(String popular) {
        this.popular = popular;
    }

    public String getThisMonth() {
        return thisMonth;
    }

    public void setThisMonth(String thisMonth) {
        this.thisMonth = thisMonth;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getLatest() {
        return latest;
    }

    public void setLatest(String latest) {
        this.latest = latest;
    }

    public String getThisWeekend() {
        return thisWeekend;
    }

    public void setThisWeekend(String thisWeekend) {
        this.thisWeekend = thisWeekend;
    }

    public String getThisWeek() {
        return thisWeek;
    }

    public void setThisWeek(String thisWeek) {
        this.thisWeek = thisWeek;
    }

    public String getPageView() {
        if (TextUtils.isEmpty(pageView)) {
            return "0";
        }
        return pageView;
    }

    @Override
    public String toString() {
        return "ClassPojo [free = " + free + ", popular = " + popular + ", thisMonth = " + thisMonth + ", today = " + today + ", latest = " + latest + ", thisWeekend = " + thisWeekend + ", thisWeek = " + thisWeek + "]";
    }
}
