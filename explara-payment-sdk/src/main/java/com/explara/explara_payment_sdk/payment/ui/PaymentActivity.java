package com.explara.explara_payment_sdk.payment.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
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
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.ui.events.FragmentCallbacks;
import com.citrus.sdk.ui.fragments.AddMoneyFragment;
import com.citrus.sdk.ui.utils.CitrusFlowManager;
import com.citrus.sdk.ui.utils.ResultModel;
import com.citrus.sdk.ui.utils.UIConstants;
import com.explara.explara_payment_sdk.R;
import com.explara.explara_payment_sdk.payment.PaymentManager;
import com.explara.explara_payment_sdk.payment.dto.OlaPaymentResposneDto;
import com.explara.explara_payment_sdk.utils.UrlConstants;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk_ui.tickets.ui.TicketsDetailActivity;
import com.explara.explara_ticketing_sdk_ui.tickets.ui.TicketsDetailsFragment;
import com.explara_core.common.BaseWithOutNavActivity;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.FragmentHelper;
import com.explara_core.utils.Log;
import com.explara_core.utils.PreferenceManager;
import com.orhanobut.logger.Logger;

/**
 * Created by debasishpanda on 12/09/15.
 */
public class PaymentActivity extends BaseWithOutNavActivity implements FragmentCallbacks {

