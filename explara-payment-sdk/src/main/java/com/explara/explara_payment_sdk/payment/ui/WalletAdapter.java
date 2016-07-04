package com.explara.explara_payment_sdk.payment.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;
import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.response.CitrusResponse;
import com.citrus.sdk.ui.fragments.AddMoneyOptionsFragment;
import com.citrus.sdk.ui.utils.CitrusFlowManager;
import com.citrus.sdk.ui.utils.Utils;
import com.explara.explara_payment_sdk.R;
import com.explara.explara_payment_sdk.payment.PaymentManager;
import com.explara.explara_payment_sdk.payment.dto.PayTmUserProfile;
import com.explara.explara_payment_sdk.payment.dto.PaytmUserTempDetails;
import com.explara.explara_payment_sdk.payment.dto.WalletItemDto;
import com.explara.explara_payment_sdk.payment.io.PaymentConnectionManager;
import com.explara.explara_payment_sdk.utils.UrlConstants;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.FragmentHelper;
import com.explara_core.utils.Log;
import com.explara_core.utils.PreferenceManager;
import com.explara_core.utils.Utility;
import com.explara_core.utils.WidgetsColorUtil;

import java.util.List;

/**
 * Created by anudeep on 19/11/15.
 */
public class WalletAdapter extends BaseExpandableListAdapter {


    private static final String TAG = WalletAdapter.class.getSimpleName();
    private List<WalletItemDto> mWallets;
    private Context mContext;
    private WalletFragment.WalletCallBacks mWalletCallBacks;
    private LayoutInflater mLayoutInflater;
    private PaytmUserTempDetails paytmUserTempDetails = new PaytmUserTempDetails();
    private boolean validateData = false;
    private MaterialDialog mMaterialDialog;
    private boolean mClickedOnSelectPrefferedWalletOption;
    private int prefferedWalletOption;


    private EditText mEmailId, mMobileNum, mPassword;
    private Button mLinkButton;
    private TextInputLayout mTextInputLayoutEmail, mTextInputLayoutMobileNo, mTextInputLayoutPassword;

    private AppCompatCheckBox mCheckBox;


    public WalletAdapter(List<WalletItemDto> wallets, Context context, WalletFragment.WalletCallBacks walletCallBacks) {
        mWallets = wallets;
        mContext = context;
        mWalletCallBacks = walletCallBacks;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getGroupCount() {
        return mWallets.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mWallets.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        convertView = mLayoutInflater.inflate(R.layout.payment_item_list, parent, false);
        ((ImageView) convertView.findViewById(R.id.wallet_img)).setImageResource(0);
        handlePrefereedWallet((RadioButton) convertView.findViewById(R.id.default_wallet_chooser), groupPosition);

        if (((WalletItemDto) getGroup(groupPosition)).walletId == PaymentManager.PAYTM_ID) {
            String balance = PaymentManager.getInstance().getUserPayTmBalance();
            String payTmAccessToken = PreferenceManager.getInstance(convertView.getContext()).getPayTmAccessToken();

            ((ImageView) convertView.findViewById(R.id.wallet_img)).setImageResource(R.drawable.paytm);

            if (!TextUtils.isEmpty(balance) && !TextUtils.isEmpty(payTmAccessToken))
                ((TextView) convertView.findViewById(R.id.paytm_rupee_text)).setText(String.format("%s %s", mContext.getString(R.string.rupee_symbol), balance));


            final TextView balancetxtVew = (TextView) convertView.findViewById(R.id.paytm_rupee_text);
            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progress_bar);
            WidgetsColorUtil.setProgressBarTintColor(progressBar, mContext.getResources().getColor(R.color.accentColor));
            //progressBar.getIndeterminateDrawable().setColorFilter(convertView.getResources().getColor(R.color.accentColor), PorterDuff.Mode.SRC_IN);
            final View balanceContainer = convertView.findViewById(R.id.balance_container);
            ((TextView) convertView.findViewById(R.id.paytm_rupee_text)).setText(String.format("%s %s", mContext.getString(R.string.rupee_symbol), PaymentManager.getInstance().getUserPayTmBalance()));

            if (!TextUtils.isEmpty(payTmAccessToken)) {
                PaymentManager.getInstance().getPayTmUserDetails(convertView.getContext(), payTmAccessToken
                        , new PaymentManager.FetchPaytmProfileListener() {
                    @Override
                    public void onFetchPaytmUserProfile(PayTmUserProfile payTmUserProfile) {
                        if (balancetxtVew != null && progressBar != null && balanceContainer != null) {
                            if (payTmUserProfile != null && payTmUserProfile.WALLETBALANCE != null)
                                balancetxtVew.setText(String.format("%s %s", mContext.getString(R.string.rupee_symbol), payTmUserProfile.WALLETBALANCE));
                            progressBar.setVisibility(View.GONE);
                            balanceContainer.setVisibility(View.VISIBLE);
                        }
                    }


                    @Override
                    public void onFetchpaytmUserProfileFailed(VolleyError volleyError) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, TAG);
            } else {
                progressBar.setVisibility(View.GONE);
                balanceContainer.setVisibility(View.GONE);
            }
        } else {
            convertView.findViewById(R.id.wallet_title).setVisibility(View.GONE);
            ((ImageView) convertView.findViewById(R.id.wallet_img)).setImageResource(R.drawable.citrus);
            convertView.findViewById(R.id.wallet_img).setVisibility(View.VISIBLE);
            String balance = PreferenceManager.getInstance(mContext).getCitrusUserBalance();
            if (!TextUtils.isEmpty(balance)) {
                ((TextView) convertView.findViewById(R.id.paytm_rupee_text)).setText(String.format("%s %s", mContext.getString(R.string.rupee_symbol), balance));
                final View balanceContainer = convertView.findViewById(R.id.balance_container);
                final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progress_bar);
                //progressBar.getIndeterminateDrawable().setColorFilter(convertView.getResources().getColor(R.color.accentColor), PorterDuff.Mode.SRC_IN);
                WidgetsColorUtil.setProgressBarTintColor(progressBar, mContext.getResources().getColor(R.color.accentColor));
                progressBar.setVisibility(View.GONE);
                balanceContainer.setVisibility(View.VISIBLE);
            }

        }
        return convertView;
    }

