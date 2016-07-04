package com.explara_core.utils;

/**
 * Created by anudeep on 04/11/15.
 */
public class ConstantKeys {

    public interface BundleKeys {

        String FROM_NOTIFICATION = "from_notification";
        String SEARCH_TEXT = "search_text";
        String URL_STRING = "url";
        String FROM_DEEP_LINK = "from_deep_link";
        String ORDER_ID = "order_id";
        String FROM_SCREEN = "from_Screen";
        String OLA_RESULT_KEY = "payment_result";
        String VERSION_NUMBER = "version";
        String CLEVER_TAP_EVENT = "clevertap_event";
        String CLEVER_TAP_SOURCE_PAGE = "facebook";
    }

    public interface NavigationIds {
        int HOME = 0;
        int WALLET = HOME + 1;
        int MY_TICKETS = HOME + 2;
        int MY_TOPICS = HOME + 3;
        int MY_FAV = HOME + 4;
        int SETTINGS = HOME + 5;
        int RATE = HOME + 6;
        int SHARE = HOME + 7;
        int ABOUT_US = HOME + 8;
        int NOTIFICATIONS = HOME + 9;
        int MY_EVENT = HOME + 10;
    }

    public interface Wallets {
        String CITRUS = "Citrus";
        String PAYTM = "Paytm";
    }

    public interface PAYTM_WALLET_URL_KEYS {
        String EMAILID = "emailId";
        String MOBILENUM = "mobileNo";
        String STATE = "state";
        String OTP = "otp";
        String ACCESS_TOKEN = "accessToken";
        String OLA_STATUS_RESPONSE = "response";
        String ORDER_NUMBER = "orderNo";
        String AMOUNT = "amount";
    }

    public interface OlaMoneyUrlKeys {
        String ORDER_ID = "orderNo";
    }

    public interface NOTIFICATION_KEYS {
        String NOTIFICATION_TYPE = "notification_type";
        String TOPIC_ID = "topic_id";
        String EVENT_ID = "event_id";
        String CATEGORY_ID = "category_id";
        String CATEGORY_NAME = "category_name";
        String NOTIFICATION_STYLE = "notification_style";
        String BIG_IMAGE_STYLE = "big_image_style";
        String COLLECTION_ID = "collection_id";
        String COLLECTION_NAME = "collection_name";
        String SOURCE_PAGE = "from_push_notification";
        String COLLECTION_POSITION = "collection_position";

    }

    public interface PaymentOptionIds {
        int PAYPAL = 1;
        int PAYTM = 2;
        int CCAVENUE = 3;
        int OLA_MONEY = 4;

        int DEPOSIT_ID = 5;
        int dd = 6;
        int VENUE_ID = 7;
        int CHEQUE_ID = 8;
        int CITRUS_ID = 9;
        int CITRUS_WALLET_ID = 10;

        String CHEQUE = "Cheque";
        String DD = "DD";
        String DEPOSIT = "Deposit";
        String VENUE = "Venue";

        String CC_AVENUE_TITLE = "CCAvenue";
        String PAY_PAL_TITLE = "PayPal";
        String OLA_MONEY_TITLE = "Pay with Ola Money";
        String PAY_TM_WALLET_TITLE = "Pay with PayTm";

        String CITRUS_PAYMENT_TITLE = "Citrus";
        String CITRUS_WALLET_TITLE = "Pay With Citrus Wallet";

        String TRANSACTION_SUCESS = "TXN_SUCCESS";
    }

    public interface CleverTapIntentKeys{
        String EMAIL_KEY = "email";
        String NAME_KEY = "name";
        String ACCESS_TOKEN_KEY = "access_token";
        String MOBILE_NUMBER_KEY = "mobile_number";
        String PROFILE_IMAGE_KEY = "profile_image_url";
        String CLEVER_TAP_EVENTS = "clever_tap";
        String CLEVER_TAP_LOGIN_SOURCE_PAGE = "facebook";
    }


    public interface REQUEST_CODES {
        int OLA_REQUEST_CODE = 100;
        int PAY_TM_ADD_MONEY_REQUEST_CODE = 200;

    }

    public interface FromScreen {
        int PAYMENT_SCREEN = 1000;
        int WALLET_SCREEN = 2000;
        String HOME_SCREEN = "home_screen";
        int HOME_SCREEN_SOURCE_PAGE_CODE = 3000;

        String LOGIN_SCREEN = "login_screen";
        String LOGIN_SCREEN_FACEBOOK = "facebook_login_screen";
        String SIGNUP_SCREEN = "signup_screen";
        String SIGNUP_SCREEN_FACEBOOK = "facebook_signup_screen";
        String CREATE_AN_EVENT = "create_an_event_screen";
        String CREATE_EVENT_SUCCESSFULLY = "create_event_successfully";
    }

    public interface UrlKeys {
        String ORDER_NO = "orderNo";
        String CATEGORY_NAME = "category";
        String SEGMENT = "segment";
        String COLLECTION_ID = "collectionId";
        String COUNTRY_ID = "country";
        String ACCESS_TOKEN = "accessToken";
    }

    public interface AttendeeFormTypes {
        String EDIT_TEXT = "Text";
        String RADIO_GROUP = "Radio";
        String SPINNER = "Select";
        String CHECK_BOX = "MultiCheckbox";
        String TEXT_AREA = "Textarea";
        String LABEL = "Label";
        String FILE = "File";
        String TERMS_AND_CONDITIONS = "Terms & Conditions";
    }

