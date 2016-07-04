package com.explara.explara_ticketing_sdk.tickets.io;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk.tickets.dto.ConfirmRsvpDto;
import com.explara.explara_ticketing_sdk.tickets.dto.DiscountResponse;
import com.explara.explara_ticketing_sdk.tickets.dto.Order;
import com.explara.explara_ticketing_sdk.tickets.dto.PackageDetailsWithTimingsDto;
import com.explara.explara_ticketing_sdk.tickets.dto.SaveBuyerFormDataDto;
import com.explara.explara_ticketing_sdk.tickets.dto.TicketDatesDto;
import com.explara.explara_ticketing_sdk.tickets.dto.TicketDetailResponse;
import com.explara.explara_ticketing_sdk.tickets.dto.TicketDetailsWithTimingsDto;
import com.explara.explara_ticketing_sdk.tickets.dto.UpdatePaymentStatusResponseDto;
import com.explara.explara_ticketing_sdk.utils.TicketingConstantKeys;
import com.explara.explara_ticketing_sdk.utils.UrlConstants;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.GsonRequest;
import com.explara_core.utils.Log;
import com.explara_core.utils.VolleyManager;
import com.google.gson.Gson;

/**
 * Created by ananthasooraj on 9/3/15.
 */
public class TicketsConnectionManager {

    public static final String TAG = TicketsConnectionManager.class.getSimpleName();

    /*public void downloadTickets(Context context, Response.Listener<GetMyTicketsResponse> successListener,
                                Response.ErrorListener errorListener, String tag) {
        final GsonRequest<GetMyTicketsResponse> gsonRequest = new
                GsonRequest<GetMyTicketsResponse>(context, Request.Method.GET,
                getTicketsUrl(Constants.GET_ALL_ORDER_BY_LOGGED_IN_USER_URL, context),
                GetMyTicketsResponse.class, successListener, errorListener);
        gsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(gsonRequest);

    }


    private String getTicketsUrl(String url, Context context) {
        String getTicketsUrl = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(Constants.ACCESS_TOKEN_KEY, PreferenceManager.getInstance(context).getAccessToken())
                .toString();
        Log.i(TAG, getTicketsUrl);
        return getTicketsUrl;
    }*/

    public void updatePaymentStatus(Context context, String jsonUrl, Response.Listener<UpdatePaymentStatusResponseDto> success, Response.ErrorListener errorListener) {
        String url = UrlConstants.UPDATE_PAYMENT_STATUS_URL;
        Log.i(TAG, url);
        GsonRequest<UpdatePaymentStatusResponseDto> orderGsonRequest = new GsonRequest<>(context, Request.Method.POST, url, UpdatePaymentStatusResponseDto.class, null, jsonUrl, success, errorListener);
        //orderGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(orderGsonRequest);
    }

