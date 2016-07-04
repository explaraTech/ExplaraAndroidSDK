package com.explara.explara_payment_sdk.payment.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.explara.explara_payment_sdk.R;
import com.explara.explara_payment_sdk.payment.PaymentManager;
import com.explara_core.common.BaseWithOutNavActivity;
import com.explara_core.utils.FragmentHelper;

/**
 * Created by debasishpanda on 12/09/15.
 */
public class ConfirmationOfflineActivity extends BaseWithOutNavActivity {


    private MenuItem mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        removeContainerPadding();
    }

    @Override
    protected void addContentFragment() {
        displayBackButton();
        FragmentHelper.replaceContentFragment(this, R.id.fragment_container, ConfirmationOfflineFragment.getInstance(getIntent()));
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
        //Intent homeIntent = new Intent(ConfirmationOfflineActivity.this, PersonalizeScreenActivity.class);
       /* Intent homeIntent = new Intent();
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
        getSupportActionBar().setTitle("Pending Payment");
    }


}
