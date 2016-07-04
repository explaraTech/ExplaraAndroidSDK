package com.explara.explara_eventslisting_sdk_ui.events.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.explara.explara_eventslisting_sdk.events.dto.Topics;
import com.explara.explara_eventslisting_sdk_ui.R;
import com.explara_core.utils.ImageCacheManager;

import java.util.List;

/**
 * Created by ananthasooraj on 1/21/16.
 */
public class EventDetailTopicsAdapter extends RecyclerView.Adapter<EventDetailTopicsAdapter.TopicsViewHolder> {

    private static final String TAG = EventDetailTopicsAdapter.class.getSimpleName();
    List<Topics> mTopicsList;
    private EventsDetailPagerFragment.EventsDetailFragmentListener mListItemClickListener;

    public EventDetailTopicsAdapter(List<Topics> mTopicsList, EventsDetailPagerFragment.EventsDetailFragmentListener listItemClickListener) {
        this.mTopicsList = mTopicsList;
        mListItemClickListener = listItemClickListener;
    }

    @Override
    public EventDetailTopicsAdapter.TopicsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_category_layout, parent, false);
        TopicsViewHolder topicsViewHolder;
        topicsViewHolder = new TopicsViewHolder(view, mListItemClickListener);

        return topicsViewHolder;
    }

    @Override
    public void onBindViewHolder(EventDetailTopicsAdapter.TopicsViewHolder holder, int position) {
        Topics topics = mTopicsList.get(position);

        holder.topicName.setText(topics.topicName);
        holder.topicImage.setImageUrl(topics.eventDetailTopicImage, holder.imageLoader);

        holder.topicImage.setBackgroundColourFromImage(false);
        holder.itemView.setTag(topics.topicId);

    }

    @Override
    public int getItemCount() {
        return mTopicsList.size();
    }

    public static class TopicsViewHolder extends RecyclerView.ViewHolder {

        TextView topicName;
        NetworkImageView topicImage;
        ImageLoader imageLoader;

        public TopicsViewHolder(View itemView, final EventsDetailPagerFragment.EventsDetailFragmentListener eventsDetailFragmentListener) {
            super(itemView);

            topicName = (TextView) itemView.findViewById(R.id.category_name);
            topicImage = (NetworkImageView) itemView.findViewById(R.id.imgview_event_detail_topics);
            imageLoader = ImageCacheManager.getInstance(itemView.getContext()).getImageLoader();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventsDetailFragmentListener.onTopicsListItemClick((String) v.getTag());
                }
            });
        }

    }
}
