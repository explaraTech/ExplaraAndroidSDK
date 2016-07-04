package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by debasish on 17/11/15.
 */
public class SessionsDesc {

    @SerializedName("startTime")
    public String startTime;
    @SerializedName("endTime")
    public String endTime;
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("venue")
    public String venue;
    @SerializedName("description")
    public String description;
    @SerializedName("currency")
    public String currency;
    @SerializedName("price")
    public String price;
    @SerializedName("ticketTypeId")
    public String ticketTypeId;
    @SerializedName("sessionTime")
    public String sessionTime;
    @SerializedName("selected")
    private boolean selected;

    //public List<Ticket> ticket;

    // Added for selected quantity for that ticket
    public transient int userSelectedTicketQuantity;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
