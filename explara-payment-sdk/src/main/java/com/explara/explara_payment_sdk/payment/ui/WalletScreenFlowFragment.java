package com.explara.explara_payment_sdk.payment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.TransactionResponse;
import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.classes.CitrusException;
import com.citrus.sdk.payment.PaymentType;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.response.CitrusResponse;
import com.citrus.sdk.ui.activities.LoginFlowActivity;
import com.citrus.sdk.ui.fragments.AddMoneyFragment;
import com.citrus.sdk.ui.utils.CitrusFlowManager;
import com.citrus.sdk.ui.utils.UIConstants;
import com.citrus.sdk.ui.utils.Utils;
import com.explara.explara_payment_sdk.R;
import com.explara.explara_payment_sdk.common.PaymentBaseFragment;
import com.explara.explara_payment_sdk.utils.UrlConstants;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk_ui.tickets.ui.TicketsDetailActivity;
import com.explara_core.utils.Constants;
import com.explara_core.utils.FragmentHelper;
import com.explara_core.utils.Log;
import com.explara_core.utils.PreferenceManager;
import com.explara_core.utils.Utility;
import com.explara_core.utils.WidgetsColorUtil;
import com.orhanobut.logger.Logger;

/**
 * Created by anudeep on 28/12/15.
 */
public class WalletScreenFlowFragment extends PaymentBaseFragment {

    private static final String TAG = WalletScreenFlowFragment.class.getSimpleName();
    //    private ImageView mPaymentProcessStepImg;
    private EditText mEmailId, mMobileNum;
    private Button mLinkButton;
    private TextInputLayout mTextInputLayoutEmail, mTextInputLayoutMobileNo, mTextInputLayoutPassword;
    private AppCompatCheckBox mCheckBox;
    private EditText mPassword;
    private MaterialDialog mMaterialDialog;
    private TextView mForgotPassword;
    private TextView mTicketPriceTxt;
    private TextView mWalletBalanceTxt, mTotalPayText, mPaymentSuccessText;
    private Button mProceedBtn, mDoneTransactionBtn;

    private ProgressBar mBalanceProgressBar, mTransactionSucessProgresssBar;

    private View.OnClickListener mSignUpListener;
    private View.OnClickListener mSignInListener;
    private View.OnClickListener mAddMoneyClickListener;
    private View.OnClickListener mDoneTransactionClickListener;

    private View mStepOneLayout;
    private View mStepTwoLayout;
    private View mStepThreeLayout;
    private String mEventID;

    private TextView mStep1Txt;
    private TextView mStep2Txt;
    private TextView mStep3Txt;
    private boolean launchDefaultPayForCitrus;

    /**
     * Public Constructor for wallet screen flow
     */
    public WalletScreenFlowFragment() {

    }

    public static WalletScreenFlowFragment newInstance(Intent intent) {
        WalletScreenFlowFragment walletScreenFlowFragment = new WalletScreenFlowFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString(Constants.EVENT_ID, intent.getStringExtra(Constants.EVENT_ID));
        bundle.putBoolean(PaymentOptionsListFragment.LAUNCH_DEFAULT_PAY, intent.getBooleanExtra(PaymentOptionsListFragment.LAUNCH_DEFAULT_PAY, false));
        walletScreenFlowFragment.setArguments(bundle);
        return walletScreenFlowFragment;
    }

