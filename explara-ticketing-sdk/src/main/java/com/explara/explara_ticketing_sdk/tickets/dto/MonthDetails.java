package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by debasishpanda on 17/11/15.
 */
public class MonthDetails {

    @SerializedName("monthName")
    public String monthName;

    @SerializedName("startPosition")
    public int startPosition;

    @SerializedName("endPosition")
    public int endPosition;
}
