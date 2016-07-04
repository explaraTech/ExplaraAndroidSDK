package com.explara.explara_eventslisting_sdk.events.io;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.explara.explara_eventslisting_sdk.events.dto.CategoriesResponseDto;
import com.explara.explara_eventslisting_sdk.events.dto.CityNamesResponseDto;
import com.explara.explara_eventslisting_sdk.events.dto.CollectionEventsDto;
import com.explara.explara_eventslisting_sdk.events.dto.EnquiryResponseDto;
import com.explara.explara_eventslisting_sdk.events.dto.EventsDetailDto;
import com.explara.explara_eventslisting_sdk.events.dto.EventsWithTopicsDto;
import com.explara.explara_eventslisting_sdk.events.dto.FavouriteEventsDto;
import com.explara.explara_eventslisting_sdk.events.dto.OrganizerEventsDto;
import com.explara.explara_eventslisting_sdk.events.dto.SaveTopicsResponse;
import com.explara.explara_eventslisting_sdk.utils.EventsListingConstantKeys;
import com.explara.explara_eventslisting_sdk.utils.UrlConstants;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.GsonRequest;
import com.explara_core.utils.Log;
import com.explara_core.utils.PreferenceManager;
import com.explara_core.utils.VolleyManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by anudeep on 04/09/15.
 */
public class EventsConnectionManger {

    private static final String TAG = EventsConnectionManger.class.getSimpleName();

    public void downloadOrganizerEvents(Context context, String accountId, Response.Listener<OrganizerEventsDto> successListener, Response.ErrorListener errorListener, String tag) {
        final GsonRequest<OrganizerEventsDto> gsonRequest = new
                GsonRequest<>(context, Request.Method.GET,
                getOrganizerEventsUrl(accountId),
                OrganizerEventsDto.class, successListener, errorListener);
        gsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(gsonRequest);
    }

    private String getOrganizerEventsUrl(String accountId) {

        String getOrgEventsUrl = String.format("%s%s", Constants.ROOT_DOMAIN_OLD, UrlConstants.ORGANIZER_EVENTS_URL);
        String getOrgEventsApiUrl = Uri.parse(getOrgEventsUrl)
                .buildUpon()
                .appendQueryParameter(Constants.ACCOUNT_ID, accountId).build().toString();
        Log.d("OrgEvents Url", getOrgEventsApiUrl);
        return getOrgEventsApiUrl;
    }

    public void enquiry(Context context, String name, String emailId, String phoneNo, String enquiry, String eventId, Response.Listener<EnquiryResponseDto>
            successListener, Response.ErrorListener errorListener, String tag) {

        final GsonRequest<EnquiryResponseDto> gsonRequest = new
                GsonRequest<>(context, Request.Method.GET,
                getEnquiryUrl(context, name, emailId, phoneNo, enquiry, eventId),
                EnquiryResponseDto.class, successListener, errorListener);
        gsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(gsonRequest);
    }

    private String getEnquiryUrl(Context context, String name, String emailId, String phoneNo, String enquiry, String eventId) {

        String enquiryUrl = String.format("%s%s", Constants.ROOT_DOMAIN_OLD, UrlConstants.ENQUIRY_URL);
        String enquiryApiUrl = Uri.parse(enquiryUrl)
                .buildUpon().appendQueryParameter(EventsListingConstantKeys.EventKeys.ENQUIRY_NAME, name)
                .appendQueryParameter(EventsListingConstantKeys.EventKeys.ENQUIRY_EMAILID, emailId)
                .appendQueryParameter(EventsListingConstantKeys.EventKeys.ENQUIRY_PHONE_NO_KEY, phoneNo)
                .appendQueryParameter(EventsListingConstantKeys.EventKeys.ENQUIRY_ENQUIRY_TEXT, enquiry)
                .appendQueryParameter(Constants.EVENT_ID, eventId).build().toString();
        Log.d("Enquiry Url", enquiryApiUrl);
        return enquiryApiUrl;
    }

    public void downloadDetailEvents(Context context, String eventId, Response.Listener<EventsDetailDto>
            successListener, Response.ErrorListener errorListener, String tag) {

        final GsonRequest<EventsDetailDto> gsonRequest = new
                GsonRequest<>(context, Request.Method.GET,
                generateEventDetailsUrl(context, UrlConstants.GET_EVENT_DETAILS_URL, eventId), EventsDetailDto.class, successListener, errorListener);
        gsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(gsonRequest);
    }

    private String generateEventDetailsUrl(Context context, String url, String eventId) {
        String eventDetailsUrl = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(Constants.EVENT_ID, eventId)
                .appendQueryParameter(Constants.ACCESS_TOKEN_KEY, PreferenceManager.getInstance(context).getAccessToken())
                .build()
                .toString();
        Log.d(TAG, eventDetailsUrl);
        return eventDetailsUrl;
    }

