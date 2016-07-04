package com.explara_core.communication;


import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.explara_core.common.BaseManager;
import com.explara_core.communication.dto.BuyTicketDataDto;
import com.explara_core.communication.dto.CheckoutOfflineCallBackDataDto;
import com.explara_core.communication.dto.LunchPaymentActivityDataDto;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;

/**
 * Created by debasishpanda on 22/09/15.
 */
public class BaseCommunicationManager extends BaseManager {

    private static final String TAG = BaseCommunicationManager.class.getSimpleName();
    //public TransactionDto mTransaction = new TransactionDto();
    private static BaseCommunicationManager mCommunicationManager;

    private BaseCommunicationManager() {

    }

    public static synchronized BaseCommunicationManager getInstance() {
        if (mCommunicationManager == null) {
            mCommunicationManager = new BaseCommunicationManager();
        }
        return mCommunicationManager;
    }

    public interface EventListingCallBackListener {
        void onBuyTicketButtonClicked(Context context, BuyTicketDataDto buyTicketDataDto);

        Fragment getRsvpFragmentFromEventId(String eventId);
    }

    public interface TicketListingCallBackListnener {
        void onPaymentActivity(Context context, LunchPaymentActivityDataDto lunchPaymentActivityDataDto);

        void onCheckoutOffline(Context context, CheckoutOfflineCallBackDataDto checkoutOfflineCallBackDataDto);

        void getEventSessionTypeFromEventId(String eventId);

        void initialiseCitrus(Context context);

        void loadPreferredWalletBalanceListener(Context context, String tag);

        void storeDataInTransactionDtoFromPerference(Context context);

        void storeDataInTransactionDtoFromBuyerForm(String buyerName, String buyerEmailId, String buyerMobile);

        void storeEventIdInTransactionDto(String eventId);

        void storeCartTotalAmountInTransactionDto(String totalAmount);

        void storeOrderNoInTransactionDto(String orderNo);

        void resetTransactionDto();


    }

    public interface PaymentCallBackListener {
        void getEventDetailsFromEventId(String EventId);

        String getCurrencyFromEventId(String EventId);
    }

    public interface SetOnTransactionCompleteLister {
        void onTransactionComplete(Context context);
    }

    public interface AppCallBackListener extends SetOnTransactionCompleteLister {
        void launchTicketPage(Context context);

        void getSelectedCollectionIdFromPosition(int selectedSessionPosition);

        void launchRsvpFormPage(Context context, String eventId);

        void launchTopicsPage(Context context, String topicId);

        void launchEnquiryPage(Context context, String eventId);
    }

    public interface AnalyticsListener {
        void sendScreenName(String screenName, Application application, Context context);

        void sendAnEvent(String categoryId, String actionId, Application application, Context context);

        void sendEcommerce(String screenName, Application application, Product product, ProductAction productAction, Context context);
    }


    @Override
    public void cleanUp() {
        mCommunicationManager = null;
    }

}
