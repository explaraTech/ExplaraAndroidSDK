package com.explara.explara_eventslisting_sdk_ui.utils;

import android.widget.Filter;

import com.explara.explara_eventslisting_sdk.events.dto.CollectionEventsDto;
import com.explara.explara_eventslisting_sdk.utils.EventsListingConstantKeys;
import com.explara_core.utils.ConstantKeys;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by anudeep on 17/01/16.
 */
public class EventsFilter extends Filter {
    private static final String TAG = EventsFilter.class.getSimpleName();
    private CopyOnWriteArrayList<CollectionEventsDto.Events> mEvents;
    private CopyOnWriteArrayList<CollectionEventsDto.Events> mFilteredList = new CopyOnWriteArrayList<>();
    private FilterUpdateListener filterUpdateListener;
    private static final int PRICE_RANGE_TYPE_ONE = 499;
    private static final int PRICE_RANGE_TYPE_TWO = 999;
    private static final int PRICE_RANGE_TYPE_THREE = 4999;
    private static final int PRICE_RANGE_TYPE_FOUR = 5000;
    private int filterOption1;
    private int filterOption2;
    private int filterOption3;

    public static final int INVALID_FILTER_OPTION = -999;

    public void setList(CopyOnWriteArrayList<CollectionEventsDto.Events> events) {
        mEvents = events;
    }


    public void setFilters(int filterOption1, int filterOption2, int filterOption3) {
        this.filterOption1 = filterOption1;
        this.filterOption2 = filterOption2;
        this.filterOption3 = filterOption3;
        mFilteredList = new CopyOnWriteArrayList<>();
    }

    public void setFilterUpdateListener(FilterUpdateListener filterUpdateListener) {
        this.filterUpdateListener = filterUpdateListener;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        if (filterOption1 != INVALID_FILTER_OPTION)
            filterEvents(mEvents, filterOption1);

        if (filterOption2 != INVALID_FILTER_OPTION) {
            List<CollectionEventsDto.Events> eventsList = new ArrayList<>();
            eventsList.addAll(mFilteredList.isEmpty() ? mEvents : mFilteredList);
            mFilteredList.clear();
            filterEvents(eventsList, filterOption2);
        }
        //
        if (filterOption3 != INVALID_FILTER_OPTION) {
            List<CollectionEventsDto.Events> eventsList = new ArrayList<>();
            eventsList.addAll(mFilteredList.isEmpty() ? mEvents : mFilteredList);
            mFilteredList.clear();
            filterEvents(eventsList, filterOption3);
        }

        return null;
    }

