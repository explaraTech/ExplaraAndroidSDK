package com.explara.explara_ticketing_sdk_ui.tickets.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara.explara_ticketing_sdk_ui.utils.TicketingUiConstantKeys;
import com.explara_core.common.BaseWithOutNavActivity;
import com.explara_core.utils.Constants;
import com.explara_core.utils.FragmentHelper;

/**
 * Created by Debasish on 12/09/15.
 */
public class MultidatePackagesActivity extends BaseWithOutNavActivity implements PackagesByDateFragment.PackageSelectedListener {

    private String mEventId;
    private String mCurrency;
    private String mIsAttendeeFormEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TicketsManager.getInstance().mTicketListingCallBackListnener.initialiseCitrus(this);
        super.onCreate(savedInstanceState);
        removeContainerPadding();
        TicketsManager.getInstance().clearConfCartobj();
        //PaymentManager.getInstance().resetCurrentTransactionObj();
        if (Constants.EXPLARA_ONLY) {
            TicketsManager.getInstance().mTicketListingCallBackListnener.resetTransactionDto();
        }
        if (TicketsManager.getInstance().mAnalyticsListener != null) {
            TicketsManager.getInstance().mAnalyticsListener.sendScreenName(getString(R.string.multida_multisession_screen), getApplication(), getApplicationContext());
        }
    }

    @Override
    protected void setToolBarTitle() {
        getSupportActionBar().setTitle("Select Packages");
    }

    @Override
    protected void addContentFragment() {
        displayBackButton();
        mEventId = getIntent().getStringExtra(Constants.EVENT_ID);
        mCurrency = getIntent().getStringExtra(Constants.CURRENCY);
        mIsAttendeeFormEnabled = getIntent().getStringExtra(Constants.IS_ATTENDEE_FORM_ENABLED);
        FragmentHelper.replaceContentFragment(this, R.id.fragment_container, MultidatePackagesFragment.newInstance(mEventId, mCurrency));
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
    public void onPackageSelected(String packageId) {
        Intent intent = new Intent(getBaseContext(), TicketsDetailActivity.class);
        intent.putExtra(TicketingUiConstantKeys.TicketingKeys.PACKAGE_ID, packageId);
        intent.putExtra(Constants.EVENT_ID, mEventId);
        intent.putExtra(Constants.CURRENCY, mCurrency.equals("$") ? "USD" : (mCurrency.equals("&#8377;") ? "INR" : mCurrency));
        intent.putExtra(Constants.IS_ATTENDEE_FORM_ENABLED, mIsAttendeeFormEnabled);
        startActivity(intent);
    }
}
