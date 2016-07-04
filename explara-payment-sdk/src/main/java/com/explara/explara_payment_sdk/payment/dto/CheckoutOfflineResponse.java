package com.explara.explara_payment_sdk.payment.dto;


import com.google.gson.annotations.SerializedName;

/**
 * Created by ajeetkumar on 27/03/15.
 */
public class CheckoutOfflineResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("Offline-Response")
    private OfflineResponse offlineResponse;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OfflineResponse getOfflineResponse() {
        return offlineResponse;
    }

    public void setOfflineResponse(OfflineResponse offlineResponse) {
        this.offlineResponse = offlineResponse;
    }

}
