package com.explara.explara_payment_sdk.payment.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.ui.utils.CitrusFlowManager;
import com.explara.explara_payment_sdk.R;
import com.explara.explara_payment_sdk.common.PaymentBaseFragment;
import com.explara.explara_payment_sdk.payment.PaymentManager;
import com.explara.explara_payment_sdk.payment.dto.CheckoutOfflineResponse;
import com.explara.explara_payment_sdk.payment.dto.OlaBill;
import com.explara.explara_payment_sdk.payment.dto.PayTmUserProfile;
import com.explara.explara_payment_sdk.payment.dto.PaymentOptionDto;
import com.explara.explara_payment_sdk.payment.dto.PaypalCheckoutResponse;
import com.explara.explara_payment_sdk.payment.dto.PaytmPayMentResposnseDto;
import com.explara.explara_payment_sdk.utils.GlobalVar;
import com.explara.explara_payment_sdk.utils.UrlConstants;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk.tickets.dto.Online;
import com.explara.explara_ticketing_sdk_ui.tickets.ui.TicketsDetailsFragment;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.Log;
import com.explara_core.utils.PreferenceManager;
import com.explara_core.utils.WidgetsColorUtil;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalAdvancedPayment;
import com.paypal.android.MEP.PayPalPayment;
import com.paypal.android.MEP.PayPalReceiverDetails;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by anudeep on 30/11/15.
 */
