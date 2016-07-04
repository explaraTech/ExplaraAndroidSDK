package com.explara_core.login.login_dto;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by anudeep on 06/10/15.
 */
@DatabaseTable
public class Account {

    @DatabaseField
    @SerializedName("lastName")
    public String lastName;

    @DatabaseField(id = true)
    @SerializedName("emailId")
    public String emailId;

    @DatabaseField
    @SerializedName("profileImage")
    public String profileImage;

    @DatabaseField
    @SerializedName("accessToken")
    public String accessToken;

    @DatabaseField
    @SerializedName("mobileNumber")
    public String mobileNumber;

    @DatabaseField
    @SerializedName("firstName")
    public String firstName;

    public Account() {

    }

    public String getName() {
        return firstName + lastName;
    }
}
