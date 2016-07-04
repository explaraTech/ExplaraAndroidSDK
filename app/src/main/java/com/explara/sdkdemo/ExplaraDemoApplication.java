package com.explara.sdkdemo;

import android.app.Application;
import android.os.AsyncTask;

import com.explara.explara_sdk.communication.CommunicationManager;
import com.explara.sdkdemo.communication.AppCommunicationManager;
import com.explara_core.utils.Constants;
import com.paypal.android.MEP.PayPal;

/**
 * Created by debasishpanda on 04/07/16.
 */
public class ExplaraDemoApplication extends Application {
    private boolean _paypalLibraryInit;
    @Override
    public void onCreate() {
        super.onCreate();
        CommunicationManager.getInstance();
        AppCommunicationManager.getInstance();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                initPaypalLibrary();
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    // For Paypal transaction.Only for intenational transaction (Currency other than INR)
    private void initPaypalLibrary() {
        PayPal pp = PayPal.getInstance();
        if (pp == null) {
            String appId_sandbox = "APP-80W284485P519543T";
            String appId_prod = "APP-1C439872RM460774K";
            pp = Constants.DEBUG ?
                    PayPal.initWithAppID(this, appId_sandbox, PayPal.ENV_SANDBOX) :
                    PayPal.initWithAppID(this, appId_prod, PayPal.ENV_LIVE);
            pp.setLanguage("en_US");
            // Sets who pays any transaction fees. Possible values are:
            // FEEPAYER_SENDER, FEEPAYER_PRIMARYRECEIVER, FEEPAYER_EACHRECEIVER, and FEEPAYER_SECONDARYONLY
            pp.setFeesPayer(PayPal.FEEPAYER_PRIMARYRECEIVER);
            // true = transaction requires shipping
            pp.setShippingEnabled(true);
            _paypalLibraryInit = true;
        }
    }
}
