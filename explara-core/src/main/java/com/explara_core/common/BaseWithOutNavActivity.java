package com.explara_core.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.explara_core.R;
import com.explara_core.utils.Constants;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by debasishpanda on 29/09/15.
 */
public abstract class BaseWithOutNavActivity extends AppCompatActivity {

    private FrameLayout mFrameLayout;
    private Toolbar mToolbar;
    protected TextView mToolbarTitle;
    public static Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_container_without_nav);
        initViews();
        setupToolbar();
        addContentFragment();
    }

    private void initViews() {
        mFrameLayout = (FrameLayout) findViewById(R.id.fragment_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(null);
        setToolBarTitle();
    }

    protected void setToolBarTitle() {
        mToolbarTitle.setText("Explara");
    }

    protected void displayBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_red);
    }

    protected void removeContainerPadding() {
        mFrameLayout.setPadding(0, 0, 0, 0);
    }

    protected abstract void addContentFragment();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void launchHome() {
        /*Intent homeIntent = new Intent(BaseWithOutNavActivity.this, PersonalizeScreenActivity.class);
        startActivitySafely(homeIntent);*/
    }

    private void startActivitySafely(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.base, menu);
        mMenu = menu;
        menu.findItem(R.id.search).setVisible(Constants.EXPLARA_ONLY ? true : false);
        menu.findItem(R.id.create_event).setVisible(Constants.EXPLARA_ONLY ? true : false);

        // return super.onCreateOptionsMenu(menu);
        return true;
    }
}
