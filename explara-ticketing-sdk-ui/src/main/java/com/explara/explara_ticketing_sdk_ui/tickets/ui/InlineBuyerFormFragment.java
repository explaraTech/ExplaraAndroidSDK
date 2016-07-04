package com.explara.explara_ticketing_sdk_ui.tickets.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara.explara_ticketing_sdk_ui.common.TicketingBaseFragment;
import com.explara_core.login.LoginScreenManager;
import com.explara_core.login.login_dto.SignupResponseDto;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.PreferenceManager;

/**
 * Created by Debasish on 04/01/16.
 */
public class InlineBuyerFormFragment extends TicketingBaseFragment {
    private String mEventId;
    private TextInputLayout mBuyerNameTextInput;
    private TextInputLayout mBuyerEmailTextInput;
    private TextInputLayout mBuyerMobileTextInput;
    private TextInputLayout mBuyerPasswordTextInput;
    private EditText mBuyerNameEditText;
    private EditText mBuyerEmailEditText;
    private EditText mBuyerMobileEditText;
    private EditText mBuyerPasswordEditText;
    public CheckBox mCheckbox;
    public TextView mCheckBoxText;
    private Button mProceedBtn;
    boolean processStarted = false;
    private MaterialDialog mMaterialDialog;
    public String mBuyerName;
    public String mBuyerEmail;
    public String mBuyerPhone;
    public String mBuyerPassword;
    boolean mSignupProcessStarted = false;
    public String mSourcePage;

    private static final String TAG = InlineBuyerFormFragment.class.getSimpleName();


