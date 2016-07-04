package com.explara.explara_payment_sdk.payment.ui;

import android.view.Menu;
import android.view.MenuItem;

import com.explara.explara_payment_sdk.R;
import com.explara_core.common.BaseWithOutNavActivity;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.FragmentHelper;

/**
 * Created by anudeep on 21/11/15.
 */
public class OlaWebViewPaymentActivity extends BaseWithOutNavActivity {

    private static final String TAG = OlaWebViewPaymentFragment.class.getSimpleName();

    String mOrderId;
    private MenuItem mItem;

    @Override
    protected void addContentFragment() {
        mOrderId = getIntent().getStringExtra(ConstantKeys.BundleKeys.ORDER_ID);
        FragmentHelper.replaceContentFragment(this, R.id.fragment_container, OlaWebViewPaymentFragment.newInstance(mOrderId), TAG);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        mItem = menu.findItem(R.id.create_event);
        mItem.setVisible(false);
        return true;
    }


}
