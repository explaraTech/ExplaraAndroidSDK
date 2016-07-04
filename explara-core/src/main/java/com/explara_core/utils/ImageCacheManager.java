package com.explara_core.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by anudeep on 25/08/15.
 */
public class ImageCacheManager {
    private static ImageCacheManager sImageCacheManager;

    public static LruBitmapCache mVolleyLruBitmapCache;

    private ImageLoader mImageLoader;

    private ImageCacheManager(Context context) {
        initImageCache(context);

    }

    public synchronized static ImageCacheManager getInstance(Context context) {
        if (sImageCacheManager == null) {
            sImageCacheManager = new ImageCacheManager(context);
        }
        return sImageCacheManager;
    }

    public void initImageCache(Context context) {
        mVolleyLruBitmapCache = new LruBitmapCache();
        mImageLoader = new ImageLoader(VolleyManager.getInstance(context).getImageRequestQueue(), mVolleyLruBitmapCache);
        mImageLoader.setBatchedResponseDelay(0);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void cleanUp() {
        if (mVolleyLruBitmapCache != null) {
            mVolleyLruBitmapCache.evictAll();
            mVolleyLruBitmapCache = null;
        }
        sImageCacheManager = null;
    }

    public void putBitmap(Bitmap bitmap, int resourceId) {
        mVolleyLruBitmapCache.putBitmap(String.valueOf(resourceId), bitmap);
    }

    public void putBitmap(Bitmap bitmap, String resourceId) {
        mVolleyLruBitmapCache.putBitmap(resourceId, bitmap);
    }

    public Bitmap getBitmap(int resourceId) {
        return mVolleyLruBitmapCache.getBitmap(String.valueOf(resourceId));
    }

    public Bitmap getBitmap(String resourceId) {
        return mVolleyLruBitmapCache.getBitmap(String.valueOf(resourceId));
    }
}
