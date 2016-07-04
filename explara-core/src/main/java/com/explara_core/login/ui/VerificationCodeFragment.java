package com.explara_core.login.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsMessage;
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
import com.explara_core.utils.CompatibilityUtil;
import com.explara_core.utils.Constants;


/**
 * Created by debasishpanda on 22/09/15.
 */
public class VerificationCodeFragment extends LoginBaseFragment {

    private static final String SMS_SENDER = "EXPLRA";
    private static final String TAG = VerificationCodeFragment.class.getSimpleName();
    // For the alert box
    private MaterialDialog mMaterialAlertDialog;
    // For progress dialog box
    private MaterialDialog mMaterialProgressDialog;
    private EditText mCodeEditText;
    private Button mVerifyButton;
    private static String mEmail = "";
    private String mCode = "";
    private static final int smsReadPermission = 1;
    private static final int smsRecievePermission = 2;
    public int registerForSms = 1;

    public static VerificationCodeFragment getInstance(Intent intent) {
        VerificationCodeFragment verificationCodeFragment = new VerificationCodeFragment();
        String email = intent.getStringExtra(Constants.EMAIL_ID_KEY);
        Bundle bundle = new Bundle();
        bundle.putString("emailId", email);
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
        View view = inflater.inflate(R.layout.activity_forgot_password_verification, container, false);
        extractArguments();
        initViews(view);
        setWizRocket();
        return view;
    }

    public void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEmail = args.getString("emailId");
        }
    }

    public void initViews(View view) {

        mCodeEditText = (EditText) view.findViewById(R.id.code_verification_edit_text);
        mVerifyButton = (Button) view.findViewById(R.id.verifyBtn);
        mVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVerifyBtnClicked();
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

    public void onVerifyBtnClicked() {
        googleAnalyticsSendAnEvent();
        if (checkFormData()) {
            mCode = mCodeEditText.getText().toString();
            showMaterialDialog();
            dismissKeyboard();
            verifyCode();
        }
    }

    private BroadcastReceiver smsReceiver = new BroadcastReceiver() {
        public static final String SMS_BUNDLE = "pdus";

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle intentExtras = intent.getExtras();
            if (intentExtras != null) {
                Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
                for (int i = 0; i < sms.length; ++i) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                    if (smsMessage.getOriginatingAddress().contains(SMS_SENDER)) {
                        abortBroadcast();
                        mCodeEditText.setText(smsMessage.getMessageBody().toString().replaceAll("\\D+", ""));
                        onVerifyBtnClicked();
                    }
                }
            }
        }
    };

    // Api call for login with username and password
    private void verifyCode() {
        com.explara_core.login.LoginScreenManager.getInstance().verifyCode(getActivity().getApplicationContext(), mCode, mEmail, new com.explara_core.login.LoginScreenManager.VerifyCodeListener() {
            @Override
            public void onCodeVerified(com.explara_core.login.login_dto.LoginResponseDto loginResponse) {
                if (getActivity() != null && loginResponse != null) {
                    if (loginResponse.status.equals(Constants.STATUS_ERROR)) {
                        //Constants.createToastWithMessage(this, response.getMessage());
                        mMaterialProgressDialog.dismiss();
                        AppUtility.createSnackWithMessage(getActivity().findViewById(R.id.forgot_verification_activity_relative_layout), loginResponse.message);
                    } else {
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
                                        navigateToResetPage();
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
            public void onCodeVerifyFailed() {
                if (getActivity() != null) {
                    mMaterialProgressDialog.dismiss();
                    Toast.makeText(getActivity(), "Oops! Could not verify the code.", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    }

    private boolean checkFormData() {

        boolean valid = true;
        if (mCodeEditText.getText() == null || mCodeEditText.getText().toString().isEmpty()) {
            Constants.createToastWithMessage(getActivity(), getActivity().getString(R.string.empty_verification_code));
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
        //if (Constants.WIZ_ROCKET_API != null) Constants.WIZ_ROCKET_API.event.push(TAG);
        if (LoginScreenManager.getInstance().mAnalyticsListener != null) {
            LoginScreenManager.getInstance().mAnalyticsListener.sendScreenName(getString(R.string.verification_code), getActivity().getApplication(), getContext());
        }
        //AnalyticsHelper.sendScreenName(getString(R.string.verification_code), getActivity().getApplication(), getContext());
    }


    private void navigateToResetPage() {
        Intent i = new Intent(getActivity(), ResetPasswordWithOutNavActivity.class);
        i.putExtra(Constants.EMAIL_ID_KEY, mEmail);
        i.putExtra(Constants.VERIFICATION_CODE_KEY, mCode);
        startActivity(i);
        getActivity().finish();
    }

    @Override
    public void refresh() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        registerForSms();
        if (CompatibilityUtil.isMarshmallow()) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_LONG).show();
                    registerForSms();
                }


            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_SMS)) {
                        Toast.makeText(getContext(), "Permission is Required", Toast.LENGTH_LONG).show();
                    }
                }
//                requestPermissions(new String[]{Manifest.permission.READ_SMS}, smsReadPermission);
                requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, smsRecievePermission);
            }

        } else {
            registerForSms();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == smsRecievePermission) {
            if (permissions.length > 0 && grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    registerForSms();
                } else {
                    Toast.makeText(getContext(), "Permission not given", Toast.LENGTH_LONG).show();

                }
            } else {
                Toast.makeText(getContext(), " Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (registerForSms == 2) {
            getActivity().unregisterReceiver(smsReceiver);
        }
    }

    private void registerForSms() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);
        registerForSms = 2;
        getActivity().registerReceiver(smsReceiver, filter);
    }

    private void googleAnalyticsSendAnEvent() {
        if (LoginScreenManager.getInstance().mAnalyticsListener != null) {
            LoginScreenManager.getInstance().mAnalyticsListener.sendAnEvent(getString(R.string.event_category_verificationcodescreen_verificationcode),
                    getString(R.string.event_action_verificationcodescreen_verificationcode), getActivity().getApplication(), getContext());
        }
        //AnalyticsHelper.sendAnEvent(getString(R.string.event_category_verificationcodescreen_verificationcode), getString(R.string.event_action_verificationcodescreen_verificationcode), getActivity().getApplication(), getContext());
    }
}

