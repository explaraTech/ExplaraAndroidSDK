package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Debasish on 26/03/15.
 */
public class Paypal {

    @SerializedName("isEnable")
    private boolean isEnable;

    @SerializedName("orgPaypalId")
    public String orgPaypalId;

    public boolean getIsEnable() {
        return isEnable;
    }

    public String getOrgPaypalId() {
        return orgPaypalId;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public void setOrgPaypalId(String paypalId) {
        this.orgPaypalId = paypalId;
    }
}
