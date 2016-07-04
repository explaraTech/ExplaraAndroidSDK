package com.explara.explara_sdk.communication;

import com.explara.explara_eventslisting_sdk.events.EventsManger;
import com.explara.explara_payment_sdk.payment.PaymentManager;
import com.explara.explara_sdk.communication.dto.TransactionDto;
import com.explara.explara_sdk.communication.ui.EventListingCallBack;
import com.explara.explara_sdk.communication.ui.PaymentCallBack;
import com.explara.explara_sdk.communication.ui.TicketListingCallBack;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara_core.common.BaseManager;

/**
 * Created by debasishpanda on 22/09/15.
 */
public class CommunicationManager extends BaseManager {

    private static final String TAG = CommunicationManager.class.getSimpleName();

    public static CommunicationManager mCommunicationManager;
    public TransactionDto mTransaction = new TransactionDto();

    private CommunicationManager() {
        EventsManger.getInstance().initListeners(new EventListingCallBack());
        TicketsManager.getInstance().initListener(new TicketListingCallBack());
        PaymentManager.getInstance().initListener(new PaymentCallBack());
    }

    public static synchronized CommunicationManager getInstance() {
        if (mCommunicationManager == null) {
            mCommunicationManager = new CommunicationManager();
        }
        return mCommunicationManager;
    }

    @Override
    public void cleanUp() {
        mCommunicationManager = null;
        mTransaction = null;
    }

}
