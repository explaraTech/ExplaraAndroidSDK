package com.explara.explara_ticketing_sdk.tickets.dto;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Debasish.
 */
public class BuyerFormDataObj {

    public String orderNo;
    public LinkedHashMap<String, List<AttendeeFields>> attendees = new LinkedHashMap<>();

}
