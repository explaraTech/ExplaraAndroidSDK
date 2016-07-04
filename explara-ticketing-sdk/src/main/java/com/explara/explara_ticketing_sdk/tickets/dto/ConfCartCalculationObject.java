package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ajeetkumar on 24/03/15.
 */
public class ConfCartCalculationObject {

    @SerializedName("eventId")
    public String eventId;

    @SerializedName("sessions")
    public List<CartSession> sessions;

    @SerializedName("discountCode")
    public String discountCode;

    @SerializedName("quantity")
    public int quantity;

    @SerializedName("version")
    public String version;

    public String getEventId() {
        return eventId;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public List<CartSession> getSessions() {
        return sessions;
    }

    public int getQuantity() {
        return quantity;
    }

    public class CartSession {

        @SerializedName("sessionId")
        public String sessionId;

        @SerializedName("sessionPrice")
        public String sessionPrice;
    }

}
