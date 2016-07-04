package com.explara.explara_payment_sdk.payment.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;
import com.explara.explara_payment_sdk.R;
import com.explara.explara_payment_sdk.payment.PaymentManager;
import com.explara.explara_payment_sdk.payment.dto.PaytmPayMentResposnseDto;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara_core.common.BaseWithOutNavActivity;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.FragmentHelper;
import com.explara_core.utils.Log;
import com.explara_core.utils.PreferenceManager;

/**
 * Created by ananthasooraj on 1/25/16.
 */
public class PaytmWalletFlowActvity extends BaseWithOutNavActivity {


    private static final String TAG = PaytmWalletFlowActvity.class.getSimpleName();
    private int mFromScreen;
    private MaterialDialog mMaterialDialog;
    private String mEventId;
    private MenuItem mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paytm_fragment_container);
        mFromScreen = getIntent().getIntExtra(ConstantKeys.BundleKeys.FROM_SCREEN, 0);
    }

    @Override
    protected void addContentFragment() {
        mEventId = getIntent().getStringExtra(Constants.EVENT_ID);
        mFromScreen = getIntent().getIntExtra(ConstantKeys.BundleKeys.FROM_SCREEN, 0);
        FragmentHelper.replaceContentFragment(this, R.id.fragment_container_paytm, PaytmWalletFlowFragment.newInstance(getIntent()), TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "requestCode : " + requestCode + " result code :" + resultCode + "" + data);
        if (requestCode == ConstantKeys.REQUEST_CODES.PAY_TM_ADD_MONEY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                Toast.makeText(PaytmWalletFlowActvity.this, "Success", Toast.LENGTH_LONG).show();
                PaytmWalletFlowFragment walletFlowFragment = (PaytmWalletFlowFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container_paytm);

                View stepAddMoney = walletFlowFragment.getView().findViewById(R.id.add_money_layout);
                stepAddMoney.setVisibility(View.GONE);
                walletFlowFragment.paytmUserbalance();

                if (mFromScreen == ConstantKeys.FromScreen.PAYMENT_SCREEN) {
                    showMaterialDialog();
                    PaymentManager.getInstance().payWithPaytm(PaytmWalletFlowActvity.this, PreferenceManager.getInstance(PaytmWalletFlowActvity.this).getPayTmAccessToken(), TicketsManager.getInstance().mOrder.getOrderNo(), new PaymentManager.PayWithPaytmStatusListener() {
                        @Override
                        public void onPayWithPaytmStatusSuccess(PaytmPayMentResposnseDto payTmUserProfile) {
                            if (ConstantKeys.PaymentOptionIds.TRANSACTION_SUCESS.equals(payTmUserProfile.getStatus())) {
                                if (mMaterialDialog != null) {
                                    mMaterialDialog.dismiss();
                                }
                                Log.d(TAG, payTmUserProfile.Status);
                                Intent intent = new Intent(PaytmWalletFlowActvity.this, ConfirmationOnlineActivity.class);
                                intent.putExtra(Constants.EVENT_ID, getIntent().getStringExtra(Constants.EVENT_ID));
                                startActivity(intent);
                            } else {
                                if (mMaterialDialog != null) {
                                    mMaterialDialog.dismiss();
                                }
                                Toast.makeText(PaytmWalletFlowActvity.this, payTmUserProfile.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onPayWithPaytmStatusFailed(VolleyError volleyError) {
                            Log.d(TAG, "payMent failed");
                            if (mMaterialDialog != null) {
                                mMaterialDialog.dismiss();
                            }
                        }
                    }, TAG);
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        mItem = menu.findItem(R.id.create_event);
        mItem.setVisible(false);
        return true;
    }

    private void showMaterialDialog() {
        mMaterialDialog = new MaterialDialog.Builder(PaytmWalletFlowActvity.this)
                .content("Please wait..")
                .cancelable(false)
                .progress(true, 0)
                .show();

    }
}
