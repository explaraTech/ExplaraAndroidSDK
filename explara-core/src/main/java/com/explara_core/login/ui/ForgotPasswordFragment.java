package com.explara_core.login.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.explara_core.login.LoginScreenManager;
import com.explara_core.utils.AppUtility;
import com.explara_core.utils.Constants;

/**
 * Created by debasishpanda on 22/09/15.
 */
public class ForgotPasswordFragment extends LoginBaseFragment {
    private static final String TAG = ForgotPasswordFragment.class.getSimpleName();
    // For the alert box
    private MaterialDialog mMaterialAlertDialog;
    // For progress dialog box
    private MaterialDialog mMaterialProgressDialog;
    private EditText mEmailEditText;
    private EditText mMobileNumberEditText;
    private Button mForgotPasswordButton;
    private String mEmail = "";
    private boolean isCodeSentToEmail = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_forgot_password, container, false);
        initViews(view);
        setupWizRocketAndGoogleAnalytics();
        return view;
    }

    public void initViews(View view) {

        mEmailEditText = (EditText) view.findViewById(R.id.email_edit_text);
        mMobileNumberEditText = (EditText) view.findViewById(R.id.mobile_number_edit_text);
        mForgotPasswordButton = (Button) view.findViewById(R.id.forgot_password_btn);
        mForgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onForgotPasswordBtnClicked();
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

    public void onForgotPasswordBtnClicked() {
        googleAnalyticsSendAnEvent();
        if (!mEmail.equals(mEmailEditText.getText().toString())) {
            isCodeSentToEmail = false;
        }
        if (checkFormData() && !isCodeSentToEmail) {
            mEmail = mEmailEditText.getText().toString();
            showMaterialDialog();
            dismissKeyboard();
            SendVerificationCode();
        }
    }

    // Api call for login with username and password
    private void SendVerificationCode() {
        com.explara_core.login.LoginScreenManager.getInstance().sendVerificationCode(getActivity().getApplicationContext(), mEmail, new com.explara_core.login.LoginScreenManager.SendVerificationCodeListener() {
            @Override
            public void onVerificationCodeSent(com.explara_core.login.login_dto.LoginResponseDto loginResponse) {
                if (getActivity() != null && loginResponse != null) {
                    if (loginResponse.status.equals(Constants.STATUS_ERROR)) {
                        //Constants.createToastWithMessage(this, response.getMessage());
                        mMaterialProgressDialog.dismiss();
                        AppUtility.createSnackWithMessage(getActivity().findViewById(R.id.forgot_password_relative_layout), loginResponse.message);
                    } else {
                        isCodeSentToEmail = true;
                        mMaterialProgressDialog.dismiss();
                        mMaterialAlertDialog = new MaterialDialog.Builder(getActivity())
                                .title("Alert")
                                .content(loginResponse.message)
                                .positiveText("Ok")
                                .positiveColor(Color.RED)
                                .negativeColor(Color.GRAY)
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        super.onPositive(dialog);
                                        // navigate to login screen
                                        navigateToVerificationCodeScreen();
                                    }

                                    @Override
                                    public void onNegative(MaterialDialog dialog) {
                                        super.onNegative(dialog);
                                    }
                                })
                                .show();

                    }

                }
            }

            @Override
            public void onVerificationCodeSentFailed() {
                if (getActivity() != null) {
                    mMaterialProgressDialog.dismiss();
                    Toast.makeText(getActivity(), "Oops! Could not sent verification code.", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    }

    private boolean checkFormData() {

        boolean valid = true;
        if (mEmailEditText.getText().toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailEditText.getText().toString()).matches()) {
            Constants.createToastWithMessage(getActivity(), getActivity().getString(R.string.empty_username));
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

    public void setupWizRocketAndGoogleAnalytics() {
        //if (Constants.WIZ_ROCKET_API != null) Constants.WIZ_ROCKET_API.event.push(TAG);
        if (LoginScreenManager.getInstance().mAnalyticsListener != null) {
            LoginScreenManager.getInstance().mAnalyticsListener.sendScreenName(getString(R.string.forgot_password_screen), getActivity().getApplication(), getContext());
        }
        //AnalyticsHelper.sendScreenName(getString(R.string.forgot_password_screen), getActivity().getApplication(), getContext());
    }


    private void navigateToVerificationCodeScreen() {
        Intent i = new Intent(getActivity(), VerificationCodeWithOutNavActivity.class);
        i.putExtra(Constants.EMAIL_ID_KEY, mEmail);
        startActivity(i);
        getActivity().finish();
    }

    @Override
    public void refresh() {

    }

    private void googleAnalyticsSendAnEvent(){
        if (LoginScreenManager.getInstance().mAnalyticsListener != null) {
            LoginScreenManager.getInstance().mAnalyticsListener.sendAnEvent(getString(R.string.event_category_forgotpasswordscreen_forgotpassword),
                    getString(R.string.event_action_forgotpasswordscreen_forgotpassword), getActivity().getApplication(), getContext());
        }

        //AnalyticsHelper.sendAnEvent(getString(R.string.event_category_forgotpasswordscreen_forgotpassword), getString(R.string.event_action_forgotpasswordscreen_forgotpassword), getActivity().getApplication(), getContext());
    }
}

