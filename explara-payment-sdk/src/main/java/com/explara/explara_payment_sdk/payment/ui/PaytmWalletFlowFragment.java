package com.explara.explara_payment_sdk.payment.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;
import com.citrus.sdk.classes.Amount;
import com.explara.explara_payment_sdk.R;
import com.explara.explara_payment_sdk.common.PaymentBaseFragment;
import com.explara.explara_payment_sdk.payment.PaymentManager;
import com.explara.explara_payment_sdk.payment.dto.OtpResponseDto;
import com.explara.explara_payment_sdk.payment.dto.PayTmUserProfile;
import com.explara.explara_payment_sdk.payment.dto.PaytmPayMentResposnseDto;
import com.explara.explara_payment_sdk.payment.dto.PaytmUserTempDetails;
import com.explara.explara_payment_sdk.payment.dto.ValidateOtpResposneDto;
import com.explara.explara_payment_sdk.payment.io.PaymentConnectionManager;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.Log;
import com.explara_core.utils.PreferenceManager;
import com.explara_core.utils.Utility;
import com.explara_core.utils.WidgetsColorUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ananthasooraj on 1/25/16.
 */

public class PaytmWalletFlowFragment extends PaymentBaseFragment {

    private static final String TAG = PaytmWalletFlowFragment.class.getSimpleName();
    private static final String OTP_SENDER = "-IPAYTM";

    private EditText mEmailId;
    private EditText mMobileNumber;
    private EditText mPassword;
    private EditText mEditTextMoney;

    private TextInputLayout mTextInputLayoutEmailID, mTextInputLayoutPhoneNumber, mTextInputLayoutEnterOtp;

    private Button mSignInButton, mSendOtpButton, mProceedButton;
    private Button mRechargeWalletBtn;
    private Button mDoneTransactionBtn;

    private View.OnClickListener mAddMoneyClickListener, mPaywithPaytmClickListener;

    private CheckBox mCheckboxTermsOfService;

    private View mStepOneLayout;
    public View mStepTwoLayout;
    private View mStepThreeLayout;
    private View mAddMoneyView;


    private TextView mWalletBalanceTxt;
    private TextView mTicketPriceTxt;
    private TextView mTxtAmountAdd100, mTxtAmountAdd500, mTxtAmountAdd1000;
    private TextView mStep1Txt;
    private TextView mStep2Txt;
    private TextView mStep3Txt;
    private TextView mTotalAmountToPayTxt;
    private TextView mPaymentSuccessText;

    private ProgressBar mProgressBar;
    private ProgressBar mWalletStepProgressBar;
    private ProgressBar mTransactionSucessProgresssBar;


    private String mEventId;

    private PaymentManager paymentManager = PaymentManager.getInstance();
    private PaytmUserTempDetails paytmUserTempDetails = new PaytmUserTempDetails();

    private MaterialDialog mMaterialDialog;

    private FrameLayout mPaytmStepsLayout;

    private WalletFragment.WalletCallBacks mWalletCallBacks;


    @Override
    public void refresh() {

    }

    public PaytmWalletFlowFragment() {
    }


