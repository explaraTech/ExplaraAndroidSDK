package com.explara_core.common_dto;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;

public class CleverTapIntentDataDto implements Serializable {

    private static final long serialVersionUID = -7060210544600464481L;

    // @SerializedName("cleverTapType")
    // public String cleverTapType;

    @SerializedName("userDetailsProfileMap")
    public Map<String, Object> userDetailsProfileMap;

    @SerializedName("userDetailsEventMap")
    public Map<String, Object> userDetailsEventMap;

    @SerializedName("fbLoginObject")
    public JSONObject fbLoginObject;

    @SerializedName("cleverTapEventName")
    public String cleverTapEventName;

}