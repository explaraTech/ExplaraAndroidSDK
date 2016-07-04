package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Debasish.
 */
public class SaveBuyerFormDataDto {
    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String message;
    @SerializedName("orderNo")
    public String orderNo;
}
