package com.explara_core.utils;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

/**
 * Created by anudeep on 25/08/15.
 */
public class VolleyManager {

    public static String TAG = VolleyManager.class.getSimpleName();

    private VolleyManager() {
    }

    private VolleyManager(Context context) {
        initVolley(context);
    }

    private static VolleyManager sVolleyHelper;

    public static synchronized VolleyManager getInstance(Context context) {

        if (null == sVolleyHelper) {
            sVolleyHelper = new VolleyManager(context);
        }

        return sVolleyHelper;
    }

    private static final String CACHE_DIR = "explara_images_and_feeds";

    private int mMaxCacheByteSize = 1024 * 1024 * 50;

    private RequestQueue mRequestQueue;

    private void initVolley(Context context) {

        Network network = new BasicNetwork(new HurlStack());

        Cache cache = new DiskBasedCache(FileUtility.getDiskCacheDir(context, CACHE_DIR), mMaxCacheByteSize);

        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public RequestQueue getImageRequestQueue() {
        return mRequestQueue;
    }

    public void startProcessingRequestQueue() {
        mRequestQueue.start();
    }

    public void stopProcessingRequestQueue() {
        mRequestQueue.stop();
    }

    /**
     * This cancels all Requests from all activities/fragments, and doesn't work
     * favorably with the Activity Lifecycle. The best way to manage this is to
     * add a String tag unique to your fragment.
     */
    public void cancelAllPendingDownloadRequests() {
        if (mRequestQueue != null) {
            Log.d(TAG, "cancelAllPendingDownloadRequests called");
            mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
    }

    public void cancelRequest(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            mRequestQueue.cancelAll(tag);
        }

    }
}
