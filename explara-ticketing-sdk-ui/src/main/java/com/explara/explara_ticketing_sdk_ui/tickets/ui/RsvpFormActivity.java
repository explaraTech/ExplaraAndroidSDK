package com.explara.explara_ticketing_sdk_ui.tickets.ui;

import android.os.Bundle;
import android.view.MenuItem;

import com.explara.explara_ticketing_sdk_ui.R;
import com.explara_core.common.BaseWithOutNavActivity;
import com.explara_core.utils.FragmentHelper;

/**
 * Created by Debasish on 12/09/15.
 */
public class RsvpFormActivity extends BaseWithOutNavActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        removeContainerPadding();

    }

    @Override
    protected void setToolBarTitle() {
        getSupportActionBar().setTitle("Registration Form");
    }

    @Override
    protected void addContentFragment() {
        displayBackButton();
        FragmentHelper.replaceContentFragment(this, R.id.fragment_container, RsvpFormFragment.getInstance(getIntent()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

   /* @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(RsvpFormActivity.this, CollectionsActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
    }*/
}
