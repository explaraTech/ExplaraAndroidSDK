package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Debasish.
 */

public class AttendeeFields {
    @SerializedName("id")
    public String id;
    @SerializedName("label")
    public String label;
    @SerializedName("value")
    public String value;
    public List<String> valueArr;
}
