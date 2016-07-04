package com.explara.explara_payment_sdk.payment.io;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.explara.explara_payment_sdk.payment.dto.CheckoutOfflineResponse;
import com.explara.explara_payment_sdk.payment.dto.OlaBill;
import com.explara.explara_payment_sdk.payment.dto.OlaPaymentResposneDto;
import com.explara.explara_payment_sdk.payment.dto.OtpResponseDto;
import com.explara.explara_payment_sdk.payment.dto.PayTmUserProfile;
import com.explara.explara_payment_sdk.payment.dto.PaypalCheckoutResponse;
import com.explara.explara_payment_sdk.payment.dto.PaytmPayMentResposnseDto;
import com.explara.explara_payment_sdk.payment.dto.SendMutipleEmailDto;
import com.explara.explara_payment_sdk.payment.dto.ValidateOtpResposneDto;
import com.explara.explara_payment_sdk.utils.PaymentConstantKeys;
import com.explara.explara_payment_sdk.utils.UrlConstants;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.GsonRequest;
import com.explara_core.utils.Log;
import com.explara_core.utils.VolleyManager;

/**
 * Created by Debasish on 9/2/15.
 */
public class PaymentConnectionManager {

    public static final String TAG = PaymentConnectionManager.class.getSimpleName();

    public void checkoutOffline(Context context, String paymentOptionName, Response.Listener<CheckoutOfflineResponse> success, Response.ErrorListener errorListener, String tag) {
        String url = UrlConstants.CHECKOUT_OFFLINE_URL + "?orderNo=" + TicketsManager.getInstance().mOrder.getOrderNo() +
                "&paymentOption=" + paymentOptionName;
        Log.i(TAG, url);
        GsonRequest<CheckoutOfflineResponse> checkoutOfflineResponseGsonRequest = new GsonRequest<CheckoutOfflineResponse>(context, Request.Method.POST, url, CheckoutOfflineResponse.class, null, null, success, errorListener);
        checkoutOfflineResponseGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(checkoutOfflineResponseGsonRequest);
    }

    public void getPaypalFinalOrder(Context context, String payKey, Response.Listener<PaypalCheckoutResponse> success, Response.ErrorListener errorListener, String tag) {
        String paypalCheckoutUrlStr = UrlConstants.CHECKOUT_PAYPAL_URL + "?transactionId=" + payKey + "&orderNo=" + TicketsManager.getInstance().mOrder.getOrderNo();
        Log.i(TAG, paypalCheckoutUrlStr);
        GsonRequest<PaypalCheckoutResponse> paypalCheckoutResponseGsonRequest = new GsonRequest<PaypalCheckoutResponse>(context, Request.Method.POST, paypalCheckoutUrlStr, PaypalCheckoutResponse.class, null, null, success, errorListener);
        paypalCheckoutResponseGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(paypalCheckoutResponseGsonRequest);
    }

    public void generateOlaBill(Context context, String orderId, Response.Listener<OlaBill> sucOlaBillListener, Response.ErrorListener errorListener, String tag) {
        GsonRequest<OlaBill> olaBillGsonRequest = new GsonRequest<>(context, Request.Method.GET, generateOlaBillUrl(orderId), OlaBill.class, null, null, sucOlaBillListener, errorListener);
        olaBillGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(olaBillGsonRequest);
    }

    private String generateOlaBillUrl(String orderId) {
        String olaBillUrl = Uri.parse(UrlConstants.OLA_BILL_GENERATION_URL)
                .buildUpon()
                .appendQueryParameter(ConstantKeys.OlaMoneyUrlKeys.ORDER_ID, orderId)
                .toString();
        return olaBillUrl;
    }

    public void requestPayTmOtpGeneration(Context context, final String emailId, final String phoneNum, Response.Listener<OtpResponseDto> otpListener, Response.ErrorListener errorListener, String tag) {
        GsonRequest<OtpResponseDto> otpResposneDtoGsonRequest = new GsonRequest<>(context, Request.Method.GET, generateRequestOtpurl(emailId, phoneNum), OtpResponseDto.class, null, null, otpListener, errorListener);
        otpResposneDtoGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(otpResposneDtoGsonRequest);
    }

    public void validatePayTmotp(Context context, final String otp, final String state, Response.Listener<ValidateOtpResposneDto> validateOtpResposneDtoListener, Response.ErrorListener errorListener, String tag) {
        GsonRequest<ValidateOtpResposneDto> validateOtpResposneDtoGsonRequest = new GsonRequest<>(context, Request.Method.GET, getValidateOtpurl(otp, state), ValidateOtpResposneDto.class, null, null, validateOtpResposneDtoListener, errorListener);
        validateOtpResposneDtoGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(validateOtpResposneDtoGsonRequest);

    }

    public void fetchPayTmProfile(Context context, final String accesstToken, Response.Listener<PayTmUserProfile> payTmUserProfileListener, Response.ErrorListener errorListener, String tag) {
        GsonRequest<PayTmUserProfile> payTmUserProfileGsonRequest = new GsonRequest<>(context, Request.Method.GET, getUserProfile(accesstToken), PayTmUserProfile.class, null, null, payTmUserProfileListener, errorListener);
        payTmUserProfileGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(payTmUserProfileGsonRequest);
    }

