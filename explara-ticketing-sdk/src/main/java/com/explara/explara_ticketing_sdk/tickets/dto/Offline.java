package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ajeetkumar on 26/03/15.
 */
public class Offline {

    @SerializedName("dd")
    private Dd dd;

    @SerializedName("deposit")
    private Deposit deposit;

    @SerializedName("venue")
    private Venue venue;

    @SerializedName("cheque")
    private Cheque cheque;

    public Dd getDd() {
        return dd;
    }

    public void setDd(Dd dd) {
        this.dd = dd;
    }

    public Deposit getDeposit() {
        return deposit;
    }

    public void setDeposit(Deposit deposit) {
        this.deposit = deposit;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Cheque getCheque() {
        return cheque;
    }

    public void setCheque(Cheque cheque) {
        this.cheque = cheque;
    }

}
