package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Debasish on 26/03/15.
 */
public class Ccavenue {
    @SerializedName("isEnable")
    private boolean isEnable;

    public boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    @Override
    public String toString() {
        return "ClassPojo [isEnable = " + isEnable + "]";
    }

}
