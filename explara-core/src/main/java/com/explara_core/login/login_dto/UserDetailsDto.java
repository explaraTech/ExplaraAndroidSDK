package com.explara_core.login.login_dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Debasish.
 */
public class UserDetailsDto {

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("fbAccessToken")
    public String fbAccessToken;

    @SerializedName("fbAppId")
    public String fbAppId;

    @SerializedName("phoneNo")
    public String phoneNo;

    @SerializedName("name")
    public String name;

    @SerializedName("sendVerification")
    public String sendVerification;

}
