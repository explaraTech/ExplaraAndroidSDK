package com.explara.explara_eventslisting_sdk_ui.events.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;
import com.explara.explara_eventslisting_sdk.events.EventsManger;
import com.explara.explara_eventslisting_sdk.events.dto.EnquiryResponseDto;
import com.explara.explara_eventslisting_sdk_ui.R;
import com.explara.explara_eventslisting_sdk_ui.common.EventBaseFragment;
import com.explara_core.utils.Log;

/**
 * Created by akshaya on 04/01/16.
 */
public class EnquiryFragment extends EventBaseFragment {

    private TextInputLayout mNameTextInput;
    private TextInputLayout mMobileTextInput;
    private TextInputLayout mEmailTextInput;
    private TextInputLayout mEnquiryTextInput;
    private boolean processStarted = false;
    private MaterialDialog mMaterialDialog;
    private MaterialDialog mMaterialDailog;
    private Button mSubmitEnquiryBtn;
    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mEnquiryEditText;
    private EditText mMobileEditText;
    private static final String TAG = EnquiryFragment.class.getSimpleName();
    private String mEventId;


    public static EnquiryFragment getInstance(String eventId) {
        EnquiryFragment enquiryFragment = new EnquiryFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("eventId", eventId);
        enquiryFragment.setArguments(bundle);
        return enquiryFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_enquiry_form, container, false);
        extractArguments();
        intiViews(view);
        return view;
    }


    private void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEventId = args.getString("eventId");
        }
    }


    private void intiViews(View view) {
        mNameEditText = (EditText) view.findViewById(R.id.enquiry_name);
        mEmailEditText = (EditText) view.findViewById(R.id.enquiry_emailid);
        mEnquiryEditText = (EditText) view.findViewById(R.id.enqiry_text);
        mMobileEditText = (EditText) view.findViewById(R.id.enquiry_mobile_no);
        mSubmitEnquiryBtn = (Button) view.findViewById(R.id.submit_enquiry_btn);
        mNameTextInput = (TextInputLayout) view.findViewById(R.id.input_layout_textview_name);
        mEmailTextInput = (TextInputLayout) view.findViewById(R.id.input_layout_textview_email);
        mMobileTextInput = (TextInputLayout) view.findViewById(R.id.input_layout_textview_mobile);
        mEnquiryTextInput = (TextInputLayout) view.findViewById(R.id.input_layout_textview_enquiry);
        mSubmitEnquiryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnclicked();
            }
        });
    }


    private void btnclicked() {
        if (!processStarted) {
            if (checkForEnquiryData()) {
                processStarted = true;
                showMaterialDialog();
                String name = mNameEditText.getText().toString();
                String mobileNo = mMobileEditText.getText().toString();
                String enquiry = mEnquiryEditText.getText().toString();
                String emailId = mEmailEditText.getText().toString();
                sendFeedbackResponseFromUserToServer(name, emailId, mobileNo, enquiry, mEventId);
            }
        }
    }

    public void clearEditText() {
        mNameEditText.setText("");
        mMobileEditText.setText("");
        mEnquiryEditText.setText("");
        mEmailEditText.setText("");
    }

    private void sendFeedbackResponseFromUserToServer(final String name, final String emailId, final String mobileNumber, final String enquiry, final String eventId) {
        EventsManger.getInstance().enquiry(getActivity().getApplicationContext(), name, emailId, mobileNumber, enquiry, eventId, new EventsManger.SendEnquiryListener() {
            @Override
            public void onSendEnquirySuccess(EnquiryResponseDto enquiryResponseDto) {

                if (getActivity() != null && enquiryResponseDto != null) {

                    Log.d(TAG, "" + name);
                    Log.d(TAG, "" + emailId);
                    Log.d(TAG, "" + mobileNumber);
                    Log.d(TAG, "" + enquiry);
                    Log.d(TAG, "" + eventId);

                    if (mMaterialDialog != null && mMaterialDialog.isShowing()) {
                        mMaterialDialog.dismiss();
                    }
                    processStarted = false;
                    if ("1".equals(enquiryResponseDto.id)) {
                        Toast.makeText(getActivity(), enquiryResponseDto.message, Toast.LENGTH_LONG).show();
                        dismissKeyboard();
                        clearEditText();
                        ((EventDetailFragment) getParentFragment()).hideShowBottomSlideContainer();
                    } else {
                        Toast.makeText(getActivity(), enquiryResponseDto.message, Toast.LENGTH_LONG).show();
                    }
                }
            }


            @Override
            public void onSendEnquiryFailed(VolleyError volleyError) {
                if (getActivity() != null) {
                    if (mMaterialDialog != null && mMaterialDialog.isShowing()) {
                        mMaterialDialog.dismiss();
                    }
                    processStarted = false;
                    Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    }


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

        if (mNameEditText.getText().toString().trim().isEmpty()) {
            mNameTextInput.setError("Name seems to be empty");
            valid = false;
        } else {
            mNameTextInput.setError(null);
        }

        if (mEnquiryEditText.getText().toString().isEmpty()) {

            mEnquiryTextInput.setError("Enquiry seems to be empty");
            valid = false;
        } else {
            mEnquiryTextInput.setError(null);
        }

        if (mEmailEditText.getText().toString().trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmailEditText.getText().toString()).matches()) {
            //mUsernameTextInput.setErrorEnabled(true);

            mEmailTextInput.setError("Enter a valid email address");
            //mUsernameEditText.getBackground().setColorFilter(getResources().getColor(R.color.style_color_default_red), PorterDuff.Mode.SRC_ATOP);
            valid = false;
        } else {
            mEmailTextInput.setError(null);
        }


        if (mMobileEditText.getText().toString().isEmpty()) {
            // mMobileTextInput.setErrorEnabled(true);
            mMobileTextInput.setError("Number seems to be empty");
            //mMobileEditText.getBackground().setColorFilter(getResources().getColor(R.color.style_color_default_red), PorterDuff.Mode.SRC_ATOP);
            valid = false;
        } else {
            mMobileTextInput.setError(null);
        }

        if (!Patterns.PHONE.matcher(mMobileEditText.getText().toString()).matches()) {
            mMobileTextInput.setError("Mobile Number is not valid");
            valid = false;
        } else {
            mMobileTextInput.setError(null);
        }


        return valid;
    }

    @Override
    public void refresh() {

    }
}
