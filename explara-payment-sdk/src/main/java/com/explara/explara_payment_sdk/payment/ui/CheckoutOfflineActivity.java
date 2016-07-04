package com.explara.explara_payment_sdk.payment.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.explara.explara_payment_sdk.R;
import com.explara.explara_payment_sdk.payment.PaymentManager;
import com.explara.explara_payment_sdk.payment.dto.CheckoutOfflineResponse;
import com.explara.explara_payment_sdk.utils.PaymentConstantKeys;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara_core.common.BaseWithOutNavActivity;
import com.explara_core.utils.Constants;

/**
 * Created by debasishpanda on 12/09/15.
 */
public class CheckoutOfflineActivity extends BaseWithOutNavActivity {

    public MaterialDialog mMaterialDialog;
    private MenuItem mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        removeContainerPadding();
        showMaterialDialog();
        String eventId = getIntent().getStringExtra(PaymentConstantKeys.PaymentKeys.EVENT_ID);
        String paymentOptions = getIntent().getStringExtra(PaymentConstantKeys.PaymentKeys.PAYMENT_OPTION);
        String tag = getIntent().getStringExtra(PaymentConstantKeys.PaymentKeys.TAG);
        //Checkout offline api call
        checkoutOffline(eventId, paymentOptions, tag);
    }

    private void checkoutOffline(final String eventId, String paymentOptionName, String TAG) {
        PaymentManager.getInstance().checkoutOffline(this, paymentOptionName.toLowerCase(), new PaymentManager.CheckoutOfflineListener() {
            @Override
            public void onOfflineCheckout(CheckoutOfflineResponse checkoutOfflineResponse) {
                if (this != null && checkoutOfflineResponse != null) {
                    if (checkoutOfflineResponse.getStatus().equals("success")) {
                        navigateToOfflinePaymentConfirmationPage(eventId);
                    } else {
                        dismissMaterialDialog();
                        Toast.makeText(getApplicationContext(), "Payment Failed", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onOfflineCheckoutFailed() {
                if (this != null) {
                    dismissMaterialDialog();
                    Toast.makeText(getApplicationContext(), "Checkout offline failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    }

    private void navigateToOfflinePaymentConfirmationPage(String eventId) {

        if (TicketsManager.getInstance().mTotal == 0) {
            Intent intent = new Intent(this, ConfirmationOnlineActivity.class);
            intent.putExtra(Constants.EVENT_ID, eventId);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ConfirmationOfflineActivity.class);
            intent.putExtra(Constants.EVENT_ID, eventId);
            startActivity(intent);
        }

    }

    @Override
    protected void addContentFragment() {
        displayBackButton();
        //FragmentHelper.replaceContentFragment(this, R.id.fragment_container, ConfirmationOfflineFragment.getInstance(getIntent()));
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
        //Intent homeIntent = new Intent(ConfirmationOfflineActivity.this, PersonalizeScreenActivity.class);
        Intent homeIntent = new Intent();
        homeIntent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME, getString(R.string.personalized_screen_activity)));
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        mItem = menu.findItem(R.id.create_event);
        mItem.setVisible(false);
        return true;
    }

    @Override
    protected void setToolBarTitle() {
        getSupportActionBar().setTitle("Processing");
    }

    public void showMaterialDialog() {
        mMaterialDialog = new MaterialDialog.Builder(this)
                //.title("Explara Login")
                .content("Payment processing..")
                .cancelable(false)
                        //.iconRes(R.drawable.e_logo)
                .progress(true, 0)
                .show();
    }

    public void dismissMaterialDialog() {
        if (mMaterialDialog != null && mMaterialDialog.isShowing()) {
            mMaterialDialog.dismiss();
        }
    }

}
