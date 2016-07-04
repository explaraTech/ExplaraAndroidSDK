package com.explara_core.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by anudeep on 31/08/15.
 */
public class FragmentHelper {

    /**
     * this method won't add fragment to backstack
     *
     * @param activity
     * @param containerId
     * @param fragment
     * @param tag
     */
    public static void replaceContentFragment(final FragmentActivity activity, final int containerId,
                                              final Fragment fragment, String tag) {
        if (activity != null && !activity.isFinishing() && fragment != null) {
            FragmentTransaction beginTransaction = activity.getSupportFragmentManager().beginTransaction();
            beginTransaction.replace(containerId, fragment, tag).commitAllowingStateLoss();
        }
    }

    /**
     * this method won't add fragment to backstack
     *
     * @param activity
     * @param containerId
     * @param fragment
     */
    public static void replaceContentFragment(final FragmentActivity activity, final int containerId,
                                              final Fragment fragment) {
        if (activity != null && !activity.isFinishing() && fragment != null) {
            FragmentTransaction beginTransaction = activity.getSupportFragmentManager().beginTransaction();
            beginTransaction.replace(containerId, fragment).commitAllowingStateLoss();
        }
    }

    /**
     * this method will add fragment to backstack
     *
     * @param activity
     * @param containerId
     * @param fragment
     * @param tag
     */
    public static void replaceAndAddContentFragment(final FragmentActivity activity, final int containerId,
                                                    final Fragment fragment, String tag) {
        if (activity != null && !activity.isFinishing() && fragment != null) {
            FragmentManager manager = activity.getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(containerId, fragment);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();

        }
    }

    public static void popContentFragment(final FragmentActivity activity, String tag) {
        if (activity != null && !activity.isFinishing()) {
            FragmentManager manager = activity.getSupportFragmentManager();
            manager.popBackStack();
        }
    }

    public static void clearBackStack(final FragmentActivity activity) {
        try {
            if (null != activity)
                activity.getSupportFragmentManager().popBackStackImmediate(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public static Fragment getFragment(final FragmentActivity activity, final int containerId) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        return fragmentManager.findFragmentById(containerId);
    }

    public static Fragment getChildFragment(final Fragment context, final int containerId) {
        FragmentManager fragmentManager = context.getChildFragmentManager();
        return fragmentManager.findFragmentById(containerId);
    }

    /**
     * this method won't add fragment to backstack
     *
     * @param context
     * @param containerId
     * @param fragment
     */
    public static void replaceChildFragment(final Fragment context, final int containerId, final Fragment fragment) {
        FragmentTransaction beginTransaction = context.getChildFragmentManager().beginTransaction();
        beginTransaction.replace(containerId, fragment).commitAllowingStateLoss();

    }
}
