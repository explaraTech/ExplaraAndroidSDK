package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by debasish on 17/11/15.
 */
public class MultiSessionDto {

    @SerializedName("discountCodeUsed")
    public String discountCodeUsed;

    @SerializedName("attendeeQuantity")
    public int attendeeQuantity = 0;

}