    public static PaytmWalletFlowFragment newInstance(Intent intent) {
        PaytmWalletFlowFragment paytmWalletFlowFragment = new PaytmWalletFlowFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString(Constants.EVENT_ID, intent.getStringExtra(Constants.EVENT_ID));
        paytmWalletFlowFragment.setArguments(bundle);
        return paytmWalletFlowFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.paytm_wallet_layout, container, false);
        extractArguments();
        initViews(view);
        return view;
    }

    private void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEventId = args.getString(Constants.EVENT_ID);
        }
    }

    private void initViews(View view) {
        mEmailId = (EditText) view.findViewById(R.id.paytm_email_id);
        mMobileNumber = (EditText) view.findViewById(R.id.paytm_phone_number);
        mPassword = (EditText) view.findViewById(R.id.password);
        mEditTextMoney = (EditText) view.findViewById(R.id.amount_edt_txt);

        mSendOtpButton = (Button) view.findViewById(R.id.sendOtpButton);
        mSignInButton = (Button) view.findViewById(R.id.sign_in_button);
        mProceedButton = (Button) view.findViewById(R.id.proceed_btn);
        mRechargeWalletBtn = (Button) view.findViewById(R.id.recharge_btn);
        mDoneTransactionBtn = (Button) view.findViewById(R.id.done_transaction_btn);

        mTextInputLayoutEmailID = (TextInputLayout) view.findViewById(R.id.textinput_emailId);
        mTextInputLayoutPhoneNumber = (TextInputLayout) view.findViewById(R.id.textinput_phoneNumber);
        mTextInputLayoutEnterOtp = (TextInputLayout) view.findViewById(R.id.textinput_enterOtp);

        mCheckboxTermsOfService = (CheckBox) view.findViewById(R.id.agree_to_t_and_c_text);

        mWalletBalanceTxt = (TextView) view.findViewById(R.id.balance_amount_txt);
        mTicketPriceTxt = (TextView) view.findViewById(R.id.ticket_price_txt);
        mTxtAmountAdd100 = (TextView) view.findViewById(R.id.amount_1000);
        mTxtAmountAdd500 = (TextView) view.findViewById(R.id.amount_1500);
        mTxtAmountAdd1000 = (TextView) view.findViewById(R.id.amount_2000);
        mStep1Txt = (TextView) view.findViewById(R.id.step_one);
        mStep2Txt = (TextView) view.findViewById(R.id.step_two);
        mStep3Txt = (TextView) view.findViewById(R.id.step_three);
        mTotalAmountToPayTxt = (TextView) view.findViewById(R.id.txt_grand_total);
        mPaymentSuccessText = (TextView) view.findViewById(R.id.payment_success_text);

        mStepOneLayout = view.findViewById(R.id.step_one_layout);
        mStepTwoLayout = view.findViewById(R.id.step_two_layout);
        mStepThreeLayout = view.findViewById(R.id.step_three_layout);
        mAddMoneyView = view.findViewById(R.id.add_money_layout);

        mPaytmStepsLayout = (FrameLayout) view.findViewById(R.id.paytm_step_layout);

        mProgressBar = (ProgressBar) view.findViewById(R.id.balance_progress_bar);
        //mProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.accentColor), PorterDuff.Mode.SRC_IN);
        WidgetsColorUtil.setProgressBarTintColor(mProgressBar, getResources().getColor(R.color.accentColor));
        mTransactionSucessProgresssBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mWalletStepProgressBar = (ProgressBar) view.findViewById(R.id.progressBar_paytm_wallet);

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForWalletSignUpData() && checkForOtpData()) {
                    mWalletCallBacks.verifyOtp(mPassword.getText().toString());
                    showMaterialDialog();
                }
            }
        });

        mSendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForWalletSignUpData()) {
                    mWalletCallBacks.sendOtpClicked(mEmailId.getText().toString(), mMobileNumber.getText().toString());
                    mPassword.setVisibility(View.VISIBLE);
                    mCheckboxTermsOfService.setVisibility(View.VISIBLE);
                    mSignInButton.setVisibility(View.VISIBLE);
                    mSendOtpButton.setText("Resend OTP");
                    mSendOtpButton.setTag("SendClicked");
                    paytmUserTempDetails.email = mEmailId.getText().toString();
                    paytmUserTempDetails.phoneNum = mMobileNumber.getText().toString();
                    paytmUserTempDetails.showEnterOtp = true;
                }

            }
        });

        if (paytmUserTempDetails.showEnterOtp) {
            mPassword.setVisibility(View.VISIBLE);
            mCheckboxTermsOfService.setVisibility(View.VISIBLE);
            mSignInButton.setVisibility(View.VISIBLE);
            mSendOtpButton.setText("Resend OTP");
        }

        mEmailId.setText(PreferenceManager.getInstance(getContext()).getEmail());
        mMobileNumber.setText(PreferenceManager.getInstance(getContext()).getPhoneNo());
        if (!TextUtils.isEmpty(paytmUserTempDetails.email))
            mEmailId.setText(paytmUserTempDetails.email);
        if (!TextUtils.isEmpty(paytmUserTempDetails.phoneNum))
            mMobileNumber.setText(paytmUserTempDetails.phoneNum);

        initialzeCallbacks();

        setupClickListeners();

        if (!PreferenceManager.getInstance(getActivity().getApplicationContext()).getPayTmAccessToken().isEmpty()) {
            paytmUserbalance();
        } else {
            mStepTwoLayout.setVisibility(View.GONE);
            mStepOneLayout.setVisibility(View.VISIBLE);
            mPaytmStepsLayout.setVisibility(View.VISIBLE);
        }
    }

    public void paytmUserbalance() {
        showMaterialDialog();
        paymentManager.getPayTmUserDetails(getContext(), PreferenceManager.getInstance(getContext()).getPayTmAccessToken(), new PaymentManager.FetchPaytmProfileListener() {
            @Override
            public void onFetchPaytmUserProfile(PayTmUserProfile payTmUserProfile) {

                if (getActivity() != null) {
                    if (payTmUserProfile != null && payTmUserProfile.WALLETBALANCE != null && !payTmUserProfile.WALLETBALANCE.isEmpty()) {
                        if (mMaterialDialog != null && mMaterialDialog.isShowing()) {
                            mMaterialDialog.dismiss();
                        }

                        mWalletStepProgressBar.setVisibility(View.GONE);
                        mStep1Txt.setBackgroundResource(R.drawable.wallet_shape_gray);
                        mStep2Txt.setTextColor(getResources().getColor(R.color.white));
                        mStep2Txt.setBackgroundResource(R.drawable.wallet_step_bg);
                        mPaytmStepsLayout.setVisibility(View.VISIBLE);
                        mStepOneLayout.setVisibility(View.GONE);
                        mStepTwoLayout.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                        mWalletBalanceTxt.setVisibility(View.VISIBLE);
                        mWalletBalanceTxt.setText(payTmUserProfile.WALLETBALANCE);
                        mTicketPriceTxt.setVisibility(View.VISIBLE);

                        Double ticketPrice = null;

                        if (TicketsManager.getInstance().mTotal != null && !TicketsManager.getInstance().mTotal.toString().isEmpty()) {
                            ticketPrice = Double.parseDouble(TicketsManager.getInstance().getGrandTotal());
                        }

                        if (ticketPrice != null && !ticketPrice.toString().isEmpty()) {
                            mTicketPriceTxt.setText((String.format("%s %s", getContext().getString(R.string.rupee_symbol), ticketPrice)));
                            if (Double.valueOf(payTmUserProfile.WALLETBALANCE) < ticketPrice) {
                                mProceedButton.setText("Add Money");
                                mProceedButton.setOnClickListener(mAddMoneyClickListener);
                            } else {
                                mProceedButton.setText("Proceed");
                                mProceedButton.setOnClickListener(mPaywithPaytmClickListener);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFetchpaytmUserProfileFailed(VolleyError volleyError) {
                if (getActivity() != null) {
                    Log.d(TAG, "" + volleyError);

                    if (mMaterialDialog != null && mMaterialDialog.isShowing()) {
                        mMaterialDialog.dismiss();
                    }
                }
            }
        }, TAG);
    }

    public void showMaterialDialog() {
        mMaterialDialog = new MaterialDialog.Builder(getContext())
                .content("Please wait..")
                .cancelable(false)
                .progress(true, 0)
                .show();
    }

    private void initialzeCallbacks() {
        mWalletCallBacks = new WalletFragment.WalletCallBacks() {
            @Override
            public void sendOtpClicked(String emailId, String phoneNum) {
                PaymentManager.getInstance().requestForOtp(getActivity().getApplicationContext(), new PaymentManager.RequestOtpListener() {
                    @Override
                    public void onOtpReadSuccess(OtpResponseDto otpResponseDto) {
                        Toast.makeText(getActivity(), otpResponseDto.message, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onOtpFailed(VolleyError volleyError) {
                        Log.d(TAG, volleyError.getMessage());
                    }
                }, emailId, phoneNum, TAG);
            }

            @Override
            public void verifyOtp(String otp) {

                final PaymentManager paymentManager = PaymentManager.getInstance();
                paymentManager.validateOtp(getActivity().getApplicationContext(), new PaymentManager.ValidatePayTmOtpListener() {
                    @Override
                    public void onOtpReadSuccess(ValidateOtpResposneDto validateOtpResposneDto) {

                        if (getActivity() != null && validateOtpResposneDto != null && validateOtpResposneDto.access_token != null) {
                            Log.d(TAG, validateOtpResposneDto.access_token);
                            PreferenceManager.getInstance(getActivity()).setPayTmAccessToken(validateOtpResposneDto.access_token);
                            paymentManager.getPayTmUserDetails(getActivity(), validateOtpResposneDto.access_token, new PaymentManager.FetchPaytmProfileListener() {
                                @Override
                                public void onFetchPaytmUserProfile(PayTmUserProfile payTmUserProfile) {
                                    if (mMaterialDialog != null && mMaterialDialog.isShowing()) {
                                        mMaterialDialog.dismiss();
                                    }
                                    mPaytmStepsLayout.setVisibility(View.VISIBLE);
                                    Double ticketPrice = TicketsManager.getInstance().mTotal;
                                    mStepOneLayout.setVisibility(View.GONE);
                                    mStep1Txt.setTextColor(getResources().getColor(R.color.white));
                                    mStep1Txt.setBackgroundResource(R.drawable.wallet_shape_gray);

                                    mStep2Txt.setTextColor(getResources().getColor(R.color.white));
                                    mStep2Txt.setBackgroundResource(R.drawable.wallet_step_bg);


                                    mStepTwoLayout.setVisibility(View.VISIBLE);
                                    mStepThreeLayout.setVisibility(View.GONE);

                                    mProgressBar.setVisibility(View.GONE);
                                    mWalletStepProgressBar.setVisibility(View.GONE);
                                    mWalletBalanceTxt.setVisibility(View.VISIBLE);
                                    mWalletBalanceTxt.setText(payTmUserProfile.WALLETBALANCE);
                                    mTicketPriceTxt.setText(String.format("%s %s", getContext().getString(R.string.rupee_symbol), ticketPrice));


                                    if (Double.valueOf(payTmUserProfile.WALLETBALANCE) < ticketPrice) {
                                        Toast.makeText(getActivity(), "Please Add Money to Wallet", Toast.LENGTH_LONG).show();
                                        mProceedButton.setText("Add money");
                                        mProceedButton.setOnClickListener(mAddMoneyClickListener);
                                    } else {
                                        mProceedButton.setOnClickListener(mPaywithPaytmClickListener);
                                    }
                                }

                                @Override
                                public void onFetchpaytmUserProfileFailed(VolleyError volleyError) {
                                    if (getActivity() != null) {
                                        Log.d(TAG, "VolleyError" + volleyError);
                                    }

                                    if (mMaterialDialog != null && mMaterialDialog.isShowing()) {
                                        mMaterialDialog.dismiss();
                                    }
                                }
                            }, TAG);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Ooops Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onOtpFailed(VolleyError volleyError) {
                        if (getActivity() != null) {
                            Log.d(TAG, "" + volleyError);
                        }
                    }
                }, otp, paymentManager.otpResposneDto.state, TAG);
            }

            @Override
            public void expandGroup(int groupPosition) {

            }
        };
    }


    public void setupClickListeners() {


        mPaywithPaytmClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //proceedTwoStepThree();
                payWithPaytm();
            }
        };


        mAddMoneyClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStepOneLayout.setVisibility(View.GONE);
                mStepTwoLayout.setVisibility(View.GONE);
                mAddMoneyView.setVisibility(View.VISIBLE);


                mTxtAmountAdd100.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "===100===");
                        mEditTextMoney.setText("100");
                    }
                });

                mTxtAmountAdd500.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "===500===");
                        mEditTextMoney.setText("500");
                    }
                });

                mTxtAmountAdd1000.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "===1500===");
                        mEditTextMoney.setText("1500");
                    }
                });


                mRechargeWalletBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String amount = mEditTextMoney.getText().toString();
                        if (!amount.trim().isEmpty() && amount.trim() != null) {
                            Double enteredAmount = Double.parseDouble(amount);
                            if (enteredAmount > 0) {
                                addMoney(amount);
                            } else {
                                Constants.createToastWithMessage(getContext(), getContext().getString(R.string.empty_wallet_amout));
                            }
                        } else {
                            Constants.createToastWithMessage(getContext(), getContext().getString(R.string.empty_wallet_amout));
                        }
                    }
                });
            }
        };
    }

    private void proceedTwoStepThree() {
        final Amount amountToPay = new Amount(TicketsManager.getInstance().mDiscountResponse.getCart().getGrandTotal());
        mStepTwoLayout.setVisibility(View.GONE);
        mWalletStepProgressBar.setVisibility(View.GONE);
        mStepThreeLayout.setVisibility(View.VISIBLE);
        mDoneTransactionBtn.setText("Proceed");
        mStep2Txt.setBackgroundResource(R.drawable.wallet_shape_gray);
        mStep2Txt.setTextColor(getResources().getColor(R.color.white));

        mTransactionSucessProgresssBar.setVisibility(View.GONE);
        mTotalAmountToPayTxt.setText(amountToPay.getValueAsDouble() + "");
        mPaymentSuccessText.setText("Your transaction is successful. Proceed to continue");

        mStep3Txt.setBackgroundResource(R.drawable.wallet_step_bg);
        mStep3Txt.setTextColor(getResources().getColor(R.color.white));

        mDoneTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payWithPaytm();
            }
        });
    }

    private void addMoney(String amount) {
        String accessToken = PreferenceManager.getInstance(getContext()).getPayTmAccessToken();
        PaymentConnectionManager paymentConnectionManager = new PaymentConnectionManager();
        Intent intent = new Intent(getContext(), WebviewActivity.class);
        intent.putExtra(ConstantKeys.BundleKeys.URL_STRING, paymentConnectionManager.addMoneyForPaytmUrl(accessToken, amount));
        intent.putExtra(ConstantKeys.BundleKeys.FROM_SCREEN, getActivity().getIntent().getIntExtra(ConstantKeys.BundleKeys.FROM_SCREEN, ConstantKeys.FromScreen.WALLET_SCREEN));
        getActivity().startActivityForResult(intent, ConstantKeys.REQUEST_CODES.PAY_TM_ADD_MONEY_REQUEST_CODE);
    }


    private void payWithPaytm() {
        showMaterialDialog();
        if (Utility.isNetworkAvailable(getActivity())) {
            PaymentManager paymentManager = PaymentManager.getInstance();
            String accessToken = PreferenceManager.getInstance(getActivity()).getPayTmAccessToken();
            if (!TextUtils.isEmpty(accessToken)) {
                PaymentManager.getInstance().payWithPaytm(getActivity(), accessToken, TicketsManager.getInstance().mOrder.getOrderNo(), new PaymentManager.PayWithPaytmStatusListener() {
                    @Override
                    public void onPayWithPaytmStatusSuccess(PaytmPayMentResposnseDto payTmUserProfile) {
                        if (payTmUserProfile.Status.equals(ConstantKeys.PaymentOptionIds.TRANSACTION_SUCESS)) {
                            Log.d(TAG, "Transaction Success" + payTmUserProfile.getStatus());
                            if (mMaterialDialog != null && mMaterialDialog.isShowing()) {
                                mMaterialDialog.dismiss();
                            }
                            Intent intent = new Intent(getActivity(), ConfirmationOnlineActivity.class);
                            intent.putExtra(Constants.EVENT_ID, mEventId);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onPayWithPaytmStatusFailed(VolleyError volleyError) {
                        Log.d(TAG, "payMent failed");

                        if (mMaterialDialog != null && mMaterialDialog.isShowing()) {
                            mMaterialDialog.dismiss();
                        }
                    }
                }, TAG);
            } else {
                Intent intent = new Intent(getActivity(), WalletScreenActivity.class);
                intent.putExtra(ConstantKeys.BundleKeys.FROM_SCREEN, ConstantKeys.FromScreen.PAYMENT_SCREEN);
                getActivity().startActivity(intent);
            }

        } else {
            Toast.makeText(getActivity(), R.string.internet_check_msg, Toast.LENGTH_LONG).show();
            if (mMaterialDialog != null && mMaterialDialog.isShowing()) {
                mMaterialDialog.dismiss();
            }
        }
    }

    public boolean checkForWalletSignUpData() {
        boolean validateData = true;

        if (mEmailId.getText().toString().trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailId.getText().toString()).matches()) {
            mTextInputLayoutEmailID.setError("email address seems to be empty");
            validateData = false;
        } else {
            mTextInputLayoutEmailID.setError(null);
        }

        if (mMobileNumber.getText().toString().trim().isEmpty()) {
            mTextInputLayoutPhoneNumber.setError("mobile number seems to be empty");
            validateData = false;
        } else {
            mTextInputLayoutPhoneNumber.setError(null);
        }

        return validateData;
    }

    private boolean checkForOtpData() {
        boolean validateOtpInput = true;


        if (mPassword.getText().toString().trim().isEmpty()) {
            mTextInputLayoutEnterOtp.setError("OTP Seems to be empty");
            validateOtpInput = false;
        } else {
            mTextInputLayoutEnterOtp.setError(null);
        }

        if (!mCheckboxTermsOfService.isChecked()) {
            mCheckboxTermsOfService.setError(""); // need not to give error text, check box itself will show an error image
            validateOtpInput = false;
        } else {
            mCheckboxTermsOfService.setError(null);
        }
        return validateOtpInput;
    }

    private BroadcastReceiver smsReceiver = new BroadcastReceiver() {
        public static final String SMS_BUNDLE = "pdus";

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle intentExtras = intent.getExtras();
            if (intentExtras != null) {
                Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
                for (Object sm : sms) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sm);
                    if (smsMessage.getOriginatingAddress().toUpperCase().contains(OTP_SENDER)) {
                        abortBroadcast();
                        String otp = getOtp(smsMessage.getMessageBody());

                        mPassword.setText(otp);
                    }
                }
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        registerForSms();
    }

    public String getOtp(String message) {
        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(message);
        while (m.find()) {
            String integer = m.group();
            if (integer.length() == 6) {
                return integer;
            }
        }
        return "";
    }

    private void registerForSms() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);
        getActivity().registerReceiver(smsReceiver, filter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(smsReceiver);
    }

}