public class PaymentOptionsListFragment extends PaymentBaseFragment {
    public static final String LAUNCH_DEFAULT_PAY = "launch_default_pay";
    private static final int REQUEST_PAYPAL_CHECKOUT = 2;
    private static final String TAG = PaymentOptionsListFragment.class.getSimpleName();
    private static boolean launchDefaultWalletOPtion;
    private ListView mListView;
    private String mEventID;
    private ProgressBar mProgressBar;
    PaymentOptionsListAdapter listAdapter;
    //private MaterialDialog mMaterialDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_options_list, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        WidgetsColorUtil.setProgressBarTintColor(mProgressBar, getResources().getColor(R.color.accentColor));
        mListView = (ListView) view.findViewById(R.id.payment_option_list_view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listAdapter != null) {
                    PaymentOptionDto item = listAdapter.getItem(position);

                    switch (item.id) {
                        case ConstantKeys.PaymentOptionIds.OLA_MONEY:
                            payWithOla();
                            break;
                        case ConstantKeys.PaymentOptionIds.CCAVENUE:
                            initiateCCAvenueCheckout();
                            break;
                        case ConstantKeys.PaymentOptionIds.PAYTM:
                            paywithPaytm();
                            break;
                        case ConstantKeys.PaymentOptionIds.VENUE_ID:
                            GlobalVar.INSTRUCTION = PaymentManager.getInstance().mPaymentOptions.getOffline().getVenue().getInfo();
                            checkoutOffline(ConstantKeys.PaymentOptionIds.VENUE);
                            break;
                        case ConstantKeys.PaymentOptionIds.DEPOSIT_ID:
                            GlobalVar.INSTRUCTION = PaymentManager.getInstance().mPaymentOptions.getOffline().getDeposit().getInfo();
                            checkoutOffline(ConstantKeys.PaymentOptionIds.DEPOSIT);
                            break;
                        case ConstantKeys.PaymentOptionIds.CHEQUE_ID:
                            GlobalVar.INSTRUCTION = PaymentManager.getInstance().mPaymentOptions.getOffline().getCheque().getInfo();
                            checkoutOffline(ConstantKeys.PaymentOptionIds.CHEQUE);
                            break;
                        case ConstantKeys.PaymentOptionIds.dd:
                            GlobalVar.INSTRUCTION = PaymentManager.getInstance().mPaymentOptions.getOffline().getDd().getInfo();
                            checkoutOffline(ConstantKeys.PaymentOptionIds.DD);
                            break;
                        case ConstantKeys.PaymentOptionIds.PAYPAL:
                            payPalButtonClick();
                            break;
                        case ConstantKeys.PaymentOptionIds.CITRUS_WALLET_ID:
//                            Toast.makeText(getActivity(), "Citrus Wallet clicked", Toast.LENGTH_LONG).show();
                            payWithCitrusWallet();
                            break;
                        case ConstantKeys.PaymentOptionIds.CITRUS_ID:
//                            Toast.makeText(getActivity(), "Citrus Clicked", Toast.LENGTH_LONG).show();

//                            mListener.navigateTo(AddCardFragment.newInstance(UIConstants.TRANS_QUICK_PAY, "")
//                                    , UIConstants.SCREEN_ADD_CARD);\
                            CitrusFlowManager.billGenerator = UrlConstants.CITRUS_BILL_GENERATOR + TicketsManager.getInstance().mOrder.getOrderNo();
                            CitrusFlowManager.startShoppingFlowStyle(getActivity(), PreferenceManager.getInstance(getActivity(
                                    )).getEmail(), PreferenceManager.getInstance(getActivity()).getPhoneNo(),
                                    TicketsManager.getInstance().mDiscountResponse.getCart().getGrandTotal(), R.style.AppTheme);
//                            FragmentHelper.replaceAndAddContentFragment(getActivity(), R.id.fragment_container, QuickPayFragment.newInstance(PreferenceManager.getInstance(getActivity()).getEmail(), PreferenceManager.getInstance(getActivity()).getPhoneNo(), TicketsManager.getInstance().mDiscountResponse.getCart().getGrandTotal()), TAG);
                            break;
                    }
                }
            }
        });
        extractArguments();
        return view;
    }

    private void payWithCitrusWallet() {
        Intent intent = new Intent(getActivity(), WalletScreenFlowActivity.class);
        intent.putExtra(Constants.EVENT_ID, mEventID);
        intent.putExtra(LAUNCH_DEFAULT_PAY, shouldLaunchDefaultCitrus);
        getActivity().startActivity(intent);
    }

    public static PaymentOptionsListFragment newInstance(String eventId, boolean launchDefaultWalletOPtion) {
        PaymentOptionsListFragment paymentOptionsListFragment = new PaymentOptionsListFragment();
        Bundle bundle = new Bundle(2);
        bundle.putString(Constants.EVENT_ID, eventId);
        bundle.putBoolean(TicketsDetailsFragment.SHOULD_LAUNCH_PREFFERED_WALLET, launchDefaultWalletOPtion);
        paymentOptionsListFragment.setArguments(bundle);
        return paymentOptionsListFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       /* if (Utility.isNetworkAvailable(getContext())) {
            getAllPaymentOptions();
        } else {
            Toast.makeText(getContext(), R.string.internet_check_msg, Toast.LENGTH_LONG).show();
        }*/
        setUpAllPaymentOptions();
    }

    public void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEventID = args.getString(Constants.EVENT_ID);
            launchDefaultWalletOPtion = args.getBoolean(TicketsDetailsFragment.SHOULD_LAUNCH_PREFFERED_WALLET);
        }
    }

    private void setUpAllPaymentOptions() {
        if (TicketsManager.getInstance().mOrder.getPaymentOption() != null) {
            if (mProgressBar != null) {
                mProgressBar.setVisibility(View.GONE);
            }
            mListView.setVisibility(View.VISIBLE);
            List<PaymentOptionDto> paymentOptionsList = PaymentManager.getInstance().getPaymentOptionsList();
            listAdapter = new PaymentOptionsListAdapter(getActivity(), paymentOptionsList);
            mListView.setAdapter(listAdapter);

            performItemClickForDefaultWalletOption();
        } else {
            if (mProgressBar != null) {
                mProgressBar.setVisibility(View.GONE);
            }
            Toast.makeText(getActivity().getApplicationContext(), "Get payment options failed", Toast.LENGTH_SHORT).show();
        }
    }

   /* public void getAllPaymentOptions() {
        PaymentManager.getInstance().getAllPaymentOptions(getActivity().getApplicationContext(), mEventID, new PaymentManager.PaymentOptionsListener() {
            @Override
            public void onGetAllPaymentOptions(PaymentOptions paymentOptions) {
                if (getActivity() != null && paymentOptions != null) {
                    if (mProgressBar != null) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                    mListView.setVisibility(View.VISIBLE);
                    List<PaymentOptionDto> paymentOptionsList = PaymentManager.getInstance().getPaymentOptionsList();
                    listAdapter = new PaymentOptionsListAdapter(getActivity(), paymentOptionsList);
                    mListView.setAdapter(new PaymentOptionsListAdapter(getActivity(), paymentOptionsList));

                    performItemClickForDefaultWalletOption();
                }
            }

            @Override
            public void onGetAllPaymentOptionsFailed() {
                if (getActivity() != null) {
                    if (mProgressBar != null) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                    Toast.makeText(getActivity().getApplicationContext(), "Get payment options failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    } */

    private boolean shouldLaunchDefaultCitrus = false;

    private void performItemClickForDefaultWalletOption() {
        if (launchDefaultWalletOPtion && PreferenceManager.getInstance(getContext()).isPreferredWalletOptionSelected()) {
            int walletOption = PreferenceManager.getInstance(getContext()).getPreferredWalletOption();
            int walletOptionPosition = 0;
            if (walletOption == PreferenceManager.CITRUS_PREFFRED_WALLET) {

                int i = 0;
                for (PaymentOptionDto paymentOptionDto : listAdapter.paymentList) {
                    if (paymentOptionDto.id == ConstantKeys.PaymentOptionIds.CITRUS_WALLET_ID) {
                        break;
                    }
                    i++;
                }
                walletOptionPosition = i;
                shouldLaunchDefaultCitrus = true;
                mListView.performItemClick(null, walletOptionPosition, 0);
            } else if (walletOption == PreferenceManager.PAY_TM_PREFERRED_WALLET) {
                int i = 0;
                for (PaymentOptionDto paymentOptionDto : listAdapter.paymentList) {
                    if (paymentOptionDto.id == ConstantKeys.PaymentOptionIds.PAYTM) {
                        break;
                    }
                    i++;
                }
                walletOptionPosition = i;
                handlePayTm(walletOptionPosition);

            }
//            mListView.performItemClick(null, walletOptionPosition, 0);
        }

    }

    boolean shouldLaunchDefaultMode = false;

    private void handlePayTm(final int paymentOption) {
        shouldLaunchDefaultMode = false;

        if (PreferenceManager.getInstance(getContext()).isPaytmUserLoggedIn()) {
            showMaterialDialog();
            String payTmAccessToken = PreferenceManager.getInstance(getContext()).getPayTmAccessToken();
            PaymentManager.getInstance().getPayTmUserDetails(getContext(), payTmAccessToken, new PaymentManager.FetchPaytmProfileListener() {
                @Override
                public void onFetchPaytmUserProfile(PayTmUserProfile payTmUserProfile) {
                    if (getActivity() != null) {
                        if (payTmUserProfile != null && payTmUserProfile.WALLETBALANCE != null && !payTmUserProfile.WALLETBALANCE.isEmpty()) {
                            Double ticketPrice = null;
                            if (TicketsManager.getInstance().mTotal != null && !TicketsManager.getInstance().mTotal.toString().isEmpty()) {
                                ticketPrice = Double.parseDouble(TicketsManager.getInstance().getGrandTotal());
                                PaymentManager paymentManager = PaymentManager.getInstance();
                                if ((Double.valueOf(payTmUserProfile.WALLETBALANCE) >= ticketPrice)) {
                                    paymentManager.payWithPaytm(getActivity(), PreferenceManager.getInstance(getContext()).getPayTmAccessToken(), TicketsManager.getInstance().mOrder.getOrderNo(), new PaymentManager.PayWithPaytmStatusListener() {
                                        @Override
                                        public void onPayWithPaytmStatusSuccess(PaytmPayMentResposnseDto payTmUserProfile) {
                                            if (ConstantKeys.PaymentOptionIds.TRANSACTION_SUCESS.equals(payTmUserProfile.Status)) {
                                                Log.d(TAG, "Transaction Success" + payTmUserProfile.getStatus());
                                                dismissMaterialDialog();
                                                Intent intent = new Intent(getActivity(), ConfirmationOnlineActivity.class);
                                                intent.putExtra(Constants.EVENT_ID, mEventID);
                                                startActivity(intent);
                                            } else {
                                                if (!TextUtils.isEmpty(payTmUserProfile.STATUSMESSAGE)) {
                                                    Toast.makeText(getContext(), payTmUserProfile.STATUSMESSAGE, Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(getContext(), "Payment Failed, Retry !", Toast.LENGTH_LONG).show();
                                                }
                                                getActivity().finish();
                                            }

                                        }

                                        @Override
                                        public void onPayWithPaytmStatusFailed(VolleyError volleyError) {
                                            launchDefaultPaymentMethod(paymentOption);

                                            Log.d(TAG, "payMent failed");
                                            Toast.makeText(getContext(), "Payment Failed, Retry !", Toast.LENGTH_LONG).show();
                                            dismissMaterialDialog();
                                        }
                                    }, TAG);
                                } else {
                                    launchDefaultPaymentMethod(paymentOption);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFetchpaytmUserProfileFailed(VolleyError volleyError) {

                }
            }, TAG);
        } else {
            launchDefaultPaymentMethod(paymentOption);
        }


    }

    private void launchDefaultPaymentMethod(int paymentOption) {
        dismissMaterialDialog();
        mListView.performItemClick(null, paymentOption, 0);
    }

    private void handledCitrus(final int paymentOption) {
        CitrusClient citrusClient = CitrusClient.getInstance(getContext());
        citrusClient.isUserSignedIn(new Callback<Boolean>() {
            @Override
            public void success(Boolean aBoolean) {
                if (aBoolean) {

                } else {
                    launchDefaultPaymentMethod(paymentOption);
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

    private void payWithOla() {

        if (checkOlacabs()) {
            PaymentManager.getInstance().payWithOlaMoney(getActivity(), new PaymentManager.GetOlaBillListener() {
                @Override
                public void onGetOlaBillSuccess(OlaBill olaBill) {
                    Log.d(TAG, olaBill.bill);
                    try {
                        Intent intent = new Intent("com.olacabs.olamoney.pay");
                        intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        intent.putExtra("bill", olaBill.bill); //Pass the string representation of the json bill
                        intent.setPackage("com.olacabs.customer");
                        getActivity().startActivityForResult(intent, ConstantKeys.REQUEST_CODES.OLA_REQUEST_CODE);
                    } catch (ActivityNotFoundException e) {
                    /*Activity not found exception - route the flow through webview*/
                    }
                }

                @Override
                public void onGetOlaBillFail() {
                    Log.d(TAG, "olaBill failed");

                }
            }, TAG, TicketsManager.getInstance().mOrder.getOrderNo());
        } else {
            Intent intent = new Intent(getActivity(), OlaWebViewPaymentActivity.class);
            intent.putExtra(ConstantKeys.BundleKeys.ORDER_ID, TicketsManager.getInstance().mOrder.getOrderNo());
            intent.putExtra(Constants.EVENT_ID, mEventID);
            getActivity().startActivity(intent);
        }
    }

    private boolean checkOlacabs() {
        try {
            getActivity().getPackageManager().getApplicationInfo("com.olacabs.customer", 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void initiateCCAvenueCheckout() {
        Intent intent = new Intent(getActivity(), WebViewPaymentActivity.class);
        intent.putExtra(Constants.EVENT_ID, mEventID);
        startActivity(intent);
    }


    private void googleAnalyticsSendScreenName() {
        if (PaymentManager.getInstance().mAnalyticsListener != null) {
            PaymentManager.getInstance().mAnalyticsListener.sendScreenName(getResources().getString(R.string.payment_screen_paytm), getActivity().getApplication(), getContext());
        }
        //AnalyticsHelper.sendScreenName(getResources().getString(R.string.payment_screen_paytm), getActivity().getApplication(), getContext());
    }

    private void paywithPaytm() {

        Intent intent = new Intent(getActivity(), PaytmWalletFlowActvity.class);
        intent.putExtra("screen_name", ConstantKeys.PaymentOptionIds.PAYTM);
        intent.putExtra(Constants.EVENT_ID, mEventID);
        startActivity(intent);
    }

    private void checkoutOffline(String paymentOptionName) {
        showMaterialDialog();
        PaymentManager.getInstance().checkoutOffline(getActivity().getApplicationContext(), paymentOptionName.toLowerCase(), new PaymentManager.CheckoutOfflineListener() {
            @Override
            public void onOfflineCheckout(CheckoutOfflineResponse checkoutOfflineResponse) {
                if (getActivity() != null && checkoutOfflineResponse != null) {
                    dismissMaterialDialog();
                    if (checkoutOfflineResponse.getStatus().equals("success")) {
                        navigateToOfflinePaymentConfirmationPage();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Payment Failed", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onOfflineCheckoutFailed() {
                if (getActivity() != null) {
                    dismissMaterialDialog();
                    Toast.makeText(getActivity().getApplicationContext(), "Checkout offline failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
                , TAG);
    }

    private void navigateToOfflinePaymentConfirmationPage() {
        if (TicketsManager.getInstance().mTotal == 0) {
            Intent intent = new Intent(getActivity(), ConfirmationOnlineActivity.class);
            intent.putExtra(Constants.EVENT_ID, mEventID);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), ConfirmationOfflineActivity.class);
            intent.putExtra(Constants.EVENT_ID, mEventID);
            startActivity(intent);
        }
    }

    public void payPalButtonClick() {
        String currency = PaymentManager.getInstance().mPaymentCallBackListener.getCurrencyFromEventId(mEventID);
        // Create a basic PayPal payment
        PayPalPayment payment = new PayPalPayment();
        // Set the currency type
        payment.setCurrencyType(currency);

        PayPalReceiverDetails primary_receiver, secondary_receiver;

        // Primary receiver is organizer - pass ticketcost to organiser
        primary_receiver = new PayPalReceiverDetails();
        primary_receiver.setIsPrimary(true);
        Online online = TicketsManager.getInstance().mPaymentOptions.getOnline();
        primary_receiver.setRecipient(online.getPaypal().getOrgPaypalId());
        primary_receiver.setSubtotal(new BigDecimal(Double.parseDouble(TicketsManager.getInstance().mOrder.getTicketCost())));

        // secondary receiver is Explara -  pass explaraFee
        secondary_receiver = new PayPalReceiverDetails();
        secondary_receiver.setIsPrimary(false);
        secondary_receiver.setRecipient(Constants.DEBUG ? "santosh@explara.com" : "paypalIN@explara.com");
        secondary_receiver.setSubtotal(new BigDecimal(Double.parseDouble(TicketsManager.getInstance().mOrder.getExplaraFee())));

        PayPalAdvancedPayment advPayment = new PayPalAdvancedPayment();
        advPayment.setCurrencyType(currency);
        advPayment.getReceivers().add(primary_receiver);
        advPayment.getReceivers().add(secondary_receiver);

        Intent paypalIntent = PayPal.getInstance().checkout(advPayment, getActivity());
        this.startActivityForResult(paypalIntent, REQUEST_PAYPAL_CHECKOUT);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_PAYPAL_CHECKOUT) {
            PayPalActivityResult(requestCode, resultCode, intent);
        }
    }

    // Handle the response returned from PayPal
    public void PayPalActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (resultCode) {

            // The payment succeeded
            case Activity.RESULT_OK:
                String payKey = intent.getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
                showMaterialDialog();
                getPaypalFinalOrder(payKey);
                break;

            // The payment was canceled
            case Activity.RESULT_CANCELED:
                Toast.makeText(getActivity(), "Payment Cancelled", Toast.LENGTH_SHORT).show();
                break;

            // The payment failed, get the error from the EXTRA_ERROR_ID and EXTRA_ERROR_MESSAGE
            case PayPalActivity.RESULT_FAILURE:
                String errorID = intent.getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
                String errorMessage = intent.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
                Toast.makeText(getActivity(), errorID + ": " + errorMessage + "(Failed)", Toast.LENGTH_SHORT).show();
        }
    }

    // for Paypal Sdk
    private void getPaypalFinalOrder(String payKey) {
        PaymentManager.getInstance().getPaypalFinalOrder(getActivity().getApplicationContext(), payKey, new PaymentManager.GetPaypalFinalOrderListener() {
            @Override
            public void onGetPaypalFinalOrderSuccess(PaypalCheckoutResponse paypalCheckoutResponse) {
                if (getActivity() != null && paypalCheckoutResponse != null) {
                    dismissMaterialDialog();
                    if (paypalCheckoutResponse.getStatus().equals("success")) {
                        navigateToConfirmationPage();
                    } else {
                        // Show a toast that confirmation was not successful
                        Toast.makeText(getActivity(), paypalCheckoutResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onGetPaypalFinalOrderFailed() {
                if (getActivity() != null) {
                    dismissMaterialDialog();
                    Toast.makeText(getActivity().getApplicationContext(), "Paypal final order generation failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    }

    private void navigateToConfirmationPage() {
        Intent intent = new Intent(getActivity(), ConfirmationOnlineActivity.class);
        intent.putExtra(Constants.EVENT_ID, mEventID);
        startActivity(intent);
    }


}
