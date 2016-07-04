package com.explara.explara_payment_sdk.payment.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;
import com.citrus.sdk.CitrusUser;
import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.classes.CashoutInfo;
import com.citrus.sdk.classes.CitrusException;
import com.citrus.sdk.payment.CardOption;
import com.citrus.sdk.payment.CreditCardOption;
import com.citrus.sdk.payment.DebitCardOption;
import com.citrus.sdk.payment.PaymentOption;
import com.citrus.sdk.payment.PaymentType;
import com.citrus.sdk.ui.events.BalanceUpdateEvent;
import com.citrus.sdk.ui.events.FragmentCallbacks;
import com.citrus.sdk.ui.utils.CitrusFlowManager;
import com.citrus.sdk.ui.utils.ResultModel;
import com.citrus.sdk.ui.utils.Utils;
import com.explara.explara_payment_sdk.R;
import com.explara.explara_payment_sdk.payment.PaymentManager;
import com.explara.explara_payment_sdk.payment.dto.PaytmPayMentResposnseDto;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara_core.common.BaseWithOutNavActivity;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.FragmentHelper;
import com.explara_core.utils.Log;
import com.explara_core.utils.PreferenceManager;
import com.orhanobut.logger.Logger;

/**
 * Created by anudeep on 19/11/15.
 */
public class WalletScreenActivity extends BaseWithOutNavActivity implements FragmentCallbacks {


    private static final String TAG = WalletScreenActivity.class.getSimpleName();
    private int mFromScreen;
    private MaterialDialog mMaterialDialog;
    public String eventId;
    private MenuItem mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PaymentManager.getInstance().initialiseCitrus(this);
        mFromScreen = getIntent().getIntExtra(ConstantKeys.BundleKeys.FROM_SCREEN, 0);
//        if (from == 1) {
//            setTheme(android.R.style.Theme_Dialog);
//        }
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
        Log.d(TAG, getCallingActivity() + "");
    }

    @Override
    protected void addContentFragment() {
        displayBackButton();
        mFromScreen = getIntent().getIntExtra(ConstantKeys.BundleKeys.FROM_SCREEN, 0);
        Log.d(TAG, "From Screen" + "=======" + mFromScreen);
        eventId = getIntent().getStringExtra(Constants.EVENT_ID);
        FragmentHelper.replaceContentFragment(this, R.id.fragment_container, WalletFragment.newInstance(mFromScreen, eventId));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "requestCode : " + requestCode + " result code :" + resultCode);
        if (requestCode == ConstantKeys.REQUEST_CODES.PAY_TM_ADD_MONEY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Toast.makeText(WalletScreenActivity.this, "Success", Toast.LENGTH_LONG).show();
                if (mFromScreen == ConstantKeys.FromScreen.PAYMENT_SCREEN) {
                    showMaterialDialog();
                    PaymentManager.getInstance().payWithPaytm(WalletScreenActivity.this, PreferenceManager.getInstance(WalletScreenActivity.this).getPayTmAccessToken(), TicketsManager.getInstance().mOrder.getOrderNo(), new PaymentManager.PayWithPaytmStatusListener() {
                        @Override
                        public void onPayWithPaytmStatusSuccess(PaytmPayMentResposnseDto payTmUserProfile) {
                            if (ConstantKeys.PaymentOptionIds.TRANSACTION_SUCESS.equals(payTmUserProfile.getStatus())) {
                                dismissDialog();
                                Log.d(TAG, payTmUserProfile.Status);
                                Intent intent = new Intent(WalletScreenActivity.this, ConfirmationOnlineActivity.class);
                                intent.putExtra(Constants.EVENT_ID, getIntent().getStringExtra(Constants.EVENT_ID));
                                startActivity(intent);
                            } else {
                                dismissDialog();
                                Toast.makeText(WalletScreenActivity.this, payTmUserProfile.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onPayWithPaytmStatusFailed(VolleyError volleyError) {
                            Log.d(TAG, "payMent failed");
                            dismissDialog();
                        }
                    }, TAG);
                }
                ((WalletFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).setAdapter();
            }
        }
    }


    public void showMaterialDialog() {
        mMaterialDialog = new MaterialDialog.Builder(WalletScreenActivity.this)
                //.title("Explara Login")
                .content("Please wait..")
                .cancelable(false)
                        //.iconRes(R.drawable.e_logo)
                .progress(true, 0)
                .show();
    }

    private void dismissDialog() {
        if (mMaterialDialog != null && mMaterialDialog.isShowing()) {
            mMaterialDialog.dismiss();
        }
    }

    @Override
    protected void setToolBarTitle() {
        getSupportActionBar().setTitle("My Wallets");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        launchHome();
    }


    @Override
    public void navigateTo(Fragment fragment, int screenName) {
        Log.d(TAG, "Class Name :" + fragment.getClass().getSimpleName() + "\nScreen Name : " + screenName);
        FragmentHelper.replaceAndAddContentFragment(this, R.id.fragment_container, fragment, TAG);

    }

    @Override
    public void makePayment(PaymentOption paymentOption) {

        CitrusUser citrusUser = new CitrusUser(PreferenceManager.getInstance(this).getEmail(), PreferenceManager.getInstance(this).getPhoneNo());
        Amount amount = new Amount(TicketsManager.getInstance().getGrandTotal());
        try {
            PaymentType paymentType = new PaymentType.PGPayment(amount, CitrusFlowManager.billGenerator, paymentOption, citrusUser);
            Intent intent = new Intent(this, com.citrus.sdk.CitrusActivity.class);
            intent.putExtra(com.citrus.sdk.Constants.INTENT_EXTRA_PAYMENT_TYPE, paymentType);
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

    }

    @Override
    public void dismissProgressDialog() {

    }

    @Override
    public void onWalletTransactionComplete(ResultModel resultModel) {
        Utils.getBalance(this);
        FragmentHelper.replaceContentFragment(this, R.id.fragment_container, WalletFragment.newInstance(mFromScreen, eventId));
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

        return null;
    }

    @Override
    public int getStyle() {
        return 0;
    }

    @Override
    public void toggleAmountVisibility(int visibility) {

    }

    @Override
    public void displayTerms(Activity activity) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        mItem = menu.findItem(R.id.create_event);
        mItem.setVisible(false);
        return true;
    }

    public void onEvent(BalanceUpdateEvent event) {
//        Toast.makeText(WalletScreenActivity.this, event.getAmount() + "", Toast.LENGTH_LONG).show();
    }
}
