package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by debasishpanda on 14/09/15.
 */
public class ConfirmRsvpDto {
    @SerializedName("success")
    public int success;

    @SerializedName("message")
    public String message;
}
