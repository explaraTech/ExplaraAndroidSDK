package com.explara.explara_payment_sdk.payment.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.ui.events.BalanceUpdateEvent;
import com.citrus.sdk.ui.utils.Utils;
import com.explara.explara_payment_sdk.R;
import com.explara.explara_payment_sdk.common.PaymentBaseFragment;
import com.explara.explara_payment_sdk.payment.PaymentManager;
import com.explara.explara_payment_sdk.payment.dto.OtpResponseDto;
import com.explara.explara_payment_sdk.payment.dto.PayTmUserProfile;
import com.explara.explara_payment_sdk.payment.dto.PaytmPayMentResposnseDto;
import com.explara.explara_payment_sdk.payment.dto.ValidateOtpResposneDto;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara_core.utils.CompatibilityUtil;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.Log;
import com.explara_core.utils.PreferenceManager;
import com.explara_core.utils.Utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

/**
 * Created by anudeep on 19/11/15.
 */
public class WalletFragment extends PaymentBaseFragment {
    private static final String OTP_SENDER = "-IPAYTM";

    private static final String TAG = WalletFragment.class.getSimpleName();
    private ExpandableListView walletListView;
    private int lastExpandedPosition = -1;
    private int mFromScreen;
    private WalletCallBacks mWalletCallBacks;
    private String mEventId;
    private WalletAdapter mWalletAdapter;
    private static final int smsReadPermission = 1;
    private static final int smsReceivePermission = 2;
    public int registerforsmsPermission = 1;

    public interface WalletCallBacks {
        void sendOtpClicked(String emailId, String phoneNum);

        void verifyOtp(String otp);

        void expandGroup(int groupPosition);
    }

    public WalletFragment() {
    }

