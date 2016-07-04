package com.explara.explara_payment_sdk.payment.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.explara.explara_payment_sdk.R;
import com.explara.explara_payment_sdk.payment.PaymentManager;
import com.explara.explara_payment_sdk.payment.dto.PayTmUserProfile;
import com.explara.explara_payment_sdk.payment.dto.PaymentOptionDto;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.PreferenceManager;
import com.explara_core.utils.WidgetsColorUtil;

import java.util.List;

/**
 * Created by anudeep on 30/11/15.
 */
public class PaymentOptionsListAdapter extends ArrayAdapter<PaymentOptionDto> {

    private static final String TAG = PaymentOptionsListAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    public List<PaymentOptionDto> paymentList;

    public PaymentOptionsListAdapter(Context context, List<PaymentOptionDto> objects) {
        super(context, 0, objects);
        this.paymentList = objects;
        mInflater = LayoutInflater.from(context);
    }


    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (getItem(position).id != ConstantKeys.PaymentOptionIds.PAYTM) {
            convertView = mInflater.inflate(R.layout.payment_options_list_item, parent, false);
            TextView paymentOption = (TextView) convertView.findViewById(R.id.payment_option_name);
            paymentOption.setText(getItem(position).title);

            ImageView paymentOptionImage = (ImageView) convertView.findViewById(R.id.paymentOptions_Image);
            paymentOptionImage.setImageResource(0);

            if (getItem(position).id == ConstantKeys.PaymentOptionIds.CCAVENUE) {
                paymentOptionImage.setImageResource(R.drawable.ccavenue);
                paymentOption.setText("");
            } else if (getItem(position).id == ConstantKeys.PaymentOptionIds.OLA_MONEY) {
                paymentOption.setText("");
                paymentOptionImage.setImageResource(R.drawable.olamoney);
            } else if (getItem(position).id == ConstantKeys.PaymentOptionIds.PAYPAL) {
                paymentOptionImage.setImageResource(R.drawable.paypal);
                paymentOption.setText("");
            } else if (getItem(position).id == ConstantKeys.PaymentOptionIds.CITRUS_WALLET_ID) {
                paymentOptionImage.setImageResource(R.drawable.citrus_cash);
                paymentOption.setText("Citrus Wallet");

            } else if (getItem(position).id == ConstantKeys.PaymentOptionIds.CITRUS_ID) {
                paymentOptionImage.setImageResource(R.drawable.citrus);
                paymentOption.setText("");

            }
        } else {
            convertView = mInflater.inflate(R.layout.payment_item_list, parent, false);
            final TextView balance = (TextView) convertView.findViewById(R.id.paytm_rupee_text);
            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progress_bar);
            WidgetsColorUtil.setProgressBarTintColor(progressBar, convertView.getResources().getColor(R.color.accentColor));
            final View balanceContainer = convertView.findViewById(R.id.balance_container);
            ((ImageView) convertView.findViewById(R.id.wallet_img)).setImageResource(R.drawable.paytm);
            ((TextView) convertView.findViewById(R.id.paytm_rupee_text)).setText(PaymentManager.getInstance().getUserPayTmBalance());
            PaymentManager.getInstance().getPayTmUserDetails(getContext(), PreferenceManager.getInstance(getContext()).getPayTmAccessToken()
                    , new PaymentManager.FetchPaytmProfileListener() {
                @Override
                public void onFetchPaytmUserProfile(PayTmUserProfile payTmUserProfile) {
                    if (balance != null && progressBar != null && balanceContainer != null && !TextUtils.isEmpty(payTmUserProfile.WALLETBALANCE))
                        balance.setText(String.format("%s %s", getContext().getString(R.string.rupee_symbol), payTmUserProfile.WALLETBALANCE));
                    progressBar.setVisibility(View.GONE);
                    balanceContainer.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFetchpaytmUserProfileFailed(VolleyError volleyError) {

                }
            }, TAG);
        }

        return convertView;
    }

//    @Override
//    public int getItemViewType(int position) {
//        return getItem(position).id;
//    }
//
//
//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }

}
