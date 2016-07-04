package com.explara_core.login.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.explara_core.R;
import com.explara_core.utils.FragmentHelper;
import com.explara_core.utils.PreferenceManager;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by debasishpanda on 22/09/15.
 */
public class LoginActivity extends FragmentActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.getInstance(this).setCurrentPage(PreferenceManager.SINGUP_LOGIN_SCREEN);
        setContentView(R.layout.activity_forgot_password_screen);
        FragmentHelper.replaceContentFragment(this, R.id.forgot_password_screen_fragment_container,LoginNewFragment.getInstance(getIntent()));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
