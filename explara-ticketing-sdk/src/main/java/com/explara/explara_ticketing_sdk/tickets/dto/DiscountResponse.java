package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ajeetkumar on 20/03/15.
 */
public class DiscountResponse {


    private static final long serialVersionUID = 4970782337027303629L;

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("cart")
    private Cart cart;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

}
