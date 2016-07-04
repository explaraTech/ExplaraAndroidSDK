package com.explara_core.login;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.explara_core.common.BaseManager;
import com.explara_core.common_dto.ProfileResponse;
import com.explara_core.communication.BaseCommunicationManager;
import com.explara_core.login.io.LoginConnectionManager;
import com.explara_core.login.login_dto.ForgotPasswordResponse;
import com.explara_core.login.login_dto.LoginResponseDto;
import com.explara_core.login.login_dto.SignupResponseDto;
import com.explara_core.login.login_dto.UserDetailsDto;
import com.explara_core.utils.Log;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by debasishpanda on 22/09/15.
 */
public class LoginScreenManager extends BaseManager {

    private static final String TAG = LoginScreenManager.class.getSimpleName();
    public static LoginScreenManager mLoginManager;
    public LoginResponseDto mLoginResponse;
    public ProfileResponse mProfileResponse;
    public ForgotPasswordResponse mForgotPasswordResponse;
    public SignupResponseDto mSignupResponseDto;
    public BaseCommunicationManager.AnalyticsListener mAnalyticsListener;
    public JSONObject mJsonObject = new JSONObject();

    public void initAnalyticsListener(BaseCommunicationManager.AnalyticsListener analyticsListener) {
        this.mAnalyticsListener = analyticsListener;
    }

    private LoginScreenManager() {

    }

    public static synchronized LoginScreenManager getInstance() {
        if (mLoginManager == null) {
            Log.d(TAG, "" + TAG);
            mLoginManager = new LoginScreenManager();
        }
        return mLoginManager;
    }

    public interface CheckAccountExistsListener{
        void onAccountExistChecked(LoginResponseDto loginResponseDto);
        void onAccountExistCheckFailed();
    }

    public interface UserLoginListener {
        void onUserLogin(LoginResponseDto loginResponse);

        void onUserLoginFailed();
    }

    public interface UserSignupListener {
        void onUserSignup(SignupResponseDto signupResponseDto);

        void onUserSignupFailed();
    }

    public interface UserSignupWithAccessTokenListener {
        void onUserSignup(LoginResponseDto loginResponseDto);

        void onUserSignupFailed();
    }

    public interface UserSignupWithOutVerifyListener {
        void onUserSignup(SignupResponseDto signupResponseDto);

        void onUserSignupFailed();
    }

    public interface FbLoginListener {
        void onFbLogin(LoginResponseDto loginResponse);

        void onFbLoginFailed();
    }

    public interface UserDetailsListener {
        void onGettingUserDetails(ProfileResponse profileResponse);

        void onGettingUserDetailsFailed();
    }

    public interface SendVerificationCodeListener {
        void onVerificationCodeSent(LoginResponseDto loginResponse);

        void onVerificationCodeSentFailed();
    }

    public interface VerifyCodeListener {
        void onCodeVerified(LoginResponseDto loginResponse);

        void onCodeVerifyFailed();
    }

    public interface VerifySignUpCodeListener {
        void onCodeVerified(LoginResponseDto loginResponse);

        void onCodeVerifyFailed(VolleyError error);
    }

    public interface ReSendSignUpCodeListener {
        void onCodeResent(SignupResponseDto signupResponseDto);

        void onCodeResendFailed();
    }

    public interface ResetPasswordListener {
        void onResetPassword(ForgotPasswordResponse forgotPasswordResponse);

        void onResetPasswordFailed();
    }



    public void loginWithFacebook(Context context, String fbAccessToken, FbLoginListener fbLoginListener, String tag) {
        LoginConnectionManager loginConnectionManager = new LoginConnectionManager();
        loginConnectionManager.loginWithFacebook(context, fbAccessToken, fbLoginSuccessListener(fbLoginListener), fbLoginErrorListener(fbLoginListener), tag);
    }

    public void getUserDetails(Context context, String accessToken, UserDetailsListener userDetailsListener, String tag) {
        LoginConnectionManager loginConnectionManager = new LoginConnectionManager();
        loginConnectionManager.getUserDetails(context, accessToken, userDetailsSuccessListener(userDetailsListener), userDetailsErrorListener(userDetailsListener), tag);
    }

    public void loginWithUsernameAndPassword(Context context, String username, String password, UserLoginListener userLoginListener, String tag) {
        LoginConnectionManager loginConnectionManager = new LoginConnectionManager();
        loginConnectionManager.loginWithUsernameAndPassword(context, username, password, userLoginSuccessListener(userLoginListener), userLoginErrorListener(userLoginListener), tag);
    }

