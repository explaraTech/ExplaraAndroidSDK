package com.explara.explara_payment_sdk.payment.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Debasish on 08/04/15.
 */
public class Order_Offline {


    private static final long serialVersionUID = -2908805283591486759L;

    @SerializedName("orderNo")
    private String orderNo;

    @SerializedName("paymentType")
    private String paymentType;

    @SerializedName("datePurchased")
    private Date datePurchased;

    @SerializedName("paidAmount")
    private String paidAmount;

    @SerializedName("orderStatus")
    private String orderStatus;

    @SerializedName("buyerEmailId")
    private String buyerEmailId;

    @SerializedName("buyerName")
    private String buyerName;

    @SerializedName("buyerTelephone")
    private String buyerTelephone;


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Date getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(Date datePurchased) {
        this.datePurchased = datePurchased;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getBuyerEmailId() {
        return buyerEmailId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getBuyerTelephone() {
        return buyerTelephone;
    }
}