    public interface AttendeeFormValidationKeys {
        String EMAIL_ADDRESS = "EmailAddress";
        String LINKS = "Hostname";
        String DIGITS = "Digits";
        String DECIMAL = "Decimal";
        String DATE_AND_TIME = "DateTime";
        String DATE = "Date";
        String FLOAT = "Float";
        String FROM_ATTEENDEE_SCREEN = "attendee_screen";
    }

    public interface ValidationMessages {
        String EMPTY_EMAIL = "Email id is empty";
        String INVALID_EMAIL = "Invalid email id";
        String EMPTY_URL = "URL is empty";
        String INVALID_URL = "Invalid URL";
        String EMPTY_FIELD = "Field is empty";
        String DATE_TIME_FORMAT = "Please select date and time";
        String DATE_FORMAT = "Please select date";
        String RADIO_UNSELECT = "Select one from list";
        String CHECK_UNCHECKED = "select at least one";
        String UPLOAD_FILE_EMPTY = "Upload a file";
        String RE_UPLOAD = "Please upload again";
        String UPLOAD_SUCCESS = "Uploaded successfully";
        String UPLOAD_FAILED = "Upload failed";
        String UPLOAD_FILE = "Please upload file";

    }


    public interface FeaturedCity {
        String cityName = "city";
        String limit = "limit";
    }

    public interface InlineFormKeys {
        String sourcePage = "sourcePage";
        String ticketingPage = "ticketingPage";
        String theaterPage = "theaterPage";
        String conferencePage = "conferencePage";
    }

    public interface IntentKeys {
        String BLOG = "blog";
        String BLOG_NAME = "blog_name";
        String CHECK_LOGIN_STATUS = "login_status";
        String SOURCE_PAGE = "source_page";
        String VIEW_PAGER_KEYS = "from_viewpager";
        String RECOMMENDED_PAGE_KEYS = "recommended_page";
        String TOPICS_PAGE_KEYS = "topics";
        String RECOMMENDED_KEYS = "recommended";
        String FROM_LOGIN = "login";
        String FROM_SIGNUP = "signup";
        String FROM_FACEBOOK_LOGIN = "fb_login";
        String FROM_FACEBOOK_SIGNUP = "fb_signup";
        String FROM_CREATE_EVENT = "create_event";
        String FROM_CREATE_SUCCESSFULLY = "create_event_successfully";

    }

    public interface EVENT_FILTER_KEYS {
        String PRICE = "free";
        int PRICE_RANGE_TYPE_ONE = 499;
        int PRICE_RANGE_TYPE_TWO = 999;
        int PRICE_RANGE_TYPE_THREE = 4999;
        int PRICE_RANGE_TYPE_FOUR = 5000;

        String FILTER_EVENTS_THIS_MONTH = "true";
        String FILTER_EVENTS_THIS_WEEKEND = "true";
        String FILTER_EVENTS_THIS_WEEK = "true";
        String FILTER_EVENTS_TODAY = "true";
        String FILTER_EVENTS_POPULAR = "true";
        String FILTER_EVENT_TRENDING = "true";
    }

    public interface SOURCE_PAGE {
        String SOURCE_PAGE_KEY = "source_page_key";
        String FROM_COLLECTION_SCREEN = "from_collection_page";
        String FROM_NOTIFICATION_SCREEN = "from_push_notification";
        String FROM_CATEGORIES_SCREEN = "category_screen";
        String FROM_RECOMMENDED_SCREEN = "from_recommended";
        String FROM_TOPICS_SCREEN = "from_topics";
        String FROM_NOTIFICATION_ACTIVITY = "from_notification_activity";
    }


    public interface NAVIGATION_DRAWER_KEYS {

        String ACTION_TYPE = "action_type";
        String MY_TICKET = "my_ticket";
        String MY_TOPIC = "my_topic";
        String MY_EVENT = "my_event";
        String MY_FAVOURITE = "my_favourite";
    }

    public interface CREATE_EVENT_KEYS {
        int IMAGE_REQUEST_CODE = 0;
        String ADD_TICKET_BUTTON = "add_ticket_button";
        String TICKET_LIST_ITEM = "ticket_list_item";
        String PAID_TICKET_TYPE = "paid";
        String FREE_TICKET_TYPE = "free";
        String FROM_START_DATE_TAG = "start_date";
        String FROM_END_DATE_TAG = "end_date";
        String COUNTRY_INDIA = "India";
        String ACTION_TYPE = "action_type";
        String EDIT_ACTION_TYPE = "edit";
        String CREATE_ACTION_TYPE = "create";
        String THROUGH_ANDROID = "android";
        String TICKETING_TYPE = "ticketing";
        String CONFERENCE_TYPE = "conference";
        String RSVP_TYPE = "rsvp";
        String REG_TYPE = "registration";
        String DELETE_EVENT = "delete";
        String VENUE_TYPE = "venue";
        String ONLINE_TYPE = "online";

        String PERSONALIZE_TOPIC = "topic";
    }

    public interface EVENT_SESSION_TYPE {
        String THEATER = "theater";
        String CONFERENCE = "conference";
        String MULTIDATE = "multiDate";
        String TICKETING = "ticketing";
    }

}
