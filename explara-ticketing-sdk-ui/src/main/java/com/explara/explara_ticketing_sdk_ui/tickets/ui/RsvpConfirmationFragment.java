package com.explara.explara_ticketing_sdk_ui.tickets.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara.explara_ticketing_sdk_ui.common.TicketingBaseFragment;
import com.explara_core.utils.Constants;

/**
 * Created by ananthasooraj on 9/4/15.
 */
public class RsvpConfirmationFragment extends TicketingBaseFragment {

    private static final String TAG = RsvpConfirmationFragment.class.getSimpleName();
    private String mEventId;
    private TextView mConfirmationText;
    private Button mConfirmRsvpBtn;

    public RsvpConfirmationFragment() {

    }


    public static RsvpConfirmationFragment getInstance(Intent intent) {
        RsvpConfirmationFragment rsvpConfirmationFragment = new RsvpConfirmationFragment();
        String eventId = intent.getStringExtra(Constants.EVENT_ID);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EVENT_ID, eventId);
        rsvpConfirmationFragment.setArguments(bundle);
        return rsvpConfirmationFragment;
    }

    @Override
    public void refresh() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rsvp_confirmation_fragment, container, false);
        extractArguments();
        initViews(view);
        return view;
    }


    private void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEventId = args.getString(Constants.EVENT_ID);
        }
    }

    private void googleAnalyticsSendScreenName() {
        if (TicketsManager.getInstance().mAnalyticsListener != null) {
            TicketsManager.getInstance().mAnalyticsListener.sendScreenName(getResources().getString(R.string.rsvp_confirmation), getActivity().getApplication(), getContext());
        }
        //AnalyticsHelper.sendScreenName(getResources().getString(R.string.rsvp_confirmation), getActivity().getApplication(), getContext());
    }

    public void initViews(View view) {
        mConfirmationText = (TextView) view.findViewById(R.id.confirmation_text);
        mConfirmRsvpBtn = (Button) view.findViewById(R.id.confirm_btn);
        googleAnalyticsSendScreenName();
        mConfirmRsvpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TicketsManager.getInstance().mAppCallBackListener.onTransactionComplete(getContext());
               /* Intent homeIntent = new Intent(getActivity(),   PersonalizeScreenActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);

                Intent homeIntent = new Intent();
                homeIntent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME, "com.explara.android.personalizedHome.ui.PersonalizeScreenActivity"));
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent); */
            }
        });
    }

}
