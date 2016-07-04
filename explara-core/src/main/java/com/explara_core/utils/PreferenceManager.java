package com.explara_core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.explara_core.common.BaseManager;


/**
 * Created by anudeep on 01/09/15.
 */
public class PreferenceManager extends BaseManager {
    private static PreferenceManager sInstance;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEdit;

    // Set current page
    public static final int TUTORIAL_SCREEN = 1;
    public static final int SINGUP_LOGIN_SCREEN = 2;
    public static final int SIGNUP_MOBILE_NO_SCREEN = 3;
    public static final int CODE_VERIFICATION_SCREEN = 4;
    public static final int SELECT_TOPIC_SCREEN = 5;
    public static final int SELECT_LOCATION_SCREEN = 6;
    public static final int HOME_COLLECTON_SCREEN = 7;

    private static final String FILE_NAME = "com.explara.android.preferences";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String FB_ACCESS_TOKEN = "fb_access_token";
    public static final String SELCTED_CITY = "selected_city";
    public static final String CATEGORY_SCREEN_SELECTED_CITY = "category_screen_selected_city";
    public static final String COUNTRY_CODE = "country_code";
    public static final String IP_ADDRESS = "ip_address";
    public static final String CURRENT_PAGE = "current_page";
    public static final String FILTER_OPTION_TIME = "filter_option";

    public static final String FILTER_OPTION_PRICE = "filter_option_price";

    public static final String FILTER_OPTION_SORTBY = "filter_option_sort_by";
    private static final String USER_IMAGE = "user_image";
    public static final String USER_NAME = "firstName";
    public static final String EMAIL = "emailid";
    public static final String PHONE_NO = "phonenummber";
    public static final String VERSION_DIALOG = "versionCheck";
    public static final String PAY_TM_ACCESS_TOKEN = "pay_tm_access_token";
    public static final String CITRUS_USER_BALANCE = "citrus_user_balance";
    public static final String PREFERED_WALLET_OPTION = "Preferred_wallet_option";
    public static final int PAY_TM_PREFERRED_WALLET = 51;
    public static final int CITRUS_PREFFRED_WALLET = 52;
    public static final int NO_WALLET_SELECTED = 999;
    public static final String ACCOUNT_VERIFIED = "account_verified";
    public static final String BANK_DETAILS = "bank_details";
    public static final String CURRENCY_LIST = "currency_list";
    public static final String COUNTRY_CURRENCY_LIST = "country_currency_list";

    private PreferenceManager(Context context) {
        mPrefs = context.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        mEdit = mPrefs.edit();
    }


