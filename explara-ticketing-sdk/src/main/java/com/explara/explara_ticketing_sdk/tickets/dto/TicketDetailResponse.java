package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dev on 02/07/15.
 */
public class TicketDetailResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("tickets")
    private List<Ticket> tickets;

    @SerializedName("category")
    private List<Category> mCategoryList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public List<Category> getCategoryList() {
        return mCategoryList;
    }
}
