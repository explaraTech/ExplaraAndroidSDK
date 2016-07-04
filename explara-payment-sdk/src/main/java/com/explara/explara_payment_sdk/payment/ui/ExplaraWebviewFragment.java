package com.explara.explara_payment_sdk.payment.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.explara.explara_payment_sdk.R;
import com.explara.explara_payment_sdk.common.PaymentBaseFragment;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Log;
import com.explara_core.utils.WidgetsColorUtil;

/**
 * Created by anudeep on 09/11/15.
 */
public class ExplaraWebviewFragment extends PaymentBaseFragment {
    private final static String TAG = PayTmJavaScriptInterface.class.getSimpleName();

    private String mUrl;
    private ProgressBar mProgressBar;
    private WebView mWebView;

    public static ExplaraWebviewFragment newInstance(String url) {
        ExplaraWebviewFragment explaraWebviewFragment = new ExplaraWebviewFragment();
        Bundle args = new Bundle(1);
        args.putString(ConstantKeys.BundleKeys.URL_STRING, url);
        explaraWebviewFragment.setArguments(args);
        return explaraWebviewFragment;
    }

    @Override
    public void refresh() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.web_view_layout_fragment, container, false);
        intiViews(view);
        extractArguments();
        return view;
    }

    private void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mUrl = args.getString(ConstantKeys.BundleKeys.URL_STRING);
        }
    }

    private void intiViews(View view) {
        mWebView = (WebView) view.findViewById(R.id.webview);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar_webview);
        WidgetsColorUtil.setProgressBarTintColor(mProgressBar, getResources().getColor(R.color.accentColor));
        //mProgressBar.getIndeterminateDrawable().setColorFilter(getActivity().getResources().getColor(R.color.accentColor), PorterDuff.Mode.SRC_IN);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.addJavascriptInterface(new PayTmJavaScriptInterface(), "Paytm");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mWebView.setVisibility(View.VISIBLE);
        mWebView.loadUrl(mUrl);


    }

    private class PayTmJavaScriptInterface {

        @JavascriptInterface
        public void addMoneyStatus(String status) {
            Log.d(TAG, status);
//            Toast.makeText(getActivity(), status, Toast.LENGTH_LONG).show();
            if (status.equals("TXN_SUCCESS")) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            } else if (status.equals("TXN_FAILURE")) {
                if (getActivity() != null) {
                    getActivity().setResult(Activity.RESULT_CANCELED);
                    Toast.makeText(getActivity(), "transaction Failed", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
            }
        }
    }


}
