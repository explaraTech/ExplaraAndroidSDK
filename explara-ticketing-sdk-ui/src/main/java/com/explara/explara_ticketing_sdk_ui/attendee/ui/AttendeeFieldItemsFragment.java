package com.explara.explara_ticketing_sdk_ui.attendee.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.explara.explara_ticketing_sdk.attendee.AttendeeManager;
import com.explara.explara_ticketing_sdk.attendee.dto.AttendeeDetailsResponseDto;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara.explara_ticketing_sdk_ui.common.TicketingBaseFragment;
import com.explara.explara_ticketing_sdk_ui.utils.TicketingUiConstantKeys;

import java.util.List;

/**
 * Created by Debasish on 08/01/16.
 */
public class AttendeeFieldItemsFragment extends TicketingBaseFragment {

    // public static final String ATTENDEE_POSITION = "attendee_position";
    private static final String TAG = AttendeeFieldItemsFragment.class.getSimpleName();
    //private String mEventId;
    private int mAttendeePosition;
    public AttendeeCustomLayout mAttendeeBaseLayout;
    private LinearLayout mAttendeeFormLayout;


    public AttendeeFieldItemsFragment() {

    }

    public static AttendeeFieldItemsFragment newInstance(int position) {
        AttendeeFieldItemsFragment attendeeFormFragment = new AttendeeFieldItemsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TicketingUiConstantKeys.AttendeeFormKeys.SELECTED_ATTENDEE_POSITION, position);
        attendeeFormFragment.setArguments(bundle);
        return attendeeFormFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.attendee_form_fragment, container, false);
        initViews(view);
        extractArguments();
        return view;
    }

    private void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mAttendeePosition = args.getInt(TicketingUiConstantKeys.AttendeeFormKeys.SELECTED_ATTENDEE_POSITION);
        }
    }

    private void initViews(View view) {
        mAttendeeBaseLayout = (AttendeeCustomLayout) view.findViewById(R.id.attendee_base_layout);
        mAttendeeFormLayout = (LinearLayout) view.findViewById(R.id.attendee_form_layout);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpAttendeeFields();
    }

    private void setUpAttendeeFields() {
        String key = (String) AttendeeManager.getInstance().attendeeDetailsResponseDto.attendeeform.keySet().toArray()[mAttendeePosition];
        List<AttendeeDetailsResponseDto.AttendeeDto> attendeeDtos = AttendeeManager.getInstance().attendeeDetailsResponseDto.attendeeform.get(key);
        mAttendeeBaseLayout.addElements(attendeeDtos);
    }

    @Override
    public void refresh() {

    }

}
