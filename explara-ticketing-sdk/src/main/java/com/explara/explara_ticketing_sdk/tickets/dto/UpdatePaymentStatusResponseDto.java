package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Debasish.
 */
public class UpdatePaymentStatusResponseDto {
    @SerializedName("success")
    public String success;
    @SerializedName("message")
    public String message;
    @SerializedName("orderNo")
    public String orderNo;
    @SerializedName("orderStatus")
    public String orderStatus;
}
