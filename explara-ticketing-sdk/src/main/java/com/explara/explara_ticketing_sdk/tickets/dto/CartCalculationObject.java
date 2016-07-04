package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ajeetkumar on 24/03/15.
 */
public class CartCalculationObject {


    private static final long serialVersionUID = -5631090668277194478L;

    @SerializedName("eventId")
    public String eventId;

    @SerializedName("multiDateId")
    public String multiDateId;

    @SerializedName("version")
    public String version;

    @SerializedName("tickets")
    public List<CartTicket> tickets;

    public String getEventId() {
        return eventId;
    }

    public List<CartTicket> getTickets() {
        return tickets;
    }


    public class CartTicket {

        @SerializedName("ticketId")
        public int ticketId;
        @SerializedName("quantity")
        public int quantity;
        @SerializedName("discountCode")
        public String discountCode;
        @SerializedName("categoryId")
        public String categoryId;
        @SerializedName("ticketPrice")
        public String ticketPrice;

        public transient String ticketName;

    }

}
