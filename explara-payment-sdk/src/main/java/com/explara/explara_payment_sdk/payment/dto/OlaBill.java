package com.explara.explara_payment_sdk.payment.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anudeep on 11/11/15.
 */
public class OlaBill {
    @SerializedName("status")
    public String status;
    @SerializedName("bill")
    public String bill;
}
