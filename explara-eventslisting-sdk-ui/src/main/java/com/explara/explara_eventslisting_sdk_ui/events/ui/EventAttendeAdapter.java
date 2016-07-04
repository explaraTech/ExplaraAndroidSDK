package com.explara.explara_eventslisting_sdk_ui.events.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.explara.explara_eventslisting_sdk.events.dto.Attendee;
import com.explara.explara_eventslisting_sdk_ui.R;
import com.explara_core.utils.CircularNetworkImageView;
import com.explara_core.utils.ImageCacheManager;

import java.util.List;

/**
 * Created by anudeep on 26/10/15.
 */
public class EventAttendeAdapter extends RecyclerView.Adapter<EventAttendeAdapter.ViewHolder> {

    private List<Attendee> mAttendeeList;

    public EventAttendeAdapter(List<Attendee> attendeeList) {

        mAttendeeList = attendeeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.atendee_event_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ateendeImage.setImageUrl(mAttendeeList.get(position).getProfileImage(), ImageCacheManager.getInstance(holder.itemView.getContext()).getImageLoader());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mAttendeeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircularNetworkImageView ateendeImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ateendeImage = (CircularNetworkImageView) itemView.findViewById(R.id.attendee_img);
        }
    }
}
