package com.explara.explara_ticketing_sdk.attendee.dto;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by anudeep on 08/01/16.
 */
public class AttendeeDetailsResponseDto {
    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String message;
    @SerializedName("attendeeForm")
    public LinkedHashMap<String, List<AttendeeDto>> attendeeform;


    public static class AttendeeDto {
        @SerializedName("id")
        public String id;
        @SerializedName("label")
        public String label;
        @SerializedName("type")
        public String type;
        @SerializedName("description")
        public String description;
        @SerializedName("validation")
        public String validation;
        @SerializedName("mandatory")
        public boolean mandatory;

        public transient boolean isValid = false;

        //only for fileupload field
        public transient String fileName;

        //only for fileupload field
        public transient TransferState fileUploadStatus;

        //only for file upload
        public transient int requestCodeFileUpload;

        @SerializedName("options")
        public LinkedHashMap<String, String> options;


    }

}
