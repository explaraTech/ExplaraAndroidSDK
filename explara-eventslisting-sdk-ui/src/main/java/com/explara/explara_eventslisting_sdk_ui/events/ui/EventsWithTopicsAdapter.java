package com.explara.explara_eventslisting_sdk_ui.events.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.explara.explara_eventslisting_sdk.events.dto.EventsWithTopicsDto;
import com.explara.explara_eventslisting_sdk_ui.R;
import com.explara_core.utils.ImageCacheManager;

import java.util.List;

/**
 * Created by ananthasooraj on 1/14/16.
 */
public class EventsWithTopicsAdapter extends RecyclerView.Adapter<EventsWithTopicsAdapter.TopicsViewHolder> {


    List<EventsWithTopicsDto.TopicItemDto> mTopicItemDtoList;

    CollectionEventFragment.FragmentListener mFragmentListener;


    public EventsWithTopicsAdapter(List<EventsWithTopicsDto.TopicItemDto> mTopicItemDtoList, CollectionEventFragment.FragmentListener fragmentListener) {
        this.mTopicItemDtoList = mTopicItemDtoList;
        mFragmentListener = fragmentListener;
    }

    @Override
    public EventsWithTopicsAdapter.TopicsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_with_topics_item_layout, parent, false);
        TopicsViewHolder topicsViewHolder;
        topicsViewHolder = new TopicsViewHolder(view);

        return topicsViewHolder;
    }

    @Override
    public void onBindViewHolder(TopicsViewHolder holder, int position) {
        final EventsWithTopicsDto.TopicItemDto topicItemDto = mTopicItemDtoList.get(position);


        if (topicItemDto.topicName != null && !topicItemDto.topicName.trim().isEmpty()) {
            holder.mTopicsName.setText(topicItemDto.topicName);
        } else {
            holder.mTopicsName.setVisibility(View.GONE);
        }

        if (topicItemDto.topicImg != null && !topicItemDto.topicImg.trim().isEmpty()) {
            holder.mTopicsImage.setImageUrl(topicItemDto.topicImg, holder.imageLoader);
        } else {
            holder.mTopicsImage.setVisibility(View.GONE);
        }

        holder.mTopicsImage.setBackgroundColourFromImage(false);

        holder.mTopicsName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentListener.onEventsTopicsClick(topicItemDto.topicName, topicItemDto.topicId, 1);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mTopicItemDtoList.size();
    }


    public static class TopicsViewHolder extends RecyclerView.ViewHolder {


        TextView mTopicsName;
        NetworkImageView mTopicsImage;
        ImageLoader imageLoader;

        public TopicsViewHolder(View itemView) {
            super(itemView);

            mTopicsName = (TextView) itemView.findViewById(R.id.topic_name);
            mTopicsImage = (NetworkImageView) itemView.findViewById(R.id.imgview_events_with_topics);

            imageLoader = ImageCacheManager.getInstance(itemView.getContext().getApplicationContext()).getImageLoader();
        }

    }


}
