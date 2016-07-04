package com.explara.explara_sdk.communication.dto;

import com.explara.explara_ticketing_sdk.tickets.dto.BuyerDetailWithOutAttendeeFormDto;
import com.explara.explara_ticketing_sdk.tickets.dto.CartCalculationObject;
import com.explara.explara_ticketing_sdk.tickets.dto.ConfCartCalculationObject;
import com.explara.explara_ticketing_sdk.tickets.dto.SelectedSessionDetailsDto;
import com.google.gson.annotations.SerializedName;


/**
 * Created by Debasish
 */
public class TransactionDto {

    @SerializedName("eventId")
    public String eventId;
    // for ticketing events
    @SerializedName("cartCalculationObject")
    public CartCalculationObject cartCalculationObject;
    // for theater
    @SerializedName("selectedSessionDetailsDto")
    public SelectedSessionDetailsDto selectedSessionDetailsDto;
    // For conference
    @SerializedName("confCartCalculationObject")
    public ConfCartCalculationObject confCartCalculationObject;

    public String grandTotalAmount;

    public String orderNo;

    //@SerializedName("cart")
    //public Cart cart;

    //@SerializedName("order")
    //public Order order;

    @SerializedName("buyerDetailWithOutAttendeeFormDto")
    public BuyerDetailWithOutAttendeeFormDto buyerDetailWithOutAttendeeFormDto;

    //@SerializedName("paymentOptions")
    //public PaymentOptions paymentOptions;


    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setBuyerDetail(BuyerDetailWithOutAttendeeFormDto buyerDetailWithOutAttendeeFormDto) {
        this.buyerDetailWithOutAttendeeFormDto = buyerDetailWithOutAttendeeFormDto;
    }

}
