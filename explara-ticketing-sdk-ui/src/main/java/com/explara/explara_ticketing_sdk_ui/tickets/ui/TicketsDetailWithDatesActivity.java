package com.explara.explara_ticketing_sdk_ui.tickets.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara.explara_ticketing_sdk_ui.common.BaseFragmentWithBottomSheet;
import com.explara_core.common.BaseWithOutNavActivity;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.FragmentHelper;

/**
 * Created by Debasish on 12/09/15.
 */

public class TicketsDetailWithDatesActivity extends BaseWithOutNavActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TicketsManager.getInstance().mTicketListingCallBackListnener.initialiseCitrus(this);
        super.onCreate(savedInstanceState);
        if (Constants.EXPLARA_ONLY) {
            //PaymentManager.getInstance().resetCurrentTransactionObj();
            TicketsManager.getInstance().mTicketListingCallBackListnener.resetTransactionDto();
            //PaymentManager.getInstance().setEventIdInTransactionDto(getIntent().getStringExtra(Constants.EVENT_ID));
            TicketsManager.getInstance().mTicketListingCallBackListnener.storeEventIdInTransactionDto(getIntent().getStringExtra(Constants.EVENT_ID));
        }
    }

    @Override
    protected void setToolBarTitle() {
        getSupportActionBar().setTitle("Select Tickets");
    }

    @Override
    protected void addContentFragment() {
        displayBackButton();
        FragmentHelper.replaceContentFragment(this, R.id.fragment_container, TicketsDetailsWithDatesFragment.getInstance(getIntent()));
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
//        super.onBackPressed();
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


}
