package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Debasish on 26/03/15.
 */


public class PaymentOptions {

    @SerializedName("offline")
    private Offline offline;

    @SerializedName("online")
    private Online online;

    public Offline getOffline() {
        return offline;
    }

    public void setOffline(Offline offline) {
        this.offline = offline;
    }

    public Online getOnline() {
        return online;
    }

    public void setOnline(Online online) {
        this.online = online;
    }

    @Override
    public String toString() {
        return "ClassPojo [offline = " + offline + ", online = " + online + "]";
    }

}
