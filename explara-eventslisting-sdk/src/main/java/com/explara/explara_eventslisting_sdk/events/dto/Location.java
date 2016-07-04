package com.explara.explara_eventslisting_sdk.events.dto;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dev on 02/07/15.
 */
public class Location {

    @SerializedName("address")
    private String address;

    @SerializedName("zipcode")
    private String zipcode;

    @SerializedName("state")
    private String state;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("venueName")
    private String venueName;

    @SerializedName("country")
    private String country;

    @SerializedName("city")
    public String city;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getVenueName() {

        if (TextUtils.isEmpty(venueName)) {
            if (TextUtils.isEmpty(address)) {
                return state;
            }
            return address;
        }
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "ClassPojo [address = " + address + ", zipcode = " + zipcode + ", state = " + state + ", longitude = " + longitude + ", latitude = " + latitude + ", venueName = " + venueName + ", country = " + country + "]";
    }
}
