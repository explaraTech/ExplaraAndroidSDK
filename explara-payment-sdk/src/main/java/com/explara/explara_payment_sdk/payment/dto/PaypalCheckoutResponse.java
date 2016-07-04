package com.explara.explara_payment_sdk.payment.dto;


import com.google.gson.annotations.SerializedName;

/**
 * Created by ajeetkumar on 10/04/15.
 */
public class PaypalCheckoutResponse {


    private static final long serialVersionUID = 2469294384687318608L;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
