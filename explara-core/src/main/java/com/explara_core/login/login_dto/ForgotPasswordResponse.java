package com.explara_core.login.login_dto;


import com.explara_core.common_dto.IDataModel;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Debasish on 17/07/15.
 */
public class ForgotPasswordResponse extends IDataModel {

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

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

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", status = " + status + "]";
    }
}