    private static final String TAG = PaymentActivity.class.getSimpleName();
    String eventId;
    protected ProgressDialog mProgressDialog = null;
    private MenuItem mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PaymentManager.getInstance().initialiseCitrus(this);
        super.onCreate(savedInstanceState);
        removeContainerPadding();
    }

    @Override
    protected void addContentFragment() {
        eventId = getIntent().getStringExtra(Constants.EVENT_ID);
        displayBackButton();
        boolean shouldLaunchDefaultPaymentOption = false;

        if ("INR".equals(PaymentManager.getInstance().mPaymentCallBackListener.getCurrencyFromEventId(eventId))) {
            shouldLaunchDefaultPaymentOption = getIntent().getBooleanExtra(TicketsDetailsFragment.SHOULD_LAUNCH_PREFFERED_WALLET, false);
        }

        FragmentHelper.replaceContentFragment(this, R.id.fragment_container, PaymentOptionsListFragment.newInstance(getIntent().getStringExtra(Constants.EVENT_ID), shouldLaunchDefaultPaymentOption));

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
    protected void setToolBarTitle() {
        getSupportActionBar().setTitle("Payment Options");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handleResposne(requestCode, resultCode, data);
    }

    private void handleResposne(int requestCode, int resultCode, Intent data) {
        if (requestCode == ConstantKeys.REQUEST_CODES.OLA_REQUEST_CODE) {
            if (resultCode == 104) {
                Toast.makeText(getApplicationContext(), R.string.payment_failed_txt, Toast.LENGTH_LONG).show();
            } else {
                String olaReturn = data.getExtras().getString(ConstantKeys.BundleKeys.OLA_RESULT_KEY);
                PaymentManager.getInstance().updateOlaStatusToServer(this, olaReturn, new PaymentManager.UpdateOlaStatusListener() {
                    @Override
                    public void onUpDateOlaStatusToServer(OlaPaymentResposneDto payTmUserProfile) {
                        Intent intent = new Intent(PaymentActivity.this, ConfirmationOnlineActivity.class);
                        intent.putExtra(Constants.EVENT_ID, getIntent().getStringExtra(Constants.EVENT_ID));
                        startActivity(intent);
                    }

                    @Override
                    public void onUpDateOlaStatusToServerFailed(VolleyError volleyError) {
                        Toast.makeText(PaymentActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }, TAG);
            }
        } else if (requestCode == 10000) {
            if (data != null) {
                TransactionResponse transactionResponse = data.getParcelableExtra(com.citrus.sdk.Constants.INTENT_EXTRA_TRANSACTION_RESPONSE);
                if (TransactionResponse.TransactionStatus.SUCCESSFUL.equals(transactionResponse.getTransactionStatus())) {
                    Log.d(TAG, transactionResponse.getJsonResponse());
                    Intent intent = new Intent(this, ConfirmationOnlineActivity.class);
                    intent.putExtra(Constants.EVENT_ID, eventId);
                    startActivity(intent);
                } else {
                    if (transactionResponse != null) {
                        Toast.makeText(getApplicationContext(), transactionResponse.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(PaymentActivity.this, TicketsDetailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Payment failed", Toast.LENGTH_LONG).show();
                    }

                }
            }
        } else if (requestCode == UIConstants.REQ_CODE_LOGIN_PAY && resultCode == Activity.RESULT_OK) {
            Amount amount = new Amount(TicketsManager.getInstance().getGrandTotal());
            final CitrusClient citrusClient = CitrusClient.getInstance(this); // Activity Context
            CitrusFlowManager.billGenerator = UrlConstants.CITRUS_BILL_GENERATOR + TicketsManager.getInstance().mOrder.getOrderNo();

            CitrusClient.getInstance(this).getBalance(new Callback<Amount>() {
                @Override
                public void success(Amount amount) {
                    if (amount.getValueAsDouble() >= Double.valueOf(TicketsManager.getInstance().getGrandTotal())) {
                        Logger.d(TAG + " get Balance success " + amount.getValue());
                        try {
                            citrusClient.payUsingCitrusCash(new PaymentType.CitrusCash(amount,
                                            CitrusFlowManager.billGenerator),
                                    new Callback<TransactionResponse>() {


                                        @Override
                                        public void success(TransactionResponse
                                                                    transactionResponse) {
                                            Logger.d(TAG + " Success wallet payment" +
                                                    transactionResponse
                                                            .getMessage());
                                            if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                                                Log.d(TAG, transactionResponse.getJsonResponse());
                                                Intent intent = new Intent(PaymentActivity.this, ConfirmationOnlineActivity.class);
                                                intent.putExtra(Constants.EVENT_ID, eventId);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(getApplicationContext(), transactionResponse.getMessage(), Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(PaymentActivity.this, TicketsDetailActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }
                                            //
                                            // mListener.onWalletTransactionComplete
                                            // (transactionResponse);
//                                        walletPaymentComplete = true;
//                                        walletResultModel = new ResultModel(null,
//                                                transactionResponse);
                                        }

                                        @Override
                                        public void error(CitrusError error) {
                                            Logger.d(TAG + " Could not process " + error
                                                    .getMessage());
//                                        walletPaymentComplete = true;
//                                        walletResultModel = new ResultModel(error, null);
                                        }
                                    });
                        } catch (CitrusException e) {
                            Log.e(TAG, "CitrusException" + e.getMessage());
                        }
                    } else {
                        CitrusFlowManager.billGenerator = UrlConstants.CITRUS_BILL_GENERATOR + TicketsManager.getInstance().mOrder.getOrderNo();
                        CitrusFlowManager.returnURL = UrlConstants.CITRUS_ADDMONEY_RETURN_URL;
                        Toast.makeText(getApplicationContext(), "Low balance Please Add Money ", Toast.LENGTH_LONG).show();
                        FragmentHelper.replaceAndAddContentFragment(PaymentActivity.this, R.id.fragment_container,
                                AddMoneyFragment.newInstance(true, ""), TAG);
                    }

                }

                @Override
                public void error(CitrusError citrusError) {

                    Logger.d(TAG + " get Balance failure " + citrusError.getMessage());
                }
            });


        }
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
            PaymentType paymentType = new PaymentType.PGPayment(amount, UrlConstants.CITRUS_BILL_GENERATOR + TicketsManager.getInstance().mOrder.getOrderNo(), paymentOption, citrusUser);
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
        if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
            Log.d(TAG, transactionResponse.getJsonResponse());
            Intent intent = new Intent(PaymentActivity.this, ConfirmationOnlineActivity.class);
            intent.putExtra(Constants.EVENT_ID, eventId);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), transactionResponse.getMessage(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(PaymentActivity.this, TicketsDetailActivity.class);
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
        return 0;
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
}
