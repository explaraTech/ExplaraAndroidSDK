package com.explara_core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.explara_core.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dev on 02/07/15.
 */
public class Constants {

    //login constants modified
    // Debug Info moved to ExplaraApplication class
    public static final boolean DEBUG = false;
    public static final boolean EXPLARA_ONLY = false;

    // API Urls
    public static final String PROD_NOTIFICATIONS = "https://api.explara.com/event/notification";

    public static final String TEST_DOMAIN_NEW = "http://apistaging.explara.com";
    public static final String PROD_DOMAIN_NEW = "https://api.explara.com";

    public static final String TEST_DOMAIN_OLD = "https://instaging.explara.com/api/resource";
    public static final String PROD_DOMAIN_OLD = "https://in.explara.com/api/resource";

    public static final String TEST_DOMAIN_ACCOUNT = "https://instaging.explara.com/a/api/resource";
    public static final String PROD_DOMAIN_ACCOUNT = "https://in.explara.com/a/api/resource";

    public static final String EVENT_TEST_DOMAIN = "https://instaging.explara.com/api/event";
    public static final String EVENT_PROD_DOMAIN = "https://in.explara.com/api/event";

    public static final String WEB_TEST_DOMAIN = "https://instaging.explara.com";
    public static final String WEB_PROD_DOMAIN = "https://in.explara.com";

    public static final String ROOT_DOMAIN_NEW = (DEBUG ? TEST_DOMAIN_NEW : PROD_DOMAIN_NEW);
    public static final String ROOT_DOMAIN_OLD = (DEBUG ? TEST_DOMAIN_OLD : PROD_DOMAIN_OLD);
    public static final String ROOT_DOMAIN_ACCOUNT = (DEBUG ? TEST_DOMAIN_ACCOUNT : PROD_DOMAIN_ACCOUNT);
    public static final String ROOT_MAIN_DOMAIN = (DEBUG ? WEB_TEST_DOMAIN : WEB_PROD_DOMAIN);
    public static final String EVENT_ROOT_DOMAIN = (DEBUG ? EVENT_TEST_DOMAIN : EVENT_PROD_DOMAIN);

    //AWS S3 credentials
    public static final String COGNITO_POOL_ID = "ap-northeast-1:23bcc5f1-25a3-40ab-b6d2-5784b9229de6";
    public static final String BUCKET_NAME = "explaraimage";
    public static final String CDN_URL = "http://cdn.explara.com/";

    public static String LOCATION_NAME = "";

    public static final String GET_PROFILE_URL = ROOT_DOMAIN_NEW + "/collection/get-user-profile";
    public static final String CHECK_ACCOUNT_EXIST_URL = ROOT_DOMAIN_ACCOUNT + "/check-account-exist";
    public static final String LOGIN_URL = Constants.ROOT_DOMAIN_ACCOUNT + "/login";
    public static final String SIGN_UP_WITH_OUT_CODE_URL = ROOT_DOMAIN_ACCOUNT + "/account-signup";
    public static final String FACEBOOK_LOGIN_URL = Constants.ROOT_DOMAIN_ACCOUNT + "/fb-login-mobile";
    public static final String FORGOT_PASSWORD_URL = ROOT_DOMAIN_ACCOUNT + "/forgot-password";
    public static final String VERIFY_CODE_MOBILE_URL = ROOT_DOMAIN_ACCOUNT + "/verify-code-mobile";
    public static final String VERIFY_CODE_SIGN_UP = Constants.ROOT_DOMAIN_ACCOUNT + "/activate";
    public static final String CHANGE_PASSWORD_MOBILE_URL = ROOT_DOMAIN_ACCOUNT + "/change-password-mobile";
    public static final String RESEND_VERIFY_CODE_SIGN_UP = ROOT_DOMAIN_ACCOUNT + "/resend-otp";
    public static final String NEW_PASSWORD_1_KEY = "newPassword";
    public static final String NEW_PASSWORD_2_KEY = "confirmPassword";

    //region parameter keys
    public static final String ACCESS_TOKEN_KEY = "accessToken";
    public static final String EMAIL_KEY = "email";
    public static final String PASSWORD_KEY = "password";
    public static final String VERIFICATION_CODE_KEY = "code";
    public static final String STATUS_ERROR = "error";
    public static final String STATUS_SUCCESS = "success";
    public static final String EMAIL_ID = "emailId";
    public static final String EVENT_ID = "eventId";
    public static final String EVENT_ID_KEY = "event_id";
    public static final String ACCOUNT_ID = "accountId";
    public static final String IS_ATTENDEE_FORM_ENABLED = "isAttendeeFormEnabled";
    public static final String EVENT_NAME = "eventName";
    public static final String IS_TICKET_SOLD = "isSold";
    public static final String EVENT_URL = "eventUrl";
    public static final String EVENT_DESC = "eventDesc";
    public static final String EVENT_COVER_PHOTO = "coverPhoto";
    public static final String EMAIL_ID_KEY = "emailId";
    public static final String PAYMENT_OPTION = "paymentOption";
    public static final String TAG = "tag";
    public static String ACCESS_TOKEN_VALUE = "";

