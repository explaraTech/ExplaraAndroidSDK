package com.explara_core.login.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;
import com.explara_core.R;
import com.explara_core.common.LoginBaseFragment;
import com.explara_core.login.LoginScreenManager;
import com.explara_core.login.login_dto.LoginResponseDto;
import com.explara_core.login.util.CleverTapHelper;
import com.explara_core.utils.CompatibilityUtil;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.Log;
import com.explara_core.utils.PreferenceManager;


/**
 * Created by debasishpanda on 22/09/15.
 */
public class SignUpVerificationCodeFragment extends LoginBaseFragment {

    private static final String SMS_SENDER = "EXPLRA";
    private static final String TAG = SignUpVerificationCodeFragment.class.getSimpleName();
    // For the alert box
    private MaterialDialog mMaterialAlertDialog;
    // For progress dialog box
    private MaterialDialog mMaterialProgressDialog;
    private EditText mCodeEditText;
    private Button mVerifyButton;
    private TextView mResendText;
    private String mEmail = "";
    private String mCode = "";
    private TextInputLayout mVerificationCodeLayout;
    private static final int smsReadPermission = 1;
    private static final int smsReceivePermission = 2;
    public int registerforSms = 1;

    public static SignUpVerificationCodeFragment getInstance(Intent intent) {
        SignUpVerificationCodeFragment verificationCodeFragment = new SignUpVerificationCodeFragment();
        String email = intent.getStringExtra(Constants.EMAIL_ID_KEY);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EMAIL_ID_KEY, email);
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
        View view = inflater.inflate(R.layout.activity_sign_up_verification, container, false);
        extractArguments();
        initViews(view);
        setWizRocket();
        return view;
    }

    public void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEmail = args.getString(Constants.EMAIL_ID_KEY);
        }
    }

    public void initViews(View view) {
        mVerificationCodeLayout = (TextInputLayout) view.findViewById(R.id.textinput_verfication_code);
        mCodeEditText = (EditText) view.findViewById(R.id.code_verification_edit_text);
        mResendText = (TextView) view.findViewById(R.id.resend_code_button);
        mResendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMaterialDialog();
                resendSignUpVerificationCode();
            }
        });
        mVerifyButton = (Button) view.findViewById(R.id.verifyBtn);
        mVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVerifyBtnClicked();
            }
        });
    }

    // Executed when this class is instantiated , just like a member


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

    // Api for resend verification code
    private void resendSignUpVerificationCode() {

        com.explara_core.login.LoginScreenManager.getInstance().resendSignUpVerificationCode(getActivity().getApplicationContext(), mEmail, new com.explara_core.login.LoginScreenManager.ReSendSignUpCodeListener() {
            @Override
            public void onCodeResent(com.explara_core.login.login_dto.SignupResponseDto signupResponseDto) {
                if (getActivity() != null && signupResponseDto != null) {
                    dismissMaterialDialog();
                    Toast.makeText(getActivity(), signupResponseDto.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeResendFailed() {
                dismissMaterialDialog();
                Toast.makeText(getActivity(), "Oops! Could not resend verification code.", Toast.LENGTH_SHORT).show();
            }
        }, TAG);
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

    // Api call for login with username and password
    private void verifyCode() {
        LoginScreenManager.getInstance().verifySignUpCode(getActivity().getApplicationContext(), mCode, mEmail, new LoginScreenManager.VerifySignUpCodeListener() {
            @Override
            public void onCodeVerified(LoginResponseDto loginResponse) {
                if (getActivity() != null && loginResponse != null) {
                    if (loginResponse.status.equals(Constants.STATUS_ERROR)) {
                        dismissMaterialDialog();
                        Toast.makeText(getContext(), loginResponse.message, Toast.LENGTH_SHORT).show();
                        //AppUtility.createSnackWithMessage(getActivity().findViewById(R.id.forgot_verification_activity_relative_layout), loginResponse.message);
                    } else {
                        if (loginResponse.account != null) {
                            if (loginResponse.account.accessToken != null && !loginResponse.account.accessToken.isEmpty()) {
                                saveUserDetailsWithAccessToken(loginResponse, CleverTapHelper.EventNames.SIGN_UP_EVENT, ConstantKeys.FromScreen.SIGNUP_SCREEN);
                                PreferenceManager.getInstance(getContext()).setAccountVerified(Constants.ACCOUNT_VERIFIED);
                            } else {
                                dismissMaterialDialog();
                                Toast.makeText(getContext(), "Singup Failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            dismissMaterialDialog();
                            Toast.makeText(getContext(), "Singup Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCodeVerifyFailed(VolleyError error) {
                if (getActivity() != null) {
                    dismissMaterialDialog();
                    Log.d(TAG, "" + error);
                    Toast.makeText(getActivity(), "Oops! Could not verify the code.", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    }

    private boolean checkFormData() {

        boolean valid = true;
        if (mCodeEditText.getText() == null || mCodeEditText.getText().toString().isEmpty()) {

            mVerificationCodeLayout.setError(getActivity().getResources().getString(R.string.empty_verification_code));
            //Constants.createToastWithMessage(getActivity(), getActivity().getString(R.string.empty_verification_code));
            valid = false;
        } else {
            mVerificationCodeLayout.setError(null);
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

    @Override
    public void refresh() {

    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
//        if (CompatibilityUtil.isMarshmallow()) {
//            Toast.makeText(getContext(), "marshmallow device", Toast.LENGTH_LONG).show();
//
//        } else {
//            registerForSms();
//        }


        if (CompatibilityUtil.isMarshmallow()) {

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_LONG).show();
                    registerForSms();

                }
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_SMS)) {
                        Toast.makeText(getContext(), "Permission is Required", Toast.LENGTH_LONG).show();
                    }
                }
//                requestPermissions(new String[]{Manifest.permission.READ_SMS}, smsReadPermission);
                requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, smsReceivePermission);

            }

        } else {
            registerForSms();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == smsReceivePermission) {
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
        if (registerforSms == 2) {
            getActivity().unregisterReceiver(smsReceiver);
        }
    }

    private void registerForSms() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);
        registerforSms = 2;
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

