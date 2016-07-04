package com.explara.explara_eventslisting_sdk.events.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by anudeep on 03/02/16.
 */
public class OrganizerEventsDto {


    @SerializedName("status")
    public String status;
    @SerializedName("allEvents")
    public List<AllEvents> allEvents;

    public static class AllEvents {
        @SerializedName("listType")
        public String listType;
        @SerializedName("events")
        public List<CollectionEventsDto.Events> events;
    }
}