    public static InlineBuyerFormFragment newInstance(String eventId, String sourcePage) {
        InlineBuyerFormFragment inlineBuyerFormFragment = new InlineBuyerFormFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("eventId", eventId);
        bundle.putString(ConstantKeys.InlineFormKeys.sourcePage, sourcePage);
        inlineBuyerFormFragment.setArguments(bundle);
        return inlineBuyerFormFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inline_buyer_form_fragment, container, false);
        extractArguments();
        intiViews(view);
        return view;
    }


    private void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEventId = args.getString("eventId");
            mSourcePage = args.getString(ConstantKeys.InlineFormKeys.sourcePage);
        }
    }


    private void intiViews(View view) {
        mBuyerNameTextInput = (TextInputLayout) view.findViewById(R.id.buyername_text_input);
        mBuyerEmailTextInput = (TextInputLayout) view.findViewById(R.id.buyeremail_text_input);
        mBuyerMobileTextInput = (TextInputLayout) view.findViewById(R.id.buyermobile_text_input);
        mBuyerPasswordTextInput = (TextInputLayout) view.findViewById(R.id.buyer_password_text_input);
        mBuyerNameEditText = (EditText) view.findViewById(R.id.buyernameEditText);
        mBuyerEmailEditText = (EditText) view.findViewById(R.id.buyeremailEditText);
        mBuyerMobileEditText = (EditText) view.findViewById(R.id.buyermobileEditText);
        mBuyerPasswordEditText = (EditText) view.findViewById(R.id.buyerPasswordEditText);
        mCheckbox = (CheckBox) view.findViewById(R.id.chkIos);
        mCheckBoxText = (TextView) view.findViewById(R.id.terms_n_conditions);
        if (!TextUtils.isEmpty(PreferenceManager.getInstance(getContext()).getAccessToken())) {
            mCheckbox.setVisibility(View.GONE);
            mCheckBoxText.setVisibility(View.GONE);
            mCheckbox.setChecked(false);
        } else {
            // Only for Explara.Hide it for sdk
            if (Constants.EXPLARA_ONLY) {
                mCheckbox.setVisibility(View.VISIBLE);
                mCheckBoxText.setVisibility(View.VISIBLE);
                if (mCheckbox.isChecked()) {
                    mBuyerPasswordTextInput.setVisibility(View.VISIBLE);
                } else {
                    mBuyerPasswordTextInput.setVisibility(View.GONE);
                }
                mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            mBuyerPasswordTextInput.setVisibility(View.VISIBLE);
                        } else {
                            mBuyerPasswordTextInput.setVisibility(View.GONE);
                        }
                    }
                });
            } else {
                mCheckbox.setVisibility(View.GONE);
                mCheckBoxText.setVisibility(View.GONE);
                mCheckbox.setChecked(false);
            }
        }

        // Auto fill the buyer form
        if (ConstantKeys.InlineFormKeys.ticketingPage.equals(mSourcePage)) {
            mBuyerName = ((TicketsDetailsFragment) getParentFragment()).mBuyerName;
            mBuyerEmail = ((TicketsDetailsFragment) getParentFragment()).mBuyerEmail;
            mBuyerPhone = ((TicketsDetailsFragment) getParentFragment()).mBuyerMobile;
        } else if (ConstantKeys.InlineFormKeys.theaterPage.equals(mSourcePage)) {
            mBuyerName = ((TicketsDetailsWithDatesFragment) getParentFragment()).mBuyerName;
            mBuyerEmail = ((TicketsDetailsWithDatesFragment) getParentFragment()).mBuyerEmail;
            mBuyerPhone = ((TicketsDetailsWithDatesFragment) getParentFragment()).mBuyerMobile;
        } else {
            mBuyerName = ((TicketsDetailsWithMultipleSessionFragment) getParentFragment()).mBuyerName;
            mBuyerEmail = ((TicketsDetailsWithMultipleSessionFragment) getParentFragment()).mBuyerEmail;
            mBuyerPhone = ((TicketsDetailsWithMultipleSessionFragment) getParentFragment()).mBuyerMobile;
        }

        if (!TextUtils.isEmpty(mBuyerName)) {
            mBuyerNameEditText.setText(mBuyerName);
        } else if (!TextUtils.isEmpty(PreferenceManager.getInstance(getContext()).getUserName())) {
            mBuyerNameEditText.setText(PreferenceManager.getInstance(getContext()).getUserName());
        }

        if (!TextUtils.isEmpty(mBuyerEmail)) {
            mBuyerEmailEditText.setText(mBuyerEmail);
        } else if (!TextUtils.isEmpty(PreferenceManager.getInstance(getContext()).getEmail())) {
            mBuyerEmailEditText.setText(PreferenceManager.getInstance(getContext()).getEmail());
        }

        if (!TextUtils.isEmpty(mBuyerPhone)) {
            mBuyerMobileEditText.setText(mBuyerPhone);
        } else if (!TextUtils.isEmpty(PreferenceManager.getInstance(getContext()).getPhoneNo())) {
            mBuyerMobileEditText.setText(PreferenceManager.getInstance(getContext()).getPhoneNo());
        }


        mProceedBtn = (Button) view.findViewById(R.id.proceed_btn);
        mProceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForEnquiryData()) {
                    /* Check login or not
                    *  if login, store thr buyer data in transaction dto
                    * else store in Preference Manager as well as transaction dto
                    */
                    dismissKeyboard();
                    mBuyerName = mBuyerNameEditText.getText().toString();
                    mBuyerEmail = mBuyerEmailEditText.getText().toString();
                    mBuyerPhone = mBuyerMobileEditText.getText().toString();

                    // Create account for without login and if checkbox is checked
                    if (mCheckbox.isChecked() && !TextUtils.isEmpty(mBuyerPasswordEditText.getText().toString())) {
                        mBuyerPassword = mBuyerPasswordEditText.getText().toString();
                        if (!mSignupProcessStarted) {
                            mSignupProcessStarted = true;
                            showMaterialDialog();
                            dismissKeyboard();
                            String password_md5_hash = Constants.generateMD5(mBuyerPassword);
                            if (!password_md5_hash.isEmpty()) {
                                signUpUserWithOutVerificationCode(mBuyerEmail, password_md5_hash, mBuyerPhone, mBuyerName);
                            }
                        }
                    }
                    // Hide the inline Form
                    if (ConstantKeys.InlineFormKeys.ticketingPage.equals(mSourcePage)) {
                        ((TicketsDetailsFragment) getParentFragment()).hideShowBottomSlideContainer();
                    } else if (ConstantKeys.InlineFormKeys.theaterPage.equals(mSourcePage)) {
                        ((TicketsDetailsWithDatesFragment) getParentFragment()).hideShowBottomSlideContainer();
                    } else {
                        ((TicketsDetailsWithMultipleSessionFragment) getParentFragment()).hideShowBottomSlideContainer();
                    }

                    // Storing data in preference
                    storeBuyerDetailsInPreference(mBuyerName, mBuyerEmail, mBuyerPhone);

                    // For now storing in separate obj
                    if (TicketsManager.getInstance().mBuyerDetailWithOutAttendeeFormDto != null) {
                        TicketsManager.getInstance().mBuyerDetailWithOutAttendeeFormDto.buyerName = mBuyerName;
                        TicketsManager.getInstance().mBuyerDetailWithOutAttendeeFormDto.buyerEmail = mBuyerEmail;
                        TicketsManager.getInstance().mBuyerDetailWithOutAttendeeFormDto.buyerPhone = mBuyerPhone;
                        if (Constants.EXPLARA_ONLY) {
                            TicketsManager.getInstance().mTicketListingCallBackListnener.storeDataInTransactionDtoFromBuyerForm(mBuyerName, mBuyerEmail, mBuyerPhone);
                        }
                    }

                    if (ConstantKeys.InlineFormKeys.ticketingPage.equals(mSourcePage)) {
                        ((TicketsDetailsFragment) getParentFragment()).populateBuyerData(mBuyerName, mBuyerEmail, mBuyerPhone);
                    } else if (ConstantKeys.InlineFormKeys.theaterPage.equals(mSourcePage)) {
                        ((TicketsDetailsWithDatesFragment) getParentFragment()).populateBuyerData(mBuyerName, mBuyerEmail, mBuyerPhone);
                    } else {
                        ((TicketsDetailsWithMultipleSessionFragment) getParentFragment()).populateBuyerData(mBuyerName, mBuyerEmail, mBuyerPhone);
                    }
                }
            }
        });
    }

    // Api call for singup with username and password
    private void signUpUserWithOutVerificationCode(final String username, final String password, String mobileNo, String fullName) {
        LoginScreenManager.getInstance().signupWithUsernameAndPasswordWithoutVerify(getActivity().getApplicationContext(), username, password, mobileNo, fullName, new LoginScreenManager.UserSignupWithOutVerifyListener() {
            @Override
            public void onUserSignup(SignupResponseDto signupResponseDto) {
                mSignupProcessStarted = false;

                if (signupResponseDto.getStatus().equals(Constants.STATUS_ERROR)) {
                    mMaterialDialog.dismiss();
                    Toast.makeText(getActivity(), signupResponseDto.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    mMaterialDialog.dismiss();
                    Toast.makeText(getActivity(), signupResponseDto.getMessage(), Toast.LENGTH_SHORT).show();
                    PreferenceManager.getInstance(getContext()).setAccountVerified(Constants.ACCOUNT_NOT_VERIFIED);
                }
            }

            @Override
            public void onUserSignupFailed() {
                mSignupProcessStarted = false;
                if (getActivity() != null) {
                    mMaterialDialog.dismiss();
                    Toast.makeText(getActivity(), "Oops! Signup failed.Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    }



    /*public void saveUserDetailsInLocal(GetProfileResponse getProfileResponse) {

        // Storing profile image,name,Email,Phone no
        PreferenceManager.getInstance(getActivity().getApplicationContext()).setUserImage(getProfileResponse.getProfile().getProfileImage() + "");
        if (getProfileResponse.getProfile().getFirstName() != null && getProfileResponse.getProfile().getLastName() != null) {

            PreferenceManager.getInstance(getActivity().getApplicationContext()).setUserName(getProfileResponse.getProfile().getFirstName().equals(getProfileResponse.getProfile().getLastName()) ?
                    getProfileResponse.getProfile().getFirstName() :
                    (getProfileResponse.getProfile().getFirstName() + " "
                            + getProfileResponse.getProfile().getLastName()));

        } else {
            if (!TextUtils.isEmpty(getProfileResponse.getProfile().getFirstName())) {
                PreferenceManager.getInstance(getActivity().getApplicationContext()).setUserName(getProfileResponse.getProfile().getFirstName());
            }
            if (!TextUtils.isEmpty(getProfileResponse.getProfile().getLastName())) {
                PreferenceManager.getInstance(getActivity().getApplicationContext()).setUserName(getProfileResponse.getProfile().getLastName());
            }
        }
        PreferenceManager.getInstance(getActivity().getApplicationContext()).setEmail(getProfileResponse.getProfile().getEmailId());
        PreferenceManager.getInstance(getActivity().getApplicationContext()).setPhoneNo(getProfileResponse.getProfile().getMobileNumber());

        if (mMaterialDialog != null) {
            mMaterialDialog.dismiss();
        }

        // Closing the inline window
        ((TicketsDetailsFragment)getParentFragment()).hideShowBottomSlideContainer();
    }

    public boolean checkAllBuyerDetailsFilled(){
        if(TextUtils.isEmpty(mBuyerNameEditText.getText().toString())){
            return false;
        }else if(TextUtils.isEmpty(mBuyerEmailEditText.getText().toString())){
            return false;
        }else if(TextUtils.isEmpty(mBuyerMobileEditText.getText().toString())){
            return false;
        }else{
            return true;
        }
    }*/

    public void showMaterialDialog() {
        mMaterialDialog = new MaterialDialog.Builder(getActivity())
                //.title("Explara Login")
                .content("Please wait..")
                .cancelable(false)
                        //.iconRes(R.drawable.e_logo)
                .progress(true, 0)
                .show();
    }

    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    private boolean checkForEnquiryData() {

        //AppUtility.createSnackWithMessage(getActivity().findViewById(R.id.login_activity), errorMessage);
        boolean valid = true;

        if (mBuyerNameEditText.getText().toString().trim().isEmpty()) {
            mBuyerNameTextInput.setError("Name seems to be empty");
            valid = false;
        } else {
            mBuyerNameTextInput.setError(null);
        }

        if (mBuyerEmailEditText.getText().toString().trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mBuyerEmailEditText.getText().toString()).matches()) {
            mBuyerEmailTextInput.setError("Enter a valid email address");
            valid = false;
        } else {
            mBuyerEmailTextInput.setError(null);
        }

        if (mBuyerMobileEditText.getText().toString().trim().isEmpty()) {
            mBuyerMobileTextInput.setError("Mobile number seems to be empty");
            valid = false;
        } else {
            mBuyerMobileTextInput.setError(null);
        }

        if (!Patterns.PHONE.matcher(mBuyerMobileEditText.getText().toString()).matches()) {
            mBuyerMobileTextInput.setError("Mobile number is not valid");
            valid = false;
        } else {
            mBuyerMobileTextInput.setError(null);
        }

        if (mCheckbox.isChecked()) {
            if (mBuyerPasswordEditText.getText().toString().isEmpty()) {
                mBuyerPasswordTextInput.setError(getActivity().getString(R.string.empty_password));
                valid = false;
            } else {
                mBuyerPasswordTextInput.setError(null);
            }
        }
        return valid;
    }

    @Override
    public void refresh() {

    }
}
