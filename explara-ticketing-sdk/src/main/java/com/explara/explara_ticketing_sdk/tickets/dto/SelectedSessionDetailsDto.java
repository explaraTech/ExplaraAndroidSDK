package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by debasish on 24/11/15.
 */
public class SelectedSessionDetailsDto {

//    public int monthPosition;
//    public int datePosition;
//    public String dateSelected;
//    public int timeingPosition;

    @SerializedName("eventId")
    public String eventId;
    @SerializedName("sessionId")
    public String sessionId;
    @SerializedName("sessionPrice")
    public String sessionPrice;
    @SerializedName("sessionName")
    public String sessionName;
    @SerializedName("selectedQuantity")
    public int selectedQuantity;
    @SerializedName("discountUsed")
    public String discountUsed;

    // MultiSession Events
    @SerializedName("sessionsDesc")
    public SessionsDesc sessionsDesc;
    //public CheckBox checkBox;

}