    public static WalletFragment newInstance(int from, String eventId) {
        WalletFragment walletFragment = new WalletFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ConstantKeys.BundleKeys.FROM_SCREEN, from);
        bundle.putString(Constants.EVENT_ID, eventId);
        walletFragment.setArguments(bundle);
        return walletFragment;
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
//                        Toast.makeText(getActivity(), getOtp(smsMessage.getMessageBody()), Toast.LENGTH_LONG).show();
                        mWalletAdapter.setOtp(otp);
                        mWalletAdapter.notifyDataSetChanged();
//                        mCodeEditText.setText(smsMessage.getMessageBody().toString().replaceAll("\\D+", ""));
//                        onVerifyBtnClicked();
                    }
                }
            }
        }
    };

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wallet_fragment, container, false);
        initViews(view);
        extractArguments();
        EventBus.getDefault().register(this);
        return view;
    }

    private void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mFromScreen = args.getInt(ConstantKeys.BundleKeys.FROM_SCREEN);
            mEventId = args.getString(Constants.EVENT_ID);
        }

    }

    private void initViews(View view) {
        walletListView = (ExpandableListView) view.findViewById(R.id.wallet_list_view);
        walletListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    walletListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
        googleAnalyticsSendScreenName();
    }

    private void fetchUserBalance() {
        String payTmAceessToken = PreferenceManager.getInstance(getActivity()).getPayTmAccessToken();
        if (!TextUtils.isEmpty(PreferenceManager.getInstance(getActivity()).getPayTmAccessToken())) {
            PaymentManager.getInstance().getPayTmUserDetails(getActivity(), payTmAceessToken, new PaymentManager.FetchPaytmProfileListener() {
                @Override
                public void onFetchPaytmUserProfile(PayTmUserProfile payTmUserProfile) {
                    if (getActivity() != null) {
                        if (mWalletAdapter != null) {
                            mWalletAdapter.notifyDataSetChanged();
                        } else {
                            mWalletAdapter = new WalletAdapter(PaymentManager.getInstance().getWallets(), getActivity(), mWalletCallBacks);
                            walletListView.setAdapter(mWalletAdapter);
                        }
                        if (mFromScreen == ConstantKeys.FromScreen.PAYMENT_SCREEN) {
                            walletListView.expandGroup(0);
                        }
                    }
                }

                @Override
                public void onFetchpaytmUserProfileFailed(VolleyError volleyError) {
                }
            }, TAG);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        if (!TextUtils.isEmpty(PreferenceManager.getInstance(getActivity()).getPayTmAccessToken())) {
//            fetchUserBalance();
        fetchCitrusBalance();


        mWalletCallBacks = new WalletCallBacks() {
            @Override
            public void sendOtpClicked(String emailId, String phoneNum) {

                PaymentManager.getInstance().requestForOtp(getActivity().getApplicationContext(), new PaymentManager.RequestOtpListener() {
                    @Override
                    public void onOtpReadSuccess(OtpResponseDto otpResponseDto) {
                        Toast.makeText(getContext(), otpResponseDto.message, Toast.LENGTH_LONG).show();
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
                if (paymentManager.otpResposneDto != null) {
                    paymentManager.validateOtp(getActivity().getApplicationContext(), new PaymentManager.ValidatePayTmOtpListener() {
                        @Override
                        public void onOtpReadSuccess(ValidateOtpResposneDto validateOtpResposneDto) {
                            if (getActivity() != null && validateOtpResposneDto != null && validateOtpResposneDto.access_token != null) {
                                //Toast.makeText(getActivity(), validateOtpResposneDto.access_token, Toast.LENGTH_LONG).show();
                                Log.d(TAG, validateOtpResposneDto.access_token);
                                PreferenceManager.getInstance(getActivity()).setPayTmAccessToken(validateOtpResposneDto.access_token);
                                paymentManager.getPayTmUserDetails(getActivity(), validateOtpResposneDto.access_token, new PaymentManager.FetchPaytmProfileListener() {
                                    @Override
                                    public void onFetchPaytmUserProfile(PayTmUserProfile payTmUserProfile) {
                                        mWalletAdapter.handleSelectDefaultWalletOptionSuccess();
                                        mWalletAdapter.notifyDataSetChanged();
//                                        walletListView.setAdapter(new WalletAdapter(PaymentManager.getInstance().getWallets(), getActivity(), mWalletCallBacks));
//                                walletListView.expandGroup(0);
                                        if (mFromScreen == ConstantKeys.FromScreen.PAYMENT_SCREEN) {
                                            if (TicketsManager.getInstance().mTotal < Double.valueOf(payTmUserProfile.WALLETBALANCE)) {
                                                payWithPaytm();
                                            } else {
                                                Toast.makeText(getActivity(), "Please Add Money to Wallet", Toast.LENGTH_LONG).show();
                                                walletListView.expandGroup(0);
                                            }
                                        }
//                                        Toast.makeText(getActivity(), payTmUserProfile.WALLETBALANCE, Toast.LENGTH_LONG).show();
                                        Log.d(TAG, payTmUserProfile.mobile);
                                    }

                                    @Override
                                    public void onFetchpaytmUserProfileFailed(VolleyError volleyError) {

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
                } else {
                    Log.d(TAG, "otpResponseDto is NULL");
                }

            }

            @Override
            public void expandGroup(int groupPosition) {
                walletListView.expandGroup(groupPosition);
            }
        };
        mWalletAdapter = new WalletAdapter(PaymentManager.getInstance().getWallets(), getActivity(), mWalletCallBacks);
        walletListView.setAdapter(mWalletAdapter);
        if (mFromScreen == ConstantKeys.FromScreen.PAYMENT_SCREEN) {
            walletListView.expandGroup(0);
        } else {
            walletListView.collapseGroup(0);
        }
    }

    private void fetchCitrusBalance() {
        //        }
        CitrusClient instance = CitrusClient.getInstance(getContext());
        instance.isUserSignedIn(new Callback<Boolean>() {
            @Override
            public void success(Boolean aBoolean) {
                if (aBoolean) {
                    Utils.getBalance(getContext());
                }
            }

            @Override
            public void error(CitrusError error) {

            }
        });
    }

    @Override
    public void refresh() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mWalletCallBacks = null;
        if (registerforsmsPermission == 2) {
            getActivity().unregisterReceiver(smsReceiver);
        }
    }

    private void payWithPaytm() {

        if (Utility.isNetworkAvailable(getActivity())) {

            PaymentManager paymentManager = PaymentManager.getInstance();
            String accessToken = PreferenceManager.getInstance(getActivity()).getPayTmAccessToken();
            if (!TextUtils.isEmpty(accessToken)) {
                PaymentManager.getInstance().payWithPaytm(getActivity(), accessToken, TicketsManager.getInstance().mOrder.getOrderNo(), new PaymentManager.PayWithPaytmStatusListener() {
                    @Override
                    public void onPayWithPaytmStatusSuccess(PaytmPayMentResposnseDto payTmUserProfile) {
//                        showMaterialDialog();
                        if (payTmUserProfile.Status.equals(ConstantKeys.PaymentOptionIds.TRANSACTION_SUCESS)) {
                            Log.d(TAG, payTmUserProfile.getStatus());
                            Intent intent = new Intent(getActivity(), ConfirmationOnlineActivity.class);
                            intent.putExtra(Constants.EVENT_ID, mEventId);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onPayWithPaytmStatusFailed(VolleyError volleyError) {
                        Log.d(TAG, "payMent failed");

                    }
                }, TAG);
            } else {
                Intent intent = new Intent(getActivity(), WalletScreenActivity.class);
                intent.putExtra(ConstantKeys.BundleKeys.FROM_SCREEN, ConstantKeys.FromScreen.PAYMENT_SCREEN);
                getActivity().startActivity(intent);
            }
//                if (!TextUtils.isEmpty(accessToken)) {


        } else {
            Toast.makeText(getActivity(), R.string.internet_check_msg, Toast.LENGTH_LONG).show();
        }
    }

    public void setAdapter() {
        if (getActivity() != null) {
            fetchUserBalance();
            walletListView.expandGroup(0);
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if (CompatibilityUtil.isMarshmallow()) {

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_LONG).show();
                registerForSms();


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
                Toast.makeText(getContext(), "Something wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void registerForSms() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);
        registerforsmsPermission = 2;
        getActivity().registerReceiver(smsReceiver, filter);
    }

    private void googleAnalyticsSendScreenName() {
        if (PaymentManager.getInstance().mAnalyticsListener != null) {
            PaymentManager.getInstance().mAnalyticsListener.sendScreenName(getResources().getString(R.string.wallet_screen), getActivity().getApplication(), getContext());
        }
        //AnalyticsHelper.sendScreenName(getResources().getString(R.string.wallet_screen), getActivity().getApplication(), getContext());
    }

    @Override
    public void onDestroyView() {

        EventBus.getDefault().unregister(this);

        super.onDestroyView();
    }

    public void onEvent(BalanceUpdateEvent event) {
        if (isAdded()) {
            if (getActivity() != null && event != null) {
//                Toast.makeText(getActivity(), "" + event.getAmount(), Toast.LENGTH_LONG).show();
                PreferenceManager.getInstance(getActivity()).setCitrusUserBalance(event.getAmount().getValue());
                if (mWalletAdapter != null) {
                    mWalletAdapter.notifyDataSetChanged();
                }
            }
        }
    }

}
