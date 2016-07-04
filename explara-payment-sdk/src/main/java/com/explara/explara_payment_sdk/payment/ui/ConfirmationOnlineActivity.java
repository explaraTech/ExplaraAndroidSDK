package com.explara.explara_payment_sdk.payment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.explara.explara_payment_sdk.R;
import com.explara.explara_payment_sdk.payment.PaymentManager;
import com.explara_core.common.BaseWithOutNavActivity;
import com.explara_core.utils.Constants;
import com.explara_core.utils.FragmentHelper;

/**
 * Created by debasishpanda on 12/09/15.
 */
public class ConfirmationOnlineActivity extends BaseWithOutNavActivity {

    private MenuItem mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void addContentFragment() {
        displayBackButton();
        FragmentHelper.replaceContentFragment(this, R.id.fragment_container, ConfirmationOnlineFragment.getInstance(getIntent()));
        if (Constants.EXPLARA_ONLY) {
            sendChargedEventToCleverTap();
        }
    }

    private void sendChargedEventToCleverTap() {
        Intent intent = new Intent();
        intent.setAction("com.explara.android.utils");
        intent.putExtra(Constants.CLEVER_TAP_TYPE, Constants.CLEVER_TAP_TRANSACTION_TYPE);
        sendBroadcast(intent);

        /*try {

            TransactionDto transaction = PaymentManager.getInstance().mTransaction;
            CleverTapAPI cleverTapAPI = CleverTapAPI.getInstance(getApplicationContext());
            HashMap<String, Object> chargeDetails = new HashMap<>();
            chargeDetails.put("Amount", transaction.cart.getGrandTotal());
            chargeDetails.put("Charged ID", transaction.order.getOrderNo());
            if (EventsManger.getInstance().eventsDetailDtoMap != null && EventsManger.getInstance().eventsDetailDtoMap.get(transaction.eventId) != null) {
                chargeDetails.put("Event Name", EventsManger.getInstance().eventsDetailDtoMap.get(transaction.eventId).events.getTitle());
            }

            ArrayList<HashMap<String, Object>> items = new ArrayList<>();
            if (transaction.cartCalculationObject != null && transaction.cartCalculationObject.getTickets() != null) {
                for (CartCalculationObject.CartTicket cartTicket : transaction.cartCalculationObject.getTickets()) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("Ticket Id", cartTicket.ticketId);
                    item.put("Ticket Name", cartTicket.ticketName);
                    item.put("Ticket Price", Double.valueOf(cartTicket.ticketPrice) * cartTicket.quantity);
                    items.add(item);
                }
            }

            if (transaction.confCartCalculationObject != null && transaction.confCartCalculationObject.getSessions() != null) {
                for (ConfCartCalculationObject.CartSession cartTicket : transaction.confCartCalculationObject.getSessions()) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("Ticket Id", cartTicket.sessionId);
                    item.put("Ticket Quantity", transaction.confCartCalculationObject.quantity);
                    item.put("Ticket Price", cartTicket.sessionPrice);
                    items.add(item);
                }
            }

            if (transaction.selectedSessionDetailsDto != null) {
                HashMap<String, Object> item = new HashMap<>();
                item.put("Ticket Id", transaction.selectedSessionDetailsDto.sessionId);
                item.put("Ticket Quantity", transaction.selectedSessionDetailsDto.selectedQuantity);
                item.put("Ticket Price", transaction.selectedSessionDetailsDto.sessionPrice);
                items.add(item);
            }
            try {
                cleverTapAPI.event.push(CleverTapAPI.CHARGED_EVENT, chargeDetails, items);
            } catch (InvalidEventNameException e) {
                // You have to specify the first parameter to push()
                // as CleverTap[API.CHARGED_EVENT
            }
        } catch (CleverTapMetaDataNotFoundException | CleverTapPermissionsNotSatisfied e) {
            e.printStackTrace();
        }*/
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
        PaymentManager.getInstance().mAppCallBackListener.onTransactionComplete(this);
        //Intent homeIntent = new Intent(ConfirmationOnlineActivity.this, PersonalizeScreenActivity.class);
        /*Intent homeIntent = new Intent();
        homeIntent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME, getString(R.string.personalized_screen_activity)));
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);*/
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
        getSupportActionBar().setTitle("Payment Confirmation");
    }
}
