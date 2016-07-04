package com.explara_core.login.login_dto;

/**
 * Created by anudeep on 13/09/15.
 */
public class PLaceAutocomplete {

    public CharSequence placeId;
    public CharSequence description;

    public PLaceAutocomplete(String placeId, String description) {
        this.description = description;
        this.placeId = placeId;
    }

}
