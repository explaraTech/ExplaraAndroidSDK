package com.explara.explara_eventslisting_sdk.events.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dev on 02/07/15.
 *
 * @modified Debasish
 */


public class Topics {
    @SerializedName("topicImg")
    public String topicImg;
    @SerializedName("topicId")
    public String topicId;
    @SerializedName("topicName")
    public String topicName;

    @SerializedName("topicImage")
    public String eventDetailTopicImage;

}