    private void filterEvents(List<CollectionEventsDto.Events> events, int filterOption) {

        CollectionEventsDto.Events topicEvent = events.get(0);
        if (topicEvent != null && topicEvent.itemType == EventsListingConstantKeys.EventKeys.EVENT_TOPIC_TYPE) {
            events.remove(topicEvent);
        }

        if (filterOption == FilterOptions.PAGEVIEW) {
//            CollectionEventsDto.Events events1 = events.get(0);
//            if (events1.itemType == EventsManger.EVENT_TOPIC_TYPE) {
//                events.remove(events1);
//            }
            List<CollectionEventsDto.Events> arrayListCopyOfEvents = new ArrayList<>(events);

            java.util.Collections.sort(arrayListCopyOfEvents, new Comparator<CollectionEventsDto.Events>() {
                @Override
                public int compare(CollectionEventsDto.Events lhs, CollectionEventsDto.Events rhs) {
                    Double ldouble = Double.valueOf(lhs.getFilter().getPageView());
                    Double rdouble = Double.valueOf(rhs.getFilter().getPageView());

                    return rdouble.compareTo(ldouble);
                }
            });
            if (mFilteredList.isEmpty())
                mFilteredList.addAll(arrayListCopyOfEvents);
            return;
        }

        for (CollectionEventsDto.Events event : events) {
            if (event.getFilter() != null) {

                if (filterOption == FilterOptions.PRICE) {
                    if (event.getFilter().price.equals(ConstantKeys.EVENT_FILTER_KEYS.PRICE)) {
                        mFilteredList.add(event);
                    }
                }
                if (filterOption == FilterOptions.PRICE_RANGE_UPTO_499) {
                    if (event.getFilter().priceRange == ConstantKeys.EVENT_FILTER_KEYS.PRICE_RANGE_TYPE_ONE) {
                        mFilteredList.add(event);
                    }
                } else if (filterOption == FilterOptions.PRICE_RANGE_UPTO_999) {
                    if (event.getFilter().priceRange == ConstantKeys.EVENT_FILTER_KEYS.PRICE_RANGE_TYPE_TWO) {
                        mFilteredList.add(event);
                    }
                } else if (filterOption == FilterOptions.PRICE_RANGE_UPTO_4999) {
                    if (event.getFilter().priceRange == ConstantKeys.EVENT_FILTER_KEYS.PRICE_RANGE_TYPE_THREE) {
                        mFilteredList.add(event);
                    }
                } else if (filterOption == FilterOptions.PRICE_RANGE_5000_AND_ABOVE) {
                    if (event.getFilter().priceRange == ConstantKeys.EVENT_FILTER_KEYS.PRICE_RANGE_TYPE_FOUR) {
                        mFilteredList.add(event);
                    }
                } else if (filterOption == FilterOptions.ALL) {
                    mFilteredList.add(event);
                } else if (filterOption == FilterOptions.TODAY) {
                    if (event.getFilter().getToday().equals(ConstantKeys.EVENT_FILTER_KEYS.FILTER_EVENTS_TODAY)) {
                        mFilteredList.add(event);
                    }
                } else if (filterOption == FilterOptions.THISWEEK) {
                    if (event.getFilter().getThisWeek().equals(ConstantKeys.EVENT_FILTER_KEYS.FILTER_EVENTS_THIS_WEEK)) {
                        mFilteredList.add(event);
                    }
                } else if (filterOption == FilterOptions.THIS_WEEKEND) {
                    if (event.getFilter().getThisWeekend().equals(ConstantKeys.EVENT_FILTER_KEYS.FILTER_EVENTS_THIS_WEEKEND)) {
                        mFilteredList.add(event);
                    }
                } else if (filterOption == FilterOptions.THISMONTH) {
                    if (event.getFilter().getThisWeekend().equals(ConstantKeys.EVENT_FILTER_KEYS.FILTER_EVENTS_THIS_MONTH)) {
                        mFilteredList.add(event);
                    }
                } else if (filterOption == FilterOptions.POPULAR) {

                    if (event.getFilter().getPopular().equals(ConstantKeys.EVENT_FILTER_KEYS.FILTER_EVENTS_POPULAR)) {
                        mFilteredList.add(event);
                    }


                } else if (filterOption == FilterOptions.TRENDING) {
                    if (event.getFilter().getLatest().equals(ConstantKeys.EVENT_FILTER_KEYS.FILTER_EVENT_TRENDING)) {
                        mFilteredList.add(event);
                    }
                }

            }
        }


    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        filterUpdateListener.updatedList(mFilteredList);


    }


    public interface FilterOptions {
        int FREE = 10001;
        int LATEST = 10003;
        int ALL = 10004;
        int TODAY = 10005;
        int THISWEEK = 10006;
        int THISMONTH = 10007;
        int THIS_WEEKEND = 10008;
        int PRICE_RANGE_UPTO_499 = 10009;
        int PRICE_RANGE_UPTO_999 = 10010;
        int PRICE_RANGE_UPTO_4999 = 10011;
        int PRICE_RANGE_5000_AND_ABOVE = 10012;
        int PRICE = 10013;
        int POPULAR = 10014;
        int TRENDING = 10015;
        int PAGEVIEW = 10016;
    }

    public interface FilterUpdateListener {
        void updatedList(List<CollectionEventsDto.Events> events);
    }

    public void cleanUp() {
        mEvents = null;
        mFilteredList = null;
        filterUpdateListener = null;
    }
}