    public void checkAccountExistWithFbAccessToken(Context context,String fbAccessToken,CheckAccountExistsListener checkAccountExistsListener, String tag) {
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.fbAccessToken = fbAccessToken;
        // Pass null when calling from Explara App
        userDetailsDto.fbAppId = null;
        Gson gson = new Gson();
        String jsonStr = gson.toJson(userDetailsDto);
        Log.i("JsonUrl", jsonStr);

        LoginConnectionManager loginConnectionManager = new LoginConnectionManager();
        loginConnectionManager.checkAccountExistWithFbAccessToken(context, jsonStr, checkAccountExistSuccessListener(checkAccountExistsListener), checkAccountExistErrorListener(checkAccountExistsListener), tag);
    }

    public void signupWithUsernameAndPassword(Context context, String username, String password, String mobileNo, String fullName, UserSignupListener userSignupListener, String tag) {
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.email = username;
        userDetailsDto.password = password;
        userDetailsDto.phoneNo = mobileNo;
        userDetailsDto.name = fullName;
        Gson gson = new Gson();
        String jsonStr = gson.toJson(userDetailsDto);
        Log.i("JsonUrl",jsonStr);

        LoginConnectionManager loginConnectionManager = new LoginConnectionManager();
        loginConnectionManager.singupWithUsernameAndPassword(context, jsonStr, userSignupSuccessListener(userSignupListener), userSignupErrorListener(userSignupListener), tag);
    }

    public void signupWithFbAccessToken(Context context, String username, String fbAccessToken, String mobileNo, String fullName, UserSignupWithAccessTokenListener userSignupWithAccessTokenListener, String tag) {
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.email = username;
        userDetailsDto.fbAccessToken = fbAccessToken;
        // Pass null when calling from Explara App
        userDetailsDto.fbAppId = null;
        userDetailsDto.phoneNo = mobileNo;
        userDetailsDto.name = fullName;
        Gson gson = new Gson();
        String jsonStr = gson.toJson(userDetailsDto);
        Log.i("JsonUrl",jsonStr);

        LoginConnectionManager loginConnectionManager = new LoginConnectionManager();
        loginConnectionManager.signupWithFbAccessToken(context, jsonStr, userSignupWithAccessTokenSuccessListener(userSignupWithAccessTokenListener), userSignupWithAccessTokenErrorListener(userSignupWithAccessTokenListener), tag);
    }

    public void signupWithUsernameAndPasswordWithoutVerify(Context context, String username, String password, String mobileNo, String fullName, UserSignupWithOutVerifyListener userSignupWithOutVerifyListener, String tag) {
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.email = username;
        userDetailsDto.password = password;
        userDetailsDto.phoneNo = mobileNo;
        userDetailsDto.name = fullName;
        userDetailsDto.sendVerification = "no";
        Gson gson = new Gson();
        String jsonStr = gson.toJson(userDetailsDto);
        Log.i("JsonUrl",jsonStr);

        LoginConnectionManager loginConnectionManager = new LoginConnectionManager();
        loginConnectionManager.signupWithUsernameAndPasswordWithoutVerify(context, jsonStr, userSignupWithOutVerifySuccessListener(userSignupWithOutVerifyListener), userSignupWithOutVerifyErrorListener(userSignupWithOutVerifyListener), tag);
    }

    public void sendVerificationCode(Context context, String email, SendVerificationCodeListener sendVerificationCodeListener, String tag) {
        LoginConnectionManager loginConnectionManager = new LoginConnectionManager();
        loginConnectionManager.sendVerificationCode(context, email, verificationCodeSentSuccessListener(sendVerificationCodeListener), verificationCodeSentErrorListener(sendVerificationCodeListener), tag);
    }

    public void verifyCode(Context context, String code, String emailId, VerifyCodeListener verifyCodeListener, String tag) {
        LoginConnectionManager loginConnectionManager = new LoginConnectionManager();
        loginConnectionManager.verifyCode(context, code, emailId, codeVerifySuccessListener(verifyCodeListener), codeVerifyErrorListener(verifyCodeListener), tag);
    }

    public void verifySignUpCode(Context context, String code, String emailId, VerifySignUpCodeListener verifySignUpCodeListener, String tag) {
        LoginConnectionManager loginConnectionManager = new LoginConnectionManager();
        loginConnectionManager.verifySignUpCode(context, code, emailId, signUpCodeVerifySuccessListener(verifySignUpCodeListener), signUpCodeVerifyErrorListener(verifySignUpCodeListener), tag);
    }

    public void resendSignUpVerificationCode(Context context,String emailId, ReSendSignUpCodeListener reSendSignUpCodeListener, String tag) {
        LoginConnectionManager loginConnectionManager = new LoginConnectionManager();
        loginConnectionManager.resendSignUpVerificationCode(context, emailId, signUpCodeResendSuccessListener(reSendSignUpCodeListener), signUpCodeResendErrorListener(reSendSignUpCodeListener), tag);
    }

