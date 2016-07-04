package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by debasishpanda on 17/11/15.
 */
public class TicketDate {

    @SerializedName("date")
    public String ticketDate;
    @SerializedName("day")
    public String day;
    @SerializedName("year")
    public String year;
    @SerializedName("month")
    public String month;
}
