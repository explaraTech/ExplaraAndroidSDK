package com.explara.explara_payment_sdk.payment.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anudeep on 25/11/15.
 */

public class PaymentOptionDto {

    @SerializedName("title")
    public String title;

    @SerializedName("id")
    public int id;
}
