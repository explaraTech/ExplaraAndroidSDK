package com.explara.explara_sdk.communication.ui;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.response.CitrusError;
import com.explara.explara_eventslisting_sdk.events.EventsManger;
import com.explara.explara_payment_sdk.payment.PaymentManager;
import com.explara.explara_payment_sdk.payment.dto.PayTmUserProfile;
import com.explara.explara_payment_sdk.payment.ui.CheckoutOfflineActivity;
import com.explara.explara_payment_sdk.payment.ui.PaymentActivity;
import com.explara.explara_sdk.communication.CommunicationManager;
import com.explara.explara_sdk.communication.dto.TransactionDto;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk.tickets.dto.BuyerDetailWithOutAttendeeFormDto;
import com.explara.explara_ticketing_sdk_ui.tickets.ui.TicketsDetailsFragment;
import com.explara_core.communication.BaseCommunicationManager;
import com.explara_core.communication.dto.CheckoutOfflineCallBackDataDto;
import com.explara_core.communication.dto.LunchPaymentActivityDataDto;
import com.explara_core.utils.Constants;
import com.explara_core.utils.PreferenceManager;
import com.orhanobut.logger.Logger;

/**
 * Created by debasishpanda on 31/05/16.
 */
public class TicketListingCallBack implements BaseCommunicationManager.TicketListingCallBackListnener {

    @Override
    public void onPaymentActivity(Context context, LunchPaymentActivityDataDto lunchPaymentActivityDataDto) {
        if (lunchPaymentActivityDataDto != null) {
            Intent intent = new Intent(context, PaymentActivity.class);
            intent.putExtra(Constants.EVENT_ID, lunchPaymentActivityDataDto.eventId);
            if (lunchPaymentActivityDataDto.launchDefaultPaymentOption) {
                intent.putExtra(TicketsDetailsFragment.SHOULD_LAUNCH_PREFFERED_WALLET, lunchPaymentActivityDataDto.launchDefaultPaymentOption);
            }
            context.startActivity(intent);
        }
    }

    @Override
    public void onCheckoutOffline(final Context context, CheckoutOfflineCallBackDataDto checkoutOfflineCallBackDataDto) {
        if (checkoutOfflineCallBackDataDto != null) {
            Intent intent = new Intent(context, CheckoutOfflineActivity.class);
            intent.putExtra(Constants.EVENT_ID, checkoutOfflineCallBackDataDto.eventId);
            intent.putExtra(Constants.PAYMENT_OPTION, checkoutOfflineCallBackDataDto.paymentMode);
            intent.putExtra(Constants.TAG, checkoutOfflineCallBackDataDto.tag);
            context.startActivity(intent);
        }
    }

    @Override
    public void getEventSessionTypeFromEventId(String eventId) {
        String eventSessionType = EventsManger.getInstance().eventsDetailDtoMap.get(eventId).events.getEventSessionType();
        TicketsManager.getInstance().mEventSessionType = eventSessionType;
    }

    @Override
    public void initialiseCitrus(Context context) {
        PaymentManager.getInstance().initialiseCitrus(context);
    }

    @Override
    public void loadPreferredWalletBalanceListener(Context context, final String TAG) {
        PreferenceManager preferenceManager = PreferenceManager.getInstance(context);
        if (preferenceManager.isPreferredWalletOptionSelected()) {
            int preferredWalletOption = preferenceManager.getPreferredWalletOption();
            if (preferredWalletOption == PreferenceManager.PAY_TM_PREFERRED_WALLET) {

                PaymentManager.getInstance().getPayTmUserDetails(context, preferenceManager.getPayTmAccessToken(), new PaymentManager.FetchPaytmProfileListener() {
                    @Override
                    public void onFetchPaytmUserProfile(PayTmUserProfile payTmUserProfile) {
                        TicketsManager.getInstance().mWalletbalance = payTmUserProfile.WALLETBALANCE;
                        //textView.setText(String.format("Balance -  %s %s", getContext().getString(com.explara.explara_ticketing_sdk_ui.R.string.rupee_symbol), payTmUserProfile.WALLETBALANCE));
                    }

                    @Override
                    public void onFetchpaytmUserProfileFailed(VolleyError volleyError) {

                    }
                }, TAG);
            } else if (preferredWalletOption == PreferenceManager.CITRUS_PREFFRED_WALLET) {
                CitrusClient.getInstance(context).getBalance(new Callback<Amount>() {
                    @Override
                    public void success(Amount amount) {
                        TicketsManager.getInstance().mWalletbalance = amount.getValueAsFormattedDouble("#.00");
                        //setBalance(getView(), String.format("Balance -  %s %s", getContext().getString(com.explara.explara_ticketing_sdk_ui.R.string.rupee_symbol), amount.getValueAsFormattedDouble("#.00")));

                    }

                    @Override
                    public void error(CitrusError citrusError) {
                        Logger.d(TAG + " get Balance failure " + citrusError.getMessage());
                    }
                });
            }
        }
    }

    @Override
    public void storeDataInTransactionDtoFromPerference(Context context) {
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
        CommunicationManager.getInstance().mTransaction.setBuyerDetail(buyerDetailWithOutAttendeeFormDto);
    }

    @Override
    public void storeDataInTransactionDtoFromBuyerForm(String buyerName, String buyerEmailId, String buyerMobile) {
        BuyerDetailWithOutAttendeeFormDto buyerDetailWithOutAttendeeFormDto = new BuyerDetailWithOutAttendeeFormDto();
        buyerDetailWithOutAttendeeFormDto.buyerName = buyerName;
        buyerDetailWithOutAttendeeFormDto.buyerEmail = buyerEmailId;
        buyerDetailWithOutAttendeeFormDto.buyerPhone = buyerMobile;
        CommunicationManager.getInstance().mTransaction.setBuyerDetail(buyerDetailWithOutAttendeeFormDto);
    }

    @Override
    public void storeEventIdInTransactionDto(String eventId) {
        if (CommunicationManager.getInstance().mTransaction != null) {
            CommunicationManager.getInstance().mTransaction.eventId = eventId;
        }
    }

    @Override
    public void storeCartTotalAmountInTransactionDto(String totalAmount) {
        if (CommunicationManager.getInstance().mTransaction != null) {
            CommunicationManager.getInstance().mTransaction.grandTotalAmount = totalAmount;
        }
    }

    @Override
    public void storeOrderNoInTransactionDto(String orderNo) {
        if (CommunicationManager.getInstance().mTransaction != null) {
            CommunicationManager.getInstance().mTransaction.orderNo = orderNo;
        }
    }

    @Override
    public void resetTransactionDto() {
        CommunicationManager.getInstance().mTransaction = new TransactionDto();
    }


}
