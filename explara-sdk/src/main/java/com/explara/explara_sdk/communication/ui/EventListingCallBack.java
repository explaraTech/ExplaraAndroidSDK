package com.explara.explara_sdk.communication.ui;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.explara.explara_ticketing_sdk_ui.tickets.ui.MultidatePackagesActivity;
import com.explara.explara_ticketing_sdk_ui.tickets.ui.RsvpFormFragment;
import com.explara.explara_ticketing_sdk_ui.tickets.ui.TicketsDetailActivity;
import com.explara.explara_ticketing_sdk_ui.tickets.ui.TicketsDetailWithDatesActivity;
import com.explara.explara_ticketing_sdk_ui.tickets.ui.TicketsDetailWithMultipleSessionActivity;
import com.explara_core.communication.BaseCommunicationManager;
import com.explara_core.communication.dto.BuyTicketDataDto;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;

/**
 * Created by debasishpanda on 31/05/16.
 */
public class EventListingCallBack implements BaseCommunicationManager.EventListingCallBackListener {


    @Override
    public void onBuyTicketButtonClicked(Context context, BuyTicketDataDto buyTicketDataDto) {
        if (buyTicketDataDto != null) {
            String eventSessionType = buyTicketDataDto.eventSessionType;
            Intent intent;
            if (ConstantKeys.EVENT_SESSION_TYPE.THEATER.equals(eventSessionType)) {
                intent = new Intent(context, TicketsDetailWithDatesActivity.class);
            } else if (ConstantKeys.EVENT_SESSION_TYPE.CONFERENCE.equals(eventSessionType)) {
                intent = new Intent(context, TicketsDetailWithMultipleSessionActivity.class);
            } else if (ConstantKeys.EVENT_SESSION_TYPE.MULTIDATE.equals(eventSessionType)) {
                intent = new Intent(context, MultidatePackagesActivity.class);
            } else {
                intent = new Intent(context, TicketsDetailActivity.class);
            }
            intent.putExtra(Constants.EVENT_ID, buyTicketDataDto.eventId);
            intent.putExtra(Constants.CURRENCY, buyTicketDataDto.currency.equals("$") ? "USD" : (buyTicketDataDto.currency.equals("&#8377;") ? "INR" : buyTicketDataDto.currency));
            intent.putExtra(Constants.IS_ATTENDEE_FORM_ENABLED, buyTicketDataDto.isAttendeeFormEnabled);
            context.startActivity(intent);
        }
    }

    @Override
    public Fragment getRsvpFragmentFromEventId(String eventId) {
        return RsvpFormFragment.newInstance(eventId);
    }

}
