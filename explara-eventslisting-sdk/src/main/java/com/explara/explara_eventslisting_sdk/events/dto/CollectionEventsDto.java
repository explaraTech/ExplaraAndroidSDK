package com.explara.explara_eventslisting_sdk.events.dto;

import com.explara.explara_eventslisting_sdk.utils.EventsListingConstantKeys;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by anudeep on 03/02/16.
 */
public class CollectionEventsDto {


    @SerializedName("status")
    public String status;
    @SerializedName("collections")
    public Collections collections;

    public static class Events {
        @SerializedName("id")
        public String id;
        @SerializedName("title")
        public String title;
        @SerializedName("url")
        public String url;
        @SerializedName("smallImage")
        public String smallImage;
        @SerializedName("startDateFormat")
        public String startDateFormat;
        @SerializedName("endDateFormat")
        public String endDateFormat;
        @SerializedName("startDate")
        public String startDate;
        @SerializedName("endDate")
        public String endDate;
        @SerializedName("startTime")
        public String startTime;
        @SerializedName("endTime")
        public String endTime;
        @SerializedName("createdOn")
        public String createdOn;
        @SerializedName("category")
        public String category;
        @SerializedName("type")
        public String type;
        @SerializedName("eventSessionType")
        public String eventSessionType;
        @SerializedName("largeImage")
        public String largeImage;
        @SerializedName("textDescription")
        public String textDescription;
        @SerializedName("shortDescription")
        public String shortDescription;
        @SerializedName("city")
        public String city;
        @SerializedName("state")
        public String state;
        @SerializedName("venueName")
        public String venueName;
        @SerializedName("address")
        public String address;
        @SerializedName("zipcode")
        public String zipcode;
        @SerializedName("country")
        public String country;
        @SerializedName("currency")
        public String currency;
        @SerializedName("price")
        public String price;
        @SerializedName("filter")
        public String filter;

        @SerializedName("filters")
        public Filter filters;

        public transient int itemType = EventsListingConstantKeys.EventKeys.EVENT_TYPE;

        public Filter getFilter() {
            return filters;
        }
    }

    public static class CollectionEvents {
        @SerializedName("category")
        public String category;
        @SerializedName("events")
        public List<Events> events;
    }

    public static class Collections {
        @SerializedName("collectionEvents")
        public List<CollectionEvents> collectionEvents;
        @SerializedName("collectionCategories")
        public List<String> collectionCategories;
    }
}
