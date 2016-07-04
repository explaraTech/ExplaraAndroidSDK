package com.explara.explara_ticketing_sdk_ui.attendee.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.explara.explara_ticketing_sdk_ui.R;
import com.explara_core.utils.Constants;
import com.explara_core.utils.PreferenceManager;

/**
 * Created by debasishpanda on 12/09/15.
 */
public class AttendeeFormDialogFragment extends DialogFragment {

    private static final String TAG = AttendeeFormDialogFragment.class.getSimpleName();
    private String mEventId;

    public static AttendeeFormDialogFragment newInstance(String eventId) {
        AttendeeFormDialogFragment attendeeFormDialogFragment = new AttendeeFormDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EVENT_ID, eventId);
        attendeeFormDialogFragment.setArguments(bundle);
        return attendeeFormDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_attendee_form_dialog, container, false);
        // getDialog().setCanceledOnTouchOutside(false);
        setDialogTitle();
        extractArguments();
        initView(rootView);
        return rootView;
    }

    public void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEventId = args.getString(Constants.EVENT_ID);
        }
    }

    private void initView(View view) {
        Button changeBtn = (Button) view.findViewById(R.id.change_btn);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AttendeeFormFragment) getParentFragment()).navigateToPaymentSelectionActivity(false, mEventId, TAG);
                dismiss();
            }
        });

        Button checkoutBtn = (Button) view.findViewById(R.id.checkout_btn);
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AttendeeFormFragment) getParentFragment()).navigateToPaymentSelectionActivity(PreferenceManager.getInstance(getContext()).isPreferredWalletOptionSelected(), mEventId, TAG);
                dismiss();
            }
        });

    }


    public void setDialogTitle() {
        getDialog().setTitle("      You’re almost there!");

    }


}
