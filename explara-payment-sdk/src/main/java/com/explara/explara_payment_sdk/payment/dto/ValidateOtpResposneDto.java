package com.explara.explara_payment_sdk.payment.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anudeep on 20/11/15.
 */
public class ValidateOtpResposneDto {

    @SerializedName("access_token")
    public String access_token;
    @SerializedName("expires")
    public String expires;
    @SerializedName("scope")
    public String scope;
    @SerializedName("resourceOwnerId")
    public String resourceOwnerId;
}
