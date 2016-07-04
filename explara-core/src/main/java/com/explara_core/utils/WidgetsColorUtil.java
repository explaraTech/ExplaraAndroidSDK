package com.explara_core.utils;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;


/**
 * Created by ananthasooraj on 2/9/16.
 */
public class WidgetsColorUtil {


    public static void setCheckBoxColor(CheckBox checkBoxId, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            checkBoxId.setButtonTintList(ColorStateList.valueOf(color));
        }
    }

    public static void setProgressBarTintColor(ProgressBar progressBarId, int color){
        progressBarId.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    public static void setRadioButtonColor(RadioButton radioButtonId, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            radioButtonId.setButtonTintList(ColorStateList.valueOf(color));
        }
    }

    public static void setSwipeRefreshLayoutTintColor(SwipeRefreshLayout swipeRefreshLayoutId, int color) {
        swipeRefreshLayoutId.setColorSchemeColors(color);
    }


}
