package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ajeetkumar on 26/03/15.
 */
public class Online {

    @SerializedName("ebs")
    private Ebs ebs;

    @SerializedName("paypal")
    private Paypal paypal;

    @SerializedName("paytm")
    private Paytm paytm;

    @SerializedName("ccavenue")
    private Ccavenue ccavenue;

    @SerializedName("payu")
    private Payu payu;

    @SerializedName("ola")
    public Ola ola;

    @SerializedName("citrus")
    public Citrus citrus;

    @SerializedName("citrusWallet")
    public CitrusWallet citrusWallet;


    public Ebs getEbs() {
        return ebs;
    }

    public void setEbs(Ebs ebs) {
        this.ebs = ebs;
    }

    public Paypal getPaypal() {
        return paypal;
    }

    public void setPaypal(Paypal paypal) {
        this.paypal = paypal;
    }

    public Paytm getPaytm() {
        return paytm;
    }

    public void setPaytm(Paytm paytm) {
        this.paytm = paytm;
    }

    public Ccavenue getCcavenue() {
        return ccavenue;
    }

    public void setCcavenue(Ccavenue ccavenue) {
        this.ccavenue = ccavenue;
    }

    public Payu getPayu() {
        return payu;
    }

    public void setPayu(Payu payu) {
        this.payu = payu;
    }

    public static class Citrus {
        @SerializedName("isEnable")
        public boolean isEnable;
    }


    public static class CitrusWallet {
        @SerializedName("isEnable")
        public boolean isEnable;
    }

    @Override
    public String toString() {
        return "ClassPojo [ebs = " + ebs + ", paypal = " + paypal + ", paytm = " + paytm + ", ccavenue = " + ccavenue + ", payu = " + payu + "]";
    }

}
