
package com.explara_core.utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.apache.http.HttpStatus;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author anudeep
 */
public class GsonRequest<T> extends Request<T> {

    public static final String LOG_TAG = GsonRequest.class.getSimpleName();
    private static final String PROTOCOL_CHARSET = "utf-8";
    protected static final int TIMEOUT_IN_MILLSEC = 60000;
    protected Class<T> mModelClass;
    protected Map<String, String> mParams;
    protected Map<String, String> mHeader;
    protected Listener<T> mSuccessResponseListener;
    protected Gson mGson;
    protected String mRequestBody;


    /**
     * @param method
     * @param url
     * @param listener
     * @param moddelClass
     */

    // For normal request - Ex- GET request
    public GsonRequest(Context context, int method, String url, Class<T> moddelClass, Listener<T> listener, ErrorListener errorListener) {
        this(context, method, url, moddelClass, null, listener, errorListener);
        applicationCtx = context;
    }

    // For request having params
    public GsonRequest(Context context, int method, String url, Class<T> moddelClass, Map<String, String> params, Listener<T> listener,
                       ErrorListener errorListener) {
        super(method, url, errorListener);
        mModelClass = moddelClass;
        mParams = params;
        mSuccessResponseListener = listener;
        applicationCtx = context;
        mGson = new Gson();
        RetryPolicy policy = new DefaultRetryPolicy(TIMEOUT_IN_MILLSEC, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        setRetryPolicy(policy);

    }

    // For request having requestBody Example - POST request
    public GsonRequest(Context context, int method, String url, Class<T> moddelClass, Map<String, String> params, String requestBody, Listener<T> listener,
                       ErrorListener errorListener) {
        super(method, url, errorListener);
        mModelClass = moddelClass;
        mParams = params;
        mRequestBody = requestBody;
        mSuccessResponseListener = listener;
        mGson = new Gson();
        applicationCtx = context;
        RetryPolicy policy = new DefaultRetryPolicy(TIMEOUT_IN_MILLSEC, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        setRetryPolicy(policy);

    }


    /*
     * (non-Javadoc)
     *
     * @see com.android.volley.Request#parseNetworkResponse(com.android.volley.
     * NetworkResponse)
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        if (null != response
                && (response.statusCode == HttpStatus.SC_OK || response.statusCode == HttpStatus.SC_NOT_MODIFIED)) {

            String data;

            try {

                data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                Log.i("Response Data :", data);
                return Response.success(mGson.fromJson(data, mModelClass),
                        HttpHeaderParser.parseCacheHeaders(response));

            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JsonSyntaxException e) {
                Log.d("Error", "JsonSyntaxException" + e);
                e.printStackTrace();
                return Response.error(new ParseError(e));
            }

        }

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.android.volley.Request#deliverResponse(java.lang.Object)
     */
    @Override
    protected void deliverResponse(T response) {
        mSuccessResponseListener.onResponse(response);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams != null ? mParams : super.getParams();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return mRequestBody == null ? null : mRequestBody
                    .getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {

            return null;
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        if (headers.isEmpty()) {
            headers = new HashMap<>();
            headers.put("User-agent", "Android Application");
        }
        return headers;
    }

    // /*
    // * (non-Javadoc)
    // *
    // * @see com.android.volley.Request#getHeaders()
    // */
    // @Override
    // public Map<String, String> getHeaders() throws AuthFailureError {
    //
    // Map<String, String> headers = new HashMap<String, String>();
    //
    // // Add GZIP
    // headers.put(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
    //
    // return headers;
    // }

}
