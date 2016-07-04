package com.explara.explara_payment_sdk.payment.dto;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ajeetkumar on 27/03/15.
 */
public class OfflineResponse {


    private static final long serialVersionUID = 4937018053087816676L;

    @SerializedName("showBib")
    private String showBib;

    @SerializedName("order")
    private Order_Offline order;

    @SerializedName("currencySymbol")
    private String currencySymbol;

    @SerializedName("location")
    private List<Location> location;

    @SerializedName("orgEmailId")
    private String orgEmailId;

    @SerializedName("event")
    private Event_OfflineResponse event;

    @SerializedName("orgName")
    private String orgName;

    @SerializedName("ticketInfo")
    private List<TicketInfo> ticketInfo;

    public String getShowBib() {
        return showBib;
    }

    public void setShowBib(String showBib) {
        this.showBib = showBib;
    }

    public Order_Offline getOrder() {
        return order;
    }

    public void setOrder(Order_Offline order) {
        this.order = order;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public List<Location> getLocation() {
        return location;
    }

    public void setLocation(List<Location> location) {
        this.location = location;
    }

    public String getOrgEmailId() {
        return orgEmailId;
    }

    public void setOrgEmailId(String orgEmailId) {
        this.orgEmailId = orgEmailId;
    }

    public Event_OfflineResponse getEvent() {
        return event;
    }

    public void setEvent(Event_OfflineResponse event) {
        this.event = event;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<TicketInfo> getTicketInfo() {
        return ticketInfo;
    }

    public void setTicketInfo(List<TicketInfo> ticketInfo) {
        this.ticketInfo = ticketInfo;
    }
}
