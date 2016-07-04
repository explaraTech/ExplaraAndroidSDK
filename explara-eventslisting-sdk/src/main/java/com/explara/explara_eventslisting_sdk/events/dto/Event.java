package com.explara.explara_eventslisting_sdk.events.dto;

import com.explara.explara_eventslisting_sdk.utils.EventsListingConstantKeys;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by ajeetkumar on 16/07/15.
 */
public class Event {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("smallImage")
    private String smallImage;


    @SerializedName("startDateFormat")
    private String startDateFormat;

    @SerializedName("endDateFormat")
    private String endDateFormat;

    @SerializedName("startDate")
    private String startDate;


    @SerializedName("endDate")
    private String endDate;

    @SerializedName("startTime")
    private String startTime;


    @SerializedName("endTime")
    private String endTime;


    @SerializedName("createdOn")
    private String createdOn;

    @SerializedName("type")
    private String type;

    @SerializedName("largeImage")
    private String largeImage;

    @SerializedName("textDescription")
    private String textDescription;


    @SerializedName("shortDescription")
    private String shortDescription;


    @SerializedName("city")
    private String city;


    @SerializedName("state")
    private String state;


    @SerializedName("venueName")
    private String venueName;


    @SerializedName("address")
    private String address;

    @SerializedName("zipcode")
    private String zipcode;

    @SerializedName("country")
    private String country;

    @SerializedName("currency")
    private String currency;

    @SerializedName("price")
    private String price;

    @SerializedName("filters")
    private Filter filters;

    @SerializedName("contactDetails")
    public String contactDetails;

    @SerializedName("isAttendeeFormEnabled")
    private String isAttendeeFormEnabled;

    @SerializedName("soldout")
    private String soldout;

    @SerializedName("category")
    private String category;

    @SerializedName("email")
    private String email;

    @SerializedName("eventSessionType")
    private String eventSessionType;

    @SerializedName("enquiryForm")
    private String enquiryForm;

    @SerializedName("contactInfo")
    private String contactInfo;

    @SerializedName("Location")
    private List<Location> Location;

    @SerializedName("showButton")
    private String showButton;

    @SerializedName("topics")
    private List<String> topics;

    public transient int itemType = EventsListingConstantKeys.EventKeys.EVENT_TYPE;

    @SerializedName("eventTopics")
    public List<Topics> eventTopics;

    /*public static class EventTopics {
        @SerializedName("topicId")
        public String topicId;
        @SerializedName("topicName")
        public String topicName;
        @SerializedName("topicImage")
        public String topicImage;
    }*/


    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLargeImage() {
        return largeImage;
    }

    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEndDateFormat() {
        return endDateFormat;
    }

    public void setEndDateFormat(String endDateFormat) {
        this.endDateFormat = endDateFormat;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTextDescription() {
        return textDescription;
    }

    public void setTextDescription(String textDescription) {
        this.textDescription = textDescription;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getEventSessionType() {
        return eventSessionType;
    }

    public void setEventSessionType(String eventSessionType) {
        this.eventSessionType = eventSessionType;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public String getStartDateFormat() {
        return startDateFormat;
    }

    public void setStartDateFormat(String startDateFormat) {
        this.startDateFormat = startDateFormat;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public Filter getFilter() {
        return filters;
    }

    public void setFilter(Filter filter) {
        this.filters = filter;
    }

    public List<Location> getLocation() {
        return Location;
    }

    public void setLocation(List<Location> Location) {
        this.Location = Location;
    }

    public String getShowButton() {
        return this.showButton;
    }

    public void setShowButton(String value) {
        this.showButton = value;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTopics(List topics) {
        this.topics = topics;
    }

    public List getTopics() {
        return topics;
    }

    public String getIsAttendeeFormEnabled() {
        return isAttendeeFormEnabled;
    }

    public String getIsEnquiryFormEnabled() {
        return enquiryForm;
    }

    public void setIsEnquiryFormEnabled(String enquiryForm) {
        this.enquiryForm = enquiryForm;

    }

    public void setIsSoldOut(String soldout) {
        this.soldout = soldout;
    }

    public String getIsSoldOut() {
        return soldout;

    }


    public void setIsAttendeeFormEnabled(String isAttendeeFormEnabled) {
        this.isAttendeeFormEnabled = isAttendeeFormEnabled;
    }

    @Override
    public String toString() {
        return "ClassPojo [startDate = " + startDate + ", zipcode = " + zipcode + ", state = " + state + ", largeImage = " + largeImage + ", endDate = " + endDate + ", type = " + type + ", endDateFormat = " + endDateFormat + ", endTime = " + endTime + ", textDescription = " + textDescription + ", url = " + url + ", currency = " + currency + ", country = " + country + ", city = " + city + ", id = " + id + ", startTime = " + startTime + ", createdOn = " + createdOn + ", title = " + title + ", category = " + category + ", price = " + price + ", address = " + address + ", shortDescription = " + shortDescription + ", smallImage = " + smallImage + ", startDateFormat = " + startDateFormat + ", venueName = " + venueName + ", filter = " + filters + "]";
    }
}
