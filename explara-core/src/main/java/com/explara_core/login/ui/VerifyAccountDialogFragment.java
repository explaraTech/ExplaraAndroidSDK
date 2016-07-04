package com.explara_core.login.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.telephony.SmsMessage;
import android.text.TextUtils;
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
import com.explara_core.common_dto.ProfileResponse;
import com.explara_core.login.LoginScreenManager;
import com.explara_core.utils.Constants;
import com.explara_core.utils.PreferenceManager;
import com.explara_core.utils.Utility;
import com.google.gson.Gson;

/**
 * Created by debasishpanda on 12/09/15.
 */
public class VerifyAccountDialogFragment extends DialogFragment {

    private static final String SMS_SENDER = "EXPLRA";
    private static final String TAG = VerifyAccountDialogFragment.class.getSimpleName();
    //private String mEventId;
    private Button mDismissBtn;
    private Button mVerifyBtn;
    private String mMultipleEmailIds;
    private TextInputLayout mCodeInputLayout;
    private EditText mCodeEditText;
    private MaterialDialog mMaterialProgressDialog;
    private String mCode = "";
    private boolean isVerificationCodeSent = false;
    private TextView mResendText;
    private TextView mAccountId;
    private TextView mAccountName;
    private TextView mAccountMobile;
    private String mEmailId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_verify_account_dialog, container, false);
        getDialog().setCanceledOnTouchOutside(false);
        setDialogTitle();
        extractArguments();
        initView(rootView);
        return rootView;
    }

    public static VerifyAccountDialogFragment newInstance(String emailId) {
        VerifyAccountDialogFragment verifyAccountDialogFragment = new VerifyAccountDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EMAIL_ID, emailId);
        verifyAccountDialogFragment.setArguments(bundle);
        return verifyAccountDialogFragment;
    }

    public void extractArguments(){
        Bundle args = getArguments();
        if(args != null){
            mEmailId = args.getString(Constants.EMAIL_ID);
        }
    }

    public void setDialogTitle(){
        getDialog().setTitle("Verify Account");
    }

    public void initView(View view){
        mAccountId = (TextView)view.findViewById(R.id.account_id);
        mAccountName = (TextView)view.findViewById(R.id.account_name);
        mAccountMobile = (TextView)view.findViewById(R.id.account_mobile);
        if(!TextUtils.isEmpty(PreferenceManager.getInstance(getContext()).getEmail())){
            mAccountId.setText("AccountId -" + PreferenceManager.getInstance(getContext()).getEmail());
        }else{
            mAccountId.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(PreferenceManager.getInstance(getContext()).getUserName())){
            mAccountName.setText("Name -" + PreferenceManager.getInstance(getContext()).getUserName());
        }else{
            mAccountName.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(PreferenceManager.getInstance(getContext()).getPhoneNo())){
            mAccountMobile.setText("Phone No. -" + PreferenceManager.getInstance(getContext()).getPhoneNo());
        }else{
            mAccountMobile.setVisibility(View.GONE);
        }

        mCodeInputLayout = (TextInputLayout)view.findViewById(R.id.code_text_input);
        mCodeEditText = (EditText)view.findViewById(R.id.code_edit_text);
        mResendText = (TextView)view.findViewById(R.id.resend_code_button);
        mResendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMaterialDialog();
                sendAccountVerificationCode();
            }
        });
        mDismissBtn  = (Button)view.findViewById(R.id.dismiss_btn);
        mDismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mVerifyBtn     = (Button)view.findViewById(R.id.verify_btn);
        mVerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMaterialDialog();
                if(Utility.isNetworkAvailable(getContext()))
                if(!isVerificationCodeSent){
                    sendAccountVerificationCode();
                }else{
                    onVerifyBtnClicked();
                }

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

    // Api for account verification code
    private void sendAccountVerificationCode() {
        com.explara_core.login.LoginScreenManager.getInstance().resendSignUpVerificationCode(getActivity().getApplicationContext(),mEmailId, new com.explara_core.login.LoginScreenManager.ReSendSignUpCodeListener() {
            @Override
            public void onCodeResent(com.explara_core.login.login_dto.SignupResponseDto signupResponseDto) {
                if (getActivity() != null && signupResponseDto != null) {
                    isVerificationCodeSent = true;
                    if (mMaterialProgressDialog != null && mMaterialProgressDialog.isShowing()) {
                        mMaterialProgressDialog.dismiss();
                    }
                    mCodeInputLayout.setVisibility(View.VISIBLE);
                    mResendText.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), signupResponseDto.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeResendFailed() {
                if(getActivity() != null) {
                    isVerificationCodeSent = true;
                    if (mMaterialProgressDialog != null && mMaterialProgressDialog.isShowing()) {
                        mMaterialProgressDialog.dismiss();
                    }
                    Toast.makeText(getActivity(), "Oops! Could not resend verification code.", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    }

    public void onVerifyBtnClicked() {
        //googleAnalyticsSendAnEvent();
        if (checkFormData()) {
            mCode = mCodeEditText.getText().toString();
            showMaterialDialog();
            dismissKeyboard();
            verifyCode();
        }
    }

    // Api call for login with username and password
    private void verifyCode() {
       LoginScreenManager.getInstance().verifySignUpCode(getActivity().getApplicationContext(), mCode,mEmailId, new LoginScreenManager.VerifySignUpCodeListener() {
            @Override
            public void onCodeVerified(com.explara_core.login.login_dto.LoginResponseDto loginResponse) {
                if(getActivity() != null && loginResponse != null) {
                    if (mMaterialProgressDialog != null && mMaterialProgressDialog.isShowing()) {
                        mMaterialProgressDialog.dismiss();
                    }
                    if (loginResponse.status.equals(Constants.STATUS_ERROR)) {
                        Toast.makeText(getContext(),loginResponse.message,Toast.LENGTH_SHORT).show();
                        //AppUtility.createSnackWithMessage(getActivity().findViewById(R.id.forgot_verification_activity_relative_layout), loginResponse.message);
                    } else {
                        if(loginResponse.account != null){
                            if(loginResponse.account.accessToken != null && !loginResponse.account.accessToken.isEmpty()){
                                sendLoggedInWithUserNameToCleverTap(loginResponse);

                                // Storing AccessToken into PreferenceManager
                                PreferenceManager.getInstance(getActivity().getApplicationContext()).setAccessToken(loginResponse.account.accessToken);
                                PreferenceManager.getInstance(getContext()).setAccountVerified(Constants.ACCOUNT_VERIFIED);

                                // save user access_token to local settings
                                Constants.ACCESS_TOKEN_VALUE = loginResponse.account.accessToken;
                                Constants.saveDataToLocalSettings(getActivity(), Constants.ACCESS_TOKEN_KEY, Constants.ACCESS_TOKEN_VALUE);
                                if(loginResponse.bank != null){
                                    saveBankDetailsInLocal(loginResponse.bank);
                                }
                                // get user details from access_token
                                saveUserDetailsInLocal(com.explara_core.login.util.LoginHelper.getUserProfile(loginResponse));
                            }else{
                                Toast.makeText(getContext(),"Singup Failed",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getContext(),"Singup Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCodeVerifyFailed(VolleyError error) {
                if (getActivity() != null) {
                    if (mMaterialProgressDialog != null && mMaterialProgressDialog.isShowing()) {
                        mMaterialProgressDialog.dismiss();
                    }
                    Toast.makeText(getActivity(), "Oops! Could not verify the code.", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    }

    public void saveBankDetailsInLocal(com.explara_core.login.login_dto.LoginResponseDto.Bank bank){
        Gson gson = new Gson();
        String bankDetailsJson = gson.toJson(bank);
        PreferenceManager.getInstance(getActivity().getApplicationContext()).setBankDetails(bankDetailsJson);
    }

    public void saveUserDetailsInLocal(ProfileResponse profileResponse) {

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

        // navigate to collection page
        //navigateToCollectionsScreen();

        // navigate to next page
        if(Constants.CONFIRMATION_PAGE.equals(LoginNewFragment.mRedirect)){
            lunchMyTicketsPage();
        }else {
            if (LoginNewFragment.mSource.equals("login")) {
                navigateToCollectionsScreen();
            } else {
                navigateToTopicsPage();
            }
        }
    }

    private void lunchMyTicketsPage(){ //reLook into this
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME,"com.explara.android.tickets.ui.TicketsActivity"));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void navigateToTopicsPage() {//reLook into this

        Intent intent = new Intent();
        intent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME,"com.explara.android.topics.ui.PersonalTopicsActivity"));
        intent.putExtra("sourcePage", "signupPage");
        intent.putExtra(Constants.REDIRECT,LoginNewFragment.mRedirect);
        startActivity(intent);
        getActivity().finish();
    }

    private void navigateToCollectionsScreen() {//reLook into this
        if (Constants.LOCATION_NAME == null || Constants.LOCATION_NAME.isEmpty()) {
            Intent i = new Intent();
            i.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME,"com.explara.android.selectCity.ui.SelectLocationActivity"));
            startActivity(i);
        } else {
            Intent i = new Intent();
            i.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME,"com.explara.android.personalizedHome.ui.PersonalizeScreenActivity"));
            startActivity(i);
        }
        getActivity().finish();
    }

    private void sendLoggedInWithUserNameToCleverTap(com.explara_core.login.login_dto.LoginResponseDto loginResponse) { //reLook into this
        /*try {
            CleverTapAPI cleverTapAPI = CleverTapAPI.getInstance(getActivity().getApplicationContext());
            Map<String, Object> profileUpdate = new HashMap<>();
            profileUpdate.put(CleverTapHelper.ProfileProperty.NAME, loginResponse.account.getName()); // String
            profileUpdate.put(CleverTapHelper.ProfileProperty.IDENTITY, loginResponse.account.accessToken); // String or number
            profileUpdate.put(CleverTapHelper.ProfileProperty.EMAIL, loginResponse.account.emailId); // Email address of the user
            profileUpdate.put(CleverTapHelper.ProfileProperty.PHONE, loginResponse.account.mobileNumber); // Phone(without the country code)
            profileUpdate.put(CleverTapHelper.ProfileProperty.PROFILE_IMAGE, loginResponse.account.profileImage);
            cleverTapAPI.profile.push(profileUpdate);

            HashMap<String, Object> loginAction = new HashMap<>();
            loginAction.put(CleverTapHelper.EventPropertyNames.SOURCE, "EMAIL");
            loginAction.put(CleverTapHelper.EventPropertyNames.LOGGED_EMAIL_ID, loginResponse.account.emailId);
            cleverTapAPI.event.push(CleverTapHelper.EventNames.SIGN_UP_EVENT, loginAction);

        } catch (CleverTapMetaDataNotFoundException | CleverTapPermissionsNotSatisfied e) {
            e.printStackTrace();
        }*/
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        registerForSms();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(smsReceiver);
    }

    private void registerForSms() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);
        getActivity().registerReceiver(smsReceiver, filter);
    }



}
