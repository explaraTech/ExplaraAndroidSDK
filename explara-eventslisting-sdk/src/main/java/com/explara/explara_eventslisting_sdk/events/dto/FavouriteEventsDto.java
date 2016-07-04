package com.explara.explara_eventslisting_sdk.events.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Debasish on 16/07/15.
 */
public class FavouriteEventsDto {

    @SerializedName("status")
    public String status;

    @SerializedName("events")
    public List<CollectionEventsDto.Events> events;

}
