package com.explara.explara_eventslisting_sdk.events.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by ananthasooraj on 1/14/16.
 */
public class EventsWithTopicsDto {

    @SerializedName("status")
    public String status;
    @SerializedName("topics")
    public Topics topics;

    public static class TopicItemDto {
        @SerializedName("topicId")
        public String topicId;
        @SerializedName("topicName")
        public String topicName;
        @SerializedName("topicImg")
        public String topicImg;
    }

    public static class Topics {

        @SerializedName("topics")
        public Map<String, List<TopicItemDto>> topics;
    }
}
