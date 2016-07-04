package com.explara_core.login.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.explara_core.R;
import com.explara_core.common.LoginBaseFragment;
import com.explara_core.login.LoginScreenManager;
import com.explara_core.login.util.CleverTapHelper;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


/**
 * Created by debasishpanda on 22/09/15.
 */
public class LoginTabFragment extends LoginBaseFragment {

    private static final String TAG = LoginTabFragment.class.getSimpleName();
    private TextView mUserLoginButton;
    private EditText mUsername;
    private EditText mPassword;
    private CallbackManager mCallbackManager;
    private TextView mForgetPassword;
    //private MaterialDialog mMaterialDialog;
    private TextInputLayout mUsernameTextInput;
    private TextInputLayout mPasswordTextInput;
    boolean loginProcessStarted = false;
    private LoginButton mFbLoginButtonLoginPage;
    private String mFbAccessToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*public interface LoginResponseListener {
        void onResponse(LoginResponseDto loginResponseDto);
    }

    private LoginResponseListener mLoginResponseListener;*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tab_login, container, false);
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
        mFbLoginButtonLoginPage = (LoginButton) view.findViewById(R.id.facebook_login_button_login_page);

        //  mFbLoginButtonLoginPage.setText("Login with facebook");
        mUserLoginButton = (TextView) view.findViewById(R.id.login_btn);
        mUsername = (EditText) view.findViewById(R.id.usernameEditText);
        mPassword = (EditText) view.findViewById(R.id.passwordEditText);
        mForgetPassword = (TextView) view.findViewById(R.id.forgot_password);
        mUsernameTextInput = (TextInputLayout) view.findViewById(R.id.username_text_input);
        mPasswordTextInput = (TextInputLayout) view.findViewById(R.id.password_text_input);

        onButtonClickCalls();
    }

    public void onButtonClickCalls() {
       /* mFbLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "facebook btn clicked", Toast.LENGTH_SHORT).show();
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("email", "public_profile", "user_friends"));
            }
        }); */

        mUserLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtnClicked();
            }
        });

        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginScreenManager.getInstance().mAnalyticsListener != null) {
                    LoginScreenManager.getInstance().mAnalyticsListener.sendAnEvent(getString(R.string.event_category_loginscreen_forgotPassword),
                            getString(R.string.event_action_loginscreen_forgotPasswordButton), getActivity().getApplication(), getContext());
                }
                //AnalyticsHelper.sendAnEvent(getString(R.string.event_category_loginscreen_forgotPassword), getString(R.string.event_action_loginscreen_forgotPasswordButton), getActivity().getApplication(), getContext());

                Intent i = new Intent(getActivity(), ForgotPasswordWithOutNavActivity.class);
                startActivity(i);

            }
        });
    }

    public void loginBtnClicked() {
        if (LoginScreenManager.getInstance().mAnalyticsListener != null) {
            LoginScreenManager.getInstance().mAnalyticsListener.sendAnEvent(getString(R.string.event_category_loginscreen_login),
                    getString(R.string.event_action_loginscreen_loginbutton), getActivity().getApplication(), getContext());
        }
        //AnalyticsHelper.sendAnEvent(getString(R.string.event_category_loginscreen_login), getString(R.string.event_action_loginscreen_loginbutton), getActivity().getApplication(), getContext());

        if (!loginProcessStarted) {
            if (checkForLoginData()) {
                loginProcessStarted = true;
                showMaterialDialog();
                dismissKeyboard();
                loginWithUsernameAndPassword(mUsername.getText().toString(), mPassword.getText().toString());
            }
        }
    }

    private boolean checkForLoginData() {

        //AppUtility.createSnackWithMessage(getActivity().findViewById(R.id.login_activity), errorMessage);

        boolean valid = true;

        if (mUsername.getText().toString().trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mUsername.getText().toString()).matches()) {
            //mUsernameTextInput.setErrorEnabled(true);
            mUsernameTextInput.setError(getActivity().getString(R.string.empty_username));
            //mUsername.getBackground().setColorFilter(getResources().getColor(R.color.style_color_default_red), PorterDuff.Mode.SRC_ATOP);
            valid = false;
        } else {
            mUsernameTextInput.setError(null);
        }

        if (mPassword.getText().toString().isEmpty()) {
            // mPasswordTextInput.setErrorEnabled(true);
            mPasswordTextInput.setError(getActivity().getString(R.string.empty_password));
            // mPassword.getBackground().setColorFilter(getResources().getColor(R.color.style_color_default_red), PorterDuff.Mode.SRC_ATOP);
            valid = false;
        } else {
            mPasswordTextInput.setError(null);
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
        //if (Constants.WIZ_ROCKET_API != null) Constants.WIZ_ROCKET_API.event.push(TAG);
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
        mFbLoginButtonLoginPage.setReadPermissions("email", "public_profile", "user_friends");
        mFbLoginButtonLoginPage.setFragment(getParentFragment());
        mCallbackManager = CallbackManager.Factory.create();

        mFbLoginButtonLoginPage.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (((LoginNewFragment) getParentFragment()).mViewPager.getCurrentItem() == 1) {
                    showMaterialDialog();
                    mFbAccessToken = loginResult.getAccessToken().getToken();
                    sendSignUpWithFbToCleverTap(loginResult.getAccessToken(), TAG, CleverTapHelper.EventNames.LOGIN_EVENT, ConstantKeys.FromScreen.LOGIN_SCREEN_FACEBOOK);//reLook
                }
            }

            @Override
            public void onCancel() {
                //Toast.makeText(getActivity(), "Login cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getActivity(), "Login error Occurred, Please Try Again !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Api call for login with username and password
    private void loginWithUsernameAndPassword(final String username, String password) {
        com.explara_core.login.LoginScreenManager.getInstance().loginWithUsernameAndPassword(getActivity().getApplicationContext(), username, password, new com.explara_core.login.LoginScreenManager.UserLoginListener() {
            @Override
            public void onUserLogin(com.explara_core.login.login_dto.LoginResponseDto loginResponse) {

                /*mLoginResponseListener.onResponse(loginResponse);*/

                if (getActivity() != null && loginResponse != null) {
                    loginProcessStarted = false;
                    if (loginResponse.status.equals(Constants.STATUS_ERROR)) {
                        mMaterialDialog.dismiss();
                        mUsernameTextInput.setError(null);
                        mPasswordTextInput.setError(loginResponse.message);
                        if (loginResponse.message.trim().equals(getContext().getResources().getString(R.string.account_inactive))) {
                            ((LoginNewFragment) getParentFragment()).showVerifyAccountDialog(username);
                        }
                    } else {
                        saveUserDetailsWithAccessToken(loginResponse, CleverTapHelper.EventNames.LOGIN_EVENT, ConstantKeys.FromScreen.LOGIN_SCREEN); //reLook
                    }
                }
            }

            @Override
            public void onUserLoginFailed() {
                loginProcessStarted = false;
                if (getActivity() != null) {
                    mMaterialDialog.dismiss();
                    Toast.makeText(getActivity(), "Oops! Login failed.Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        LoginManager.getInstance().logOut();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

       /* mLoginResponseListener = (LoginResponseListener) context;*/
    }

    @Override
    public void refresh() {

    }


}

