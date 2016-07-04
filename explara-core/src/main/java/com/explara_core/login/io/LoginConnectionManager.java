package com.explara_core.login.io;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.explara_core.common_dto.ProfileResponse;
import com.explara_core.login.login_dto.ForgotPasswordResponse;
import com.explara_core.login.login_dto.LoginResponseDto;
import com.explara_core.login.login_dto.SignupResponseDto;
import com.explara_core.utils.Constants;
import com.explara_core.utils.GsonRequest;
import com.explara_core.utils.Log;
import com.explara_core.utils.VolleyManager;


/**
 * Created by Debasish on 23/09/2015.
 */
public class LoginConnectionManager {

    public static final String TAG = LoginConnectionManager.class.getSimpleName();

    // Facebook login - for getting access_token
    public void loginWithFacebook(Context context, String fbAccessToken, Response.Listener<LoginResponseDto> success, Response.ErrorListener errorListener, String tag) {
        String fbLoginUrl = Constants.FACEBOOK_LOGIN_URL + "?fbAccessToken=" + fbAccessToken;
        Log.d(TAG, fbLoginUrl);

        GsonRequest<LoginResponseDto> loginResponseGsonRequest = new GsonRequest<>(context, Request.Method.GET, fbLoginUrl, LoginResponseDto.class, success, errorListener);
        loginResponseGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(loginResponseGsonRequest);
    }

    // Getting user details from access_token
    public void getUserDetails(Context context, String accessToken, Response.Listener<ProfileResponse> success, Response.ErrorListener errorListener, String tag) {
        String url = Constants.GET_PROFILE_URL + "?" + Constants.ACCESS_TOKEN_KEY + "=" + accessToken;
        Log.d(TAG, url);
        GsonRequest<ProfileResponse> getProfileResponseGsonRequest = new GsonRequest<ProfileResponse>(context, Request.Method.GET, url, ProfileResponse.class, success, errorListener);
        getProfileResponseGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(getProfileResponseGsonRequest);
    }

    // User login with username and password
    public void loginWithUsernameAndPassword(Context context, String username, String password, Response.Listener<LoginResponseDto> success, Response.ErrorListener errorListener, String tag) {
        //Create MD5 of the password
        String password_md5_hash = Constants.generateMD5(password);
        if (!password_md5_hash.isEmpty()) {
            GsonRequest<LoginResponseDto> userLoginResponseGsonRequest = new GsonRequest<LoginResponseDto>(context, Request.Method.GET, generateLoginUrl(Constants.LOGIN_URL, username, password_md5_hash), LoginResponseDto.class, success, errorListener);
            userLoginResponseGsonRequest.setTag(tag);
            VolleyManager.getInstance(context).getRequestQueue().add(userLoginResponseGsonRequest);
        }
    }

    private String generateLoginUrl(String url, String username, String password_hash) {
        if (!url.endsWith("?"))
            url += "?";

        url += Constants.EMAIL_KEY + "=" + username + "&" + Constants.PASSWORD_KEY + "=" + password_hash;

        Log.d(TAG, url);
        return url;
    }

    public void checkAccountExistWithFbAccessToken(Context context,String jsonStr, Response.Listener<LoginResponseDto> success, Response.ErrorListener errorListener, String tag) {

        //GsonRequest<SignupResponseDto> userSignupResponseGsonRequest = new GsonRequest<SignupResponseDto>(context, Request.Method.GET, generateSignUpUrl(Constants.SIGN_UP_WITH_OUT_CODE_URL, username, password, mobileNo, fullName), SignupResponseDto.class, success, errorListener);
        final GsonRequest<LoginResponseDto> userSignupResponseGsonRequest = new
                GsonRequest<LoginResponseDto>(context, Request.Method.POST,
                Constants.CHECK_ACCOUNT_EXIST_URL,
                LoginResponseDto.class, null, jsonStr, success, errorListener);
        Log.d(TAG, Constants.CHECK_ACCOUNT_EXIST_URL);
        userSignupResponseGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(userSignupResponseGsonRequest);
    }

    // User singup with username and password
    public void singupWithUsernameAndPassword(Context context,String jsonStr, Response.Listener<SignupResponseDto> success, Response.ErrorListener errorListener, String tag) {

        //GsonRequest<SignupResponseDto> userSignupResponseGsonRequest = new GsonRequest<SignupResponseDto>(context, Request.Method.GET, generateSignUpUrl(Constants.SIGN_UP_WITH_OUT_CODE_URL, username, password, mobileNo, fullName), SignupResponseDto.class, success, errorListener);
        final GsonRequest<SignupResponseDto> userSignupResponseGsonRequest = new
                GsonRequest<SignupResponseDto>(context, Request.Method.POST,
                Constants.SIGN_UP_WITH_OUT_CODE_URL,
                SignupResponseDto.class, null, jsonStr, success, errorListener);
        Log.d(TAG, Constants.SIGN_UP_WITH_OUT_CODE_URL);
        userSignupResponseGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(userSignupResponseGsonRequest);
    }

