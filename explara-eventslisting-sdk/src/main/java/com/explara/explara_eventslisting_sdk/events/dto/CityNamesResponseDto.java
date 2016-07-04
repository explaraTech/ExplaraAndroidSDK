package com.explara.explara_eventslisting_sdk.events.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ananthasooraj on 1/16/16.
 */
public class CityNamesResponseDto {

    @SerializedName("status")
    public String status;
    @SerializedName("cities")
    public List<Cities> cities;

    public static class Cities {
        @SerializedName("cityId")
        public String cityId;
        @SerializedName("cityName")
        public String cityName;
        @SerializedName("cityImage")
        public String cityImage;
    }
}
