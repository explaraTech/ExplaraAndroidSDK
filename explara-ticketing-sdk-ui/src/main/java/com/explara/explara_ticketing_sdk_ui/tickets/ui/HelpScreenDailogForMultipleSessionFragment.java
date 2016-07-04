package com.explara.explara_ticketing_sdk_ui.tickets.ui;

import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.explara.explara_ticketing_sdk_ui.R;

/**
 * Created by akshaya on 02/02/16.
 */
public class HelpScreenDailogForMultipleSessionFragment extends DialogFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_screen_dailog_for_multiple_session, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            getDialog().getWindow().requestFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        return view;
    }
}
