package com.explara.sdkdemo.communication.ui;

import android.content.Context;
import android.content.Intent;

import com.explara.sdkdemo.MainActivity;
import com.explara_core.communication.BaseCommunicationManager;

/**
 * Created by debasishpanda on 31/05/16.
 */
public class SdkCallBack implements BaseCommunicationManager.AppCallBackListener {

    @Override
    public void onTransactionComplete(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void launchTicketPage(Context context) {

    }

    @Override
    public void getSelectedCollectionIdFromPosition(int selectedSessionPosition) {

    }

    @Override
    public void launchRsvpFormPage(Context context, String eventId) {

    }

    @Override
    public void launchTopicsPage(Context context, String topicId) {

    }

    @Override
    public void launchEnquiryPage(Context context, String eventId) {

    }
}
