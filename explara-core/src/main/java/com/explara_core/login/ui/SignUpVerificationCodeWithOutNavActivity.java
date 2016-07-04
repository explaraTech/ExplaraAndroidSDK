package com.explara_core.login.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.explara_core.R;
import com.explara_core.common.BaseWithOutNavActivity;
import com.explara_core.utils.FragmentHelper;
import com.explara_core.utils.PreferenceManager;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by debasishpanda on 22/09/15.
 */
public class SignUpVerificationCodeWithOutNavActivity extends BaseWithOutNavActivity {
    private static final String TAG = SignUpVerificationCodeWithOutNavActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        removeContainerPadding();
        PreferenceManager.getInstance(this).setCurrentPage(PreferenceManager.CODE_VERIFICATION_SCREEN);
    }

    @Override
    protected void addContentFragment() {
        displayBackButton();
        FragmentHelper.replaceContentFragment(this, R.id.fragment_container, SignUpVerificationCodeFragment.getInstance(getIntent()));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void setToolBarTitle() {
        mToolbarTitle.setText("Sign Up Verification Code");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