    public void updateOlaStatusToServer(Context context, final String olaBillStatus, Response.Listener<OlaPaymentResposneDto> payTmUserProfileListener, Response.ErrorListener errorListener, String tag) {
        GsonRequest<OlaPaymentResposneDto> payTmUserProfileGsonRequest = new GsonRequest<>(context, Request.Method.GET, updateStatusUrl(olaBillStatus), OlaPaymentResposneDto.class, null, null, payTmUserProfileListener, errorListener);
        payTmUserProfileGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(payTmUserProfileGsonRequest);
    }

    public void paywithPayTm(Context context, final String orderNum, String accessToken, Response.Listener<PaytmPayMentResposnseDto> payTmUserProfileListener, Response.ErrorListener errorListener, String tag) {
        GsonRequest<PaytmPayMentResposnseDto> payTmUserProfileGsonRequest = new GsonRequest<>(context, Request.Method.GET, payWithPaytmUrl(accessToken, orderNum), PaytmPayMentResposnseDto.class, null, null, payTmUserProfileListener, errorListener);
        payTmUserProfileGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(payTmUserProfileGsonRequest);
    }

    public void onSendBtnClick(Context context, final String multipleEmailids, Response.Listener<SendMutipleEmailDto> sendMutipleEmailDtoListener, Response.ErrorListener errorListener, String tag) {
        GsonRequest<SendMutipleEmailDto> sendEmailGsonRequest = new GsonRequest<>(context, Request.Method.GET, generateSendEmailUrl(multipleEmailids), SendMutipleEmailDto.class, null, null, sendMutipleEmailDtoListener, errorListener);
        sendEmailGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(sendEmailGsonRequest);
    }

    private String generateSendEmailUrl(final String multipleEmailids) {
        String sendEmailUrl = Uri.parse(UrlConstants.SEND_MULTIPLE_EMAIL)
                .buildUpon()
                .appendQueryParameter(PaymentConstantKeys.PaymentKeys.ORDER_NO, TicketsManager.getInstance().mOrder.getOrderNo())
                .appendQueryParameter(PaymentConstantKeys.PaymentKeys.MULTIPLE_EMAIL_IDS, multipleEmailids)
                .toString();
        Log.d("sendEmailUrl", sendEmailUrl);
        return sendEmailUrl;
    }

    private String generateRequestOtpurl(final String emailId, final String phoneNum) {
        return Uri.parse(UrlConstants.REQUEST_PAYTM_OTP_GENERATION)
                .buildUpon()
                .appendQueryParameter(ConstantKeys.PAYTM_WALLET_URL_KEYS.EMAILID, emailId)
                .appendQueryParameter(ConstantKeys.PAYTM_WALLET_URL_KEYS.MOBILENUM, phoneNum)
                .toString();
    }


    private String getValidateOtpurl(final String otp, final String state) {
        return Uri.parse(UrlConstants.VALIDATE_PAYTM_OTP)
                .buildUpon()
                .appendQueryParameter(ConstantKeys.PAYTM_WALLET_URL_KEYS.STATE, state)
                .appendQueryParameter(ConstantKeys.PAYTM_WALLET_URL_KEYS.OTP, otp)
                .toString();
    }

    private String getUserProfile(final String payTmAccessToken) {
        return Uri.parse(UrlConstants.FETCH_USER_PROFILE)
                .buildUpon()
                .appendQueryParameter(ConstantKeys.PAYTM_WALLET_URL_KEYS.ACCESS_TOKEN, payTmAccessToken)
                .toString();
    }

    private String updateStatusUrl(final String olaStatus) {
        return Uri.parse(UrlConstants.OLA_RETURN_URL)
                .buildUpon()
                .appendQueryParameter(ConstantKeys.PAYTM_WALLET_URL_KEYS.OLA_STATUS_RESPONSE, olaStatus)
                .toString();
    }

    private String payWithPaytmUrl(final String accessToken, final String orderNumber) {
        return Uri.parse(UrlConstants.PAY_TM_CHECKOUT_URL)
                .buildUpon()
                .appendQueryParameter(ConstantKeys.PAYTM_WALLET_URL_KEYS.ACCESS_TOKEN, accessToken)
                .appendQueryParameter(ConstantKeys.PAYTM_WALLET_URL_KEYS.ORDER_NUMBER, orderNumber)
                .toString();
    }

    public String addMoneyForPaytmUrl(final String accessToken, final String amount) {
        return Uri.parse(UrlConstants.PAY_TM_ADD_MONEY_URL)
                .buildUpon()
                .appendQueryParameter(ConstantKeys.PAYTM_WALLET_URL_KEYS.ACCESS_TOKEN, accessToken)
                .appendQueryParameter(ConstantKeys.PAYTM_WALLET_URL_KEYS.AMOUNT, amount)
                .toString();
    }

}
