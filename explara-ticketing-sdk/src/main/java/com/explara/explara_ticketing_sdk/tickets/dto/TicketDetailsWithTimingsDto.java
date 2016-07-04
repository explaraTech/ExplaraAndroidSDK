package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by debasish on 17/11/15.
 */
public class TicketDetailsWithTimingsDto {

    @SerializedName("status")
    public String status;
    @SerializedName("isCodeAvailable")
    public String isCodeAvailable;
    @SerializedName("sessionDates")
    public List<SessionsDetails> sessionDates;
}
