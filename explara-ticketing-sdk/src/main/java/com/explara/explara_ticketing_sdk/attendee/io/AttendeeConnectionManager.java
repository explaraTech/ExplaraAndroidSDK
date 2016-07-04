package com.explara.explara_ticketing_sdk.attendee.io;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.explara.explara_ticketing_sdk.attendee.dto.AttendeeDetailsResponseDto;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk.utils.UrlConstants;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.GsonRequest;
import com.explara_core.utils.Log;
import com.explara_core.utils.VolleyManager;

/**
 * Created by anudeep on 08/01/16.
 */
public class AttendeeConnectionManager {

    private static final String TAG = AttendeeConnectionManager.class.getSimpleName();

    public void downloadAttendeeFormData(Context context, Response.Listener<AttendeeDetailsResponseDto> successListener,
                                         Response.ErrorListener errorListener, String tag) {
        String url = generateAttendeeFomUrl();
        GsonRequest<AttendeeDetailsResponseDto> gsonRequest = new GsonRequest<>(context, Request.Method.GET, url, AttendeeDetailsResponseDto.class, successListener, errorListener);
        gsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(gsonRequest);
    }

    private String generateAttendeeFomUrl() {
        Uri attendeeFromUrl = Uri.parse(UrlConstants.ATTENDEE_FORM_DOWNLOAD_URL);
        attendeeFromUrl = attendeeFromUrl
                .buildUpon()
                .appendQueryParameter(ConstantKeys.UrlKeys.ORDER_NO, TicketsManager.getInstance().mOrder.getOrderNo())
                .build();
        String url = attendeeFromUrl.toString();
        Log.d(TAG, url);
        return url;
    }
}
