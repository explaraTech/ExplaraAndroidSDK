package com.explara.explara_eventslisting_sdk.events.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dev on 02/07/15.
 */
public class EventsDetailDto {
    @SerializedName("status")
    public String status;

    @SerializedName("events")
    public Event events;

    //private List<Attendee> attendee;
    @SerializedName("attendee")
    public List<Attendee> attendee = java.util.Collections.EMPTY_LIST;

    @SerializedName("totalAttendee")
    public String totalAttendee;

    @SerializedName("soldout")
    public String soldout;

    @SerializedName("favourite")
    public String favourite;

    @SerializedName("tabs")
    public List<Tabs> tabs = java.util.Collections.EMPTY_LIST;

}
