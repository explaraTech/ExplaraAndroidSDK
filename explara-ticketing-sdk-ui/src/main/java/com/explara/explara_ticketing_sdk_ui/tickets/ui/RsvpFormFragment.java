package com.explara.explara_ticketing_sdk_ui.tickets.ui;

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

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk.tickets.dto.ConfirmRsvpDto;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara.explara_ticketing_sdk_ui.common.TicketingBaseFragment;
import com.explara_core.utils.Constants;
import com.explara_core.utils.PreferenceManager;
import com.explara_core.utils.UiValidatorUtil;
import com.explara_core.utils.VolleyManager;

/**
 * Created by ananthasooraj on 9/4/15.
 */
public class RsvpFormFragment extends TicketingBaseFragment {


    private static final String TAG = RsvpFormFragment.class.getSimpleName();
    private EditText mBuyNameEditText;
    private EditText mBuyerEmailEditText;
    private EditText mBuyerMobileEditText;
    private TextInputLayout mBuyerNameTextInputLayout;
    private TextInputLayout mBuyerEmailTextInputLayout;
    private TextInputLayout mBuyerMobileTextInputLayout;
    private MaterialDialog mMaterialDialog;
    private String mEventId;
    private Button mConfirmButton;
    boolean mConfirmProcessStarted = false;

    public RsvpFormFragment() {
    }