   /* private String generateSignUpUrl(String url, String username, String password_hash, String mobileNo, String fullName) {
        if (!url.endsWith("?"))
            url += "?";

        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair(Constants.PASSWORD_KEY, password_hash));
        params.add(new BasicNameValuePair(Constants.MOBILE_NO_KEY, mobileNo));
        params.add(new BasicNameValuePair(Constants.NAME_KEY, fullName));

        String paramString = URLEncodedUtils.format(params, Constants.UTF_8);

        url += Constants.EMAIL_KEY + "=" + username + "&";
        url += paramString;

        Log.i(TAG, url);
        return url;
    } */

    // User singup with facebook access token
    public void signupWithFbAccessToken(Context context,String jsonStr, Response.Listener<LoginResponseDto> success, Response.ErrorListener errorListener, String tag) {
        //GsonRequest<SignupResponseDto> userSignupResponseGsonRequest = new GsonRequest<SignupResponseDto>(context, Request.Method.GET, generateSignUpWithFbAccessTokenUrl(Constants.SIGN_UP_WITH_OUT_CODE_URL, username, fbAccessToken, mobileNo, fullName), SignupResponseDto.class, success, errorListener);
        final GsonRequest<LoginResponseDto> userSignupResponseGsonRequest = new
                GsonRequest<LoginResponseDto>(context, Request.Method.POST,
                Constants.SIGN_UP_WITH_OUT_CODE_URL,
                LoginResponseDto.class, null, jsonStr, success, errorListener);
        Log.d(TAG, Constants.SIGN_UP_WITH_OUT_CODE_URL);
        userSignupResponseGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(userSignupResponseGsonRequest);
    }

   /* private String generateSignUpWithFbAccessTokenUrl(String url, String username, String fbAccessToken, String mobileNo, String fullName) {
        String signUpUrl = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(Constants.EMAIL_KEY, username)
                .appendQueryParameter(Constants.FB_ACCESS_TOKEN_KEY, fbAccessToken)
                .appendQueryParameter(Constants.MOBILE_NO_KEY, mobileNo)
                .appendQueryParameter(Constants.NAME_KEY, fullName)
                .toString();
        Log.i(TAG, signUpUrl);
        return signUpUrl;
    } */

    // User singup with username and password
    public void signupWithUsernameAndPasswordWithoutVerify(Context context,String jsonStr, Response.Listener<SignupResponseDto> success, Response.ErrorListener errorListener, String tag) {
        //GsonRequest<SignupResponseDto> userSignupResponseGsonRequest = new GsonRequest<SignupResponseDto>(context, Request.Method.GET, generateSignUpWithOutVerifyUrl(Constants.SIGN_UP_WITH_OUT_CODE_URL, username, password, mobileNo, fullName), SignupResponseDto.class, success, errorListener);
        final GsonRequest<SignupResponseDto> userSignupResponseGsonRequest = new
                GsonRequest<SignupResponseDto>(context, Request.Method.POST,
                Constants.SIGN_UP_WITH_OUT_CODE_URL,
                SignupResponseDto.class, null, jsonStr, success, errorListener);
        Log.d(TAG, Constants.SIGN_UP_WITH_OUT_CODE_URL);
        userSignupResponseGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(userSignupResponseGsonRequest);
    }

 /*   private String generateSignUpWithOutVerifyUrl(String url, String username, String password_hash, String mobileNo, String fullName) {
        String signUpUrl = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(Constants.EMAIL_KEY, username)
                .appendQueryParameter(Constants.PASSWORD_KEY, password_hash)
                .appendQueryParameter(Constants.MOBILE_NO_KEY, mobileNo)
                .appendQueryParameter(Constants.NAME_KEY, fullName)
                .appendQueryParameter(Constants.SEND_VERIFICATION,"no")
                .toString();
        Log.i(TAG, signUpUrl);
        return signUpUrl;
    } */

    public void sendVerificationCode(Context context, String email, Response.Listener<LoginResponseDto> success, Response.ErrorListener errorListener, String tag) {
        GsonRequest<LoginResponseDto> sendVerificationResponseGsonRequest = new GsonRequest<LoginResponseDto>(context, Request.Method.GET, generateForgotPasswordURL(Constants.FORGOT_PASSWORD_URL, email), LoginResponseDto.class, success, errorListener);
        sendVerificationResponseGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(sendVerificationResponseGsonRequest);
    }

