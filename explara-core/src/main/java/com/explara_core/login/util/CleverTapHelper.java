package com.explara_core.login.util;

import org.json.JSONObject;

/**
 * Created by akshaya on 06/01/16.
 */
public class CleverTapHelper {

    public JSONObject jsonObject;
    public String sourcePage;

    public interface EventNames {
        String SEARCH_EVENT = "Search";
        String SKIP_EVENT = "Skip";
        String SHARE_EVENT = "Share";
        String RELATED_TOPICS_EVENT = "RelatedTopics";
        String LOGIN_EVENT = "Login";
        String SIGN_UP_EVENT = "SignUp";
        String TOPIC_EVENT_FOLLOWED = "TopicFollowed";
        String EVENT_VIEW_NAME = "EventViewName";
        String TOPIC_VIEW_NAME = "TopicViewName";
        String TOPIC_EVENT_UNFOLLOWED = "TopicUnFollowed";
        String FACEBOOK_SIGNUP = "FacebookSignup";
        String FACEBOOK_LOGIN = "FacebookLogin";
        String CREATE_AN_EVENT = "CreateAnEvent";
        String CREATE_AN_EVENT_SUCCESSFULLY = "EventCreatedSuccessFully";
    }

    public interface EventPropertyNames {
        String SEARCH_KEYWORD = "Search_KeyWord";
        String LOGGED_EMAIL_ID = "Email_Id";
        String SOURCE = "Source";
        String TOPICS = "Topics";
        String SCREEN_TITLE = "Screen_Title";
        String SCREEN_TYPE = "Screen_type";
    }


    public interface ProfileProperty {
        String NAME = "Name";
        String IDENTITY = "Identity";
        String EMAIL = "Email";
        String PHONE = "Phone";
        String PROFILE_IMAGE = "Profile Image";


    }


/*
    public static void sendLoggedInWithUserNameToCleverTap(LoginResponseDto loginResponse, Context context) {

        try {
            CleverTapAPI cleverTapAPI = CleverTapAPI.getInstance(context);
            Map<String, Object> profileUpdate = new HashMap<>();
            profileUpdate.put(CleverTapHelper.ProfileProperty.NAME, loginResponse.account.getName()); // String
            profileUpdate.put(CleverTapHelper.ProfileProperty.IDENTITY, loginResponse.access_token); // String or number
            profileUpdate.put(CleverTapHelper.ProfileProperty.EMAIL, loginResponse.account.emailId); // Email address of the user
            profileUpdate.put(CleverTapHelper.ProfileProperty.PHONE, loginResponse.account.mobileNumber); // Phone(without the country code)
            profileUpdate.put(CleverTapHelper.ProfileProperty.PROFILE_IMAGE, loginResponse.account.profileImage);
            cleverTapAPI.profile.push(profileUpdate);

            HashMap<String, Object> loginAction = new HashMap<>();
            loginAction.put(CleverTapHelper.EventPropertyNames.SOURCE, "EMAIL");
            loginAction.put(CleverTapHelper.EventPropertyNames.LOGGED_EMAIL_ID, loginResponse.account.emailId);
            cleverTapAPI.event.push(CleverTapHelper.EventNames.SIGN_UP_EVENT, loginAction);

        } catch (CleverTapMetaDataNotFoundException | CleverTapPermissionsNotSatisfied e) {
            e.printStackTrace();
        }
    }*/

}