    @Override
    public void refresh() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wallet_screen_flow_fragment, container, false);
        extractArguments();
        initViews(view);
        return view;
    }

    public void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEventID = args.getString(Constants.EVENT_ID);
            launchDefaultPayForCitrus = args.getBoolean(PaymentOptionsListFragment.LAUNCH_DEFAULT_PAY, false);
        }
    }

    private void initViews(View view) {
//        mPaymentProcessStepImg = (ImageView) view.findViewById(R.id.payment_process_step);
        mEmailId = (EditText) view.findViewById(R.id.email_id);
        mPassword = (EditText) view.findViewById(R.id.password);
        mMobileNum = (EditText) view.findViewById(R.id.phoneEditText);
        mLinkButton = (Button) view.findViewById(R.id.sign_in_button);
        mCheckBox = (AppCompatCheckBox) view.findViewById(R.id.agree_to_t_and_c_text);
        mTextInputLayoutEmail = (TextInputLayout) view.findViewById(R.id.textinput_emailId);
        mTextInputLayoutMobileNo = (TextInputLayout) view.findViewById(R.id.textinput_phoneNumber);
        mTextInputLayoutPassword = (TextInputLayout) view.findViewById(R.id.textinput_enterpassword);
        mForgotPassword = (TextView) view.findViewById(R.id.forgotPasswordText);
        final CitrusClient citrusClient = CitrusClient.getInstance(getActivity());
        mStepOneLayout = view.findViewById(R.id.step_one_layout);
        mStepTwoLayout = view.findViewById(R.id.step_two_layout);
        mStepThreeLayout = view.findViewById(R.id.step_three_layout);
        mTotalPayText = (TextView) view.findViewById(R.id.txt_grand_total);
        mPaymentSuccessText = (TextView) view.findViewById(R.id.payment_success_text);
        mBalanceProgressBar = (ProgressBar) view.findViewById(R.id.balance_progress_bar);
        WidgetsColorUtil.setProgressBarTintColor(mBalanceProgressBar, getResources().getColor(R.color.accentColor));
        mTransactionSucessProgresssBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mTicketPriceTxt = (TextView) view.findViewById(R.id.ticket_price_txt);
        mStepTwoLayout.setVisibility(View.GONE);
        mStepThreeLayout.setVisibility(View.GONE);
        mWalletBalanceTxt = (TextView) view.findViewById(R.id.balance_amount_txt);
        mEmailId.setText(PreferenceManager.getInstance(getActivity()).getEmail());
        mMobileNum.setText(PreferenceManager.getInstance(getActivity()).getPhoneNo());
        mProceedBtn = (Button) view.findViewById(R.id.proceed_btn);
        mDoneTransactionBtn = (Button) view.findViewById(R.id.done_transaction_btn);

        mProceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isNetworkAvailable(getActivity().getApplicationContext())) {
                    handleProceedClicked();
                } else {
                    Constants.createToastWithMessage(getActivity().getApplicationContext(), "" + getString(R.string.internet_check_msg));
                }

            }
        });
        mStep1Txt = (TextView) view.findViewById(R.id.step_one);
        mStep2Txt = (TextView) view.findViewById(R.id.step_two);
        mStep3Txt = (TextView) view.findViewById(R.id.step_three);

        setClickListeners(mForgotPassword, citrusClient);

    }

    private void handleProceedClicked() {
        final CitrusClient citrusClient = CitrusClient.getInstance(getActivity());

        mStepTwoLayout.setVisibility(View.GONE);
        mStepThreeLayout.setVisibility(View.VISIBLE);
        mDoneTransactionBtn.setText("Done");

        mStep2Txt.setBackgroundResource(R.drawable.wallet_shape_gray);
        mStep2Txt.setTextColor(getResources().getColor(R.color.white));

        mStep3Txt.setBackgroundResource(R.drawable.wallet_step_bg);
        mStep3Txt.setTextColor(getResources().getColor(R.color.white));


        citrusClient.isUserSignedIn(new Callback<Boolean>() {
            @Override
            public void success(Boolean success) {
                if (success) {
                    try {
                        final Amount amountToPay = new Amount(TicketsManager.getInstance().mDiscountResponse.getCart().getGrandTotal());
                        citrusClient.payUsingCitrusCash(new PaymentType.CitrusCash(amountToPay, UrlConstants.CITRUS_BILL_GENERATOR + TicketsManager.getInstance().mOrder.getOrderNo()),
                                new Callback<TransactionResponse>() {
                                    @Override
                                    public void success(final TransactionResponse transactionResponse) {

                                        Logger.d(TAG + " Success wallet payment" + transactionResponse.getMessage());
                                        mTransactionSucessProgresssBar.setVisibility(View.GONE);


                                        if (transactionResponse != null && !transactionResponse.getMessage().isEmpty()) {
                                            if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {

                                                Constants.createToastWithMessage(getActivity().getApplicationContext(), transactionResponse.getMessage());

                                                mPaymentSuccessText.setText("Your transaction is successful");
                                                mTotalPayText.setText(amountToPay.getValueAsDouble() + "");

                                                mDoneTransactionBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Log.d(TAG, "======++++++++======");
                                                        if (getActivity() != null) {
                                                            if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                                                                Log.d(TAG, transactionResponse.getJsonResponse());
                                                                Intent intent = new Intent(getActivity(), ConfirmationOnlineActivity.class);
                                                                intent.putExtra(Constants.EVENT_ID, mEventID);
                                                                startActivity(intent);
                                                            } else {
                                                                Toast.makeText(getActivity().getApplicationContext(), transactionResponse.getMessage(), Toast.LENGTH_LONG).show();
                                                                Intent intent = new Intent(getActivity(), TicketsDetailActivity.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    }
                                                });
                                            } else {
                                                dismissDialog();
                                                Constants.createToastWithMessage(getActivity().getApplicationContext(), "" + transactionResponse.getMessage());
                                            }
                                        } else {
                                            dismissDialog();
                                            Constants.createToastWithMessage(getActivity().getApplicationContext(), "" + transactionResponse.getMessage());
                                        }
                                        /*mDoneTransactionBtn.setText("Done");

                                        mTotalPayText.setText(am);*/

                                        /*if (getActivity() != null) {
                                            if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                                                Log.d(TAG, transactionResponse.getJsonResponse());
                                                Intent intent = new Intent(getActivity(), ConfirmationOnlineActivity.class);
                                                intent.putExtra(Constants.EVENT_ID, mEventID);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(getActivity().getApplicationContext(), transactionResponse.getMessage(), Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(getActivity(), TicketsDetailActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }
                                        }*/
                                        //
                                        // mListener.onWalletTransactionComplete
                                        // (transactionResponse);
//                                                walletPaymentComplete = true;
//                                                walletResultModel = new ResultModel(null,
//                                                        transactionResponse);
                                    }

                                    @Override
                                    public void error(CitrusError error) {
                                        Logger.d(TAG + " Could not process " + error.getMessage());
                                        dismissDialog();
                                        mTransactionSucessProgresssBar.setVisibility(View.GONE);
//                                                walletPaymentComplete = true;
//                                                walletResultModel = new ResultModel(error, null);
                                    }
                                });


                    } catch (CitrusException e) {
                        e.printStackTrace();
                        dismissDialog();
                    }


/*                    /*CitrusClient.getInstance(getActivity()).getBalance(new Callback<Amount>() {
                        @Override
                        public void success(Amount amount) {
                            Amount amountToPay = new Amount(TicketsManager.getInstance().mDiscountResponse.getCart().getGrandTotal());

                            if (amount.getValueAsDouble() >= Double.valueOf(TicketsManager.getInstance().getGrandTotal())) {

                                try {
                                    citrusClient.payUsingCitrusCash(new PaymentType.CitrusCash(amountToPay,
                                                    Constants.CITRUS_BILL_GENERATOR + PaymentManager.getInstance().mOrder.getOrderNo()),
                                            new Callback<TransactionResponse>() {


                                                @Override
                                                public void success(TransactionResponse transactionResponse) {
                                                    Logger.d(TAG + " Success wallet payment" + transactionResponse.getMessage());

                                                    if (getActivity() != null) {
                                                        if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                                                            Log.d(TAG, transactionResponse.getJsonResponse());
                                                            Intent intent = new Intent(getActivity(), ConfirmationOnlineActivity.class);
                                                            intent.putExtra(Constants.EVENT_ID, mEventID);
                                                            startActivity(intent);
                                                        } else {
                                                            Toast.makeText(getActivity().getApplicationContext(), transactionResponse.getMessage(), Toast.LENGTH_LONG).show();
                                                            Intent intent = new Intent(getActivity(), TicketsDetailActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                    //
                                                    // mListener.onWalletTransactionComplete
                                                    // (transactionResponse);
//                                                walletPaymentComplete = true;
//                                                walletResultModel = new ResultModel(null,
//                                                        transactionResponse);
                                                }

                                                @Override
                                                public void error(CitrusError error) {
                                                    Logger.d(TAG + " Could not process " + error.getMessage());
                                                    dismissDialog();
//                                                walletPaymentComplete = true;
//                                                walletResultModel = new ResultModel(error, null);
                                                }
                                            });


                                } catch (CitrusException e) {
                                    e.printStackTrace();
                                    dismissDialog();
                                }


                            } else {
                                launchAddMoneyScreen();
                            }
                        }

                        @Override
                        public void error(CitrusError error) {
                            Log.d(TAG, error.getMessage());
                            dismissDialog();
                        }
                    });*/
                } else {
                    //                    TODO start wallet login flow here
                    //                    mListener.navigateTo(WalletSignInFragment.newInstance(true), UIConstants
                    //                            .SCREEN_WALLET_LOGIN);
                    Intent intent = new Intent(getActivity(), LoginFlowActivity.class);
                    intent.putExtra(CitrusFlowManager.KEY_EMAIL, PreferenceManager.getInstance(getActivity()).getEmail());
                    intent.putExtra(CitrusFlowManager.KEY_MOBILE, PreferenceManager.getInstance(getActivity()).getPhoneNo());
                    intent.putExtra(CitrusFlowManager.KEY_STYLE, -1);
                    if (!TextUtils.isEmpty(TicketsManager.getInstance().getGrandTotal())) {
                        intent.putExtra(CitrusFlowManager.KEY_AMOUNT, TicketsManager.getInstance().getGrandTotal());
                    }
                    getActivity().startActivityForResult(intent, UIConstants.REQ_CODE_LOGIN_PAY);
                    Toast.makeText(getActivity(), "Not logged in ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void error(CitrusError error) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_LONG).show();
                    dismissDialog();
                }
            }
        });

    }

    private void launchAddMoneyScreen() {
        CitrusFlowManager.billGenerator = UrlConstants.CITRUS_BILL_GENERATOR + TicketsManager.getInstance().mOrder.getOrderNo();
        CitrusFlowManager.returnURL = UrlConstants.CITRUS_ADDMONEY_RETURN_URL;
        Toast.makeText(getActivity().getApplicationContext(), "Low balance Please Add Money ", Toast.LENGTH_LONG).show();
        FragmentHelper.replaceAndAddContentFragment(getActivity(), R.id.wallet_screen_flow_container,
                AddMoneyFragment.newInstance(true, ""), TAG);
    }

    private void setClickListeners(final TextView forgotPassword, final CitrusClient citrusClient) {
        mLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (Utility.isNetworkAvailable(getActivity())) {
                    if (validateWalletLoginDetails()) {
                        showMaterialDialog();
                        citrusClient.isUserSignedIn(new Callback<Boolean>() {
                            @Override
                            public void success(Boolean success) {
                                dismissDialog();
                                if (success) {
                                    getBalance();
                                } else {
                                    linkUserToCitrus(citrusClient, forgotPassword);
                                }
                            }

                            @Override
                            public void error(CitrusError error) {
                                Toast.makeText(getActivity(), "Linking failed", Toast.LENGTH_LONG).show();
                                dismissDialog();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), "" + getActivity().getResources().getString(R.string.internet_check_msg), Toast.LENGTH_SHORT).show();
                    dismissDialog();
                }


            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(v.getContext(), "Forgot Password", Toast.LENGTH_SHORT).show();
                citrusClient.resetPassword(mEmailId.getText().toString(), new Callback<CitrusResponse>() {
                    @Override
                    public void success(CitrusResponse citrusResponse) {
                        Log.d(TAG, "CitrusResponse" + citrusResponse.getMessage());
                        Log.d(TAG, "CitrusResponse" + citrusResponse.getStatus());

                        Toast.makeText(v.getContext(), "" + citrusResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void error(CitrusError error) {
                        Log.d(TAG, error.getMessage());
                        dismissDialog();
                    }
                });
            }
        });

        mSignUpListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isNetworkAvailable(getActivity())) {
                    showMaterialDialog();
                    CitrusClient.getInstance(getActivity()).signUp(mEmailId.getText().toString(), mMobileNum.getText().toString(),
                            mPassword.getText().toString().trim(), new
                                    Callback<CitrusResponse>() {

                                        @Override
                                        public void success(CitrusResponse citrusResponse) {

                                            Logger.d(TAG + " Sign up complete" + citrusResponse.getMessage());
                                            if (isAdded()) {
                                                dismissDialog();
                                                handleSignInSuccess();
//                                                showMaterialDialog();
//                                                getActivity().setResult(getActivity().RESULT_OK, new
//                                                        Intent());
//                                                getActivity().finish();
                                            }

                                        }

                                        @Override
                                        public void error(CitrusError error) {
                                            Logger.d(TAG + " Could not sign up " + error
                                                    .getMessage());
                                            Toast.makeText(getActivity(), "Sign Up Failed", Toast.LENGTH_LONG).show();
                                            dismissDialog();
                                        }
                                    });
                }
            }
        };

        mSignInListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isNetworkAvailable(getActivity())) {
                    if (checkForCitrusWalletSignupPassword(mPassword)) {
                        showMaterialDialog();
                        forgotPassword.setVisibility(View.VISIBLE);
                        citrusClient.signIn(mEmailId.getText().toString(), mPassword.getText().toString(), new Callback<CitrusResponse>() {
                            @Override
                            public void success(CitrusResponse citrusResponse) {
                                dismissDialog();
//                                getBalance();

                                handleSignInSuccess();
                            }

                            @Override
                            public void error(CitrusError error) {
                                Log.d(TAG, error.getMessage());
                                dismissDialog();
                            }
                        });
                    } else {
                        dismissDialog();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Check Internet", Toast.LENGTH_LONG).show();
                    dismissDialog();
                }
            }
        };
        mAddMoneyClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAddMoneyScreen();
                mDoneTransactionBtn.setOnClickListener(mDoneTransactionClickListener);
            }
        };

    }

    private void handleSignInSuccess() {

        mStep1Txt.setBackgroundResource(R.drawable.wallet_shape_gray);
        mStep1Txt.setTextColor(getResources().getColor(R.color.white));

        mStep2Txt.setBackgroundResource(R.drawable.wallet_step_bg);
        mStep2Txt.setTextColor(getResources().getColor(R.color.white));

        mStep3Txt.setBackgroundResource(R.drawable.wallet_shape_white_bg);
        mStep3Txt.setTextColor(getResources().getColor(R.color.black));

        mStepOneLayout.setVisibility(View.GONE);
        mStepTwoLayout.setVisibility(View.VISIBLE);
        mTicketPriceTxt.setText(TicketsManager.getInstance().mDiscountResponse.getCart().getGrandTotal());
        mProceedBtn.setVisibility(View.GONE);
        CitrusClient.getInstance(getActivity()).getBalance(new Callback<Amount>() {
            @Override
            public void success(Amount amount) {
                Log.d(TAG, " get Balance success " + amount.getValue());
                mProceedBtn.setVisibility(View.VISIBLE);
                mBalanceProgressBar.setVisibility(View.GONE);
                mWalletBalanceTxt.setVisibility(View.VISIBLE);
                mWalletBalanceTxt.setText(String.format("%s %s", getContext().getString(R.string.rupee_symbol), amount.getValueAsFormattedDouble("#.00")));
                if ((amount.getValueAsDouble() < Double.valueOf(TicketsManager.getInstance().getGrandTotal()))) {
                    mProceedBtn.setText("Add Money");
                    mProceedBtn.setOnClickListener(mAddMoneyClickListener);

                } else {
                    if (launchDefaultPayForCitrus) {
                        mProceedBtn.performClick();
                    }
                }
            }

            @Override
            public void error(CitrusError citrusError) {
                Log.d(TAG, " get Balance failure " + citrusError.getMessage());
                dismissDialog();
            }
        });
    }

    private void linkUserToCitrus(final CitrusClient citrusClient, final TextView forgotPassword) {
        showMaterialDialog();
        citrusClient.isCitrusMember(mEmailId.getText().toString(), mMobileNum.getText().toString(), new Callback<Boolean>() {
                    @Override
                    public void success(Boolean aBoolean) {
                        dismissDialog();
                        if (!aBoolean) {
                            mLinkButton.setOnClickListener(mSignUpListener);
                            handleCitrusMember(citrusClient);
                        } else {
                            showSignInDetails(forgotPassword, citrusClient);
                        }

                    }

                    @Override
                    public void error(CitrusError citrusError) {
                        dismissDialog();
                    }
                }
        );
    }

    private void dismissDialog() {
        if (mMaterialDialog != null) {
            mMaterialDialog.dismiss();
        }
    }

    private void showSignInDetails(TextView forgotPassword, CitrusClient citrusClient) {
        mLinkButton.setText("SIGN IN");
        mForgotPassword.setVisibility(View.VISIBLE);

        mPassword.setVisibility(View.VISIBLE);
        mPassword.requestFocus();
        mLinkButton.setOnClickListener(mSignInListener);

    }

    private void handleCitrusMember(CitrusClient citrusClient) {
        mLinkButton.setText("SIGN UP");
        mPassword.setVisibility(View.VISIBLE);
        mPassword.requestFocus();

        if (!mPassword.getText().toString().trim().isEmpty()) {
            citrusClient.signUp(mEmailId.getText().toString(), mMobileNum.getText().toString(), mPassword.getText().toString(), new Callback<CitrusResponse>() {
                @Override
                public void success(CitrusResponse citrusResponse) {
                    dismissDialog();
                    getBalance();
                }

                @Override
                public void error(CitrusError error) {
                    dismissDialog();
                }
            });
        } else {
            if (mMaterialDialog != null) {
                mMaterialDialog.dismiss();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        CitrusFlowManager.initCitrusConfig("33g3ywh2un-signup",
//                "c260cdcc277c0275e7a966e784b534f9", "33g3ywh2un-signin",
//                "7f96ae671b246d8b716025bea1fd8361", getResources().getColor(R.color.white),
//                getActivity(), Environment.SANDBOX, "33g3ywh2un", "", Constants.CITRUS_RETURN_URL);

        startLoginProcess();
    }

    private void startLoginProcess() {
        CitrusClient citrusClient = CitrusClient.getInstance(getActivity());

        citrusClient.isUserSignedIn(new Callback<Boolean>() {
            @Override
            public void success(Boolean isLoggedIn) {
                if (isLoggedIn) {
                    handleSignInSuccess();
                } else {

                }

            }

            @Override
            public void error(CitrusError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public boolean validateWalletLoginDetails() {
        boolean validateData = true;

        if (mEmailId.getText().toString().trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmailId.getText().toString()).matches()) {
            mTextInputLayoutEmail.setError("Email address seems to be empty");
            validateData = false;
        } else {
            mTextInputLayoutEmail.setError(null);
        }


        if (mMobileNum.getText().toString().trim().isEmpty()) {
            mTextInputLayoutMobileNo.setError("Mobile number seems to be empty");
            validateData = false;
        } else if (!Patterns.PHONE.matcher(mMobileNum.getText().toString()).matches()) {
            mTextInputLayoutMobileNo.setError("Invalid Mobile Number, Please Check");
            validateData = false;
        } else {
            mTextInputLayoutMobileNo.setError(null);
        }

        if (!mCheckBox.isChecked()) {
            mCheckBox.setError(""); // need not to give error text, check box itself will show an error image
            validateData = false;
        } else {
            mCheckBox.setError(null);
        }
            /*if (passwordEdTxt.getText().toString().trim().isEmpty()) {
                mTextInputLayoutEnterOtp.setError("O.T.P seems to be empty");
                validateData = false;
            }else {
                mTextInputLayoutEnterOtp.setError(null);
            }*/

        return validateData;
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

    private void getBalance() {
        Utils.getBalance(getActivity());
    }


    private boolean checkForCitrusWalletSignupPassword(EditText password) {
        boolean validateOtpInput = true;

        if (password.getText().toString().trim().isEmpty()) {
            mTextInputLayoutPassword.setError(null);
            validateOtpInput = false;
        } else {
            mTextInputLayoutPassword.setError(null);
        }
        return validateOtpInput;
    }

}
