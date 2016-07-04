package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by debasish on 17/11/15.
 */
public class SessionsDetails {

    @SerializedName("time")
    public String time;
    @SerializedName("isCodeDiscountAvailable")
    public String isCodeDiscountAvailable;
    @SerializedName("sessions")
    public List<SessionsDesc> sessions;

}
