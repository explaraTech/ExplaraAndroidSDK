package com.explara.explara_eventslisting_sdk.events.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Debasish on 02/07/15.
 *
 * @modified Debasish
 */


public class EventListingCustomizationDto {
    @SerializedName("showFilterLayout")
    public boolean showFilterLayout; // boolean value true to show and false to hide

    @SerializedName("showCityLayout")
    public boolean showCityLayout; // boolean value true to show and false to hide
}
