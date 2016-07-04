package com.explara.explara_payment_sdk.payment.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anudeep on 27/11/15.
 */
public class OlaResponse {

    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("amount")
    public String amount;
    @SerializedName("transactionId")
    public String transactionId;
    @SerializedName("isCashbackAttempted")
    public String isCashbackAttempted;
    @SerializedName("hash")
    public String hash;
    @SerializedName("status")
    public String status;
    @SerializedName("udf")
    public String udf;
    @SerializedName("merchantBillId")
    public String merchantBillId;
    @SerializedName("type")
    public String type;
    @SerializedName("comments")
    public String comments;
}
