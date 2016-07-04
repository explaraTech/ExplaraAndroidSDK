package com.explara_core.utils;

import android.content.Context;

import com.android.volley.RequestQueue;

/**
 * Created by dev on 02/07/15.
 */
public class ExplaraVolley {

//    private static final Object TAG = ExplaraVolley.class.getSimpleName();
//    private static RequestQueue mRequestQueue;
//    private static ImageLoader mImageLoader;
//    private static Context mContext;
//
//    private ExplaraVolley() {
//    }
//
//    /**
//     * Initialize Volley Request Queue.
//     *
//     * @param context Application Context.
//     */
//    public static void init(Context context) {
//        mContext = context;
//        mRequestQueue = Volley.newRequestQueue(context, new OkHttpStack());
//    }

    /**
     * Method to get the Volley Request Queue.
     *
     * @param context Application Context.
     * @return Request Queue
     */
    public static RequestQueue getRequestQueue(Context context) {
//        if (mRequestQueue != null) {
//            return mRequestQueue;
//        } else {
//            return mRequestQueue = Volley.newRequestQueue(context,
//                    new OkHttpStack());
//        }
        return VolleyManager.getInstance(context).getRequestQueue();
    }

//    public static ImageLoader getImageLoader() {
//        getRequestQueue(mContext);
//        if (mImageLoader == null) {
//            mImageLoader = new ImageLoader(mRequestQueue,
//                    new LruBitmapCache());
//        }
//        return mImageLoader;
//    }

//    public <T> void addToRequestQueue(Request<T> req, String tag) {
//        // set the default tag if tag is empty
//        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
//        getRequestQueue(mContext).add(req);
//    }
//
//    public <T> void addToRequestQueue(Request<T> req) {
//        req.setTag(TAG);
//        getRequestQueue(mContext).add(req);
//    }
//
//    public void cancelPendingRequests(Object tag) {
//        if (mRequestQueue != null) {
//            mRequestQueue.cancelAll(tag);
//        }
//    }
}
