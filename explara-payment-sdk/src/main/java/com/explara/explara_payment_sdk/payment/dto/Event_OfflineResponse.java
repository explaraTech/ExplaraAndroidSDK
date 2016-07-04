package com.explara.explara_payment_sdk.payment.dto;


import com.google.gson.annotations.SerializedName;

/**
 * Created by Debasish on 27/03/15.
 */
public class Event_OfflineResponse {

    private static final long serialVersionUID = 892284637686353097L;

    @SerializedName("id")
    private int id;

    @SerializedName("startDate")
    private Date startDate;

    @SerializedName("topics")
    private String topics;

    @SerializedName("contactInfo")
    private String contactInfo;

    @SerializedName("description")
    private String description;

    @SerializedName("name")
    private String name;

    @SerializedName("endDate")
    private Date endDate;

    @SerializedName("eventType")
    private String eventType;

    @SerializedName("publishedDate")
    private Date publishedDate;

    @SerializedName("eventStatus")
    private String eventStatus;

    @SerializedName("eventCategory")
    private String eventCategory;

    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
