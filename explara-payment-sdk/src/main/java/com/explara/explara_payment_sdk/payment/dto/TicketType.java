package com.explara.explara_payment_sdk.payment.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ajeetkumar on 08/04/15.
 */
public class TicketType {


    private static final long serialVersionUID = -9213681853023498148L;

    @SerializedName("cummalativeDiscount")
    private String cummalativeDiscount;

    @SerializedName("availableTo")
    private AvailableTo availableTo;

    @SerializedName("status")
    private String status;

    @SerializedName("bibCount")
    private String bibCount;

    @SerializedName("isDiscountAvailable")
    private String isDiscountAvailable;

    @SerializedName("typeDesc")
    private String typeDesc;

    @SerializedName("maxQuantity")
    private String maxQuantity;

    @SerializedName("bibNo")
    private String bibNo;

    @SerializedName("ticketOrder")
    private String ticketOrder;

    @SerializedName("currency")
    private String currency;

    @SerializedName("id")
    private String id;

    @SerializedName("typeName")
    private String typeName;

    @SerializedName("price")
    private String price;

    @SerializedName("initialQuantity")
    private String initialQuantity;

    @SerializedName("availableFrom")
    private AvailableFrom availableFrom;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("minQuantity")
    private String minQuantity;


    public String getCummalativeDiscount() {
        return cummalativeDiscount;
    }

    public void setCummalativeDiscount(String cummalativeDiscount) {
        this.cummalativeDiscount = cummalativeDiscount;
    }

    public AvailableTo getAvailableTo() {
        return availableTo;
    }

    public void setAvailableTo(AvailableTo availableTo) {
        this.availableTo = availableTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBibCount() {
        return bibCount;
    }

    public void setBibCount(String bibCount) {
        this.bibCount = bibCount;
    }

    public String getIsDiscountAvailable() {
        return isDiscountAvailable;
    }

    public void setIsDiscountAvailable(String isDiscountAvailable) {
        this.isDiscountAvailable = isDiscountAvailable;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(String maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public String getBibNo() {
        return bibNo;
    }

    public void setBibNo(String bibNo) {
        this.bibNo = bibNo;
    }

    public String getTicketOrder() {
        return ticketOrder;
    }

    public void setTicketOrder(String ticketOrder) {
        this.ticketOrder = ticketOrder;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(String initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public AvailableFrom getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(AvailableFrom availableFrom) {
        this.availableFrom = availableFrom;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(String minQuantity) {
        this.minQuantity = minQuantity;
    }
}
