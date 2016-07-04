package com.explara.explara_ticketing_sdk_ui.tickets.ui;

import android.os.Bundle;
import android.view.MenuItem;

import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara_core.common.BaseWithOutNavActivity;
import com.explara_core.utils.FragmentHelper;

/**
 * Created by anudeep on 12/09/15.
 */
public class RsvpConfirmationActivity extends BaseWithOutNavActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        removeContainerPadding();

    }

    @Override
    protected void setToolBarTitle() {
        getSupportActionBar().setTitle("Rsvp Confirmation");
    }

    @Override
    protected void addContentFragment() {
        displayBackButton();
        FragmentHelper.replaceContentFragment(this, R.id.fragment_container, RsvpConfirmationFragment.getInstance(getIntent()));
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
        TicketsManager.getInstance().mAppCallBackListener.onTransactionComplete(this);

       /* Intent homeIntent = new Intent(RsvpConfirmationActivity.this, PersonalizeScreenActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);

        Intent homeIntent = new Intent();
        homeIntent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME, "com.explara.android.personalizedHome.ui.PersonalizeScreenActivity"));
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);*/
    }
}
