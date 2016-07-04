package com.explara.explara_payment_sdk.payment.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anudeep on 20/11/15.
 */
public class OtpResponseDto {

    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String message;
    @SerializedName("responseCode")
    public String responseCode;
    @SerializedName("state")
    public String state;
}
