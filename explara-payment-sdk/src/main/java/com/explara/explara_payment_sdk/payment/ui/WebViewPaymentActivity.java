package com.explara.explara_payment_sdk.payment.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.explara.explara_payment_sdk.R;
import com.explara.explara_payment_sdk.utils.UrlConstants;
import com.explara.explara_payment_sdk.utils.ccavenue.AvenuesParams;
import com.explara.explara_payment_sdk.utils.ccavenue.Constants;
import com.explara.explara_payment_sdk.utils.ccavenue.RSAUtility;
import com.explara.explara_payment_sdk.utils.ccavenue.ServiceHandler;
import com.explara.explara_payment_sdk.utils.ccavenue.ServiceUtility;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk_ui.tickets.ui.TicketsDetailActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class WebViewPaymentActivity extends Activity {

    private static final String TAG = WebViewPaymentActivity.class.getSimpleName();
    private ProgressDialog dialog;
    Intent mainIntent;
    String html, encVal;
    private static String mEventID;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_web_view);
        mainIntent = getIntent();
        if (mainIntent.hasExtra(com.explara_core.utils.Constants.EVENT_ID)) {
            mEventID = mainIntent.getStringExtra(com.explara_core.utils.Constants.EVENT_ID);
        }

        // Calling async task to get display content
        new RenderView().execute();

        // Logging event to WizRocket
//        if (com.explara.android.utils.Constants.WIZ_ROCKET_API != null)
//            com.explara.android.utils.Constants.WIZ_ROCKET_API.event.push(TAG);
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            dialog = new ProgressDialog(WebViewPaymentActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(AvenuesParams.ACCESS_CODE, UrlConstants.accessCode));
            params.add(new BasicNameValuePair(AvenuesParams.ORDER_ID, TicketsManager.getInstance().mOrder.getOrderNo()));

            String vResponse = sh.makeServiceCall(UrlConstants.rsaKeyUrl, ServiceHandler.POST, params);
//            System.out.println(vResponse);
            if (!ServiceUtility.chkNull(vResponse).equals("")
                    && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR") == -1) {
                StringBuffer vEncVal = new StringBuffer("");
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, TicketsManager.getInstance().mOrder.getTicketCost()));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, TicketsManager.getInstance().mDiscountResponse.getCart().getCurrency()));

                try {
                    JSONObject jsonObject = new JSONObject(vResponse);
                    String key = jsonObject.getString("result");
                    encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), key);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            final WebView webview = (WebView) findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.addJavascriptInterface(new MyJavaScriptInterface(), "Ccavenue");
            webview.setWebChromeClient(new WebChromeClient());
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(webview, url);
                    if (dialog.isShowing())
                        dialog.dismiss();
                   /* if (url.indexOf("/ccavenue-response-mobile") != -1) {
                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    } */
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                }
            });


            try {
                /* An instance of this class will be registered as a JavaScript interface */
                StringBuffer params = new StringBuffer();
                params.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE, UrlConstants.accessCode));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_ID, UrlConstants.merchantId));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.ORDER_ID, TicketsManager.getInstance().mOrder.getOrderNo()));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.REDIRECT_URL, UrlConstants.redirectUrl));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.CANCEL_URL, UrlConstants.cancelUrl));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL, URLEncoder.encode(encVal)));

                String vPostParams = params.substring(0, params.length() - 1);

                webview.postUrl(Constants.TRANS_URL, EncodingUtils.getBytes(vPostParams, "UTF-8"));
            } catch (Exception e) {
                showToast("Exception occured while opening CCAvenue payment page. Please try again.");
                finish();
            }
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
    }

    private void navigateToOnlineConfirmaionPage() {
        Intent intent = new Intent(getApplicationContext(), ConfirmationOnlineActivity.class);
        intent.putExtra(com.explara_core.utils.Constants.EVENT_ID, mEventID);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("unused")
    public class MyJavaScriptInterface {

        @JavascriptInterface
        public void navigateToConfirmationPage() {
            //Toast.makeText(getApplicationContext(), "navigateToConfirmationPage Called", Toast.LENGTH_LONG).show();
            navigateToOnlineConfirmaionPage();
        }

        @JavascriptInterface
        public void navigateToPaymentPage() {
            Intent intent = new Intent(WebViewPaymentActivity.this, PaymentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }

        @JavascriptInterface
        public void onOrderExpiredNavigateToTicketPage() {
            Toast.makeText(WebViewPaymentActivity.this, "Order expired", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(WebViewPaymentActivity.this, TicketsDetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }

    }

}
