package com.explara.explara_payment_sdk.payment.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.explara.explara_payment_sdk.R;
import com.explara_core.common.BaseWithOutNavActivity;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.FragmentHelper;
import com.explara_core.utils.Log;

/**
 * Created by anudeep on 09/11/15.
 */
public class WebviewActivity extends BaseWithOutNavActivity {


    private static final String TAG = WebviewActivity.class.getSimpleName();
    String mBlogTitle;
    private MenuItem mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlogTitle = getIntent().getStringExtra(ConstantKeys.IntentKeys.BLOG_NAME);
        Log.d(TAG, "BLOGTit=" + mBlogTitle);

        displayBackButton();
        setToolBarTitle();
    }

    @Override
    protected void addContentFragment() {

        FragmentHelper.replaceContentFragment(this, R.id.fragment_container, ExplaraWebviewFragment.newInstance(getIntent().getStringExtra(ConstantKeys.BundleKeys.URL_STRING)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        Log.d(TAG, "setToolbar" + mBlogTitle);
        getSupportActionBar().setTitle(mBlogTitle);
    }
}
