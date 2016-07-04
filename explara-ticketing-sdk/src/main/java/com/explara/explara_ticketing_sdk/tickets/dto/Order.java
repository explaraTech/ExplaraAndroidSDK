package com.explara.explara_ticketing_sdk.tickets.dto;


import com.google.gson.annotations.SerializedName;

/**
 * Created by ajeetkumar on 24/03/15.
 */
public class Order {

    @SerializedName("status")
    private String status;

    @SerializedName("orderNo")
    private String orderNo;

    @SerializedName("explaraFee")
    private String explaraFee;

    @SerializedName("ticketCost")
    private String ticketCost;

    @SerializedName("paymentOption")
    private PaymentOptions paymentOption;

    public String getStatus() {
        return status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getExplaraFee() {
        return explaraFee;
    }

    public String getTicketCost() {
        return ticketCost;
    }

    public PaymentOptions getPaymentOption() {
        return paymentOption;
    }

}
