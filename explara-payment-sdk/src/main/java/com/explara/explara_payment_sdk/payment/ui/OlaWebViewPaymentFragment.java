package com.explara.explara_payment_sdk.payment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.explara.explara_payment_sdk.R;
import com.explara.explara_payment_sdk.common.PaymentBaseFragment;
import com.explara.explara_payment_sdk.payment.PaymentManager;
import com.explara.explara_payment_sdk.payment.dto.OlaBill;
import com.explara.explara_payment_sdk.payment.dto.OlaPaymentResposneDto;
import com.explara.explara_payment_sdk.payment.dto.OlaResponse;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Log;
import com.explara_core.utils.PreferenceManager;
import com.explara_core.utils.Utility;
import com.explara_core.utils.WidgetsColorUtil;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by anudeep on 21/11/15.
 */
public class OlaWebViewPaymentFragment extends PaymentBaseFragment {
    private static final String TAG = OlaWebViewPaymentFragment.class.getSimpleName();
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private String mOrderId;

    interface OlaMoneyInteface {
        @android.webkit.JavascriptInterface
        void onPaymentDone(String jsonResponse);
    }

    public static OlaWebViewPaymentFragment newInstance(String orderId) {
        OlaWebViewPaymentFragment olaWebViewPaymentFragment = new OlaWebViewPaymentFragment();
        Bundle args = new Bundle(1);
        args.putString(ConstantKeys.BundleKeys.ORDER_ID, orderId);
        olaWebViewPaymentFragment.setArguments(args);
        return olaWebViewPaymentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ola_payment_webview_layout_fragment, container, false);
        intiViews(view);
        extractArguments();
        return view;
    }

    private void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mOrderId = args.getString(ConstantKeys.BundleKeys.ORDER_ID);
        }
    }

    private void googleAnalyticsSendScreenName() {
        if (PaymentManager.getInstance().mAnalyticsListener != null) {
            PaymentManager.getInstance().mAnalyticsListener.sendScreenName(getResources().getString(R.string.payment_screen_ola), getActivity().getApplication(), getContext());
        }
        //AnalyticsHelper.sendScreenName(getResources().getString(R.string.payment_screen_ola), getActivity().getApplication(), getContext());
    }

    private void intiViews(View view) {
        mWebView = (WebView) view.findViewById(R.id.webview);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        //mProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.accentColor), PorterDuff.Mode.SRC_IN);
        WidgetsColorUtil.setProgressBarTintColor(mProgressBar, getResources().getColor(R.color.accentColor));
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }
        });
        mWebView.addJavascriptInterface(new OlaMoneyInteface() {
            @Override
            @android.webkit.JavascriptInterface
            public void onPaymentDone(String jsonResponse) {
                Log.d(TAG, jsonResponse);
                Gson gson = new Gson();
                OlaResponse olaResponse = gson.fromJson(jsonResponse, OlaResponse.class);
                if (olaResponse.status.equals("success")) {

                    PaymentManager.getInstance().updateOlaStatusToServer(getActivity(), jsonResponse, new PaymentManager.UpdateOlaStatusListener() {
                        @Override
                        public void onUpDateOlaStatusToServer(OlaPaymentResposneDto olaPaymentResposneDto) {
                            if (olaPaymentResposneDto.status.equals("success")) {
                                googleAnalyticsSendScreenName();
                                Intent intent = new Intent(getActivity(), ConfirmationOnlineActivity.class);
                                intent.putExtra(com.explara_core.utils.Constants.EVENT_ID, getActivity().getIntent().getStringExtra(com.explara_core.utils.Constants.EVENT_ID));
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onUpDateOlaStatusToServerFailed(VolleyError volleyError) {
                            Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }, TAG);
                } else {
                    Toast.makeText(getActivity(), R.string.payment_failed_txt, Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }

            }
        }, "OlaMoney");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        downloadOlaBill();
    }


    private void downloadOlaBill() {
        if (Utility.isNetworkAvailable(getActivity())) {
            PaymentManager.getInstance().payWithOlaMoney(getActivity(), new PaymentManager.GetOlaBillListener() {
                @Override
                public void onGetOlaBillSuccess(OlaBill olaBill) {
                    loadOlaBill(olaBill);
                }

                @Override
                public void onGetOlaBillFail() {

                }
            }, TAG, mOrderId);
        } else {
            Toast.makeText(getActivity(), "Please Check your Internet and Try again !", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    private void loadOlaBill(OlaBill olaBill) {
        StringBuffer buffer = new StringBuffer("https://om.olacabs.com/olamoney/webview/index.html");
        String base64_bill = Base64.encodeToString(olaBill.bill.getBytes(), Base64.DEFAULT);
        buffer.append("?bill=" + base64_bill);
        try {
            buffer.append("&phone=" + URLEncoder.encode(PreferenceManager.getInstance(getActivity()).getPhoneNo(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = buffer.toString();
        Log.d(TAG, url);
        mWebView.loadUrl(url);
    }


    @Override
    public void refresh() {

    }
}
