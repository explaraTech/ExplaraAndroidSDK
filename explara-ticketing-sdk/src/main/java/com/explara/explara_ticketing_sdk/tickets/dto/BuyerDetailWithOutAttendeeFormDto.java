package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Debasish.
 */

public class BuyerDetailWithOutAttendeeFormDto {
    @SerializedName("buyerName")
    public String buyerName;

    @SerializedName("buyerEmail")
    public String buyerEmail;

    @SerializedName("buyerPhone")
    public String buyerPhone;
}
