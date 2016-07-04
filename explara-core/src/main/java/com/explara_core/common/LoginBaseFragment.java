package com.explara_core.common;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.explara_core.common_dto.CleverTapIntentDataDto;
import com.explara_core.common_dto.ProfileResponse;
import com.explara_core.database.DataBaseManager;
import com.explara_core.database.DatabaseHelper;
import com.explara_core.login.LoginScreenManager;
import com.explara_core.login.login_dto.Account;
import com.explara_core.login.login_dto.LoginResponseDto;
import com.explara_core.login.ui.FbUserDetailsWithOutNavActivity;
import com.explara_core.login.ui.LoginNewFragment;
import com.explara_core.login.ui.SignUpVerificationCodeWithOutNavActivity;
import com.explara_core.login.util.CleverTapHelper;
import com.explara_core.login.util.LoginHelper;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.Log;
import com.explara_core.utils.PreferenceManager;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ananthasooraj on 07/04/16.
 */
public abstract class LoginBaseFragment extends BaseFragment {

    private static final String TAG = LoginBaseFragment.class.getSimpleName();

    // Reference of DatabaseHelper class to access its DAOs and other components
    private DatabaseHelper databaseHelper = null;



    private String mSourcePage = "";

    public LoginBaseFragment() {
    }


    public abstract void refresh();

    // Storing userData and access token
    public void saveUserDetailsWithAccessToken(LoginResponseDto loginResponse, String eventName, String loginScreen) {

        Log.d(TAG, "====" + loginScreen);
        // Storing AccessToken into PreferenceManager
        PreferenceManager.getInstance(getActivity().getApplicationContext()).setAccessToken(loginResponse.account.accessToken);
        //sendLoggedInWithUserNameToCleverTap(loginResponse, eventName);  commenting for now

        // get user details from access_token
        /*if (loginResponse.bank != null) {
            saveBankDetailsInLocal(loginResponse.bank); //now its fetching from create_event_basefragment
        }*/

        // Save Bank details in DB
        DataBaseManager.getInstance(getContext()).saveBankDetailsInDb(loginResponse, getContext());

        saveUserDetailsInLocal(LoginHelper.getUserProfile(loginResponse), eventName, loginScreen);

    }

    public void saveUserDetailsInLocal(ProfileResponse profileResponse, String eventName, String sourceScreen) {
        Log.d(TAG, "===" + sourceScreen);
        Log.d("=====****=====", "=====****===== LoginBaseFragment ");
        // Storing profile image,name,Email,Phone no
        if (!TextUtils.isEmpty(profileResponse.getProfile().getProfileImage())) {
            PreferenceManager.getInstance(getActivity().getApplicationContext()).setUserImage(profileResponse.getProfile().getProfileImage());
        }

        if (profileResponse.getProfile().getFirstName() != null && profileResponse.getProfile().getLastName() != null) {

            PreferenceManager.getInstance(getActivity().getApplicationContext()).setUserName(profileResponse.getProfile().getFirstName().equals(profileResponse.getProfile().getLastName()) ?
                    profileResponse.getProfile().getFirstName() :
                    (profileResponse.getProfile().getFirstName() + " "
                            + profileResponse.getProfile().getLastName()));

        } else {
            if (!TextUtils.isEmpty(profileResponse.getProfile().getFirstName())) {
                PreferenceManager.getInstance(getActivity().getApplicationContext()).setUserName(profileResponse.getProfile().getFirstName());
            }
            if (!TextUtils.isEmpty(profileResponse.getProfile().getLastName())) {
                PreferenceManager.getInstance(getActivity().getApplicationContext()).setUserName(profileResponse.getProfile().getLastName());
            }
        }

        PreferenceManager.getInstance(getActivity().getApplicationContext()).setEmail(profileResponse.getProfile().getEmailId());
        PreferenceManager.getInstance(getActivity().getApplicationContext()).setPhoneNo(profileResponse.getProfile().getMobileNumber());

        dismissMaterialDialog();

        if (Constants.CONFIRMATION_PAGE.equals(LoginNewFragment.mRedirect)) {
            launchMyTicketsPage();
        } else if (Constants.CREATE_EVENT_PAGE.equals(LoginNewFragment.mRedirect)) {
            launchCreateEventPage();
        } else if (Constants.MY_TICKET_PAGE.equals(LoginNewFragment.mRedirect)) {
            launchMyTicketsPage();
        } else if (Constants.MY_TOPIC_PAGE.equals(LoginNewFragment.mRedirect)) {
            launchMyTopicPage();
        } else if (Constants.MY_EVENT_PAGE.equals(LoginNewFragment.mRedirect)) {
            launchMyEventPage();
        } else if (Constants.MY_FAVOURITE.equals(LoginNewFragment.mRedirect)) {
            launchMyFavourite();
        } else if (Constants.PERSONALISED_SCREEN_RECOMMENDED_EVENTS.equals(LoginNewFragment.mRedirect)
                || Constants.PERSONALISED_SCREEN_TOPICS.equals(LoginNewFragment.mRedirect)) {
            launchHomeScreen();
        } else {
            if (LoginNewFragment.mSource.equals("login")) {
                navigateToCollectionsScreen(eventName, sourceScreen);
            } else {
                navigateToTopicsPage(eventName, sourceScreen);
            }
        }
    }

