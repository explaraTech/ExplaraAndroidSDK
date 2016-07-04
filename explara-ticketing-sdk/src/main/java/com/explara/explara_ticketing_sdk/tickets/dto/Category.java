package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Category {

    private static final long serialVersionUID = -5516335416968376979L;

    @SerializedName("id")
    private String mCategoryID;
    @SerializedName("name")
    private String mCategoryName;
    @SerializedName("Tickets")
    private List<Ticket> mTickets;

    public String getCategoryID() {
        return mCategoryID;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public List<Ticket> getTickets() {
        return mTickets;
    }
}
