package com.explara_core.utils;

import android.os.Build;

/**
 * Created by ananthasooraj on 9/17/15.
 */
public class CompatibilityUtil {

    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static boolean isLolipop() {
        return getSdkVersion() >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isMarshmallow() {
        return getSdkVersion() >= Build.VERSION_CODES.M;
    }

    public static boolean isIceCreamSandwich() {
        return getSdkVersion() >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean isJellyBean() {
        return getSdkVersion() >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean isKitkat() {
        return getSdkVersion() >= Build.VERSION_CODES.KITKAT;
    }

}