    private void navigateToCollectionsScreen(String eventName, String sourceScreen) {

        sendBroadcastForCleverTap(eventName, sourceScreen);

        if (TextUtils.isEmpty(PreferenceManager.getInstance(getContext()).getSelectedCity())) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME, "com.explara.android.selectCity.ui.SelectLocationActivity"));
            startActivity(intent);
        } else {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME, "com.explara.android.personalizedHome.ui.PersonalizeScreenActivity"));
            startActivity(intent);
        }
        getActivity().finish();
    }

    private void launchHomeScreen() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME, "com.explara.android.personalizedHome.ui.PersonalizeScreenActivity"));
        intent.putExtra("recommended", "from_recommended");
        intent.putExtra(ConstantKeys.IntentKeys.TOPICS_PAGE_KEYS, "from_topics");
        intent.putExtra(ConstantKeys.IntentKeys.VIEW_PAGER_KEYS, LoginNewFragment.mViewPagerPosition);
        startActivitySafely(intent);
    }

    private void sendBroadcastForCleverTap(String eventName, String screenName) {

        Intent intent = new Intent();
        intent.setAction("com.explara.android.utils");
        Bundle bundle = new Bundle();
        if (ConstantKeys.FromScreen.LOGIN_SCREEN.equals(screenName) || ConstantKeys.FromScreen.SIGNUP_SCREEN.equals(screenName)) {
            Account account = LoginScreenManager.getInstance().mLoginResponse.account;
            Map<String, Object> userDetailsProfileMap = new HashMap<>();
            if (!TextUtils.isEmpty(account.getName())) {
                userDetailsProfileMap.put(CleverTapHelper.ProfileProperty.NAME, account.getName());
            }
            if (!TextUtils.isEmpty(account.accessToken)) {
                userDetailsProfileMap.put(CleverTapHelper.ProfileProperty.IDENTITY, account.accessToken);
            }
            if (!TextUtils.isEmpty(account.emailId)) {
                userDetailsProfileMap.put(CleverTapHelper.ProfileProperty.EMAIL, account.emailId);
            }
            if (!TextUtils.isEmpty(account.mobileNumber)) {
                userDetailsProfileMap.put(CleverTapHelper.ProfileProperty.PHONE, account.mobileNumber);
            }
            if (!TextUtils.isEmpty(account.profileImage)) {
                userDetailsProfileMap.put(CleverTapHelper.ProfileProperty.PROFILE_IMAGE, account.profileImage);
            }

            HashMap<String, Object> userDetailsEventMap = new HashMap<>();
            userDetailsEventMap.put(CleverTapHelper.EventPropertyNames.SOURCE, "EMAIL");
            if (!TextUtils.isEmpty(account.emailId)) {
                userDetailsEventMap.put(CleverTapHelper.EventPropertyNames.LOGGED_EMAIL_ID, account.emailId);
            }
            // create CleverTapIntentDataDto object
            CleverTapIntentDataDto cleverTapIntentDataDto = new CleverTapIntentDataDto();

            cleverTapIntentDataDto.userDetailsProfileMap = userDetailsProfileMap;
            cleverTapIntentDataDto.userDetailsEventMap = userDetailsEventMap;
            cleverTapIntentDataDto.cleverTapEventName = eventName;
            // Put is into bundle
            bundle.putString(Constants.CLEVER_TAP_TYPE, Constants.CLEVER_TAP_PROFILE_TYPE);
            bundle.putSerializable(Constants.CLEVER_TAP_OBJECT_KEY, cleverTapIntentDataDto);
            intent.putExtras(bundle);
        } else if (ConstantKeys.FromScreen.LOGIN_SCREEN_FACEBOOK.equals(screenName) || ConstantKeys.FromScreen.SIGNUP_SCREEN_FACEBOOK.equals(screenName)) {
            intent.putExtra(Constants.CLEVER_TAP_TYPE, Constants.CLEVER_TAP_FB_PROFILE_TYPE);
            if (ConstantKeys.FromScreen.LOGIN_SCREEN_FACEBOOK.equals(screenName)) {
                intent.putExtra(Constants.CLEVER_TAP_EVENT_KEY, CleverTapHelper.EventNames.FACEBOOK_LOGIN);
            } else if (ConstantKeys.FromScreen.SIGNUP_SCREEN_FACEBOOK.equals(screenName)) {
                intent.putExtra(Constants.CLEVER_TAP_EVENT_KEY, CleverTapHelper.EventNames.FACEBOOK_SIGNUP);
            }
        }
        getActivity().sendBroadcast(intent);
    }

    private void launchMyFavourite() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME, "com.explara.android.events.ui.FavEventsActivity"));
        startActivitySafely(intent);
    }

    private void launchMyEventPage() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME, "com.explara.android.myEvents.MyEventsActivity"));
        startActivitySafely(intent);
    }

    private void launchMyTopicPage() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME, "com.explara.android.topics.ui.TopicsListActivity"));
        startActivitySafely(intent);
    }

    private void launchCreateEventPage() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME, "com.explara.create_event.ui.CreateEventActivity"));
        intent.putExtra(ConstantKeys.CREATE_EVENT_KEYS.ACTION_TYPE, ConstantKeys.CREATE_EVENT_KEYS.CREATE_ACTION_TYPE);
        startActivitySafely(intent);
    }

    private void launchMyTicketsPage() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME, "com.explara.android.tickets.ui.TicketsActivity"));
        startActivitySafely(intent);
    }


    private void navigateToLandingScreen() {
       /* Intent intent = new Intent(getActivity(), TestLandingActivity.class);
        startActivitySafely(intent);*/
    }

    public void sendSignUpWithFbToCleverTap(final AccessToken accessToken, final String TAG, final String eventName, final String facebookSignup) {
        final GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        // Application code
                        //Log.v("LoginActivity", response.toString());
                        String email = getGraphUserPropertySafely(object, "email", "");
                        PreferenceManager.getInstance(getActivity().getApplicationContext()).setEmail(email);
                        String name = getGraphUserPropertySafely(object, "name", "");
                        PreferenceManager.getInstance(getActivity().getApplicationContext()).setUserName(name);
                        try {
                            String profilePicUrl = response.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url");
                            Log.d("profilePicUrl", profilePicUrl);
                            if (!TextUtils.isEmpty(profilePicUrl)) {
                                PreferenceManager.getInstance(getActivity().getApplicationContext()).setUserImage(profilePicUrl);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                      /*  if(LoginScreenManager.getInstance().mJsonObject == null){*/
                        LoginScreenManager.getInstance().mJsonObject = object;
                    /*    }*/
                        // performCleverTapEventPush(object, email, eventName, facebookSignup);


                        //lunchFbSignupUserDetailsForm(name,email);
                        PreferenceManager.getInstance(getContext()).setFbAccessToken(accessToken.getToken());
                        checkAccountExistWithFbAccessToken(accessToken.getToken(), name, email, TAG, eventName, facebookSignup);

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,email,cover,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

  /*  private void performCleverTapEventPush(JSONObject object, String email, String eventName, String facebookSignup) {

        CleverTapHelper cleverTapHelper = new CleverTapHelper();
        cleverTapHelper.sendLoggedInWithUserNameToCleverTap(object,email,eventName,facebookSignup);
        sendBroadcastForCleverTap(eventName, mSourcePage);
    }*/

    private void checkAccountExistWithFbAccessToken(final String fbAccessToken, final String name, final String email, final String TAG, final String eventName, final String facebookSignup) {
        LoginScreenManager.getInstance().checkAccountExistWithFbAccessToken(getActivity().getApplicationContext(), fbAccessToken, new LoginScreenManager.CheckAccountExistsListener() {
            @Override
            public void onAccountExistChecked(LoginResponseDto loginResponseDto) {
                if (getActivity() != null && loginResponseDto != null) {
                    /*if (mMaterialDialog != null && mMaterialDialog.isShowing()) {
                        mMaterialDialog.dismiss(); recheck
                    }*/

                    dismissMaterialDialog();

                    if (Constants.STATUS_ERROR.equals(loginResponseDto.status)) {
                        launchFbSignupUserDetailsForm(name, email, fbAccessToken);
                    } else {
                        // Storing AccessToken into PreferenceManager
                        PreferenceManager.getInstance(getActivity().getApplicationContext()).setAccessToken(loginResponseDto.access_token);

                        // save user access_token to local settings
                        Constants.ACCESS_TOKEN_VALUE = loginResponseDto.access_token;
                        Constants.saveDataToLocalSettings(getActivity(), Constants.ACCESS_TOKEN_KEY, Constants.ACCESS_TOKEN_VALUE);

                        // get user details from access_token
                        // getUsersProfileInfo(loginResponse.access_token);

                     /*   if (loginResponseDto.bank != null) {
                            saveBankDetailsInLocal(loginResponseDto.bank);
                        }*/

                        saveUserDetailsInLocal(LoginHelper.getUserProfile(loginResponseDto), eventName, facebookSignup);

                    }
                }

            }

            @Override
            public void onAccountExistCheckFailed() {
                if (getActivity() != null) {
                    if (mMaterialDialog != null && mMaterialDialog.isShowing()) {
                        mMaterialDialog.dismiss();
                    }
                    Toast.makeText(getActivity(), "Oops! checking account existance failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    }

    private void launchFbSignupUserDetailsForm(String name, String email, String mFbAccessToken) {
        Intent intent = new Intent(getActivity(), FbUserDetailsWithOutNavActivity.class);
        intent.putExtra(Constants.FB_ACCESS_TOKEN_KEY, mFbAccessToken);
        intent.putExtra(Constants.NAME_KEY, name);
        intent.putExtra(Constants.EMAIL_KEY, email);
        startActivity(intent);
        getActivity().finish();
    }

    public void lunchSignUpVerificationCode(String emailId) {
        Intent intent = new Intent(getActivity(), SignUpVerificationCodeWithOutNavActivity.class);
        intent.putExtra(Constants.EMAIL_ID_KEY, emailId);
        startActivity(intent);
        getActivity().finish();
    }

    private String getGraphUserPropertySafely(JSONObject graphUser, String key, String def) {
        try {
            String t = (String) graphUser.get(key);
            return t != null ? t : def;
        } catch (Throwable var5) {
            return def;
        }
    }

    private void navigateToTopicsPage(String eventName, String sourceScreen) {
        sendBroadcastForCleverTap(eventName, sourceScreen);
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME, "com.explara.android.topics.ui.PersonalTopicsActivity"));
        intent.putExtra("sourcePage", "signupPage");
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DataBaseManager.getInstance(getContext()).cleanUp();
    }
}
