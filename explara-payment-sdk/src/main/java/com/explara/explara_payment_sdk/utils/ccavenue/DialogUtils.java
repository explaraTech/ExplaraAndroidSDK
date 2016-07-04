package com.explara.explara_payment_sdk.utils.ccavenue;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by Kausthubh on 8/28/15.
 */
public class DialogUtils {

    private MaterialDialog materialDialog;

    public MaterialDialog progressDialog(Context context) {
        materialDialog = new MaterialDialog.Builder(context)
                .content("Loading")
                .progress(true, 0)
                .cancelable(false)
                .build();

        return materialDialog;
    }


}
