package com.explara.explara_ticketing_sdk_ui.tickets.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.explara.explara_ticketing_sdk.tickets.dto.SessionsDetails;
import com.explara.explara_ticketing_sdk_ui.R;

import java.util.List;

/**
 * Created by Debasish on 31/08/15.
 */
public class SessionTimingsAdapter extends RecyclerView.Adapter<SessionTimingsAdapter.ViewHolder> {
    private List<SessionsDetails> mList;
    private int mSelectedSessionTimePosition = 0;
    //

    public interface SessionTimeOnClickListener {
        void onSessionTimeClicked(int position);
    }

    private SessionTimeOnClickListener sessionTimeOnClickListener;

    public SessionTimingsAdapter(List<SessionsDetails> collectionList, SessionTimeOnClickListener sessionTimeOnClickListener, int mSelectedSessionTimePosition) {
        mList = collectionList;
        this.sessionTimeOnClickListener = sessionTimeOnClickListener;
        this.mSelectedSessionTimePosition = mSelectedSessionTimePosition;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_times_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView, sessionTimeOnClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mSessionStartTime.setText(mList.get(position).time);
        if (position == mSelectedSessionTimePosition) {
            holder.mSessionStartTime.setTextColor(holder.mSessionStartTime.getContext().getResources().getColor(R.color.colorPrimary));
        }
        //holder.itemView.setTag(R.id.session_start_time,position);
        //holder.itemView.setTag(R.id.item_layout_sessions_timing,holder.getItemId());
        holder.mSessionStartTime.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mSessionStartTime;
        private SessionTimeOnClickListener sessionTimeOnClickListener;

        public ViewHolder(View itemView, SessionTimeOnClickListener sessionTimeOnClickListener) {
            super(itemView);
            this.sessionTimeOnClickListener = sessionTimeOnClickListener;

            mSessionStartTime = (TextView) itemView.findViewById(R.id.session_start_time);
            mSessionStartTime.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            sessionTimeOnClickListener.onSessionTimeClicked((Integer) v.getTag());

        }
    }

    public void updateSelectedPosition(int position) {
        mSelectedSessionTimePosition = position;
    }
}