    private void handlePrefereedWallet(final RadioButton radioButton, final int position) {

        radioButton.setVisibility(View.VISIBLE);
        final WalletItemDto walletItemDto = mWallets.get(position);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWalletCallBacks.expandGroup(position);
                mClickedOnSelectPrefferedWalletOption = true;
                if (walletItemDto.walletId == PaymentManager.PAYTM_ID) {
                    prefferedWalletOption = PreferenceManager.PAY_TM_PREFERRED_WALLET;
                    if (PreferenceManager.getInstance(radioButton.getContext()).isPaytmUserLoggedIn()) {
                        handleSelectDefaultWalletOptionSuccess();
                    }
//                    PreferenceManager.getInstance(radioButton.getContext()).setPreferredWallet(PreferenceManager.PAY_TM_PREFERRED_WALLET);
                } else {
//                    PreferenceManager.getInstance(radioButton.getContext()).setPreferredWallet(PreferenceManager.CITRUS_PREFFRED_WALLET);
                    prefferedWalletOption = PreferenceManager.CITRUS_PREFFRED_WALLET;
                    CitrusClient.getInstance(radioButton.getContext()).isUserSignedIn(new Callback<Boolean>() {
                        @Override
                        public void success(Boolean aBoolean) {
                            if (aBoolean) {
                                handleSelectDefaultWalletOptionSuccess();
                            }
                        }

                        @Override
                        public void error(CitrusError error) {

                        }
                    });
                }
                notifyDataSetChanged();
            }
        });
        radioButton.setChecked(false);
        if (PreferenceManager.getInstance(radioButton.getContext()).isPreferredWalletOptionSelected()) {
            int preferredWalletOption = PreferenceManager.getInstance(radioButton.getContext()).getPreferredWalletOption();
            if (preferredWalletOption == PreferenceManager.PAY_TM_PREFERRED_WALLET && position == 0) {
                radioButton.setChecked(true);
            } else if (preferredWalletOption == PreferenceManager.CITRUS_PREFFRED_WALLET && position == 1) {
                radioButton.setChecked(true);
            }
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (((WalletItemDto) getGroup(groupPosition)).walletId == PaymentManager.PAYTM_ID) {
            // Paytm UI
            if (TextUtils.isEmpty(PreferenceManager.getInstance(mContext).getPayTmAccessToken())) {
                convertView = loadCollectInfoView(parent);
            } else {
                convertView = loadAddMoneyView(parent, groupPosition);
            }
        } else {
            // Citrus UI
            if (TextUtils.isEmpty(PreferenceManager.getInstance(mContext).getCitrusUserBalance())) {

                convertView = mLayoutInflater.inflate(R.layout.wallet_child_citrus_collect_details_layout, parent, false);

                mEmailId = (EditText) convertView.findViewById(R.id.email_id);
                mPassword = (EditText) convertView.findViewById(R.id.password);
                mMobileNum = (EditText) convertView.findViewById(R.id.phoneEditText);
                mLinkButton = (Button) convertView.findViewById(R.id.sign_in_button);
                mCheckBox = (AppCompatCheckBox) convertView.findViewById(R.id.agree_to_t_and_c_text);
                mTextInputLayoutEmail = (TextInputLayout) convertView.findViewById(R.id.textinput_emailId);
                mTextInputLayoutMobileNo = (TextInputLayout) convertView.findViewById(R.id.textinput_phoneNumber);
                mTextInputLayoutPassword = (TextInputLayout) convertView.findViewById(R.id.textinput_enterpassword);
                final TextView forgotPassword = (TextView) convertView.findViewById(R.id.forgotPasswordText);
                final CitrusClient citrusClient = CitrusClient.getInstance(mContext);

                mEmailId.setText(PreferenceManager.getInstance(mContext).getEmail());
                mMobileNum.setText(PreferenceManager.getInstance(mContext).getPhoneNo());


                mLinkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (Utility.isNetworkAvailable(mContext)) {
                            if (checkForCitrusWalletLogin()) {
                                showMaterialDialog();

                                citrusClient.isCitrusMember(mEmailId.getText().toString(), mMobileNum.getText().toString(), new Callback<Boolean>() {
                                            @Override
                                            public void success(Boolean aBoolean) {
                                                if (!aBoolean) {
                                                    mLinkButton.setText("SIGN UP");
                                                    mPassword.setVisibility(View.VISIBLE);
                                                    mPassword.requestFocus();

                                                    if (checkForCitrusWalletSignupPassword(mPassword)) {
                                                        citrusClient.signUp(mEmailId.getText().toString(), mMobileNum.getText().toString(), mPassword.getText().toString(), new Callback<CitrusResponse>() {
                                                            @Override
                                                            public void success(CitrusResponse citrusResponse) {
                                                                handleSelectDefaultWalletOptionSuccess();
                                                                if (mMaterialDialog != null) {
                                                                    mMaterialDialog.dismiss();
                                                                }
                                                                Toast.makeText(mContext, "Sign Up Successful", Toast.LENGTH_LONG).show();
                                                                getBalance();
                                                                notifyDataSetChanged();
                                                            }

                                                            @Override
                                                            public void error(CitrusError error) {
                                                                if (mMaterialDialog != null) {
                                                                    mMaterialDialog.dismiss();
                                                                }
                                                            }
                                                        });
                                                    } else {

                                                        if (mMaterialDialog != null) {
                                                            mMaterialDialog.dismiss();
                                                        }
                                                    }

                                                } else {
                                                    mLinkButton.setText("SIGN IN");
                                                    forgotPassword.setVisibility(View.VISIBLE);

                                                    mPassword.setVisibility(View.VISIBLE);
                                                    mPassword.requestFocus();

                                                    if (checkForCitrusWalletSignupPassword(mPassword)) {
                                                        citrusClient.signIn(mEmailId.getText().toString(), mPassword.getText().toString(), new Callback<CitrusResponse>() {
                                                            @Override
                                                            public void success(CitrusResponse citrusResponse) {
                                                                handleSelectDefaultWalletOptionSuccess();
                                                                if (mMaterialDialog != null) {
                                                                    mMaterialDialog.dismiss();
                                                                }
                                                                getBalance();
                                                            }

                                                            @Override
                                                            public void error(CitrusError error) {
                                                                Log.d(TAG, error.getMessage());

                                                                if (mMaterialDialog != null) {
                                                                    mMaterialDialog.dismiss();
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        if (mMaterialDialog != null) {
                                                            mMaterialDialog.dismiss();
                                                        }
                                                    }

                                                }

                                            }

                                            @Override
                                            public void error(CitrusError citrusError) {
                                                if (mMaterialDialog != null) {
                                                    mMaterialDialog.dismiss();
                                                }
                                            }
                                        }
                                );
                            }
                        } else {
                            Toast.makeText(mContext, "" + mContext.getResources().getString(R.string.internet_check_msg), Toast.LENGTH_SHORT).show();
                            if (mMaterialDialog != null) {
                                mMaterialDialog.dismiss();
                            }
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
                                if (mMaterialDialog != null) {
                                    mMaterialDialog.dismiss();
                                }
                            }
                        });
                    }
                });


            } else {
                convertView = loadAddMoneyView(parent, groupPosition);
            }
        }

        return convertView;
    }


    public void handleSelectDefaultWalletOptionSuccess() {
        if (mClickedOnSelectPrefferedWalletOption) {
            PreferenceManager.getInstance(mContext).setPreferredWallet(prefferedWalletOption);
        }
    }

    @NonNull
    private View loadAddMoneyView(ViewGroup parent, final int groupPosition) {
        View convertView;
        convertView = mLayoutInflater.inflate(R.layout.payment_item_child, parent, false);
        final EditText moneyText = (EditText) convertView.findViewById(R.id.amount_edt_txt);
        convertView.findViewById(R.id.amount_1000).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moneyText.setText("100");
            }
        });
        convertView.findViewById(R.id.amount_1500).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moneyText.setText("500");

            }
        });
        convertView.findViewById(R.id.amount_2000).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moneyText.setText("1500");

            }
        });

        convertView.findViewById(R.id.recharge_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = moneyText.getText().toString();
                if (!amount.trim().isEmpty() && amount.trim() != null) {
                    Double enteredAmount = Double.parseDouble(amount);
                    if (enteredAmount > 0) {
                        addMoney(amount, groupPosition);
                    } else {
                        Constants.createToastWithMessage(mContext, mContext.getString(R.string.empty_wallet_amout));
                    }
                } else {
                    Constants.createToastWithMessage(mContext, mContext.getString(R.string.empty_wallet_amout));
                }
            }
        });
        return convertView;
    }

    @NonNull
    private View loadCollectInfoView(ViewGroup parent) {
        View convertView;
        final ChildViewHolder childViewHolder = new ChildViewHolder();
        convertView = mLayoutInflater.inflate(R.layout.wallet_child_layout, parent, false);
        childViewHolder.emailId = (EditText) convertView.findViewById(R.id.email_id);
        childViewHolder.phoneNumber = (EditText) convertView.findViewById(R.id.phoneEditText);
        childViewHolder.sendOtpButton = (Button) convertView.findViewById(R.id.sendOtpButton);
        childViewHolder.signInButton = (Button) convertView.findViewById(R.id.sign_in_button);
        childViewHolder.passwordEdTxt = (EditText) convertView.findViewById(R.id.password);
        childViewHolder.mCheckboxTermsOfService = (CheckBox) convertView.findViewById(R.id.agree_to_t_and_c_text);
        childViewHolder.mTextInputLayoutEmailID = (TextInputLayout) convertView.findViewById(R.id.textinput_emailId);
        childViewHolder.mTextInputLayoutPhoneNumber = (TextInputLayout) convertView.findViewById(R.id.textinput_phoneNumber);
        childViewHolder.mTextInputLayoutEnterOtp = (TextInputLayout) convertView.findViewById(R.id.textinput_enterOtp);
        childViewHolder.passwordEdTxt.setText(paytmUserTempDetails.otp);


        childViewHolder.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childViewHolder.checkForWalletSignUpData() && childViewHolder.checkForOtpData()) {
                    mWalletCallBacks.verifyOtp(childViewHolder.passwordEdTxt.getText().toString());
                }
            }
        });


        childViewHolder.sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childViewHolder.checkForWalletSignUpData()) {
                    mWalletCallBacks.sendOtpClicked(childViewHolder.emailId.getText().toString(), childViewHolder.phoneNumber.getText().toString());
                    childViewHolder.passwordEdTxt.setVisibility(View.VISIBLE);
                    childViewHolder.mCheckboxTermsOfService.setVisibility(View.VISIBLE);
                    childViewHolder.signInButton.setVisibility(View.VISIBLE);
                    childViewHolder.sendOtpButton.setText("Resend OTP");
                    childViewHolder.sendOtpButton.setTag("SendClicked");
                    paytmUserTempDetails.email = childViewHolder.emailId.getText().toString();
                    paytmUserTempDetails.phoneNum = childViewHolder.phoneNumber.getText().toString();
                    paytmUserTempDetails.showEnterOtp = true;
                }
            }
        });

        if (paytmUserTempDetails.showEnterOtp) {
            childViewHolder.passwordEdTxt.setVisibility(View.VISIBLE);
            childViewHolder.mCheckboxTermsOfService.setVisibility(View.VISIBLE);
            childViewHolder.signInButton.setVisibility(View.VISIBLE);
            childViewHolder.sendOtpButton.setText("Resend OTP");

        }

        childViewHolder.emailId.setText(PreferenceManager.getInstance(mContext).getEmail());
        childViewHolder.phoneNumber.setText(PreferenceManager.getInstance(mContext).getPhoneNo());
        if (!TextUtils.isEmpty(paytmUserTempDetails.email))
            childViewHolder.emailId.setText(paytmUserTempDetails.email);
        if (!TextUtils.isEmpty(paytmUserTempDetails.phoneNum))
            childViewHolder.phoneNumber.setText(paytmUserTempDetails.phoneNum);
        return convertView;
    }

    private void addMoney(String amount, int groupPosition) {

        if (((WalletItemDto) getGroup(groupPosition)).walletId == PaymentManager.PAYTM_ID) {

            String accessToken = PreferenceManager.getInstance(mContext).getPayTmAccessToken();
            PaymentConnectionManager paymentConnectionManager = new PaymentConnectionManager();
            Intent intent = new Intent(mContext, WebviewActivity.class);
            intent.putExtra(ConstantKeys.BundleKeys.URL_STRING, paymentConnectionManager.addMoneyForPaytmUrl(accessToken, amount));
            intent.putExtra(ConstantKeys.BundleKeys.FROM_SCREEN, ((WalletScreenActivity) mContext).getIntent().getIntExtra(ConstantKeys.BundleKeys.FROM_SCREEN, ConstantKeys.FromScreen.WALLET_SCREEN));
            ((Activity) mContext).startActivityForResult(intent, ConstantKeys.REQUEST_CODES.PAY_TM_ADD_MONEY_REQUEST_CODE);
        } else {
            CitrusFlowManager.returnURL = UrlConstants.CITRUS_ADDMONEY_RETURN_URL;

            FragmentHelper.replaceContentFragment((FragmentActivity) mContext, R.id.fragment_container, AddMoneyOptionsFragment.newInstance(false, amount));
        }
    }
