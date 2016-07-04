package com.explara.sdkdemo.communication;


import com.explara.explara_eventslisting_sdk.events.EventsManger;
import com.explara.explara_payment_sdk.payment.PaymentManager;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.sdkdemo.communication.ui.SdkCallBack;
import com.explara_core.common.BaseManager;

/**
 * Created by debasishpanda on 22/09/15.
 */
public class AppCommunicationManager extends BaseManager {

    private static final String TAG = AppCommunicationManager.class.getSimpleName();
    //public TransactionDto mTransaction = new TransactionDto();
    public static AppCommunicationManager mAppCommunicationManager;

    private AppCommunicationManager() {

    }

    public static synchronized AppCommunicationManager getInstance() {
        if (mAppCommunicationManager == null) {
            mAppCommunicationManager = new AppCommunicationManager();
            EventsManger.getInstance().initAppListeners(new SdkCallBack());
            TicketsManager.getInstance().initAppListener(new SdkCallBack());
            PaymentManager.getInstance().initAppListener(new SdkCallBack());
        }
        return mAppCommunicationManager;
    }

    @Override
    public void cleanUp() {
        // mTransaction = null;
    }

}
