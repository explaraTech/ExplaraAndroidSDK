package com.explara.explara_payment_sdk.payment.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anudeep on 20/11/15.
 */
public class PayTmUserProfile {

    @SerializedName("id")
    public String id;
    @SerializedName("email")
    public String email;
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("expires")
    public String expires;
    @SerializedName("WALLETBALANCE")
    public String WALLETBALANCE;
    @SerializedName("STATUS")
    public String STATUS;
    @SerializedName("RESPONSECODE")
    public String RESPONSECODE;
}
