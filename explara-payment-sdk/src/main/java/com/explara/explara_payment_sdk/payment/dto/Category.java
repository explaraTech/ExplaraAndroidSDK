package com.explara.explara_payment_sdk.payment.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Debasish on 26/03/15.
 */
public class Category {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("eventId")
    private String eventId;
    @SerializedName("associatedWith")
    private List<String> associatedWith;

    public String getName() {
        return name;
    }

}
