package com.explara_core.common;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by Debasish on 31/08/15.
 */
public abstract class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();
    public MaterialDialog mMaterialDialog;


    public BaseFragment() {

    }

    public abstract void refresh();

    public void showMaterialDialog() {
        mMaterialDialog = new MaterialDialog.Builder(getActivity())
                //.title("Explara Login")
                .content("Please wait..")
                .cancelable(false)
                        //.iconRes(R.drawable.e_logo)
                .progress(true, 0)
                .show();
    }

    public void dismissMaterialDialog() {
        if (mMaterialDialog != null && mMaterialDialog.isShowing()) {
            mMaterialDialog.dismiss();
        }
    }

    public void startActivitySafely(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    /*private String getGraphUserPropertySafely(JSONObject graphUser, String key, String def) {
        try {
            String t = (String) graphUser.get(key);
            return t != null ? t : def;
        } catch (Throwable var5) {
            return def;
        }
    }*/
}