    public static synchronized PreferenceManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferenceManager(context);
        }
        return sInstance;
    }

    public void setAccessToken(String accessToken) {
        mEdit.putString(ACCESS_TOKEN, accessToken).apply();
    }

    public String getAccessToken() {
        return mPrefs.getString(ACCESS_TOKEN, "");
    }

    public void setFbAccessToken(String fbAccessToken) {
        mEdit.putString(FB_ACCESS_TOKEN, fbAccessToken).apply();
    }

    public String getFbAccessToken() {
        return mPrefs.getString(FB_ACCESS_TOKEN, "");
    }


    public void setSelectedCity(String selectedCity) {
        mEdit.putString(SELCTED_CITY, selectedCity).apply();
    }

    public void setCategoryScreenSelectedCity(String selectedCity) {
        mEdit.putString(CATEGORY_SCREEN_SELECTED_CITY, selectedCity).apply();
    }

    public void setCountryCode(String countryCode) {
        mEdit.putString(COUNTRY_CODE, countryCode).apply();
    }

    public String getCountryCode() {
        return mPrefs.getString(COUNTRY_CODE, "");

    }

    public void setIpAddress(String ipAddress) {

        mEdit.putString(IP_ADDRESS, ipAddress).apply();
    }

    public String getIpAddress() {

        return mPrefs.getString(IP_ADDRESS, "");
    }


    public String getFilterTimeSelected() {

        return mPrefs.getString(FILTER_OPTION_TIME, "");

    }

    public void setFilterTimeSelected(String filter) {
        mEdit.putString(FILTER_OPTION_TIME, filter).apply();
    }

    public void setFilterPrice(String filterPrice) {
        mEdit.putString(FILTER_OPTION_PRICE, filterPrice).apply();


    }

    public String getFilterOptionPrice() {
        return mPrefs.getString(FILTER_OPTION_PRICE, "");


    }


    public void setFilterOptionSortBy(String filterOptionSortBy) {

        mEdit.putString(FILTER_OPTION_SORTBY, filterOptionSortBy).apply();

    }

    public String getFilterOptionSortby() {
        return mPrefs.getString(FILTER_OPTION_SORTBY, "");

    }

    public String getSelectedCity() {
        return mPrefs.getString(SELCTED_CITY, "");
    }

    public String getCategoryScreenSelectedCity() {
        return mPrefs.getString(CATEGORY_SCREEN_SELECTED_CITY, "");
    }

    public void setUserName(String userName) {
        mEdit.putString(USER_NAME, userName).apply();
    }

    public String getUserName() {
        return mPrefs.getString(USER_NAME, "");
    }

    public void setUserImage(String userImage) {
        mEdit.putString(USER_IMAGE, userImage).apply();
    }

    public String getUserImage() {
        return mPrefs.getString(USER_IMAGE, "");
    }

    public String getEmail() {
        return mPrefs.getString(EMAIL, "");
    }

    public void setEmail(String email) {
        mEdit.putString(EMAIL, email).apply();
    }

    public String getPhoneNo() {
        return mPrefs.getString(PHONE_NO, "");
    }

    public void setPhoneNo(String phoneNo) {
        mEdit.putString(PHONE_NO, phoneNo).apply();
    }

    public void setPayTmAccessToken(String accessToken) {
        mEdit.putString(PAY_TM_ACCESS_TOKEN, accessToken).apply();
    }

    public String getPayTmAccessToken() {
        return mPrefs.getString(PAY_TM_ACCESS_TOKEN, "");
    }


    public boolean getVersionDialogValueFromPref() {
        return mPrefs.getBoolean(VERSION_DIALOG, false);
    }

    public void setVersionDialogValueToPref(boolean isVersionDialogShown) {
        mEdit.putBoolean(VERSION_DIALOG, isVersionDialogShown).apply();
    }

    public void setCitrusUserBalance(String balance) {
        mEdit.putString(CITRUS_USER_BALANCE, balance).apply();
    }

    public String getCitrusUserBalance() {
        return mPrefs.getString(CITRUS_USER_BALANCE, null);
    }

    public void clearUser(Activity context) {
        setAccessToken("");
        setUserImage("");
        setUserName("");
        setEmail("");
        setPhoneNo("");
        Constants.saveDataToLocalSettings(context, Constants.ACCESS_TOKEN_KEY, "");
        setPayTmAccessToken("");
        setCitrusUserBalance("");
        setPreferredWallet(NO_WALLET_SELECTED);



    }

    @Override
    public void cleanUp() {
        sInstance = null;
    }

    public boolean isLogin() {
        return !TextUtils.isEmpty(mPrefs.getString(ACCESS_TOKEN, ""));
    }

    public void setPreferredWallet(int walletOption) {
        mEdit.putInt(PREFERED_WALLET_OPTION, walletOption).apply();
    }

    public int getPreferredWalletOption() {
        return mPrefs.getInt(PREFERED_WALLET_OPTION, NO_WALLET_SELECTED);
    }

    public boolean isPreferredWalletOptionSelected() {
        return getPreferredWalletOption() != PreferenceManager.NO_WALLET_SELECTED;
    }

    public void setAccountVerified(String accountVerified) {
        mEdit.putString(ACCOUNT_VERIFIED, accountVerified).apply();
    }

    public String getAccountVerified() {
        return mPrefs.getString(ACCOUNT_VERIFIED, Constants.ACCOUNT_VERIFIED);
    }

    public boolean isAccountVerified() {
        return !Constants.ACCOUNT_NOT_VERIFIED.equals(getAccountVerified());
    }

    public boolean isPaytmUserLoggedIn() {
        return !TextUtils.isEmpty(getPayTmAccessToken());
    }

    public void setBankDetails(String bankDetailsJson) {
        mEdit.putString(BANK_DETAILS, bankDetailsJson).apply();
    }

    public String getBankDetails() {
        return mPrefs.getString(BANK_DETAILS,"");
    }

    public void setCurrencyList(String currencyList) {
        mEdit.putString(CURRENCY_LIST, currencyList).apply();
    }

    public String getCurrencyList() {
        return mPrefs.getString(CURRENCY_LIST,"");
    }

    public void setCountryCurrencyList(String countryCurrencyList) {
        mEdit.putString(COUNTRY_CURRENCY_LIST, countryCurrencyList).apply();

    }

    public String getCountryCurrencyList() {
        return mPrefs.getString(COUNTRY_CURRENCY_LIST,"");
    }

    public void setCurrentPage(int currentPage){
        mEdit.putInt(CURRENT_PAGE,currentPage).apply();
    }

    public int getCurrentPage(){
        return mPrefs.getInt(CURRENT_PAGE,1);
    }
}
