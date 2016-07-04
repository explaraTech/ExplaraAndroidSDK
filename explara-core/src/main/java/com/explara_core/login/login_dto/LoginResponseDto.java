package com.explara_core.login.login_dto;


import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by anudeep on 06/10/15.
 */

public class LoginResponseDto {

    @SerializedName("status")
    public String status;


    @SerializedName("message")
    public String message;


    @SerializedName("accountStatus")
    public String accountStatus;


    @SerializedName("access_token")
    public String access_token;


    @SerializedName("topics")
    public List<String> topics;


    @SerializedName("account")
    public Account account;


    @SerializedName("bank")
    public Bank bank;

    @DatabaseTable
    public static class Bank {

        @DatabaseField
        @SerializedName("holderName")
        public String holderName;

        @DatabaseField(id = true)
        @SerializedName("accountNumber")
        public String accountNumber;

        @DatabaseField
        @SerializedName("bankName")
        public String bankName;

        @DatabaseField
        @SerializedName("bankCode")
        public String bankCode;

        @DatabaseField
        @SerializedName("branch")
        public String branch;

        @DatabaseField
        @SerializedName("address")
        public String address;

        @DatabaseField
        @SerializedName("country")
        public String country;

        public Bank() {

        }
    }

    public JSONObject jsonObject;
    public String fbEmailId;




}
