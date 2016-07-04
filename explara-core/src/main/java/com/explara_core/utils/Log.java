package com.explara_core.utils;

/**
 * Created by dev on 02/07/15.
 */
public class Log {

    public static void d(String tag, String message) {
        //if (Constants.DEBUG)
        android.util.Log.d(tag, message);
    }

    public static void i(String tag, String message) {
        //if (Constants.DEBUG)
        android.util.Log.i(tag, message);
    }

    public static void v(String tag, String message) {
        //if (Constants.DEBUG)
        android.util.Log.v(tag, message);
    }

    public static void e(String tag, String message) {
        //if (Constants.DEBUG)
        android.util.Log.v(tag, message);
    }

}
