package com.explara_core.login.login_dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Debasish on 17/07/15.
 */
public class SignupResponseDto implements Serializable {

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    @SerializedName("accountStatus")
    private String accountStatus;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", status = " + status + "]";
    }
}
