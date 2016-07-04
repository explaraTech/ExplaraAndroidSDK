package com.explara_core.login.ui;

import android.os.Bundle;
import android.view.MenuItem;

import com.explara_core.R;
import com.explara_core.common.BaseWithOutNavActivity;
import com.explara_core.utils.FragmentHelper;
import com.explara_core.utils.PreferenceManager;


/**
 * Created by anudeep on 12/09/15.
 */
public class FbUserDetailsWithOutNavActivity extends BaseWithOutNavActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        removeContainerPadding();
        PreferenceManager.getInstance(this).setCurrentPage(PreferenceManager.SIGNUP_MOBILE_NO_SCREEN);
    }

    @Override
    protected void setToolBarTitle() {
        getSupportActionBar().setTitle("Registration Form");
    }

    @Override
    protected void addContentFragment() {
        displayBackButton();
        FragmentHelper.replaceContentFragment(this, R.id.fragment_container, FbUserDetailsFragment.newInstance(getIntent()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
