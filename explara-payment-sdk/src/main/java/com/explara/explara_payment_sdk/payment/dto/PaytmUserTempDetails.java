package com.explara.explara_payment_sdk.payment.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anudeep on 02/12/15.
 */
public class PaytmUserTempDetails {
    @SerializedName("email")
    public String email = "";
    @SerializedName("phoneNum")
    public String phoneNum = "";
    @SerializedName("otp")
    public String otp = "";
    @SerializedName("showEnterOtp")
    public boolean showEnterOtp = false;
}
