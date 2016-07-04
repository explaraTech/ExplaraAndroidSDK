package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dev on 02/07/15.
 */
public class Discount {

    @SerializedName("startTime")
    private String startTime;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("discountType")
    private String discountType;

    @SerializedName("status")
    private String status;

    @SerializedName("endDate")
    private String endDate;

    @SerializedName("endTime")
    private String endTime;

    @SerializedName("type")
    private String type;

    @SerializedName("discount")
    private String discount;

    @SerializedName("discountDescription")
    private String discountDescription;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountDescription() {
        return this.discountDescription;
    }

    public void setDiscountDescription(String description) {
        this.discountDescription = description;
    }

    @Override
    public String toString() {
        return "ClassPojo [startTime = " + startTime + ", startDate = " + startDate + ", discountType = " + discountType + ", status = " + status + ", endDate = " + endDate + ", endTime = " + endTime + ", type = " + type + ", discount = " + discount + "]";
    }
}
