package com.explara.explara_payment_sdk.payment.dto;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Debasish on 25/11/15.
 */

public class PaytmPayMentResposnseDto {

    @SerializedName("TxnId")
    public String TxnId;

    @SerializedName("MID")
    public String MID;

    @SerializedName("OrderId")
    public String OrderId;

    @SerializedName("TxnAmount")
    public String TxnAmount;

    @SerializedName("BankTxnId")
    public String BankTxnId;

    @SerializedName("ResponseCode")
    public String ResponseCode;
    @SerializedName("ResponseMessage")
    public String ResponseMessage;
    @SerializedName("Status")
    public String Status;
    @SerializedName("PaymentMode")
    public String PaymentMode;

    @SerializedName("BankName")
    public String BankName;

    @SerializedName("CheckSum")
    public String CheckSum;

    @SerializedName("CustId")
    public String CustId;

    @SerializedName("MBID")
    public String MBID;

    @SerializedName("STATUS")
    public String STATUS;

    @SerializedName("STATUSMESSAGE")
    public String STATUSMESSAGE;

    @SerializedName("BLOCKEDAMOUNT")
    public String BLOCKEDAMOUNT;

    @SerializedName("ErrorCode")
    public String ErrorCode;

    @SerializedName("ErrorMsg")
    public String ErrorMsg;

    public String getStatus() {
        if (TextUtils.isEmpty(Status)) {
            return STATUS;
        }
        return Status;
    }

    public String getMessage() {
        if (TextUtils.isEmpty(STATUSMESSAGE)) {
            return ResponseMessage;
        }
        return STATUSMESSAGE;
    }

    /**
     The following code for reference for the various paytm responses
     */

//    ErrorCode: "330",
//    ErrorMsg: "Paytm checksum mismatch."

//    {
//        "ORDER_ID":"E5DAFFGXXXX56597E0E4CA7B", "BLOCKEDAMOUNT":"1.0",
//            "STATUS":"TXN_FAILURE", "PREAUTH_ID":"",
//            "STATUSMESSAGE":
//        "Your balance is insufficient for this request. Please add money in your wallet before proceeding."
//    }

}