//    @Override
//    public int getChildTypeCount() {
//        return 2;
//    }

//    @Override
//    public int getChildType(int groupPosition, int childPosition) {
//        if (TextUtils.isEmpty(PreferenceManager.getInstance(mContext).getPayTmAccessToken())) {
//            return LOG_IN_LAYOUT;
//        } else {
//            return ADD_MONEY_LAYOUT;
//        }
//    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private static class ChildViewHolder {
        public EditText emailId;
        public EditText phoneNumber;
        public EditText passwordEdTxt;
        public Button sendOtpButton;
        public Button signInButton;
        public CheckBox mCheckboxTermsOfService;

        public TextInputLayout mTextInputLayoutEmailID;
        public TextInputLayout mTextInputLayoutPhoneNumber;
        public TextInputLayout mTextInputLayoutEnterOtp;

        public boolean checkForWalletSignUpData() {
            boolean validateData = true;

            if (emailId.getText().toString().trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailId.getText().toString()).matches()) {
                mTextInputLayoutEmailID.setError("email address seems to be empty");
                validateData = false;
            } else {
                mTextInputLayoutEmailID.setError(null);
            }

            if (phoneNumber.getText().toString().trim().isEmpty()) {
                mTextInputLayoutPhoneNumber.setError("mobile number seems to be empty");
                validateData = false;
            } else {
                mTextInputLayoutPhoneNumber.setError(null);
            }


            /*if (passwordEdTxt.getText().toString().trim().isEmpty()) {
                mTextInputLayoutEnterOtp.setError("O.T.P seems to be empty");
                validateData = false;
            }else {
                mTextInputLayoutEnterOtp.setError(null);
            }*/

            return validateData;
        }

        private boolean checkForOtpData() {
            boolean validateOtpInput = true;


            if (passwordEdTxt.getText().toString().trim().isEmpty()) {
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

    }

    public void showMaterialDialog() {
        mMaterialDialog = new MaterialDialog.Builder(mContext)
                //.title("Explara Login")
                .content("Please wait..")
                .cancelable(false)
                        //.iconRes(R.drawable.e_logo)
                .progress(true, 0)
                .show();
    }

    public boolean checkForCitrusWalletLogin() {
        boolean validateData = true;

        if (mEmailId.getText().toString().trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailId.getText().toString()).matches()) {
            mTextInputLayoutEmail.setError("email address seems to be empty");
            validateData = false;
        } else {
            mTextInputLayoutEmail.setError(null);
        }

        if (mMobileNum.getText().toString().trim().isEmpty()) {
            mTextInputLayoutMobileNo.setError("mobile number seems to be empty");
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

    private boolean checkForCitrusWalletSignupPassword(EditText password) {
        boolean validateOtpInput = true;

        if (password.getText().toString().trim().isEmpty()) {
            mTextInputLayoutPassword.setError("Please enter your password");
            validateOtpInput = false;
        } else {
            mTextInputLayoutPassword.setError(null);
        }
        return validateOtpInput;
    }


    public void setOtp(String otp) {
        paytmUserTempDetails.otp = otp;
    }

    private void getBalance() {
        Utils.getBalance(mContext);
    }

}
