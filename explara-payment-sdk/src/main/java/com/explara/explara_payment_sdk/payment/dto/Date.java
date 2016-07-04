package com.explara.explara_payment_sdk.payment.dto;


import com.google.gson.annotations.SerializedName;

/**
 * Created by Debasish on 27/03/15.
 */
public class Date {

    @SerializedName("timezone")
    private String timezone;

    @SerializedName("timezone_type")
    private String timezone_type;

    @SerializedName("date")
    private String date;

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimezone_type() {
        return timezone_type;
    }

    public void setTimezone_type(String timezone_type) {
        this.timezone_type = timezone_type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
