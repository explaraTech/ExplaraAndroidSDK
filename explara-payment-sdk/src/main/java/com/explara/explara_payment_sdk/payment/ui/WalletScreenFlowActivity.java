package com.explara.explara_payment_sdk.payment.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.citrus.sdk.CitrusUser;
import com.citrus.sdk.TransactionResponse;
import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.classes.CashoutInfo;
import com.citrus.sdk.classes.CitrusException;
import com.citrus.sdk.payment.CardOption;
import com.citrus.sdk.payment.CreditCardOption;
import com.citrus.sdk.payment.DebitCardOption;
import com.citrus.sdk.payment.PaymentOption;
import com.citrus.sdk.payment.PaymentType;
import com.citrus.sdk.ui.events.FragmentCallbacks;
import com.citrus.sdk.ui.utils.CitrusFlowManager;
import com.citrus.sdk.ui.utils.ResultModel;
import com.explara.explara_payment_sdk.R;
import com.explara.explara_payment_sdk.utils.UrlConstants;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk_ui.tickets.ui.TicketsDetailActivity;
import com.explara_core.utils.Constants;
import com.explara_core.utils.FragmentHelper;
import com.explara_core.utils.Log;
import com.explara_core.utils.PreferenceManager;
import com.orhanobut.logger.Logger;

/**
 * Created by anudeep on 28/12/15.
 */
public class WalletScreenFlowActivity extends AppCompatActivity implements FragmentCallbacks {

    private static final String TAG = WalletScreenFlowActivity.class.getSimpleName();
    public String eventId;
    private MenuItem mItem;
    protected ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_screen_flow_activity);
        eventId = getIntent().getStringExtra(Constants.EVENT_ID);
        FragmentHelper.replaceContentFragment(this, R.id.wallet_screen_flow_container, WalletScreenFlowFragment.newInstance(getIntent()), TAG);

    }

    @Override
    public void navigateTo(Fragment fragment, int screenName) {
        Log.d(TAG, "Class Name :" + fragment.getClass().getSimpleName() + "\nScreen Name : " + screenName);
        FragmentHelper.replaceAndAddContentFragment(this, R.id.wallet_screen_flow_container, fragment, TAG);

    }

    @Override
    public void makePayment(PaymentOption paymentOption) {

        CitrusUser citrusUser = new CitrusUser(PreferenceManager.getInstance(this).getEmail(), PreferenceManager.getInstance(this).getPhoneNo());
        Amount amount = new Amount(TicketsManager.getInstance().getGrandTotal());
        try {
            PaymentType paymentType = new PaymentType.PGPayment(amount, UrlConstants.CITRUS_BILL_GENERATOR + TicketsManager.getInstance().mOrder.getOrderNo(), paymentOption, citrusUser);
            Intent intent = new Intent(this, com.citrus.sdk.CitrusActivity.class);
            intent.putExtra(com.citrus.sdk.Constants.INTENT_EXTRA_PAYMENT_TYPE, paymentType);
            intent.putExtra(CitrusFlowManager.KEY_STYLE, R.style.AppTheme);
            startActivityForResult(intent, com.citrus.sdk.Constants.REQUEST_CODE_PAYMENT);
        } catch (CitrusException e) {
            Log.e(TAG, "CitrusException" + e.getMessage());
        }
    }

    @Override
    public void withdrawMoney(CashoutInfo cashoutInfo) {

    }

    @Override
    public void makeCardPayment(CardOption cardOption) {
        if (cardOption instanceof DebitCardOption) {
            Logger.d(TAG + " is Debit card");
            DebitCardOption debitCardOption = (DebitCardOption) cardOption;
            makePayment(debitCardOption);


        } else if (cardOption instanceof CreditCardOption) {
            Logger.d(TAG + " is Credit card");
            CreditCardOption creditCardOption = (CreditCardOption) cardOption;
            makePayment(creditCardOption);

        }
    }


    @Override
    public void showProgressDialog(boolean cancelable, String message) {
        if (mProgressDialog != null) {
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(cancelable);
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    @Override
    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }


    @Override
    public void onWalletTransactionComplete(ResultModel resultModel) {
        TransactionResponse transactionResponse = resultModel.getTransactionResponse();
        /*Fragment fragment = FragmentHelper.getFragment(this, R.id.wallet_screen_flow_container);
        ((WalletScreenFlowFragment)fragment).h*/
        if (transactionResponse != null && transactionResponse.getTransactionStatus() != null) {
            if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                Log.d(TAG, transactionResponse.getJsonResponse());
                Intent intent = new Intent(WalletScreenFlowActivity.this, ConfirmationOnlineActivity.class);
                intent.putExtra(Constants.EVENT_ID, eventId);
                startActivity(intent);
            }
        } else {
            Log.d("Transaction Failed", "");
            //Toast.makeText(getApplicationContext(), transactionResponse.getMessage(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(WalletScreenFlowActivity.this, TicketsDetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
//        Utils.getBalance(this);
//        FragmentHelper.replaceContentFragment(this, R.id.fragment_container, WalletFragment.newInstance(mFromScreen, eventId));
    }

    @Override
    public void showAmount(String amount) {

    }

    @Override
    public void showWalletBalance(String amount) {

    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getMobile() {
        return null;
    }

    @Override
    public String getAmount() {
        return TicketsManager.getInstance().mDiscountResponse.getCart().getGrandTotal();
    }

    @Override
    public int getStyle() {
        return R.style.AppTheme;
    }

    @Override
    public void toggleAmountVisibility(int visibility) {

    }

    @Override
    public void displayTerms(Activity activity) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        mItem = menu.findItem(R.id.create_event);
        mItem.setVisible(false);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Toast.makeText(this, "On New Intent Called", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
