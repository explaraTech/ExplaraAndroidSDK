package com.explara_core.login.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.explara_core.R;
import com.explara_core.common.LoginBaseFragment;
import com.explara_core.login.LoginScreenManager;
import com.explara_core.login.login_dto.LoginResponseDto;
import com.explara_core.login.util.CleverTapHelper;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.PreferenceManager;
import com.explara_core.utils.UiValidatorUtil;
import com.explara_core.utils.VolleyManager;

/**
 * Created by ananthasooraj on 9/4/15.
 */
public class FbUserDetailsFragment extends LoginBaseFragment {


    private static final String TAG = FbUserDetailsFragment.class.getSimpleName();
    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mMobileEditText;
    private TextInputLayout mNameTextInputLayout;
    private TextInputLayout mEmailTextInputLayout;
    private TextInputLayout mMobileTextInputLayout;
    private String mFbAccessToken;
    private String mName;
    private String mEmailId;
    private String mMobileNo;
    private Button mConfirmButton;
    boolean mConfirmProcessStarted = false;

    public FbUserDetailsFragment() {
        
    }

    public static FbUserDetailsFragment newInstance(Intent intent) {
        FbUserDetailsFragment fbUserDetailsFragment = new FbUserDetailsFragment();
        String fbAccessToken = intent.getStringExtra(Constants.FB_ACCESS_TOKEN_KEY);
        String name = intent.getStringExtra(Constants.NAME_KEY);
        String emailId = intent.getStringExtra(Constants.EMAIL_KEY);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FB_ACCESS_TOKEN_KEY, fbAccessToken);
        bundle.putString(Constants.NAME_KEY, name);
        bundle.putString(Constants.EMAIL_KEY, emailId);
        fbUserDetailsFragment.setArguments(bundle);
        return fbUserDetailsFragment;
    }
    
    @Override
    public void refresh() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fb_user_details_form_fragment, container, false);
        extractArguments();
        initViews(view);
        populateDefaultValues();
        return view;
    }


    private void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mFbAccessToken = args.getString(Constants.FB_ACCESS_TOKEN_KEY);
            mName = args.getString(Constants.NAME_KEY);
            mEmailId = args.getString(Constants.EMAIL_KEY);
        }
    }

    public void initViews(View view) {
        mNameTextInputLayout = (TextInputLayout)view.findViewById(R.id.buyername_text_input);
        mEmailTextInputLayout = (TextInputLayout)view.findViewById(R.id.buyeremail_text_input);
        mMobileTextInputLayout = (TextInputLayout)view.findViewById(R.id.buyermobile_text_input);
        mNameEditText = (EditText)view.findViewById(R.id.buyernameEditText);
        mEmailEditText = (EditText)view.findViewById(R.id.buyeremailEditText);
        mMobileEditText = (EditText)view.findViewById(R.id.buyermobileEditText);
        mConfirmButton = (Button)view.findViewById(R.id.rsvp_confirm_button);
    }

    public void populateDefaultValues(){
         if(!TextUtils.isEmpty(mName)){
            mNameEditText.setText(mName);
        }

        if(!TextUtils.isEmpty(mEmailId)){
            mEmailEditText.setText(mEmailId);
            mEmailEditText.setEnabled(false);
        }else{
            mEmailEditText.setEnabled(true);
        }

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mConfirmProcessStarted) {
                    if (checkForFacebookConfirmData()) {
                        mName = mNameEditText.getText().toString().trim();
                        mEmailId = mEmailEditText.getText().toString().trim();
                        mMobileNo = mMobileEditText.getText().toString().trim();
                        mConfirmProcessStarted = true;
                        showMaterialDialog();
                        dismissKeyboard();
                        signupWithFbAccessToken();
                    }
                }
            }
        });
    }

    public boolean checkForFacebookConfirmData(){
            boolean valid = true;

            if (mNameEditText.getText().toString().isEmpty()) {
                // mFullNameTextInput.setErrorEnabled(true);
                mNameTextInputLayout.setError(getActivity().getString(R.string.empty_full_name));
                // mFullNameEditText.getBackground().setColorFilter(getResources().getColor(R.color.style_color_default_red), PorterDuff.Mode.SRC_ATOP);
                valid = false;
            } else {
                mNameTextInputLayout.setError(null);
            }

            if (mEmailEditText.getText().toString().trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailEditText.getText().toString()).matches()) {
                //mUsernameTextInput.setErrorEnabled(true);
                mEmailTextInputLayout.setError(getActivity().getString(R.string.empty_username));
                //mUsernameEditText.getBackground().setColorFilter(getResources().getColor(R.color.style_color_default_red), PorterDuff.Mode.SRC_ATOP);
                valid = false;
            } else {
                mEmailTextInputLayout.setError(null);
            }

            if (mMobileEditText.getText().toString().isEmpty()) {
                // mMobileTextInput.setErrorEnabled(true);
                mMobileTextInputLayout.setError(getActivity().getString(R.string.empty_mobile_number));
                //mMobileEditText.getBackground().setColorFilter(getResources().getColor(R.color.style_color_default_red), PorterDuff.Mode.SRC_ATOP);
                valid = false;
            } else {
                mMobileTextInputLayout.setError(null);
            }



            if (!(UiValidatorUtil.isPhoneNumberValid(mMobileEditText.getText().toString()))) {
                // mMobileTextInput.setErrorEnabled(true);
                mMobileTextInputLayout.setError(getActivity().getString(R.string.mobile_is_number));
                //mMobileEditText.getBackground().setColorFilter(getResources().getColor(R.color.style_color_default_red), PorterDuff.Mode.SRC_ATOP);
                valid = false;
            } else {
                mMobileTextInputLayout.setError(null);
            }

            return valid;

    }


    // Api call for singup with username and password
    private void signupWithFbAccessToken() {
        LoginScreenManager.getInstance().signupWithFbAccessToken(getActivity().getApplicationContext(), mEmailId, mFbAccessToken, mMobileNo, mName,
                new LoginScreenManager.UserSignupWithAccessTokenListener() {
                    @Override
                    public void onUserSignup(LoginResponseDto loginResponseDto) {
                        if (getActivity() != null && loginResponseDto != null) {
                            mConfirmProcessStarted = false;
                            dismissMaterialDialog();
                            if (Constants.STATUS_ERROR.equals(loginResponseDto.status)) {
                                mEmailTextInputLayout.setError(loginResponseDto.message);
                                mMobileTextInputLayout.setError(null);
                                mNameTextInputLayout.setError(null);
                            } else {
                                //Toast.makeText(getActivity(), loginResponseDto.message, Toast.LENGTH_SHORT).show();
                                if (getActivity().getString(R.string.account_already_exist).equals(loginResponseDto.message)) {
                                    // Account already exists . Make direct login
                                    if (loginResponseDto.account != null) {
                                        if (loginResponseDto.account.accessToken != null && !loginResponseDto.account.accessToken.isEmpty()) {
                                            saveUserDetailsWithAccessToken(loginResponseDto, CleverTapHelper.EventNames.SIGN_UP_EVENT, ConstantKeys.FromScreen.LOGIN_SCREEN);
                                            PreferenceManager.getInstance(getContext()).setAccountVerified(Constants.ACCOUNT_VERIFIED);
                                        } else {
                                            dismissMaterialDialog();
                                            Toast.makeText(getContext(), "Fb login Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        dismissMaterialDialog();
                                        Toast.makeText(getContext(), "Fb login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    lunchSignUpVerificationCode(mEmailId);
                                }
                            }

                        }
                    }

                    @Override
                    public void onUserSignupFailed() {
                        if (getActivity() != null) {
                            mConfirmProcessStarted = false;
                            dismissMaterialDialog();
                            Toast.makeText(getActivity(), "Oops! Signup failed.Please check your internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, TAG);
    }

    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() != null){
            VolleyManager.getInstance(getActivity()).cancelRequest(TAG);
        }
    }
}
