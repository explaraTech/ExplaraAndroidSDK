package com.explara.explara_ticketing_sdk.tickets.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ajeetkumar on 20/03/15.
 */
public class Cart {


    private static final long serialVersionUID = -5078165512430357301L;

    @SerializedName("serviceTax")
    private String serviceTax;

    @SerializedName("price")
    private String price;

    @SerializedName("grandTotal")
    private String grandTotal;

    @SerializedName("processingFee")
    private String processingFee;

    @SerializedName("discount")
    private String discount;

    @SerializedName("currency")
    private String currency;

    @SerializedName("codeApplied")
    private Boolean codeApplied;

    public String getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(String serviceTax) {
        this.serviceTax = serviceTax;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(String processingFee) {
        this.processingFee = processingFee;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getCodeApplied() {
        return codeApplied;
    }

    public void setCodeApplied(Boolean codeApplied) {
        this.codeApplied = codeApplied;
    }

}