    public void downloadCollectionEvents(Context context, String collectionId, Response.Listener<CollectionEventsDto> collectionEventsDtoListener, Response.ErrorListener errorListener, String tag) {
        String url = constructCollectionEventUrl(context, collectionId);
        final GsonRequest<CollectionEventsDto> gsonRequest = new
                GsonRequest<>(context, Request.Method.GET,
                url, CollectionEventsDto.class, collectionEventsDtoListener, errorListener);
        gsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(gsonRequest);

    }

    private String constructCollectionEventUrl(Context context, String collectionId) {
        String url = UrlConstants.COLLECTION_EVENTS_URL;
        String finalUrl = Uri.parse(url)
                .buildUpon().appendQueryParameter(ConstantKeys.UrlKeys.COLLECTION_ID, collectionId)
                .build()
                .toString();
        Log.d(TAG, finalUrl);
        return finalUrl;

    }

    public void downloadEvents(Context context, String categoryName, String cityName, Response.Listener<CategoriesResponseDto>
            successListener, Response.ErrorListener errorListener, String tag) {
        try {
            String url = UrlConstants.GET_CATEGORIES_URL + "?" + "city=" + URLEncoder.encode(cityName, "utf-8") + "&category=" + URLEncoder.encode(categoryName, "utf-8");
            Log.i(TAG, url);
            final GsonRequest<CategoriesResponseDto> gsonRequest = new
                    GsonRequest<CategoriesResponseDto>(context, Request.Method.GET,
                    url, CategoriesResponseDto.class, successListener, errorListener);
            gsonRequest.setTag(tag);
            VolleyManager.getInstance(context).getRequestQueue().add(gsonRequest);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void downloadFavEvents(Context context, Response.Listener<FavouriteEventsDto> success, Response.ErrorListener errorListener, String tag) {
        String url = UrlConstants.GET_FAV_EVENTS + "?" + Constants.ACCESS_TOKEN_KEY + "=" + Constants.getAccessToken(context);
        Log.i(TAG, url);
        GsonRequest<FavouriteEventsDto> favEventsDtoGsonRequest = new GsonRequest<>(context,
                Request.Method.GET, url, FavouriteEventsDto.class, success, errorListener);
        favEventsDtoGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(favEventsDtoGsonRequest);
    }

    public void downloadEventsWithTopics(Context context, String categoryName, Response.Listener<EventsWithTopicsDto> successListener, Response.ErrorListener errorListener) {
        final GsonRequest<EventsWithTopicsDto> gsonRequest = new GsonRequest<EventsWithTopicsDto>(context,
                Request.Method.GET, getEventsWithTopicsUrl(categoryName), EventsWithTopicsDto.class, successListener, errorListener);
        VolleyManager.getInstance(context).getRequestQueue().add(gsonRequest);
    }

    private String getEventsWithTopicsUrl(String categoryName) {
        String url = UrlConstants.GET_EVENTS_WITH_TOPICS_URL;

        String eventsWithTopicsUrl = Uri.parse(url)
                .buildUpon().appendQueryParameter(ConstantKeys.UrlKeys.CATEGORY_NAME, categoryName)
                .build()
                .toString();
        Log.i(TAG, eventsWithTopicsUrl);
        return eventsWithTopicsUrl;
    }

    public void saveEventFav(Context context, Response.Listener<SaveTopicsResponse> success, Response.ErrorListener errorListener, String eventId, String tag) {
        GsonRequest<SaveTopicsResponse> topicRequest = new GsonRequest<>(context,
                Request.Method.GET, genratefavEventUrl(context, UrlConstants.GET_SAVE_EVENT_FAV_URL, eventId), SaveTopicsResponse.class, success, errorListener);
        topicRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(topicRequest);
    }

    private String genratefavEventUrl(Context context, String url, String eventId) {
        String favEventUrl = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(Constants.EVENT_ID, eventId)
                .appendQueryParameter(Constants.ACCESS_TOKEN_KEY, PreferenceManager.getInstance(context).getAccessToken())
                .build()
                .toString();
        Log.d("FavEventUrl", favEventUrl);
        return favEventUrl;

    }

    public void downloadCityResponseFromurl(Context context, Response.Listener<CityNamesResponseDto> responseDtoListener, Response.ErrorListener errorListener, String segment) {
        final GsonRequest<CityNamesResponseDto> gsonRequest = new GsonRequest<CityNamesResponseDto>(context, Request.Method.GET, getCityUrl(segment), CityNamesResponseDto.class,
                responseDtoListener, errorListener);
        VolleyManager.getInstance(context).getRequestQueue().add(gsonRequest);
    }

    private String getCityUrl(String segmentName) {
        String url = UrlConstants.GET_CITY_FILTER_URL;
        String cityUrl = Uri.parse(url)
                .buildUpon().appendQueryParameter(ConstantKeys.UrlKeys.SEGMENT, segmentName)
                .build()
                .toString();
        Log.d("CityUrl", cityUrl);
        return cityUrl;
    }


}
