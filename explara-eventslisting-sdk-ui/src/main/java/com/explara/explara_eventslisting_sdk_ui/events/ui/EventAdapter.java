package com.explara.explara_eventslisting_sdk_ui.events.ui;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.explara.explara_eventslisting_sdk.events.EventsManger;
import com.explara.explara_eventslisting_sdk.events.dto.CollectionEventsDto;
import com.explara.explara_eventslisting_sdk.events.dto.EventsWithTopicsDto;
import com.explara.explara_eventslisting_sdk.utils.EventsListingConstantKeys;
import com.explara.explara_eventslisting_sdk_ui.R;
import com.explara.explara_eventslisting_sdk_ui.utils.EventsFilter;
import com.explara_core.utils.Constants;
import com.explara_core.utils.ImageCacheManager;
import com.explara_core.utils.Log;
import com.explara_core.utils.Utility;

import java.util.List;

/**
 * Created by Debasish on 04/09/15.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> implements Filterable {

    private static final String TAG = EventAdapter.class.getSimpleName();
    private int mEventPos;
    private List<CollectionEventsDto.Events> mEvents;
    private CollectionEventFragment.FragmentListener listItemClickListener;
    private static int mSourcePageNumber;
    private FavButtonClickListener mFavButtonClickListener;
    private String categoryName;
    public boolean isTopicItemExists = false;

    @Override
    public Filter getFilter() {
        return new EventsFilter();
    }

    public interface FavButtonClickListener {

        void onFavButtonClicked(String eventId, int position);
    }

    public EventAdapter(int eventPos, List<CollectionEventsDto.Events> events, CollectionEventFragment.FragmentListener listItemClickListener, int sourcePageNumber) {
        mEventPos = eventPos;
        this.mEvents = events;
        this.listItemClickListener = listItemClickListener;
        mSourcePageNumber = sourcePageNumber;
    }

    // For the my favourite page
    public EventAdapter(int eventPos, List<CollectionEventsDto.Events> events, CollectionEventFragment.FragmentListener listItemClickListener, FavButtonClickListener favButtonClickListener, int sourcePageNumber) {
        this.mEvents = events;
        this.listItemClickListener = listItemClickListener;
        this.mFavButtonClickListener = favButtonClickListener;
        mSourcePageNumber = sourcePageNumber;
        mEventPos = eventPos;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == EventsListingConstantKeys.EventKeys.EVENT_TYPE) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_list_view_item, parent, false);
            ViewHolder viewHolder;

            // For my fav page
            if (mFavButtonClickListener != null) {
                viewHolder = new ViewHolder(itemView, listItemClickListener, mFavButtonClickListener, isTopicItemExists);
            } else {
                viewHolder = new ViewHolder(itemView, listItemClickListener, isTopicItemExists);
            }
            return viewHolder;

        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_with_topics_layout, parent, false);
            ViewHolder viewHolder;
            viewHolder = new ViewHolder(view);
            return viewHolder;
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int itemType = getItemViewType(position);
        if (itemType == EventsListingConstantKeys.EventKeys.EVENT_TYPE) {
            CollectionEventsDto.Events event = this.mEvents.get(position);
            holder.titleTextView.setText(Html.fromHtml(event.title));
            if ("theater".equals(event.eventSessionType)) {
                holder.eventDateTextView.setText("Multiple Dates");
            } else {
                holder.eventDateTextView.setText(event.startDate);
            }
            String venueName = !TextUtils.isEmpty(event.venueName) ? event.venueName : event.city;
            if (!TextUtils.isEmpty(venueName)) {
                holder.eventLocationTextView.setVisibility(View.VISIBLE);
                holder.eventLocationTextView.setText(venueName);
            } else {
                holder.eventLocationTextView.setVisibility(View.INVISIBLE);
            }
            String priceAndCurrency = Utility.getPriceAndCurrency(event.currency, String.valueOf(event.price), holder.itemView.getContext().getApplicationContext());
            holder.eventPriceTextView.setText(priceAndCurrency);
            ImageLoader imageLoader = ImageCacheManager.getInstance(holder.itemView.getContext().getApplicationContext()).getImageLoader();
            holder.eventImageView.setImageUrl(event.smallImage, imageLoader);
            holder.itemView.setTag(mEvents.get(position).id);
            holder.itemView.setTag(R.id.event_title, mEventPos);
            holder.itemView.setTag(R.id.events_list_view_item_fav_image_view, position);
            //holder.itemView.setTag(R.id.events_list_view_item_date,position);
        } else {
            EventsManger instance = EventsManger.getInstance();
            if (instance.mTopicItemDtoResponse.topics.topics.get(instance.getCategory(categoryName).subCategoryId.get(mEventPos)).size() > 0 &&
                    !instance.mTopicItemDtoResponse.topics.topics.get(instance.getCategory(categoryName).subCategoryId.get(mEventPos)).toString().trim().isEmpty()) {
                holder.recyclerViewTopics.setAdapter(new EventsWithTopicsAdapter(instance.mTopicItemDtoResponse.topics.topics.get(instance.getCategory(categoryName).subCategoryId.get(mEventPos)), listItemClickListener));
            } else {

                holder.mCardViewTopics.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public void addTopicItem() {
        if (mEvents != null && !mEvents.isEmpty()) {
            CollectionEventsDto.Events eventZero = mEvents.get(0);
            if (eventZero != null) {
                if (eventZero.itemType != EventsListingConstantKeys.EventKeys.EVENT_TOPIC_TYPE) {
                    EventsManger instance = EventsManger.getInstance();
                    if (instance.getCategory(categoryName).subCategoryId != null && !instance.getCategory(categoryName).subCategoryId.isEmpty()) {
                        List<EventsWithTopicsDto.TopicItemDto> topicItemDtos = instance.mTopicItemDtoResponse.topics.topics.get(instance.getCategory(categoryName).subCategoryId.get(mEventPos));
                        if (topicItemDtos != null && !topicItemDtos.isEmpty()) {
                            CollectionEventsDto.Events event = new CollectionEventsDto.Events();
                            event.itemType = EventsListingConstantKeys.EventKeys.EVENT_TOPIC_TYPE;
                            mEvents.add(0, event);
//                            isTopicItemExists = true;
                        }
                    } else {
//                        isTopicItemExists = true;
                        Log.d(TAG, "something wrong");
                    }
                }
                notifyDataSetChanged();
            }
        }

//        checkIfTopicItemExists();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView titleTextView;
        public NetworkImageView eventImageView;
        public ImageView eventFavIconImageView;
        public TextView eventDateTextView;
        public TextView eventLocationTextView;
        public TextView eventPriceTextView;
        private CollectionEventFragment.FragmentListener listItemClickListener;
        private FavButtonClickListener favButtonClickListener;
        private boolean isTopicItemExists;

        private RecyclerView recyclerViewTopics;
        private CardView mCardViewTopics;

        public ViewHolder(View itemView, CollectionEventFragment.FragmentListener listItemClickListener, boolean isTopicAdded) {
            super(itemView);
            this.listItemClickListener = listItemClickListener;
            isTopicItemExists = isTopicAdded;
            itemView.setOnClickListener(this);
            titleTextView = (TextView) itemView.findViewById(R.id.event_title);
            eventImageView = (NetworkImageView) itemView.findViewById(R.id.event_big_image);
            eventFavIconImageView = (ImageView) itemView.findViewById(R.id.events_list_view_item_fav_image_view);
            if (mSourcePageNumber == CollectionEventFragment.From.FAV) {
                eventFavIconImageView.setVisibility(View.VISIBLE);
                eventFavIconImageView.setOnClickListener(this);
            }
            eventDateTextView = (TextView) itemView.findViewById(R.id.events_list_view_item_date);
            eventLocationTextView = (TextView) itemView.findViewById(R.id.events_list_view_item_venue);
            eventPriceTextView = (TextView) itemView.findViewById(R.id.events_list_view_item_price);
        }

        // For my fav page
        public ViewHolder(View itemView, CollectionEventFragment.FragmentListener listItemClickListener, FavButtonClickListener favButtonClickListener, boolean isTopicAdded) {
            super(itemView);
            this.listItemClickListener = listItemClickListener;
            this.favButtonClickListener = favButtonClickListener;
            isTopicItemExists = isTopicAdded;
            itemView.setOnClickListener(this);
            titleTextView = (TextView) itemView.findViewById(R.id.event_title);
            eventImageView = (NetworkImageView) itemView.findViewById(R.id.event_big_image);
            eventFavIconImageView = (ImageView) itemView.findViewById(R.id.events_list_view_item_fav_image_view);
            if (mSourcePageNumber == CollectionEventFragment.From.FAV) {
                eventFavIconImageView.setVisibility(View.VISIBLE);
                eventFavIconImageView.setOnClickListener(this);
            }
            eventDateTextView = (TextView) itemView.findViewById(R.id.events_list_view_item_date);
            eventLocationTextView = (TextView) itemView.findViewById(R.id.events_list_view_item_venue);
            eventPriceTextView = (TextView) itemView.findViewById(R.id.events_list_view_item_price);
        }

        public ViewHolder(View itemView) {
            super(itemView);
            recyclerViewTopics = (RecyclerView) itemView.findViewById(R.id.recyclerView_events_topics);
            mCardViewTopics = (CardView) itemView.findViewById(R.id.cardView_events_with_topics);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerViewTopics.setLayoutManager(linearLayoutManager);
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.events_list_view_item_fav_image_view) {
                favButtonClickListener.onFavButtonClicked((String) itemView.getTag(), getAdapterPosition());
            } else {
                int position = (Integer) itemView.getTag(R.id.events_list_view_item_fav_image_view);
                checkIfTopicItemExists();
                if (isTopicItemExists) {
                    position = position - 1;
                }
                if (Constants.EXPLARA_ONLY) {
                    listItemClickListener.onEventClicked((String) itemView.getTag(), (Integer) itemView.getTag(R.id.event_title), position);
                } else {
                    if ((mSourcePageNumber == CollectionEventFragment.From.ORG) && (mEventPos == 1)) {
                        // No onclick for organizer past events.Only for Sdk
                    } else {
                        listItemClickListener.onEventClicked((String) itemView.getTag(), (Integer) itemView.getTag(R.id.event_title), position);
                    }
                }
            }
        }

        public void checkIfTopicItemExists() {
            CollectionEventsDto.Events eventZero = mEvents.get(0);
            if (eventZero != null) {
                if (eventZero.itemType == EventsListingConstantKeys.EventKeys.EVENT_TOPIC_TYPE) {
                    isTopicItemExists = true;
                }
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        return mEvents.get(position).itemType;
    }

    public void setStringName(String categoryName) {
        this.categoryName = categoryName;
        EventsManger.getInstance().categoriesResponseFilterDtoMap.get(categoryName).getSubCategoryEvents().get(mEventPos).setEvents(mEvents);
        //EventsManger.newInstance().addFilterMap(categoryName, mEvents);
    }


}