    public static RsvpFormFragment getInstance(Intent intent) {
        RsvpFormFragment rsvpFormFragment = new RsvpFormFragment();
        String eventId = intent.getStringExtra(Constants.EVENT_ID);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EVENT_ID, eventId);
        rsvpFormFragment.setArguments(bundle);
        return rsvpFormFragment;
    }

    public static RsvpFormFragment newInstance(String eventId) {
        RsvpFormFragment rsvpFormFragment = new RsvpFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EVENT_ID, eventId);
        rsvpFormFragment.setArguments(bundle);
        return rsvpFormFragment;
    }

    @Override
    public void refresh() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rsvp_form_fragment, container, false);
        extractArguments();
        initViews(view);
        populateDefaultValues();
        return view;
    }


    private void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEventId = args.getString(Constants.EVENT_ID);
        }
    }

    private void googleAnalyticsSendScreenName() {
        if (TicketsManager.getInstance().mAnalyticsListener != null) {
            TicketsManager.getInstance().mAnalyticsListener.sendScreenName(getResources().getString(R.string.rsvp_form), getActivity().getApplication(), getContext());
        }
        //AnalyticsHelper.sendScreenName(getResources().getString(R.string.rsvp_form), getActivity().getApplication(), getContext());
    }

    public void initViews(View view) {
        mBuyerNameTextInputLayout = (TextInputLayout) view.findViewById(R.id.buyername_text_input);
        mBuyerEmailTextInputLayout = (TextInputLayout) view.findViewById(R.id.buyeremail_text_input);
        mBuyerMobileTextInputLayout = (TextInputLayout) view.findViewById(R.id.buyermobile_text_input);
        mBuyNameEditText = (EditText) view.findViewById(R.id.buyernameEditText);
        mBuyerEmailEditText = (EditText) view.findViewById(R.id.buyeremailEditText);
        mBuyerMobileEditText = (EditText) view.findViewById(R.id.buyermobileEditText);
        mConfirmButton = (Button) view.findViewById(R.id.rsvp_confirm_button);
        googleAnalyticsSendScreenName();
    }

    public void populateDefaultValues() {
        if (!TextUtils.isEmpty(PreferenceManager.getInstance(getActivity()).getUserName())) {
            mBuyNameEditText.setText(PreferenceManager.getInstance(getActivity()).getUserName());
        }

        if (!TextUtils.isEmpty(PreferenceManager.getInstance(getActivity()).getEmail())) {
            mBuyerEmailEditText.setText(PreferenceManager.getInstance(getActivity()).getEmail());
        }

        if (!TextUtils.isEmpty(PreferenceManager.getInstance(getActivity()).getPhoneNo())) {
            mBuyerMobileEditText.setText(PreferenceManager.getInstance(getActivity()).getPhoneNo());
        }

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mConfirmProcessStarted) {
                    if (checkForRsvpConfirmData()) {
                        mConfirmProcessStarted = true;
                        showMaterialDialog();
                        dismissKeyboard();
                        confirmRequestRsvp();
                    }
                }
            }
        });
    }

    public boolean checkForRsvpConfirmData() {
        boolean valid = true;

        if (mBuyNameEditText.getText().toString().isEmpty()) {
            // mFullNameTextInput.setErrorEnabled(true);
            mBuyerNameTextInputLayout.setError(getActivity().getString(R.string.empty_full_name));
            // mFullNameEditText.getBackground().setColorFilter(getResources().getColor(R.color.style_color_default_red), PorterDuff.Mode.SRC_ATOP);
            valid = false;
        } else {
            mBuyerNameTextInputLayout.setError(null);
        }

        if (mBuyerEmailEditText.getText().toString().trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mBuyerEmailEditText.getText().toString()).matches()) {
            //mUsernameTextInput.setErrorEnabled(true);
            mBuyerEmailTextInputLayout.setError(getActivity().getString(R.string.empty_username));
            //mUsernameEditText.getBackground().setColorFilter(getResources().getColor(R.color.style_color_default_red), PorterDuff.Mode.SRC_ATOP);
            valid = false;
        } else {
            mBuyerEmailTextInputLayout.setError(null);
        }

        if (mBuyerMobileEditText.getText().toString().isEmpty()) {
            // mMobileTextInput.setErrorEnabled(true);
            mBuyerMobileTextInputLayout.setError(getActivity().getString(R.string.empty_mobile_number));
            //mMobileEditText.getBackground().setColorFilter(getResources().getColor(R.color.style_color_default_red), PorterDuff.Mode.SRC_ATOP);
            valid = false;
        } else {
            mBuyerMobileTextInputLayout.setError(null);
        }


        if (!(UiValidatorUtil.isPhoneNumberValid(mBuyerMobileEditText.getText().toString()))) {
            // mMobileTextInput.setErrorEnabled(true);
            mBuyerMobileTextInputLayout.setError(getActivity().getString(R.string.mobile_is_number));
            //mMobileEditText.getBackground().setColorFilter(getResources().getColor(R.color.style_color_default_red), PorterDuff.Mode.SRC_ATOP);
            valid = false;
        } else {
            mBuyerMobileTextInputLayout.setError(null);
        }

        return valid;

    }

    public void confirmRequestRsvp() {
        TicketsManager.getInstance().confirmRequestRsvp(getActivity().getApplicationContext(), mEventId, mBuyNameEditText.getText().toString(), mBuyerEmailEditText.getText().toString(), mBuyerMobileEditText.getText().toString(), new TicketsManager.ConfirmRsvpListener() {
            @Override
            public void onConfirmRsvpListener(ConfirmRsvpDto confirmRsvpDto) {
                if (getActivity() != null && confirmRsvpDto != null) {
                    mConfirmProcessStarted = false;
                    if (mMaterialDialog.isShowing()) {
                        mMaterialDialog.dismiss();
                    }

                    if (confirmRsvpDto.success == 1) {
                        redirectToConfirmationPage();
                    } else {
                        mBuyerEmailTextInputLayout.setError(confirmRsvpDto.message);
                    }

                }
            }

            @Override
            public void onConfirmRsvpFailed(VolleyError volleyError) {
                if (getActivity() != null) {
                    mConfirmProcessStarted = false;
                    if (mMaterialDialog.isShowing()) {
                        mMaterialDialog.dismiss();
                    }
                    Toast.makeText(getActivity(), "Rsvp registration failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void redirectToConfirmationPage() {
        Intent intent = new Intent(getActivity(), RsvpConfirmationActivity.class);
        intent.putExtra(Constants.EVENT_ID, mEventId);
        startActivity(intent);
    }

    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() != null) {
            VolleyManager.getInstance(getActivity()).cancelRequest(TAG);
        }
    }
}
