package com.explara.explara_ticketing_sdk.utils;

import com.explara_core.utils.Constants;

/**
 * Created by ananthasooraj on 26/04/16.
 */
public class UrlConstants {

    // For ticketing
    public static final String GET_TICKET_DETAILS_URL = Constants.ROOT_DOMAIN_OLD + "/ticket-details";
    public static final String GET_PACKAGE_TICKET_DETAILS = Constants.EVENT_ROOT_DOMAIN + "/get-ticket-details-by-package-id";
    public static final String CART_CALCULATION_JSON_URL = Constants.ROOT_DOMAIN_OLD + "/cart-calculation-JSON";
    public static final String CONF_CART_CALCULATION_JSON_URL = Constants.ROOT_DOMAIN_OLD + "/conf-cart-calculation";
    public static final String CONFIRM_RSVP_URL = Constants.ROOT_DOMAIN_OLD + "/save-rsvp";
    public static final String GET_TICKETS_DATES = Constants.ROOT_DOMAIN_OLD + "/ticket-dates";
    public static final String GET_TICKETS_CONF_DATES = Constants.ROOT_DOMAIN_OLD + "/ticket-conf-dates";
    public static final String GET_SESSION_CART = Constants.ROOT_DOMAIN_OLD + "/session-cart-calculation";
    public static final String GENERATE_ORDER_URL = Constants.ROOT_DOMAIN_OLD + "/generate-order";
    public static final String GENERATE_ORDER_URL_MULTIPLE_SESSION = Constants.ROOT_DOMAIN_OLD + "/conf-generate-order";
    public static final String GET_SESSION_ORDER = Constants.ROOT_DOMAIN_OLD + "/session-generate-order";
    public static final String SAVE_BUYER_DATA = Constants.ROOT_DOMAIN_NEW + "/event/save-attendee";
    public static final String ATTENDEE_FORM_DOWNLOAD_URL = Constants.ROOT_DOMAIN_NEW + "/event/attendee-form";
    public static final String UPDATE_PAYMENT_STATUS_URL = Constants.ROOT_DOMAIN_NEW + "/publisher/confirm-order";
}