    private String generateForgotPasswordURL(String url, String email) {
        if (!url.endsWith("?"))
            url += "?";

        url += Constants.EMAIL_KEY + "=" + email;

        Log.d(TAG, url);
        return url;
    }

    public void verifyCode(Context context, String code, String emailId, Response.Listener<LoginResponseDto> success, Response.ErrorListener errorListener, String tag) {
        String url = Constants.VERIFY_CODE_MOBILE_URL + "?" +
                Constants.EMAIL_KEY + "=" + emailId + "&" +
                Constants.VERIFICATION_CODE_KEY + "=" + code;
        Log.d(TAG, url);
        GsonRequest<LoginResponseDto> verifyCodeResponseGsonRequest = new GsonRequest<LoginResponseDto>(context, Request.Method.GET, url, LoginResponseDto.class, success, errorListener);
        verifyCodeResponseGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(verifyCodeResponseGsonRequest);
    }

    public void resetPassword(Context context, String code, String emailId, String newPassword, String confirmPassword, Response.Listener<ForgotPasswordResponse> success, Response.ErrorListener errorListener, String tag) {

       /* String url = Constants.CHANGE_PASSWORD_MOBILE_URL + "?" +
                Constants.EMAIL_KEY + "=" + emailId + "&";

        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair(Constants.VERIFICATION_CODE_KEY, code));
        params.add(new BasicNameValuePair(Constants.NEW_PASSWORD_1_KEY, Constants.generateMD5(newPassword)));
        params.add(new BasicNameValuePair(Constants.NEW_PASSWORD_2_KEY, Constants.generateMD5(confirmPassword)));

        String paramString = URLEncodedUtils.format(params, Constants.UTF_8);

        url += paramString;

        Log.d(TAG, url); */

        GsonRequest<ForgotPasswordResponse> resetPasswordResponseGsonRequest = new GsonRequest<ForgotPasswordResponse>(context, Request.Method.GET,gererateResetPasswordUrl(code,emailId,newPassword,confirmPassword), ForgotPasswordResponse.class, success, errorListener);
        resetPasswordResponseGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(resetPasswordResponseGsonRequest);
    }

    private String gererateResetPasswordUrl(String code, String emailId, String newPassword, String confirmPassword) {
        String resetPasswordUrl = Uri.parse(Constants.CHANGE_PASSWORD_MOBILE_URL)
                .buildUpon()
                .appendQueryParameter(Constants.EMAIL_KEY, emailId)
                .appendQueryParameter(Constants.VERIFICATION_CODE_KEY, code)
                .appendQueryParameter(Constants.NEW_PASSWORD_1_KEY, Constants.generateMD5(newPassword))
                .appendQueryParameter(Constants.NEW_PASSWORD_2_KEY, Constants.generateMD5(confirmPassword))
                .toString();
        Log.i(TAG, resetPasswordUrl);
        return resetPasswordUrl;
    }

    public void verifySignUpCode(Context context, String code, String emailId, Response.Listener<LoginResponseDto> success, Response.ErrorListener errorListener, String tag) {
        String url = Constants.VERIFY_CODE_SIGN_UP + "?" +
                Constants.EMAIL_KEY + "=" + emailId + "&" +
                Constants.VERIFICATION_CODE_KEY + "=" + code;
        Log.d(TAG, url);
        GsonRequest<LoginResponseDto> verifyCodeResponseGsonRequest = new GsonRequest<LoginResponseDto>(context, Request.Method.GET, url, LoginResponseDto.class, success, errorListener);
        verifyCodeResponseGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(verifyCodeResponseGsonRequest);
    }

    public void resendSignUpVerificationCode(Context context, String emailId, Response.Listener<SignupResponseDto> success, Response.ErrorListener errorListener, String tag) {
        String url = Constants.RESEND_VERIFY_CODE_SIGN_UP + "?" +
                Constants.EMAIL_KEY + "=" + emailId;
        Log.d(TAG, url);
        GsonRequest<SignupResponseDto> verifyCodeResponseGsonRequest = new GsonRequest<SignupResponseDto>(context, Request.Method.GET, url, SignupResponseDto.class, success, errorListener);
        verifyCodeResponseGsonRequest.setTag(tag);
        VolleyManager.getInstance(context).getRequestQueue().add(verifyCodeResponseGsonRequest);
    }
}
