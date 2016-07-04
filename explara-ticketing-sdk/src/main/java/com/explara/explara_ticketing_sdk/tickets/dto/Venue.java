package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ajeetkumar on 26/03/15.
 */
public class Venue {

    @SerializedName("isEnabled")
    private boolean isEnabled;

    @SerializedName("info")
    private String info;


    public boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "ClassPojo [isEnabled = " + isEnabled + ", info = " + info + "]";
    }

}
