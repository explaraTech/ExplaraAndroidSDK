package com.explara.explara_payment_sdk.payment.dto;


import com.google.gson.annotations.SerializedName;

/**
 * Created by ajeetkumar on 08/04/15.
 */
public class TicketInfo {

    private static final long serialVersionUID = -3867139441566148392L;

    @SerializedName("ticketTypeId")
    private String ticketTypeId;

    @SerializedName("TicketType")
    private TicketType TicketType;

    @SerializedName("categoryId")
    private String categoryId;

    @SerializedName("ticketNumber")
    private String ticketNumber;

    @SerializedName("attendeeInfo")
    private AttendeeInfo attendeeInfo;

    @SerializedName("bibNo")
    private String bibNo;

    @SerializedName("price")
    private String price;

    @SerializedName("currency")
    private String currency;

    @SerializedName("categoryName")
    private Category categoryName;

    public String getTicketTypeId() {
        return ticketTypeId;
    }

    public void setTicketTypeId(String ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public TicketType getTicketType() {
        return TicketType;
    }

    public void setTicketType(TicketType TicketType) {
        this.TicketType = TicketType;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public AttendeeInfo getAttendeeInfo() {
        return attendeeInfo;
    }

    public void setAttendeeInfo(AttendeeInfo attendeeInfo) {
        this.attendeeInfo = attendeeInfo;
    }

    public String getBibNo() {
        return bibNo;
    }

    public void setBibNo(String bibNo) {
        this.bibNo = bibNo;
    }

    public String getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public Category getCategoryName() {
        return categoryName;
    }

}
