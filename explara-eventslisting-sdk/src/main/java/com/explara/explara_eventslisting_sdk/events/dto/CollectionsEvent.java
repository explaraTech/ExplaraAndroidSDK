package com.explara.explara_eventslisting_sdk.events.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ajeetkumar on 16/07/15.
 */
public class CollectionsEvent {
    @SerializedName("category")
    private String category;

    @SerializedName("events")
    private List<CollectionEventsDto.Events> events;

    @SerializedName("name")
    private String name;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CollectionEventsDto.Events> getEvents() {
        return events;
    }

    public void setEvents(List<CollectionEventsDto.Events> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "ClassPojo [category = " + category + ", events = " + events + "]";
    }
}
