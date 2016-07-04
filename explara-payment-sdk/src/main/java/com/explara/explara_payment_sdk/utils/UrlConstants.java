package com.explara.explara_payment_sdk.utils;

import com.explara_core.utils.Constants;

/**
 * Created by ananthasooraj on 26/04/16.
 */
public class UrlConstants {

    // ccaveune
    public static final String accessCode = "AVLN03BL46AH34NLHA";
    public static final String merchantId = "260";
    public static final String redirectUrl = "https://www.explara.com/em/ticket/checkout/ccavenue-response-mobile";
    public static final String cancelUrl = "https://www.explara.com/em/ticket/checkout/ccavenue-response-mobile";
    public static final String rsaKeyUrl = "https://www.explara.com/em/ticket/checkout/get-rsa";


    public static final String CHECKOUT_OFFLINE_URL = Constants.ROOT_DOMAIN_OLD + "/checkout-offline";
    public static final String CHECKOUT_PAYPAL_URL = Constants.ROOT_MAIN_DOMAIN + "/em/ticket/checkout/get-paypal-adaptive-for-mobile";
    public static final String OLA_BILL_GENERATION_URL = Constants.ROOT_MAIN_DOMAIN + "/em/ticket/checkout/ola-bill";
    public static final String SEND_MULTIPLE_EMAIL = Constants.ROOT_DOMAIN_OLD + "/email-order";
    public static final String REQUEST_PAYTM_OTP_GENERATION = Constants.ROOT_MAIN_DOMAIN + "/em/ticket/checkout/paytm-otp";
    public static final String VALIDATE_PAYTM_OTP = Constants.ROOT_MAIN_DOMAIN + "/em/ticket/checkout/paytm-authorize";
    public static final String FETCH_USER_PROFILE = Constants.ROOT_MAIN_DOMAIN + "/em/ticket/checkout/paytm-user-details";
    public static final String FETCH_USER_BALANCE = Constants.ROOT_MAIN_DOMAIN + "/em/ticket/checkout/paytm-user-balance";
    public static final String OLA_RETURN_URL = Constants.ROOT_MAIN_DOMAIN + "/em/ticket/checkout/ola-response-mobile";
    public static final String PAY_TM_CHECKOUT_URL = Constants.ROOT_MAIN_DOMAIN + "/em/ticket/checkout/paytm-redeem";
    public static final String CITRUS_BILL_GENERATOR = Constants.ROOT_MAIN_DOMAIN + "/em/ticket/checkout/citrus-bill?orderNo=";
    public static final String CITRUS_ADDMONEY_RETURN_URL = Constants.ROOT_MAIN_DOMAIN + "/em/ticket/checkout/citrus-load-money-return-url";
    public static final String PAY_TM_ADD_MONEY_URL = Constants.ROOT_MAIN_DOMAIN + "/em/ticket/checkout/paytm-add-balance";
    public static final String CITRUS_RETURN_URL = Constants.ROOT_MAIN_DOMAIN + "/em/ticket/checkout/citrus-response";
}