    // Clever Tap constants
    public static final String CLEVER_TAP_TYPE = "cleverTapType";
    public static final String CLEVER_TAP_EVENT_KEY = "cleverTapEventKey";
    public static final String CLEVER_TAP_EVENT_TYPE = "cleverTapEventType";
    public static final String CLEVER_TAP_PROFILE_TYPE = "cleverTapProfileType";
    public static final String CLEVER_TAP_FB_PROFILE_TYPE = "cleverTapFbProfileType";
    public static final String CLEVER_TAP_TOPICS_TYPE = "cleverTapTopicsType";
    public static final String CLEVER_TAP_EVENT_TITLE_TYPE = "cleverTapEventTitleType";
    public static final String CLEVER_TAP_TRANSACTION_TYPE = "cleverTapTransactionType";

    public static final String CLEVER_TAP_OBJECT_KEY = "cleverTapObjectKey";
    public static final String CLEVER_TAP_LIST_KEY = "cleverTapListKey";


    //login
    public static final String FB_ACCESS_TOKEN_KEY = "fbAccessToken";
    public static final String NAME_KEY = "name";
    public static final String SOURCE = "source";
    public static final String REDIRECT = "redirect";
    public static final String LOGIN = "login";
    public static final String SIGNUP = "signup";
    public static final String ACCOUNT_NOT_VERIFIED = "no";
    public static final String ACCOUNT_VERIFIED = "yes";

    //intent keys
    public static final String LOGIN_PAGE_SKIP_BUTTON = "login_skip_button";
    public static final String SKIP_BUTTON = "skip_button";
    public static final String TUTORIAL_SKIP_BUTTON = "tutorial_skip_btn";
    public static final String CREATE_EVENT_KEY = "CreateAnEvent";
    public static final String CREATE_EVENT_SUCCESSFULLY = "EventCreatedSuccessFully";
    public static final String FROM_LOGIN_SCREEN_FRAGMENT = "login_screen_fragment";
    public static final String FROM_TUTORIAL_SCREEN = "from_tutorial_screen";

    //packages
    public static final String BASE_PACKAGE_NAME = "com.explara.android";
    public static final String TICKETING_PACKAGE_NAME = "com.explara.explara_ticketing_sdk";
    public static final String TICKETING_UI_PACKAGE_NAME = "com.explara.explara_ticketing_sdk_ui";
    public static final String PAYMENT_PACKAGE_NAME = "com.explara.explara_payment_sdk";

    //redirect params
    public static final String CONFIRMATION_PAGE = "confirmation_page";
    public static final String CREATE_EVENT_PAGE = "create_event_page";
    public static final String MY_TICKET_PAGE = "my_ticket_page";
    public static final String MY_TOPIC_PAGE = "my_topic_page";
    public static final String MY_EVENT_PAGE = "my_event_page";
    public static final String MY_FAVOURITE = "my_favourite";
    public static final String PERSONALISED_SCREEN_RECOMMENDED_EVENTS = "personalised_home_recommended_events";
    public static final String PERSONALISED_SCREEN_TOPICS = "personalised_home_topics";


    //tickets based url and region params
    public static final String GET_MULTI_PACKAGE_DATES = Constants.EVENT_ROOT_DOMAIN + "/get-all-available-dates";
    public static final String GET_ALL_ORDER_BY_LOGGED_IN_USER_URL = Constants.ROOT_DOMAIN_OLD + "/my-orders";

    public static final String CURRENCY = "currency";
    public static final String POSITION = "position";







    //region Common generic methods like md5 generation from string should be grouped here as static methods
    public static final String generateMD5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void saveDataToLocalSettings(Activity activity, String key, String value) {
        // saving data locally and privately
        SharedPreferences sharedPrefs = activity.getSharedPreferences(activity.getResources().getString(R.string.shared_prefs_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        try {
            editor.putString(key, value);
            editor.commit();
        } catch (Exception e) {

        }
    }

    public static String getDataFromLocalSettings(Activity activity, String key) {
        SharedPreferences sharedPrefs = activity.getSharedPreferences(activity.getResources().getString(R.string.shared_prefs_key), Context.MODE_PRIVATE);
        String value = sharedPrefs.getString(key, null);
        return value;
    }

    public static void createToastWithMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static String getAccessToken(Context context) {
        return PreferenceManager.getInstance(context).getAccessToken();
    }

    public static String getMonthString(int month) {
        String value = "";
        switch (month) {
            case 1:
                value = "JAN";
                break;
            case 2:
                value = "FEB";
                break;
            case 3:
                value = "MAR";
                break;
            case 4:
                value = "APR";
                break;
            case 5:
                value = "MAY";
                break;
            case 6:
                value = "JUN";
                break;
            case 7:
                value = "JUL";
                break;
            case 8:
                value = "AUG";
                break;
            case 9:
                value = "SEP";
                break;
            case 10:
                value = "OCT";
                break;
            case 11:
                value = "NOV";
                break;
            case 12:
                value = "DEC";
                break;
        }
        return value;
    }



    //endregion
}
