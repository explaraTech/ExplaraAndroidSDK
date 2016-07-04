package com.explara_core.login.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.explara_core.R;
import com.explara_core.common.LoginBaseFragment;
import com.explara_core.login.LoginScreenManager;
import com.explara_core.login.login_dto.SignupResponseDto;
import com.explara_core.login.util.CleverTapHelper;
import com.explara_core.utils.AppUtility;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.PreferenceManager;
import com.explara_core.utils.UiValidatorUtil;
import com.explara_core.utils.Utility;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by debasishpanda on 22/09/15.
 */
public class SignUpTabFragment extends LoginBaseFragment {
    private static final String TAG = SignUpTabFragment.class.getSimpleName();
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mMobileEditText;
    private EditText mFullNameEditText;
    private CheckBox mAgreement;
    private CallbackManager mCallbackManager;
    private MaterialDialog mMaterialDialog;
    private TextView mUserSignupButton;
    private String mUsername = "";
    private String mPassword = "";
    private String mFbAccessToken;
    private TextInputLayout mUsernameTextInput;
    private TextInputLayout mPasswordTextInput;
    private TextInputLayout mMobileTextInput;
    private TextInputLayout mFullNameTextInput;
    private LoginButton mFBLoginButtonSingUpPage;
    private TextView mTermsNConditions;
    boolean mSignupProcessStarted = false;


    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_tab_signup, container, false);
        initViews(view);
        setWizRocket();
        setupFacebookLogin();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void initViews(View view) {
        mFBLoginButtonSingUpPage = (LoginButton) view.findViewById(R.id.facebook_login_button_signup_page);
        // mFBLoginButtonSingUpPage.setText("Signup with facebook");
        mUsernameEditText = (EditText) view.findViewById(R.id.usernameEditText);
        mPasswordEditText = (EditText) view.findViewById(R.id.passwordEditText);
        mMobileEditText = (EditText) view.findViewById(R.id.mobileNoEditText);
        mFullNameEditText = (EditText) view.findViewById(R.id.fullNameEditText);
        mAgreement = (CheckBox) view.findViewById(R.id.chkIos);

      /*mAgreement.setButtonDrawable(R.drawable.terms_check_active);
        mAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mAgreement.setButtonDrawable(R.drawable.terms_check_active);
                } else {
                    mAgreement.setButtonDrawable(R.drawable.terms_check_inactive);
                }
            }
        }); */

        mUserSignupButton = (TextView) view.findViewById(R.id.signup_btn);
        mUsernameTextInput = (TextInputLayout) view.findViewById(R.id.username_text_input);
        mPasswordTextInput = (TextInputLayout) view.findViewById(R.id.password_text_input);
        mMobileTextInput = (TextInputLayout) view.findViewById(R.id.mobile_text_input);
        mFullNameTextInput = (TextInputLayout) view.findViewById(R.id.fullname_text_input);
        mTermsNConditions = (TextView) view.findViewById(R.id.terms_n_conditions);

        /*String text = "By signing up, you agree to"+""+"<font color='#DB4344'>Terms of Use </font>";
        mTermsNConditions.setText(text);*/

        String text1 = "<font color=#edeaeb>By signing up, you agree to</font> <font color=#fcbf08>Terms of Use</font>";
        mTermsNConditions.setText(Html.fromHtml(text1));

        onButtonClickCalls();
    }

