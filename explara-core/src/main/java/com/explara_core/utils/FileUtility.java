package com.explara_core.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by anudeep on 25/08/15.
 */
public class FileUtility {
    private static final String TAG = FileUtility.class.getSimpleName();

    /**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * @param context    The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable() ? getExternalCacheDir(context) == null ? context
                .getCacheDir().getPath() : getExternalCacheDir(context).getAbsolutePath() : context.getCacheDir()
                .getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    private static File getExternalCacheDir(Context context) {
        File externalFilesDir = context.getExternalFilesDir(null);

        Log.v(TAG, "context.getExternalFilesDir(null) is null");
        return externalFilesDir;
    }

}
