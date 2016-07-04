package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by debasishpanda on 17/11/15.
 */
public class MultidateDataObj {

    @SerializedName("eventId")
    public String eventId;
    @SerializedName("date")
    public String date;
    @SerializedName("packageId")
    public String packageId;

}