/*    public void showMaterialDialog() { recheck
        mMaterialDialog = new MaterialDialog.Builder(getActivity())
                //.title("Explara Login")
                .content("Please wait..")
                .cancelable(false)
                        //.iconRes(R.drawable.e_logo)
                .progress(true, 0)
                .show();
    }*/

    public void onButtonClickCalls() {
       /* mFbLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "facebook btn clicked", Toast.LENGTH_SHORT).show();
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("email", "public_profile", "user_friends"));
            }
        }); */

        mTermsNConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Relook into this
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME,"com.explara.android.termsofservice.TearmsActivity"));
                startActivity(intent);
            }
        });

        mUserSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpBtnClicked();
            }
        });
    }

    public void signUpBtnClicked() {
        if (Utility.isNetworkAvailable(getActivity())) {
            if (LoginScreenManager.getInstance().mAnalyticsListener != null) {
                LoginScreenManager.getInstance().mAnalyticsListener.sendAnEvent(getString(R.string.event_category_signupscreen_signup),
                        getString(R.string.event_action_signupscreen_signup), getActivity().getApplication(), getContext());
            }

            //AnalyticsHelper.sendAnEvent(getString(R.string.event_category_signupscreen_signup), getString(R.string.event_action_signupscreen_signup), getActivity().getApplication(), getContext());

            if (!mSignupProcessStarted) {
                if (checkForSingupData()) {
                    mSignupProcessStarted = true;
                    showMaterialDialog();
                    dismissKeyboard();

                    mUsername = mUsernameEditText.getText().toString().trim();
                    PreferenceManager.getInstance(getContext()).setEmail(mUsername);
                    mPassword = mPasswordEditText.getText().toString();
                    String mobileNo = mMobileEditText.getText().toString();
                    String fullName = mFullNameEditText.getText().toString().trim();
                    String password_md5_hash = Constants.generateMD5(mPassword);

                    if (!password_md5_hash.isEmpty()) {
                        signUpUserWithVerificationCode(mUsername, password_md5_hash, mobileNo, fullName);
                    }
                }
            }
        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.internet_check_msg), Toast.LENGTH_SHORT).show();
        }
    }

    // Api call for singup with username and password
    private void signUpUserWithVerificationCode(final String username, final String password, String mobileNo, String fullName) {
        LoginScreenManager.getInstance().signupWithUsernameAndPassword(getActivity().getApplicationContext(), username, password, mobileNo, fullName,
                new com.explara_core.login.LoginScreenManager.UserSignupListener() {
                    @Override
                    public void onUserSignup(SignupResponseDto signupResponseDto) {
                        if (getActivity() != null && signupResponseDto != null) {
                            mSignupProcessStarted = false;
                           /* if (mMaterialDialog != null && mMaterialDialog.isShowing()) {
                                mMaterialDialog.dismiss();
                            }*/

                            dismissMaterialDialog();

                            if (signupResponseDto.getStatus().equals(Constants.STATUS_ERROR)) {
                                mUsernameTextInput.setError(signupResponseDto.getMessage());
                                mPasswordTextInput.setError(null);
                                mMobileTextInput.setError(null);
                                mFullNameTextInput.setError(null);
                            } else {
                                Toast.makeText(getActivity(), signupResponseDto.getMessage(), Toast.LENGTH_SHORT).show();
                                lunchSignUpVerificationCode(username);
                            }
                        }
                    }

                    @Override
                    public void onUserSignupFailed() {
                        mSignupProcessStarted = false;
                       /* if (mMaterialDialog != null && mMaterialDialog.isShowing()) {
                            mMaterialDialog.dismiss();
                        }*/
                        dismissMaterialDialog();
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), "Oops! Signup failed.Please check your internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, TAG);
    }

    private boolean checkForSingupData() {

        boolean valid = true;

        if (mUsernameEditText.getText().toString().trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mUsernameEditText.getText().toString()).matches()) {
            //mUsernameTextInput.setErrorEnabled(true);
            mUsernameTextInput.setError(getActivity().getString(R.string.empty_username));
            //mUsernameEditText.getBackground().setColorFilter(getResources().getColor(R.color.style_color_default_red), PorterDuff.Mode.SRC_ATOP);
            valid = false;
        } else {
            mUsernameTextInput.setError(null);
        }

        if (mPasswordEditText.getText().toString().trim().isEmpty()) {
            // mPasswordTextInput.setErrorEnabled(true);
            mPasswordTextInput.setError(getActivity().getString(R.string.empty_password));
            // mPasswordEditText.getBackground().setColorFilter(getResources().getColor(R.color.style_color_default_red), PorterDuff.Mode.SRC_ATOP);
            valid = false;
        } else {
            mPasswordTextInput.setError(null);
        }

        if (mMobileEditText.getText().toString().isEmpty()) {
            // mMobileTextInput.setErrorEnabled(true);
            mMobileTextInput.setError(getActivity().getString(R.string.empty_mobile_number));
            //mMobileEditText.getBackground().setColorFilter(getResources().getColor(R.color.style_color_default_red), PorterDuff.Mode.SRC_ATOP);
            valid = false;
        } else {
            mMobileTextInput.setError(null);
        }

        if (mFullNameEditText.getText().toString().isEmpty()) {
            // mFullNameTextInput.setErrorEnabled(true);
            mFullNameTextInput.setError(getActivity().getString(R.string.empty_full_name));
            // mFullNameEditText.getBackground().setColorFilter(getResources().getColor(R.color.style_color_default_red), PorterDuff.Mode.SRC_ATOP);
            valid = false;
        } else {
            mFullNameTextInput.setError(null);
        }

        if (!(UiValidatorUtil.isPhoneNumberValid(mMobileEditText.getText().toString()))) {
            // mMobileTextInput.setErrorEnabled(true);
            mMobileTextInput.setError(getActivity().getString(R.string.mobile_is_number));
            //mMobileEditText.getBackground().setColorFilter(getResources().getColor(R.color.style_color_default_red), PorterDuff.Mode.SRC_ATOP);
            valid = false;
        } else {
            mMobileTextInput.setError(null);
        }

        if (!mAgreement.isChecked()) {
            //relook the id
            AppUtility.createSnackWithMessage(getActivity().findViewById(R.id.forgot_password_screen_fragment_container), getActivity().getString(R.string.check_aggrement));
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
        if (LoginScreenManager.getInstance().mAnalyticsListener != null) {
            LoginScreenManager.getInstance().mAnalyticsListener.sendScreenName(getString(R.string.login_Screen), getActivity().getApplication(), getContext());
        }
        //AnalyticsHelper.sendScreenName(getString(R.string.login_Screen), getActivity().getApplication(), getContext());
    }

    public void setupFacebookLogin() {
        if (LoginScreenManager.getInstance().mAnalyticsListener != null) {
            LoginScreenManager.getInstance().mAnalyticsListener.sendAnEvent(getString(R.string.event_category_loginscreen_fbLogin),
                    getString(R.string.event_action_loginscreen_fbLogin), getActivity().getApplication(), getContext());
        }
        //AnalyticsHelper.sendAnEvent(getString(R.string.event_category_loginscreen_fbLogin), getString(R.string.event_action_loginscreen_fbLogin), getActivity().getApplication(), getContext());

        // For facebook login button
        mFBLoginButtonSingUpPage.setReadPermissions("public_profile", "user_friends", "email");
        mFBLoginButtonSingUpPage.setFragment(getParentFragment());
        mCallbackManager = CallbackManager.Factory.create();
        mFBLoginButtonSingUpPage.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (((LoginNewFragment) getParentFragment()).mViewPager.getCurrentItem() == 0) {
                    showMaterialDialog();
                    mFbAccessToken = loginResult.getAccessToken().getToken();
                    sendSignUpWithFbToCleverTap(loginResult.getAccessToken(), TAG, CleverTapHelper.EventNames.SIGN_UP_EVENT,  ConstantKeys.FromScreen.SIGNUP_SCREEN_FACEBOOK); //reLook
                }
            }

            @Override
            public void onCancel() {
                //Toast.makeText(getActivity(), "Login cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                //Toast.makeText(getActivity(), "Login error occured", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        LoginManager.getInstance().logOut();
    }

    @Override
    public void refresh() {

    }

}

