package com.explara.explara_payment_sdk.payment;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.ui.utils.CitrusFlowManager;
import com.explara.explara_payment_sdk.R;
import com.explara.explara_payment_sdk.common.BaseManager;
import com.explara.explara_payment_sdk.payment.dto.CheckoutOfflineResponse;
import com.explara.explara_payment_sdk.payment.dto.EventDetails;
import com.explara.explara_payment_sdk.payment.dto.OlaBill;
import com.explara.explara_payment_sdk.payment.dto.OlaPaymentResposneDto;
import com.explara.explara_payment_sdk.payment.dto.OtpResponseDto;
import com.explara.explara_payment_sdk.payment.dto.PayTmUserProfile;
import com.explara.explara_payment_sdk.payment.dto.PaymentOptionDto;
import com.explara.explara_payment_sdk.payment.dto.PaypalCheckoutResponse;
import com.explara.explara_payment_sdk.payment.dto.PaytmPayMentResposnseDto;
import com.explara.explara_payment_sdk.payment.dto.SendMutipleEmailDto;
import com.explara.explara_payment_sdk.payment.dto.ValidateOtpResposneDto;
import com.explara.explara_payment_sdk.payment.dto.WalletItemDto;
import com.explara.explara_payment_sdk.payment.io.PaymentConnectionManager;
import com.explara.explara_payment_sdk.utils.CitrusHelper;
import com.explara.explara_payment_sdk.utils.UrlConstants;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk.tickets.dto.Offline;
import com.explara.explara_ticketing_sdk.tickets.dto.Online;
import com.explara.explara_ticketing_sdk.tickets.dto.PaymentOptions;
import com.explara_core.communication.BaseCommunicationManager;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Debasish
 */
public class PaymentManager extends BaseManager {


    public static final int PAYTM_ID = 0;
    public static final int CITRUS_ID = 1;
    public static PaymentManager sTopicsManager;
    public OtpResponseDto otpResposneDto;
    public PayTmUserProfile payTmUserProfile;
    public ValidateOtpResposneDto payTmUserToken;
    public CheckoutOfflineResponse mCheckoutOfflineResponse;
    public PaypalCheckoutResponse mPaypalCheckoutResponse;
    public PaymentOptions mPaymentOptions;
    // Will be implement later
    //public TransactionDto mTransaction = new TransactionDto();

    public BaseCommunicationManager.PaymentCallBackListener mPaymentCallBackListener;
    public EventDetails mEventDetails;
    public BaseCommunicationManager.AppCallBackListener mAppCallBackListener;
    public BaseCommunicationManager.AnalyticsListener mAnalyticsListener;


    private PaymentManager() {

    }

    public void initListener(BaseCommunicationManager.PaymentCallBackListener paymentCallBackListener) {
        this.mPaymentCallBackListener = paymentCallBackListener;
    }

    public void initAppListener(BaseCommunicationManager.AppCallBackListener appCallBackListener) {
        this.mAppCallBackListener = appCallBackListener;
    }

    public void initAnalyticsLisener(BaseCommunicationManager.AnalyticsListener analyticsListener) {
        this.mAnalyticsListener = analyticsListener;
    }

    public interface SendMutipleEmailListener {
        void onMultipleEmailSent(SendMutipleEmailDto sendMutipleEmailDto);

        void onMultipleEmailSendFailed(VolleyError volleyError);
    }

    public interface CheckoutOfflineListener {
        void onOfflineCheckout(CheckoutOfflineResponse checkoutOfflineResponse);

        void onOfflineCheckoutFailed();
    }

    public interface GetPaypalFinalOrderListener {
        void onGetPaypalFinalOrderSuccess(PaypalCheckoutResponse paypalCheckoutResponse);

        void onGetPaypalFinalOrderFailed();
    }

    public interface GetOlaBillListener {
        void onGetOlaBillSuccess(OlaBill olaBill);

        void onGetOlaBillFail();
    }

    public interface RequestOtpListener {
        void onOtpReadSuccess(OtpResponseDto otpResposneDto);

        void onOtpFailed(VolleyError volleyError);
    }

    public interface ValidatePayTmOtpListener {
        void onOtpReadSuccess(ValidateOtpResposneDto validateOtpResposneDto);

        void onOtpFailed(VolleyError volleyError);
    }

    public interface FetchPaytmProfileListener {
        void onFetchPaytmUserProfile(PayTmUserProfile payTmUserProfile);

