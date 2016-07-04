package com.explara.explara_sdk.communication.ui;

import com.explara.explara_eventslisting_sdk.events.EventsManger;
import com.explara.explara_eventslisting_sdk.events.dto.Event;
import com.explara.explara_payment_sdk.payment.PaymentManager;
import com.explara.explara_payment_sdk.payment.dto.EventDetails;
import com.explara_core.communication.BaseCommunicationManager;

/**
 * Created by debasishpanda on 31/05/16.
 */
public class PaymentCallBack implements BaseCommunicationManager.PaymentCallBackListener {

    @Override
    public void getEventDetailsFromEventId(String EventId) {
        Event event = EventsManger.getInstance().eventsDetailDtoMap.get(EventId).events;
        EventDetails eventDetails = new EventDetails();
        eventDetails.eventTitle = event.getTitle();
        eventDetails.eventUrl = event.getUrl();
        eventDetails.description = event.getTextDescription();
        eventDetails.city = event.getCity();
        eventDetails.largeImage = event.getLargeImage();
        PaymentManager.getInstance().mEventDetails = eventDetails;
    }

    @Override
    public String getCurrencyFromEventId(String EventId) {
        return EventsManger.getInstance().eventsDetailDtoMap.get(EventId).events.getCurrency();
    }
}
