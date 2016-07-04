package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by debasish on 17/11/15.
 */
public class TicketDatesDto {

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("sessionDates")
    public SessionDates sessionDates;
}