        void onFetchpaytmUserProfileFailed(VolleyError volleyError);
    }

    public interface FetchPayTmUserBalance {
        void onFetchPayTmBalance(PayTmUserProfile payTmUserProfile);

        void onFetchPayTmBalanceFailed(VolleyError volleyError);
    }

    public interface UpdateOlaStatusListener {
        void onUpDateOlaStatusToServer(OlaPaymentResposneDto payTmUserProfile);

        void onUpDateOlaStatusToServerFailed(VolleyError volleyError);
    }

    public interface PayWithPaytmStatusListener {
        void onPayWithPaytmStatusSuccess(PaytmPayMentResposnseDto payTmUserProfile);

        void onPayWithPaytmStatusFailed(VolleyError volleyError);
    }

    public static synchronized PaymentManager getInstance() {
        if (sTopicsManager == null) {
            sTopicsManager = new PaymentManager();
        }
        return sTopicsManager;
    }

    public void checkoutOffline(Context context, String paymentOptionName, CheckoutOfflineListener checkoutOfflineListener, String tag) {
        PaymentConnectionManager paymentConnectionManager = new PaymentConnectionManager();
        paymentConnectionManager.checkoutOffline(context, paymentOptionName, offlineCheckoutSuccessListener(checkoutOfflineListener), offlineCheckoutErrorListener(checkoutOfflineListener), tag);
    }

    public void getPaypalFinalOrder(Context context, String payKey, GetPaypalFinalOrderListener getPaypalFinalOrderListener, String tag) {
        PaymentConnectionManager paymentConnectionManager = new PaymentConnectionManager();
        paymentConnectionManager.getPaypalFinalOrder(context, payKey, getPaypalOrderSuccessListener(getPaypalFinalOrderListener), getPaypalOrderErrorListener(getPaypalFinalOrderListener), tag);
    }

    public void payWithOlaMoney(Context context, GetOlaBillListener getOlaBillListener, String tag, final String orderId) {
        PaymentConnectionManager paymentConnectionManager = new PaymentConnectionManager();
        paymentConnectionManager.generateOlaBill(context, orderId, getOlaBillSuccessListener(getOlaBillListener), getOlaBillErrorlistener(getOlaBillListener), tag);

    }

    public void updateOlaStatusToServer(Context context, String status, UpdateOlaStatusListener updateOlaStatusListener, String tag) {
        PaymentConnectionManager paymentConnectionManager = new PaymentConnectionManager();

        paymentConnectionManager.updateOlaStatusToServer(context, status, updateOlaStatusToServerSuccessListener(updateOlaStatusListener), updateOlaStatusToServerErrorListener(updateOlaStatusListener), tag);
    }

    public void payWithPaytm(Context context, final String accessToken, final String orderNum, PayWithPaytmStatusListener payWithPaytmStatusListener, String tag) {
        PaymentConnectionManager paymentConnectionManager = new PaymentConnectionManager();
        paymentConnectionManager.paywithPayTm(context, orderNum, accessToken, payWithPaytmSuccessListener(payWithPaytmStatusListener), payWithPaytmErrorListener(payWithPaytmStatusListener), tag);
    }

    public String getUserPayTmBalance() {
        if (payTmUserProfile != null)
            return payTmUserProfile.WALLETBALANCE;
        return "";
    }

    private Response.Listener<OlaBill> getOlaBillSuccessListener(final GetOlaBillListener getOlaBill) {
        return new Response.Listener<OlaBill>() {
            @Override
            public void onResponse(OlaBill response) {
                getOlaBill.onGetOlaBillSuccess(response);
            }
        };
    }

