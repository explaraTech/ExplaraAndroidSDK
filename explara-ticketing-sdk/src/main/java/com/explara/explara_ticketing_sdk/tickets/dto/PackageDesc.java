package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by debasish on 17/11/15.
 */
public class PackageDesc {

    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("ticketId")
    public String ticketId;
    @SerializedName("startDate")
    public String startDate;
    @SerializedName("startTime")
    public String startTime;
    @SerializedName("endDate")
    public String endDate;
    @SerializedName("endTime")
    public String endTime;

}
