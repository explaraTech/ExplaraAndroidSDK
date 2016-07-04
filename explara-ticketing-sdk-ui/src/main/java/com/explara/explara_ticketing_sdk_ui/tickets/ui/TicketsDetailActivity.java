package com.explara.explara_ticketing_sdk_ui.tickets.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara.explara_ticketing_sdk_ui.common.BaseFragmentWithBottomSheet;
import com.explara_core.common.BaseWithOutNavActivity;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.FragmentHelper;

/**
 * Created by anudeep on 12/09/15.
 */
public class TicketsDetailActivity extends BaseWithOutNavActivity {

    private MenuItem mItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TicketsManager.getInstance().mTicketListingCallBackListnener.initialiseCitrus(this);
        super.onCreate(savedInstanceState);
        removeContainerPadding();
        TicketsManager.getInstance().clearCartObj();
        //PaymentManager.getInstance().resetCurrentTransactionObj();
        if (Constants.EXPLARA_ONLY) {
            TicketsManager.getInstance().mTicketListingCallBackListnener.resetTransactionDto();
            TicketsManager.getInstance().mTicketListingCallBackListnener.storeEventIdInTransactionDto(getIntent().getStringExtra(Constants.EVENT_ID));
        }
        if (TicketsManager.getInstance().mAnalyticsListener != null) {
            TicketsManager.getInstance().mAnalyticsListener.sendScreenName(getString(R.string.ticket_detail), getApplication(), getApplicationContext());
        }
    }

    @Override
    protected void setToolBarTitle() {
        getSupportActionBar().setTitle("Select Tickets");
    }

    @Override
    protected void addContentFragment() {
        displayBackButton();
        FragmentHelper.replaceContentFragment(this, R.id.fragment_container, TicketsDetailsFragment.getInstance(getIntent()));
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
        //     super.onBackPressed();

        Fragment fragment = FragmentHelper.getFragment(this, R.id.fragment_container);
        if (fragment != null) {

            if (fragment instanceof BaseFragmentWithBottomSheet) {
                BaseFragmentWithBottomSheet eventFragment = ((BaseFragmentWithBottomSheet) fragment);
                if (eventFragment.shouldHideOnBackpress()) {
                    eventFragment.hideShowBottomSlideContainer();
                    return;
                }
            }
        }

        if (getIntent().getExtras().getBoolean(ConstantKeys.BundleKeys.FROM_NOTIFICATION, false)) {
            launchHome();
        } else {
            super.onBackPressed();
        }
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
    }


}
