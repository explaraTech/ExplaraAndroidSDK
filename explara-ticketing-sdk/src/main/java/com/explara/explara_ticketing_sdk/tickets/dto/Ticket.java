package com.explara.explara_ticketing_sdk.tickets.dto;

import com.explara.explara_ticketing_sdk.utils.TicketingConstantKeys;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dev on 02/07/15.
 */
public class Ticket {

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("status")
    private String status;

    @SerializedName("ticketStatus")
    private String ticketStatus;

    @SerializedName("endDate")
    private String endDate;

    @SerializedName("endTime")
    private String endTime;

    @SerializedName("maxQuantity")
    private int maxQuantity;

    @SerializedName("currency")
    private String currency;

    @SerializedName("id")
    private int id;

    @SerializedName("startTime")
    private String startTime;

    @SerializedName("price")
    // private Double price;
    private String price;

    @SerializedName("eventId")
    private String eventId;

    @SerializedName("categoryId")
    private String categoryId;

    @SerializedName("discounts")
    private List<Discount> discounts;

    @SerializedName("name")
    private String name;

    @SerializedName("quantity")
    private String quantity;

    // Added for selected quantity for that ticket
    public transient int userSelectedTicketQuantity;

    @SerializedName("minQuantity")
    private int minQuantity;

    @SerializedName("description")
    private String description;

    public transient int itemType = TicketingConstantKeys.TicketListkeys.ITEM_TYPE;

    @SerializedName("categoryName")
    public transient String categoryName;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public String getTicketStatus() {
        return this.ticketStatus;
    }

    public void setTicketStatus(String status) {
        this.ticketStatus = status;
    }

    @Override
    public String toString() {
        return "ClassPojo [startDate = " + startDate + ", status = " + status + ", endDate = " + endDate + ", endTime = " + endTime + ", maxQuantity = " + maxQuantity + ", currency = " + currency + ", id = " + id + ", startTime = " + startTime + ", price = " + price + ", eventId = " + eventId + ", description = " + description + ", discounts = " + discounts + ", name = " + name + ", quantity = " + quantity + ", minQuantity = " + minQuantity + "]";
    }
}
