package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Debasish.
 */
public class UpdatePaymentStatusDataDto {
    @SerializedName("accessToken")
    public String accessToken;
    @SerializedName("orderNo")
    public String orderNo;
    @SerializedName("status")
    public String status;
    @SerializedName("referenceNo")
    public String referenceNo;
}
