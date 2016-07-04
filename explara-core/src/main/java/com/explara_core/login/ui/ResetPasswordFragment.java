package com.explara_core.login.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.explara_core.R;
import com.explara_core.common.LoginBaseFragment;
import com.explara_core.common_dto.ProfileResponse;
import com.explara_core.login.LoginScreenManager;
import com.explara_core.login.util.CleverTapHelper;
import com.explara_core.login.util.LoginHelper;
import com.explara_core.utils.AppUtility;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.PreferenceManager;

import org.json.JSONObject;


/**
 * Created by debasishpanda on 22/09/15.
 */
public class ResetPasswordFragment extends LoginBaseFragment {
    private static final String TAG = ResetPasswordFragment.class.getSimpleName();
    // For the alert box
    private MaterialDialog mMaterialAlertDialog;
    // For progress dialog box
    private MaterialDialog mMaterialProgressDialog;

    private EditText mNewPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private Button mResetPasswordButton;

    private static String mEmail = "";
    private String mCode = "";
    private String mNewPassword = "";
    private String mConfirmPassword = "";
    private String mResponseMessage = "";

    public static ResetPasswordFragment getInstance(Intent intent) {
        ResetPasswordFragment verificationCodeFragment = new ResetPasswordFragment();
        String email = intent.getStringExtra(Constants.EMAIL_ID_KEY);
        String code = intent.getStringExtra(Constants.VERIFICATION_CODE_KEY);
        Bundle bundle = new Bundle();
        bundle.putString("emailId", email);
        bundle.putString("code", code);
        verificationCodeFragment.setArguments(bundle);
        return verificationCodeFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_new_password, container, false);
        extractArguments();
        initViews(view);
        setWizRocket();
        googleAnalyticsSendScreenName();
        return view;
    }

    public void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEmail = args.getString("emailId");
            mCode = args.getString("code");
        }
    }

    public void initViews(View view) {

        mNewPasswordEditText = (EditText) view.findViewById(R.id.new_password_edit_text);
        mConfirmPasswordEditText = (EditText) view.findViewById(R.id.confirm_password_edit_text);
        mResetPasswordButton = (Button) view.findViewById(R.id.resetBtn);
        mResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResetPasswordBtnClicked();
            }
        });

    }

    public void showMaterialDialog() {
        mMaterialProgressDialog = new MaterialDialog.Builder(getActivity())
                //.title("Explara Login")
                .content("Please wait..")
                .cancelable(false)
                        //.iconRes(R.drawable.e_logo)
                .progress(true, 0)
                .show();
    }

    public void onResetPasswordBtnClicked() {
        if (LoginScreenManager.getInstance().mAnalyticsListener != null) {
            LoginScreenManager.getInstance().mAnalyticsListener.sendAnEvent(getString(R.string.event_category_resetpasswordscreen_resetpassword),
                    getString(R.string.event_action_resetpasswordscreen_resetpassword), getActivity().getApplication(), getContext());
        }
        //AnalyticsHelper.sendAnEvent(getString(R.string.event_category_resetpasswordscreen_resetpassword), getString(R.string.event_action_resetpasswordscreen_resetpassword), getActivity().getApplication(), getContext());

        if (checkFormData()) {
            mNewPassword = mNewPasswordEditText.getText().toString();
            mConfirmPassword = mConfirmPasswordEditText.getText().toString();
            showMaterialDialog();
            dismissKeyboard();
            resetPassword();
        }
    }

    // Api call for login with username and password
    private void resetPassword() {
        com.explara_core.login.LoginScreenManager.getInstance().resetPassword(getActivity().getApplicationContext(), mCode, mEmail, mNewPassword, mConfirmPassword, new com.explara_core.login.LoginScreenManager.ResetPasswordListener() {
            @Override
            public void onResetPassword(com.explara_core.login.login_dto.ForgotPasswordResponse forgotPasswordResponse) {
                if (getActivity() != null && forgotPasswordResponse != null) {
                    if (forgotPasswordResponse.getStatus().equals(Constants.STATUS_SUCCESS)) {
                        // let user login with new password and stored email
                        loginWithUsernameAndPassword(mEmail, mNewPassword);
                    } else {
                        mMaterialProgressDialog.dismiss();
                        AppUtility.createSnackWithMessage(getActivity().findViewById(R.id.new_password_relative_layout), forgotPasswordResponse.getMessage());
                    }



                   /* if (forgotPasswordResponse.getStatus().equals(Constants.STATUS_ERROR)) {
                        //Constants.createToastWithMessage(this, response.getMessage());

                    } else {
                        mMaterialProgressDialog.dismiss();
                        mMaterialAlertDialog = new MaterialDialog.Builder(getActivity())
                                .title("Alert")
                                .content(loginResponse.getMessage())
                                .positiveText("Ok")
                                .positiveColor(Color.RED)
                                .negativeColor(Color.GRAY)
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        super.onPositive(dialog);
                                        // navigate to login screen
                                        navigateToResetPage();
                                    }

                                    @Override
                                    public void onNegative(MaterialDialog dialog) {
                                        super.onNegative(dialog);
                                    }
                                })
                                .show();

                    }*/
                }
            }

            @Override
            public void onResetPasswordFailed() {
                if (getActivity() != null) {
                    mMaterialProgressDialog.dismiss();
                    Toast.makeText(getActivity(), "Oops! Could not reset password.", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    }

    // Api call for login with username and password
    private void loginWithUsernameAndPassword(String username, String password) {
        com.explara_core.login.LoginScreenManager.getInstance().loginWithUsernameAndPassword(getActivity().getApplicationContext(), username, password, new com.explara_core.login.LoginScreenManager.UserLoginListener() {
            @Override
            public void onUserLogin(com.explara_core.login.login_dto.LoginResponseDto loginResponse) {
                if (getActivity() != null && loginResponse != null) {
                    if (loginResponse.status.equals(Constants.STATUS_ERROR)) {
                        mMaterialProgressDialog.dismiss();
                        Toast.makeText(getActivity(), "Oops! Something gone wrong with login.", Toast.LENGTH_SHORT).show();
                    } else {
                        mResponseMessage = loginResponse.message;

                        // Storing AccessToken into PreferenceManager
                        PreferenceManager.getInstance(getActivity().getApplicationContext()).setAccessToken(loginResponse.access_token);

                        // save user access_token to local settings
                        Constants.ACCESS_TOKEN_VALUE = loginResponse.access_token;
                        Constants.saveDataToLocalSettings(getActivity(), Constants.ACCESS_TOKEN_KEY, Constants.ACCESS_TOKEN_VALUE);

                        // get user details from access_token
//                        getUsersProfileInfo(loginResponse.access_token);
                        saveUserDetailsInLocal(LoginHelper.getUserProfile(loginResponse),CleverTapHelper.EventNames.LOGIN_EVENT, ConstantKeys.FromScreen.LOGIN_SCREEN);
                    }
                }
            }

            @Override
            public void onUserLoginFailed() {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Oops! Login failed.Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    }

    // Api call for fetching userDetails
    public void getUsersProfileInfo(String accessToken) {
        LoginScreenManager.getInstance().getUserDetails(getActivity().getApplicationContext(), accessToken, new LoginScreenManager.UserDetailsListener() {
                    @Override
                    public void onGettingUserDetails(ProfileResponse profileResponse) {
                        if (getActivity() != null) {
                            if (profileResponse.getStatus().equals(Constants.STATUS_SUCCESS)) {
                                saveUserDetailsInLocal(profileResponse, CleverTapHelper.EventNames.LOGIN_EVENT, ConstantKeys.FromScreen.LOGIN_SCREEN);
                            }
                        }
                    }

                    @Override
                    public void onGettingUserDetailsFailed() {
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), "Oops!Fetching user details failed.Please check your internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, TAG

        );
    }

    // Storing details in local
    public void saveUserDetailsInLocal(ProfileResponse profileResponse, JSONObject jsonObject) {

        // Storing profile image,name,Email,Phone no
        PreferenceManager.getInstance(getActivity().getApplicationContext()).setUserImage(profileResponse.getProfile().getProfileImage() + "");
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

//        Log.d("image", "" + profileResponse.getProfile().getProfileImage());
//        Log.d("username", "" + profileResponse.getProfile().getFirstName() + "");
//        Log.d("lastname", "" + profileResponse.getProfile().getLastName());
//        Log.d("mobile", "" + profileResponse.getProfile().getMobileNumber());
//
//        Constants.LOGGED_IN_USER_PROFILE_VALUE = profileResponse.getProfile();
//        Gson gson = new Gson();
//        String jsonString = gson.toJson(Constants.LOGGED_IN_USER_PROFILE_VALUE);
//
//        // Save the information locally
//        Constants.saveDataToLocalSettings(getActivity(), Constants.LOGGED_IN_USER_PROFILE_KEY, jsonString);

        if (mMaterialProgressDialog != null) {
            mMaterialProgressDialog.dismiss();
        }

        mMaterialAlertDialog = new MaterialDialog.Builder(getActivity())
                .title("Alert")
                .content(mResponseMessage)
                .positiveText("Ok")
                .positiveColor(Color.RED)
                .negativeColor(Color.GRAY)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        // navigate to collection page
                        navigateToCollectionsScreen();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }
                })
                .show();

    }

    private void navigateToCollectionsScreen() {//reLook into this

        if (Constants.LOCATION_NAME == null || Constants.LOCATION_NAME.isEmpty()) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME,"com.explara.android.selectCity.ui.SelectLocationActivity"));
            startActivity(intent);
        } else {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME,"com.explara.android.personalizedHome.ui.PersonalizeScreenActivity"));
            startActivity(intent);
        }
        getActivity().finish();
    }

    private boolean checkFormData() {

        boolean valid = true;
        if (mNewPasswordEditText.getText().toString().isEmpty()) {
            Constants.createToastWithMessage(getActivity(), getActivity().getString(R.string.empty_new_password));
            valid = false;
        } else if (mConfirmPasswordEditText.getText().toString().isEmpty()) {
            Constants.createToastWithMessage(getActivity(), getActivity().getString(R.string.empty_confirm_password));
            valid = false;
        } else if (!mNewPasswordEditText.getText().toString().equals(mConfirmPasswordEditText.getText().toString())) {
            Constants.createToastWithMessage(getActivity(), getActivity().getString(R.string.password_not_match));
            valid = false;
        }
        return valid;

    }

    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void setWizRocket() {
//        if (Constants.WIZ_ROCKET_API != null) Constants.WIZ_ROCKET_API.event.push(TAG);
    }

    private void googleAnalyticsSendScreenName(){
        if (LoginScreenManager.getInstance().mAnalyticsListener != null) {
            LoginScreenManager.getInstance().mAnalyticsListener.sendScreenName(getString(R.string.reset_password), getActivity().getApplication(), getContext());
        }
        //AnalyticsHelper.sendScreenName(getString(R.string.reset_password), getActivity().getApplication(), getContext());
    }

    @Override
    public void refresh() {

    }


}

