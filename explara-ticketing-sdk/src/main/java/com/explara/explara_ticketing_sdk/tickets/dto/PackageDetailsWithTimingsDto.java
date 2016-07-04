package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by debasish on 17/11/15.
 */
public class PackageDetailsWithTimingsDto {

    @SerializedName("status")
    public String status;
    @SerializedName("sessions")
    public List<PackageDesc> sessions;
}