    public void resetPassword(Context context, String code, String emailId, String newPassword, String confirmPassword, ResetPasswordListener resetPasswordListener, String tag) {
        LoginConnectionManager loginConnectionManager = new LoginConnectionManager();
        loginConnectionManager.resetPassword(context, code, emailId, newPassword, confirmPassword, resetPasswordSuccessListener(resetPasswordListener), resetPasswordErrorListener(resetPasswordListener), tag);
    }

    // Reset password -  success
    private Response.Listener<ForgotPasswordResponse> resetPasswordSuccessListener(final ResetPasswordListener resetPasswordListener) {
        return new Response.Listener<ForgotPasswordResponse>() {
            @Override
            public void onResponse(ForgotPasswordResponse response) {
                mForgotPasswordResponse = response;
                if (resetPasswordListener != null) {
                    resetPasswordListener.onResetPassword(response);
                }
            }
        };
    }

    // Reset password -  Failure
    private Response.ErrorListener resetPasswordErrorListener(final ResetPasswordListener resetPasswordListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (resetPasswordListener != null) {
                    resetPasswordListener.onResetPasswordFailed();
                }
            }
        };
    }

    // check account exist with fbAccessToken
    private Response.Listener<LoginResponseDto> checkAccountExistSuccessListener(final CheckAccountExistsListener checkAccountExistsListener) {
        return new Response.Listener<LoginResponseDto>() {
            @Override
            public void onResponse(LoginResponseDto response) {
                mLoginResponse = response;
                if (checkAccountExistsListener != null) {
                    checkAccountExistsListener.onAccountExistChecked(response);
                }
            }
        };
    }

    // Reset password -  Failure
    private Response.ErrorListener checkAccountExistErrorListener(final CheckAccountExistsListener checkAccountExistsListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (checkAccountExistsListener != null) {
                    checkAccountExistsListener.onAccountExistCheckFailed();
                }
            }
        };
    }

    // Resend Signup code -  success
    private Response.Listener<SignupResponseDto> signUpCodeResendSuccessListener(final ReSendSignUpCodeListener reSendSignUpCodeListener) {
        return new Response.Listener<SignupResponseDto>() {
            @Override
            public void onResponse(SignupResponseDto response) {
                mSignupResponseDto = response;
                if (reSendSignUpCodeListener != null) {
                    reSendSignUpCodeListener.onCodeResent(response);
                }
            }
        };
    }

    // Resend Signup code -  Failure
    private Response.ErrorListener signUpCodeResendErrorListener(final ReSendSignUpCodeListener reSendSignUpCodeListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (reSendSignUpCodeListener != null) {
                    reSendSignUpCodeListener.onCodeResendFailed();
                }
            }
        };
    }

    // Verify Signup code -  success
    private Response.Listener<LoginResponseDto> signUpCodeVerifySuccessListener(final VerifySignUpCodeListener verifySignUpCodeListener) {
        return new Response.Listener<LoginResponseDto>() {
            @Override
            public void onResponse(LoginResponseDto response) {
                mLoginResponse = response;
                if (verifySignUpCodeListener != null) {
                    verifySignUpCodeListener.onCodeVerified(response);
                }
            }
        };
    }

    // Verify Signup code -  Failure
    private Response.ErrorListener signUpCodeVerifyErrorListener(final VerifySignUpCodeListener verifySignUpCodeListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (verifySignUpCodeListener != null) {
                    verifySignUpCodeListener.onCodeVerifyFailed(error);
                }
            }
        };
    }

    // Verify code -  success
    private Response.Listener<LoginResponseDto> codeVerifySuccessListener(final VerifyCodeListener verifyCodeListener) {
        return new Response.Listener<LoginResponseDto>() {
            @Override
            public void onResponse(LoginResponseDto response) {
                mLoginResponse = response;
                if (verifyCodeListener != null) {
                    verifyCodeListener.onCodeVerified(response);
                }
            }
        };
    }

    // Verify code -  Failure
    private Response.ErrorListener codeVerifyErrorListener(final VerifyCodeListener verifyCodeListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (verifyCodeListener != null) {
                    verifyCodeListener.onCodeVerifyFailed();
                }
            }
        };
    }

    // Send verification code -  success
    private Response.Listener<LoginResponseDto> verificationCodeSentSuccessListener(final SendVerificationCodeListener sendVerificationCodeListener) {
        return new Response.Listener<LoginResponseDto>() {
            @Override
            public void onResponse(LoginResponseDto response) {
                mLoginResponse = response;
                if (sendVerificationCodeListener != null) {
                    sendVerificationCodeListener.onVerificationCodeSent(response);
                }
            }
        };
    }

    // Send verification code -  Failure
    private Response.ErrorListener verificationCodeSentErrorListener(final SendVerificationCodeListener sendVerificationCodeListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (sendVerificationCodeListener != null) {
                    sendVerificationCodeListener.onVerificationCodeSentFailed();
                }
            }
        };
    }

    // Signup with usr and pwd with out verify -  success
    private Response.Listener<SignupResponseDto> userSignupWithOutVerifySuccessListener(final UserSignupWithOutVerifyListener userSignupWithOutVerifyListener) {
        return new Response.Listener<SignupResponseDto>() {
            @Override
            public void onResponse(SignupResponseDto response) {
                mSignupResponseDto = response;
                if (userSignupWithOutVerifyListener != null) {
                    userSignupWithOutVerifyListener.onUserSignup(response);
                }
            }
        };
    }

    // Signup with usr and pwd with out verify -  Failure
    private Response.ErrorListener userSignupWithOutVerifyErrorListener(final UserSignupWithOutVerifyListener userSignupWithOutVerifyListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (userSignupWithOutVerifyListener != null) {
                    userSignupWithOutVerifyListener.onUserSignupFailed();
                }
            }
        };
    }

    // Signup with access token -  success
    private Response.Listener<LoginResponseDto> userSignupWithAccessTokenSuccessListener(final UserSignupWithAccessTokenListener userSignupWithAccessTokenListener) {
        return new Response.Listener<LoginResponseDto>() {
            @Override
            public void onResponse(LoginResponseDto response) {
                mLoginResponse = response;
                if (userSignupWithAccessTokenListener != null) {
                    userSignupWithAccessTokenListener.onUserSignup(response);
                }
            }
        };
    }

    // Signup with access token -  Failure
    private Response.ErrorListener userSignupWithAccessTokenErrorListener(final UserSignupWithAccessTokenListener userSignupWithAccessTokenListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (userSignupWithAccessTokenListener != null) {
                    userSignupWithAccessTokenListener.onUserSignupFailed();
                }
            }
        };
    }

    // Signup with usr and pwd -  success
    private Response.Listener<SignupResponseDto> userSignupSuccessListener(final UserSignupListener userSignupListener) {
        return new Response.Listener<SignupResponseDto>() {
            @Override
            public void onResponse(SignupResponseDto response) {
                mSignupResponseDto = response;
                if (userSignupListener != null) {
                    userSignupListener.onUserSignup(response);
                }
            }
        };
    }

    // Signup with usr and pwd -  Failure
    private Response.ErrorListener userSignupErrorListener(final UserSignupListener userSignupListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (userSignupListener != null) {
                    userSignupListener.onUserSignupFailed();
                }
            }
        };
    }


    // Login with usr and pwd -  success
    private Response.Listener<LoginResponseDto> userLoginSuccessListener(final UserLoginListener userLoginListener) {
        return new Response.Listener<LoginResponseDto>() {
            @Override
            public void onResponse(LoginResponseDto response) {
                mLoginResponse = response;
                if (userLoginListener != null) {
                    userLoginListener.onUserLogin(response);
                }
            }
        };
    }

    // Login with usr and pwd -  Failure
    private Response.ErrorListener userLoginErrorListener(final UserLoginListener userLoginListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (userLoginListener != null) {
                    userLoginListener.onUserLoginFailed();
                }
            }
        };
    }

    // Login with facebook - success
    private Response.Listener<LoginResponseDto> fbLoginSuccessListener(final FbLoginListener fbLoginListener) {
        return new Response.Listener<LoginResponseDto>() {
            @Override
            public void onResponse(LoginResponseDto response) {
                mLoginResponse = response;
                if (fbLoginListener != null) {
                    fbLoginListener.onFbLogin(response);
                }
            }
        };
    }

    // Login with facebook - failure
    private Response.ErrorListener fbLoginErrorListener(final FbLoginListener fbLoginListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (fbLoginListener != null) {
                    fbLoginListener.onFbLoginFailed();
                }
            }
        };
    }

    // Getting user details - success
    private Response.Listener<ProfileResponse> userDetailsSuccessListener(final UserDetailsListener userDetailsListener) {
        return new Response.Listener<ProfileResponse>() {
            @Override
            public void onResponse(ProfileResponse response) {
                mProfileResponse = response;
                if (userDetailsListener != null) {
                    userDetailsListener.onGettingUserDetails(response);
                }
            }
        };
    }

    // Getting user details - Failure
    private Response.ErrorListener userDetailsErrorListener(final UserDetailsListener userDetailsListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (userDetailsListener != null) {
                    userDetailsListener.onGettingUserDetailsFailed();
                }
            }
        };
    }

    @Override
    public void cleanUp() {
        mLoginResponse = null;
        mProfileResponse = null;
        mForgotPasswordResponse = null;
        mSignupResponseDto = null;
    }

}
