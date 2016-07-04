package com.explara.explara_payment_sdk.payment.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Debasish on 26/03/15.
 */
public class EventDetails {

    @SerializedName("eventTitle")
    public String eventTitle;

    public String eventUrl;

    public String description;

    public String city;

    public String largeImage;

}