    public void generateOrderNo(Context context, Response.Listener<Order> success, Response.ErrorListener errorListener, String tag) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(TicketsManager.getInstance().cartObj);
        Log.i("OrderJsonStr", jsonStr);
        String url = UrlConstants.GENERATE_ORDER_URL;
        Log.i(TAG, url);
        GsonRequest<Order> orderGsonRequest = new GsonRequest<>(context, Request.Method.POST, url, Order.class, null, jsonStr, success, errorListener);
        orderGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(orderGsonRequest);
    }

    public void generateOrderNoForMultiSession(Context context, Response.Listener<Order> success, Response.ErrorListener errorListener, String tag) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(TicketsManager.getInstance().confCartObj);
        Log.i("JsonStringOrder", jsonStr);
        String url = UrlConstants.GENERATE_ORDER_URL_MULTIPLE_SESSION;
        Log.i(TAG, url);
        GsonRequest<Order> orderGsonRequest = new GsonRequest<>(context, Request.Method.POST, url, Order.class, null, jsonStr, success, errorListener);
        orderGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(orderGsonRequest);
    }

    public void generateOrderNoForSession(Context context, Response.Listener<Order> successListener, Response.ErrorListener errorListener) {

        final GsonRequest<Order> gsonRequest = new
                GsonRequest<Order>(context, Request.Method.GET,
                getSessionOrderUrl(UrlConstants.GET_SESSION_ORDER, context),
                Order.class, successListener, errorListener);
        VolleyManager.getInstance(context).getRequestQueue().add(gsonRequest);

    }

    private String getSessionOrderUrl(String url, Context context) {
        //String search_url = String.format("%s%s", context.getString(R.string.base_search_url), context.getString(R.string.search_url));
        PackageInfo pInfo = null;
        String versionName = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String sessionUrl = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(TicketingConstantKeys.TicketingKeys.SESSION_ID, TicketsManager.getInstance().selectedSessionDetailsDto.sessionId)
                .appendQueryParameter(TicketingConstantKeys.TicketingKeys.SELECTED_QUANTITY, String.valueOf(TicketsManager.getInstance().selectedSessionDetailsDto.selectedQuantity))
                .appendQueryParameter(TicketingConstantKeys.TicketingKeys.DISCOUNT_CODE, TicketsManager.getInstance().selectedSessionDetailsDto.discountUsed)
                .appendQueryParameter(ConstantKeys.BundleKeys.VERSION_NUMBER, versionName)
                .toString();
        Log.i(TAG, sessionUrl);
        return sessionUrl;
    }

    public void downloadTicketsDetail(Context context, String eventId, Response.Listener<TicketDetailResponse> successListener,
                                      Response.ErrorListener errorListener) {

        final GsonRequest<TicketDetailResponse> gsonRequest = new
                GsonRequest<TicketDetailResponse>(context, Request.Method.GET,
                generateTicketDetailResponseUrl(UrlConstants.GET_TICKET_DETAILS_URL, context, eventId),
                TicketDetailResponse.class, successListener, errorListener);
        VolleyManager.getInstance(context).getRequestQueue().add(gsonRequest);

    }

    private String generateTicketDetailResponseUrl(String url, Context context, String eventId) {

       /* if (!url.endsWith("?"))
            url += "?";
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair(Constants.EVENT_ID, TicketsDetailsFragment.mEventID));
        String paramString = URLEncodedUtils.format(params, Constants.UTF_8);
        url += paramString;
        Log.i(TAG, url);
        return url; */

        String getAllTicketsUrl = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(Constants.EVENT_ID, eventId)
                .toString();
        Log.i(TAG, getAllTicketsUrl);
        return getAllTicketsUrl;
    }

    public void downloadPackageDetails(Context context, String requestBody, Response.Listener<TicketDetailResponse> successListener,
                                       Response.ErrorListener errorListener) {
        Log.i(TAG, UrlConstants.GET_PACKAGE_TICKET_DETAILS);
        final GsonRequest<TicketDetailResponse> gsonRequest = new
                GsonRequest<TicketDetailResponse>(context, Request.Method.POST,
                UrlConstants.GET_PACKAGE_TICKET_DETAILS,
                TicketDetailResponse.class, null, requestBody, successListener, errorListener);
        VolleyManager.getInstance(context).getRequestQueue().add(gsonRequest);

    }

    public void getAllCartCalculation(Context context, String requestBody, Response.Listener<DiscountResponse> successListener,
                                      Response.ErrorListener errorListener) {
        Log.i(TAG, UrlConstants.CART_CALCULATION_JSON_URL);
        final GsonRequest<DiscountResponse> gsonRequest = new
                GsonRequest<DiscountResponse>(context, Request.Method.POST,
                UrlConstants.CART_CALCULATION_JSON_URL,
                DiscountResponse.class, null, requestBody, successListener, errorListener);
        VolleyManager.getInstance(context).getRequestQueue().add(gsonRequest);

    }

    public void getConfSessionCartDetails(Context context, String requestBody, Response.Listener<DiscountResponse> successListener,
                                          Response.ErrorListener errorListener) {
        Log.i(TAG, UrlConstants.CONF_CART_CALCULATION_JSON_URL);
        final GsonRequest<DiscountResponse> gsonRequest = new
                GsonRequest<DiscountResponse>(context, Request.Method.POST,
                UrlConstants.CONF_CART_CALCULATION_JSON_URL,
                DiscountResponse.class, null, requestBody, successListener, errorListener);
        VolleyManager.getInstance(context).getRequestQueue().add(gsonRequest);

    }

    public void confirmRequestRsvp(Context context, String eventId, String buyerName, String buyerEmail, String buyerMobileNo, Response.Listener<ConfirmRsvpDto> successListener,
                                   Response.ErrorListener errorListener) {

        final GsonRequest<ConfirmRsvpDto> gsonRequest = new
                GsonRequest<ConfirmRsvpDto>(context, Request.Method.GET,
                generateRsvpResponseUrl(UrlConstants.CONFIRM_RSVP_URL, buyerName, buyerEmail, buyerMobileNo, eventId),
                ConfirmRsvpDto.class, successListener, errorListener);
        VolleyManager.getInstance(context).getRequestQueue().add(gsonRequest);

    }

    private String generateRsvpResponseUrl(String url, String buyerName, String buyerEmail, String buyerMobileNo, String eventId) {

        String rsvpUrl = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(TicketingConstantKeys.TicketingKeys.EVENT_ID, eventId)
                .appendQueryParameter(TicketingConstantKeys.TicketingKeys.NAME, buyerName)
                .appendQueryParameter(TicketingConstantKeys.TicketingKeys.EMAIL_ID, buyerEmail)
                .appendQueryParameter(TicketingConstantKeys.TicketingKeys.MOBILE_NO, buyerMobileNo)
                .toString();
        Log.i(TAG, rsvpUrl);
        return rsvpUrl;
    }

    public void getAllDatesAndTimes(Context context, String eventId, boolean isConfType, Response.Listener<TicketDatesDto> successListener, Response.ErrorListener errorListener) {

        final GsonRequest<TicketDatesDto> gsonRequest;
        if (isConfType) {
            gsonRequest = new
                    GsonRequest<TicketDatesDto>(context, Request.Method.GET,
                    getTicketDatesResponseUrl(UrlConstants.GET_TICKETS_CONF_DATES, eventId),
                    TicketDatesDto.class, successListener, errorListener);
        } else {

            gsonRequest = new
                    GsonRequest<TicketDatesDto>(context, Request.Method.GET,
                    getTicketDatesResponseUrl(UrlConstants.GET_TICKETS_DATES, eventId),
                    TicketDatesDto.class, successListener, errorListener);
        }

        VolleyManager.getInstance(context).getRequestQueue().add(gsonRequest);

    }

    private String getTicketDatesResponseUrl(String url, String eventId) {

        /*if (!url.endsWith("?"))
            url += "?";
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair(Constants.EVENT_ID, eventId));
        String paramString = URLEncodedUtils.format(params, Constants.UTF_8);
        url += paramString;
        Log.i(TAG, url);
        return url;*/

        String getTicketDatesUrl = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(Constants.EVENT_ID, eventId)
                .toString();
        Log.i(TAG, getTicketDatesUrl);
        return getTicketDatesUrl;
    }

    public void getTicketDetailsByDate(Context context, String eventId, String sessionDate, boolean confType, Response.Listener<TicketDetailsWithTimingsDto> successListener, Response.ErrorListener errorListener) {
        final GsonRequest<TicketDetailsWithTimingsDto> gsonRequest;

        if (!confType) {
            gsonRequest = new
                    GsonRequest<>(context, Request.Method.GET,
                    getSessionDetailsUrl(UrlConstants.GET_TICKETS_DATES, eventId, sessionDate),
                    TicketDetailsWithTimingsDto.class, successListener, errorListener);
        } else {
            gsonRequest = new
                    GsonRequest<>(context, Request.Method.GET,
                    getSessionDetailsUrl(UrlConstants.GET_TICKETS_CONF_DATES, eventId, sessionDate),
                    TicketDetailsWithTimingsDto.class, successListener, errorListener);
        }
        VolleyManager.getInstance(context).getRequestQueue().add(gsonRequest);

    }

    private String getSessionDetailsUrl(String url, String eventId, String sessionDate) {
        //String search_url = String.format("%s%s", context.getString(R.string.base_search_url), context.getString(R.string.search_url));
        String sessionUrl = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(TicketingConstantKeys.TicketingKeys.EVENT_ID, eventId)
                .appendQueryParameter(TicketingConstantKeys.TicketingKeys.DATE, sessionDate)
                        //.appendQueryParameter(Constants.DATE, "27 NOV 2015")
                .toString();
        Log.i(TAG, sessionUrl);
        return sessionUrl;
    }

    public void getSessionCartDetails(Context context, String sessionId, int selectedQuantity, String couponCode, Response.Listener<DiscountResponse> successListener, Response.ErrorListener errorListener) {

        final GsonRequest<DiscountResponse> gsonRequest = new
                GsonRequest<DiscountResponse>(context, Request.Method.GET,
                getSessionCartUrl(UrlConstants.GET_SESSION_CART, sessionId, selectedQuantity, couponCode),
                DiscountResponse.class, successListener, errorListener);
        VolleyManager.getInstance(context).getRequestQueue().add(gsonRequest);

    }

    private String getSessionCartUrl(String url, String sessionId, int selectedQuantity, String couponCode) {
        //String search_url = String.format("%s%s", context.getString(R.string.base_search_url), context.getString(R.string.search_url));
        String sessionUrl = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(TicketingConstantKeys.TicketingKeys.SESSION_ID, sessionId)
                .appendQueryParameter(TicketingConstantKeys.TicketingKeys.SELECTED_QUANTITY, String.valueOf(selectedQuantity))
                .appendQueryParameter(TicketingConstantKeys.TicketingKeys.DISCOUNT_CODE, couponCode)
                .toString();
        Log.i(TAG, sessionUrl);
        return sessionUrl;
    }

    public void getAllPackagesDatesAndTimes(Context context, String requestBody, Response.Listener<TicketDatesDto> successListener, Response.ErrorListener errorListener) {

        Log.i(TAG, Constants.GET_MULTI_PACKAGE_DATES);
        final GsonRequest<TicketDatesDto> gsonRequest = new
                GsonRequest<TicketDatesDto>(context, Request.Method.POST,
                Constants.GET_MULTI_PACKAGE_DATES,
                TicketDatesDto.class, null, requestBody, successListener, errorListener);
        VolleyManager.getInstance(context).getRequestQueue().add(gsonRequest);
    }


    public void getPackageDetailsByDate(Context context, String requestBody, Response.Listener<PackageDetailsWithTimingsDto> successListener, Response.ErrorListener errorListener) {
        Log.i(TAG, Constants.GET_MULTI_PACKAGE_DATES);
        final GsonRequest<PackageDetailsWithTimingsDto> gsonRequest = new
                GsonRequest<PackageDetailsWithTimingsDto>(context, Request.Method.POST,
                Constants.GET_MULTI_PACKAGE_DATES,
                PackageDetailsWithTimingsDto.class, null, requestBody, successListener, errorListener);
        VolleyManager.getInstance(context).getRequestQueue().add(gsonRequest);

    }

    public void saveBuyerFormData(Context context, String jsonStr, Response.Listener<SaveBuyerFormDataDto> success, Response.ErrorListener errorListener, String tag) {
        //GsonRequest<SaveBuyerFormDataDto> orderGsonRequest = new GsonRequest<>(context, Request.Method.POST, Constants.SAVE_BUYER_DATA, SaveBuyerFormDataDto.class,buyerFormDataMap, success, errorListener);
        //GsonRequest<SaveBuyerFormDataDto> orderGsonRequest = new GsonRequest<>(context, Request.Method.GET, generateSaveBuyerFormDataUrl(jsonStr), SaveBuyerFormDataDto.class, null, null, success, errorListener);
        Log.i(TAG, UrlConstants.SAVE_BUYER_DATA);
        GsonRequest<SaveBuyerFormDataDto> orderGsonRequest = new GsonRequest<>(context, Request.Method.POST, UrlConstants.SAVE_BUYER_DATA, SaveBuyerFormDataDto.class, null, jsonStr, success, errorListener);
        orderGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(orderGsonRequest);
    }

}