    private Response.ErrorListener getOlaBillErrorlistener(final GetOlaBillListener getOlaBillListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getOlaBillListener.onGetOlaBillFail();
            }
        };
    }

    // Get paypal order status - success
    private Response.Listener<PaypalCheckoutResponse> getPaypalOrderSuccessListener(final GetPaypalFinalOrderListener getPaypalFinalOrderListener) {
        return new Response.Listener<PaypalCheckoutResponse>() {
            @Override
            public void onResponse(PaypalCheckoutResponse response) {
                mPaypalCheckoutResponse = response;
                if (getPaypalFinalOrderListener != null) {
                    getPaypalFinalOrderListener.onGetPaypalFinalOrderSuccess(response);
                }
            }
        };
    }

    // Get paypal order status - Failure
    private Response.ErrorListener getPaypalOrderErrorListener(final GetPaypalFinalOrderListener getPaypalFinalOrderListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (getPaypalFinalOrderListener != null) {
                    getPaypalFinalOrderListener.onGetPaypalFinalOrderFailed();
                }
            }
        };
    }


    // Checkout offline - success
    private Response.Listener<CheckoutOfflineResponse> offlineCheckoutSuccessListener(final CheckoutOfflineListener checkoutOfflineListener) {
        return new Response.Listener<CheckoutOfflineResponse>() {
            @Override
            public void onResponse(CheckoutOfflineResponse response) {
                mCheckoutOfflineResponse = response;
                if (checkoutOfflineListener != null) {
                    checkoutOfflineListener.onOfflineCheckout(response);
                }
            }
        };
    }

    // Checkout offline - Failure
    private Response.ErrorListener offlineCheckoutErrorListener(final CheckoutOfflineListener checkoutOfflineListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (checkoutOfflineListener != null) {
                    checkoutOfflineListener.onOfflineCheckoutFailed();
                }
            }
        };
    }


    public List<WalletItemDto> getWallets() {
        List<WalletItemDto> wallets = new ArrayList<>();
        WalletItemDto walletItemDto = new WalletItemDto();
        walletItemDto.walletId = PAYTM_ID;
        walletItemDto.walletName = ConstantKeys.Wallets.PAYTM;
        wallets.add(walletItemDto);


        walletItemDto = new WalletItemDto();
        walletItemDto.walletId = CITRUS_ID;
        walletItemDto.walletName = ConstantKeys.Wallets.CITRUS;
        wallets.add(walletItemDto);
        return wallets;
    }


    public void requestForOtp(Context context, RequestOtpListener requestOtpListener, final String emailId, String phoneNum, String tag) {
        PaymentConnectionManager paymentConnectionManager = new PaymentConnectionManager();
        paymentConnectionManager.requestPayTmOtpGeneration(context, emailId, phoneNum, generateOtpSuccessListener(requestOtpListener), generateOtpErrorListener(requestOtpListener), tag);
    }


    public void validateOtp(Context context, ValidatePayTmOtpListener validatePayTmOtpListener, final String otp, final String state, String tag) {
        PaymentConnectionManager paymentConnectionManager = new PaymentConnectionManager();
        paymentConnectionManager.validatePayTmotp(context, otp, state, validateOtpSuccessListener(validatePayTmOtpListener), validateOtpErrorListener(validatePayTmOtpListener), tag);
    }

    public void getPayTmUserDetails(Context context, String accesToken, FetchPaytmProfileListener fetchPaytmProfileListener, String tag) {
        PaymentConnectionManager paymentConnectionManager = new PaymentConnectionManager();
        paymentConnectionManager.fetchPayTmProfile(context, accesToken, fetchPayTmUserProfileSuccessListener(fetchPaytmProfileListener), fetchPayTmUserpErrorListener(fetchPaytmProfileListener), tag);
    }

    public void onSendBtnClick(Context context, String emailids, SendMutipleEmailListener sendMutipleEmailListener, String tag) {
        PaymentConnectionManager paymentConnectionManager = new PaymentConnectionManager();
        paymentConnectionManager.onSendBtnClick(context, emailids, sendEmailSuccessListener(sendMutipleEmailListener), sendEmailErrorListener(sendMutipleEmailListener), tag);
    }

    // Generate otp - success
    private Response.Listener<SendMutipleEmailDto> sendEmailSuccessListener(final SendMutipleEmailListener sendMutipleEmailListener) {
        return new Response.Listener<SendMutipleEmailDto>() {
            @Override
            public void onResponse(SendMutipleEmailDto response) {
                if (sendMutipleEmailListener != null) {
                    sendMutipleEmailListener.onMultipleEmailSent(response);
                }
            }
        };
    }

    // Generate otp - Failure
    private Response.ErrorListener sendEmailErrorListener(final SendMutipleEmailListener sendMutipleEmailListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (sendMutipleEmailListener != null) {
                    sendMutipleEmailListener.onMultipleEmailSendFailed(error);
                }
            }
        };
    }


    // Generate otp - success
    private Response.Listener<OtpResponseDto> generateOtpSuccessListener(final RequestOtpListener generateOrderListener) {
        return new Response.Listener<OtpResponseDto>() {
            @Override
            public void onResponse(OtpResponseDto response) {
                otpResposneDto = response;
                if (generateOrderListener != null) {
                    generateOrderListener.onOtpReadSuccess(response);
                }
            }
        };
    }

    // Generate otp - Failure
    private Response.ErrorListener generateOtpErrorListener(final RequestOtpListener generateOrderListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (generateOrderListener != null) {
                    generateOrderListener.onOtpFailed(error);
                }
            }
        };
    }


    // Validate otp - success
    private Response.Listener<ValidateOtpResposneDto> validateOtpSuccessListener(final ValidatePayTmOtpListener generateOrderListener) {
        return new Response.Listener<ValidateOtpResposneDto>() {

            @Override
            public void onResponse(ValidateOtpResposneDto response) {
                payTmUserToken = response;
                if (generateOrderListener != null) {
                    generateOrderListener.onOtpReadSuccess(response);
                }
            }
        };
    }

    // Validate otp - Failure
    private Response.ErrorListener validateOtpErrorListener(final ValidatePayTmOtpListener generateOrderListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (generateOrderListener != null) {
                    generateOrderListener.onOtpFailed(error);
                }
            }
        };
    }


    // Fetch USer Profile  - success
    private Response.Listener<PayTmUserProfile> fetchPayTmUserProfileSuccessListener(final FetchPaytmProfileListener generateOrderListener) {
        return new Response.Listener<PayTmUserProfile>() {
            @Override
            public void onResponse(PayTmUserProfile response) {
                payTmUserProfile = response;
                if (generateOrderListener != null) {
                    generateOrderListener.onFetchPaytmUserProfile(response);
                }
            }
        };
    }

    // Fetch user Profile  - Failure
    private Response.ErrorListener fetchPayTmUserpErrorListener(final FetchPaytmProfileListener generateOrderListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (generateOrderListener != null) {
                    generateOrderListener.onFetchpaytmUserProfileFailed(error);
                }
            }
        };
    }


    // Fetch USer balance  - success
    private Response.Listener<PayTmUserProfile> fetchPayTmUserBalanceSuccessListener(final FetchPayTmUserBalance generateOrderListener) {
        return new Response.Listener<PayTmUserProfile>() {
            @Override
            public void onResponse(PayTmUserProfile response) {
                payTmUserProfile = response;
                if (generateOrderListener != null) {
                    generateOrderListener.onFetchPayTmBalance(response);
                }
            }
        };
    }

    // Fetch user balance  - Failure
    private Response.ErrorListener fetchPayTmUserBalanceErrorListener(final FetchPayTmUserBalance generateOrderListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (generateOrderListener != null) {
                    generateOrderListener.onFetchPayTmBalanceFailed(error);
                }
            }
        };
    }

    // Ola Server status update  - success
    private Response.Listener<OlaPaymentResposneDto> updateOlaStatusToServerSuccessListener(final UpdateOlaStatusListener generateOrderListener) {
        return new Response.Listener<OlaPaymentResposneDto>() {
            @Override
            public void onResponse(OlaPaymentResposneDto response) {
                if (generateOrderListener != null) {
                    generateOrderListener.onUpDateOlaStatusToServer(response);
                }
            }
        };
    }

    // Ola Server status update  - Failure
    private Response.ErrorListener updateOlaStatusToServerErrorListener(final UpdateOlaStatusListener generateOrderListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (generateOrderListener != null) {
                    generateOrderListener.onUpDateOlaStatusToServerFailed(error);
                }
            }
        };
    }


    // Paytm Block money   - success
    private Response.Listener<PaytmPayMentResposnseDto> payWithPaytmSuccessListener(final PayWithPaytmStatusListener payWithPaytmStatusListener) {
        return new Response.Listener<PaytmPayMentResposnseDto>() {
            @Override
            public void onResponse(PaytmPayMentResposnseDto response) {
                if (payWithPaytmStatusListener != null) {
                    payWithPaytmStatusListener.onPayWithPaytmStatusSuccess(response);
                }
            }
        };
    }

    // PayTm Block money   - Failure
    private Response.ErrorListener payWithPaytmErrorListener(final PayWithPaytmStatusListener generateOrderListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (generateOrderListener != null) {
                    generateOrderListener.onPayWithPaytmStatusFailed(error);
                }
            }
        };
    }

    public List<PaymentOptionDto> getPaymentOptionsList() {
        optionDtoList = new ArrayList<>();
//        LinkedHashMap<String, List<PaymentOptionDto>> paymentOptions = new LinkedHashMap<>(2);
        // Taking from generate order
        mPaymentOptions = TicketsManager.getInstance().mOrder.getPaymentOption();
        if (mPaymentOptions != null) {

            Online onlineOptions = mPaymentOptions.getOnline();
            constructOnlineOptions(onlineOptions);

            Offline offlineOptions = mPaymentOptions.getOffline();
            constructOfflineOptions(offlineOptions);
        }
        return optionDtoList;

    }

    List<PaymentOptionDto> optionDtoList = new ArrayList<>();

    private void constructOnlineOptions(Online online) {
        if (online != null) {
            boolean isCcAvenueEnabled = online.getCcavenue().getIsEnable();
            boolean isPayPalEnabled = online.getPaypal().getIsEnable();
            boolean isPaytmEnabled = online.getPaytm().getIsEnable();
            boolean isOlaMoneyEnabled = online.ola.isEnable;
            boolean isCitrusEnabled = false;
            if (online.citrus != null)
                isCitrusEnabled = online.citrus.isEnable;
            boolean isCitrusWalletEnabled = false;
            if (online.citrusWallet != null)
                isCitrusWalletEnabled = online.citrusWallet.isEnable;
            if (isCcAvenueEnabled || isPayPalEnabled || isPaytmEnabled || isOlaMoneyEnabled || isCitrusEnabled || isCitrusWalletEnabled) {
                getOnlineOptions(isCcAvenueEnabled, isPayPalEnabled, isPaytmEnabled, isOlaMoneyEnabled, isCitrusEnabled, isCitrusWalletEnabled, optionDtoList);
            }
        }
    }

    private void constructOfflineOptions(Offline offlineOptions) {
        if (offlineOptions != null) {
            boolean isChequeEnabled = offlineOptions.getCheque().getIsEnabled();
            boolean isDdEnabled = offlineOptions.getDd().getIsEnabled();
            boolean isVenuePaymentEnabled = offlineOptions.getVenue().getIsEnabled();
            boolean isDepositEnabled = offlineOptions.getDeposit().getIsEnabled();
            if (isChequeEnabled || isDdEnabled || isDepositEnabled || isVenuePaymentEnabled) {
                getOfflineOptions(isChequeEnabled, isDdEnabled, isVenuePaymentEnabled, isDepositEnabled, optionDtoList);
            }
        }
    }

    private void getOnlineOptions(boolean isCcAvenueEnabled, boolean isPayPalEnabled, boolean isPaytmEnabled, boolean isOlaMoneyEnabled, boolean isCitrusEnabled, boolean isCitrusWalletEnabled, List<PaymentOptionDto> optionDtoList) {
        getPaymentOption(isPaytmEnabled, optionDtoList, ConstantKeys.PaymentOptionIds.PAY_TM_WALLET_TITLE, ConstantKeys.PaymentOptionIds.PAYTM);
        getPaymentOption(isCitrusEnabled, optionDtoList, ConstantKeys.PaymentOptionIds.CITRUS_PAYMENT_TITLE, ConstantKeys.PaymentOptionIds.CITRUS_ID);
        getPaymentOption(isCitrusWalletEnabled, optionDtoList, ConstantKeys.PaymentOptionIds.CITRUS_WALLET_TITLE, ConstantKeys.PaymentOptionIds.CITRUS_WALLET_ID);
        getPaymentOption(isCcAvenueEnabled, optionDtoList, ConstantKeys.PaymentOptionIds.CC_AVENUE_TITLE, ConstantKeys.PaymentOptionIds.CCAVENUE);
        getPaymentOption(isPayPalEnabled, optionDtoList, ConstantKeys.PaymentOptionIds.PAY_PAL_TITLE, ConstantKeys.PaymentOptionIds.PAYPAL);
        getPaymentOption(isOlaMoneyEnabled, optionDtoList, ConstantKeys.PaymentOptionIds.OLA_MONEY_TITLE, ConstantKeys.PaymentOptionIds.OLA_MONEY);

//        /**
//         * Hard code Citrus
//         */
//        PaymentOptionDto paymentOptionDto;
//        paymentOptionDto = new PaymentOptionDto();
//        paymentOptionDto.id = ConstantKeys.PaymentOptionIds.CITRUS_WALLET_ID;
//        paymentOptionDto.title = ConstantKeys.PaymentOptionIds.CITRUS_WALLET_TITLE;
//        optionDtoList.add(paymentOptionDto);
//
//        paymentOptionDto = new PaymentOptionDto();
//        paymentOptionDto.id = ConstantKeys.PaymentOptionIds.CITRUS_ID;
//        paymentOptionDto.title = ConstantKeys.PaymentOptionIds.CITRUS_PAYMENT_TITLE;
//        optionDtoList.add(paymentOptionDto);
    }

    private void getOfflineOptions(boolean isChequeEnabled, boolean isDdEnabled, boolean isVenuePaymentEnabled, boolean isDepositEnabled, List<PaymentOptionDto> optionDtoList) {
        getPaymentOption(isChequeEnabled, optionDtoList, ConstantKeys.PaymentOptionIds.CHEQUE, ConstantKeys.PaymentOptionIds.CHEQUE_ID);
        getPaymentOption(isDdEnabled, optionDtoList, ConstantKeys.PaymentOptionIds.DD, ConstantKeys.PaymentOptionIds.dd);
        getPaymentOption(isDepositEnabled, optionDtoList, ConstantKeys.PaymentOptionIds.DEPOSIT, ConstantKeys.PaymentOptionIds.DEPOSIT_ID);
        getPaymentOption(isVenuePaymentEnabled, optionDtoList, ConstantKeys.PaymentOptionIds.VENUE, ConstantKeys.PaymentOptionIds.VENUE_ID);


    }

    private void getPaymentOption(boolean offlineOptionStatus, List<PaymentOptionDto> optionDtoList, String title, int id) {
        PaymentOptionDto paymentOptionDto;
        if (offlineOptionStatus) {
            paymentOptionDto = new PaymentOptionDto();
            paymentOptionDto.id = id;
            paymentOptionDto.title = title;
            optionDtoList.add(paymentOptionDto);
        }
    }

    // Transaction dto part will be implemented later.Commenting fot now
    /*public void resetCurrentTransactionObj() {
        mTransaction = new TransactionDto();
    }

    public void setEventIdInTransactionDto(String eventId) {
        mTransaction.eventId = eventId;
    }

    public void storeBuyerDetailsInTransactionDto(Context context) {
        BuyerDetailWithOutAttendeeFormDto buyerDetailWithOutAttendeeFormDto = new BuyerDetailWithOutAttendeeFormDto();
        if (!TextUtils.isEmpty(PreferenceManager.getInstance(context).getUserName())) {
            buyerDetailWithOutAttendeeFormDto.buyerName = PreferenceManager.getInstance(context).getUserName();
        }

        if (!TextUtils.isEmpty(PreferenceManager.getInstance(context).getEmail())) {
            buyerDetailWithOutAttendeeFormDto.buyerEmail = PreferenceManager.getInstance(context).getEmail();
        }

        if (!TextUtils.isEmpty(PreferenceManager.getInstance(context).getPhoneNo())) {
            buyerDetailWithOutAttendeeFormDto.buyerPhone = PreferenceManager.getInstance(context).getPhoneNo();
        }
        PaymentManager.getInstance().mTransaction.setBuyerDetail(buyerDetailWithOutAttendeeFormDto);
    }*/


    public void initialiseCitrus(Context context) {
        CitrusClient citrusClient = CitrusClient.getInstance(context); // Activity Context
        if (Constants.DEBUG)
            citrusClient.enableLog(true);
        CitrusFlowManager.initCitrusConfig(CitrusHelper.getSignUpId(),
                CitrusHelper.getSignUpSecret(), CitrusHelper.getSignInId(),
                CitrusHelper.getSignInSecret(), context.getResources().getColor(R.color.white),
                context, CitrusHelper.getEnvironMent(), CitrusHelper.getVanity(), "", UrlConstants.CITRUS_RETURN_URL, UrlConstants.CITRUS_ADDMONEY_RETURN_URL);
    }

    @Override
    public void cleanUp() {
        sTopicsManager = null;
        otpResposneDto = null;
        payTmUserProfile = null;
        payTmUserToken = null;
        mCheckoutOfflineResponse = null;
        mPaypalCheckoutResponse = null;
        mPaymentOptions = null;
        mPaymentCallBackListener = null;
        mEventDetails = null;
        mAppCallBackListener = null;
        mAnalyticsListener = null;

    }


}
