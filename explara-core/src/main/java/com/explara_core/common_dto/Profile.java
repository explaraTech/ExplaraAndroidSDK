package com.explara_core.common_dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ajeetkumar on 17/07/15.
 */
public class Profile {

    @SerializedName("lastName")
    private String lastName;
    @SerializedName("emailId")
    private String emailId;
    @SerializedName("profileImage")
    private String profileImage;
    @SerializedName("mobileNumber")
    private String mobileNumber;
    @SerializedName("firstName")
    private String firstName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return "ClassPojo [lastName = " + lastName + ", emailId = " + emailId + ", profileImage = " + profileImage + ", mobileNumber = " + mobileNumber + ", firstName = " + firstName + "]";
    }
}
