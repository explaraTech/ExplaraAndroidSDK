package com.explara_core.common_dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ajeetkumar on 17/07/15.
 */
public class ProfileResponse extends IDataModel {

    @SerializedName("status")
    private String status;
    @SerializedName("profile")
    private Profile profile;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public Profile getProfile ()
    {
        return profile;
    }

    public void setProfile (Profile profile)
    {
        this.profile = profile;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [status = "+status+", profile = "+profile+"]";
    }
}
