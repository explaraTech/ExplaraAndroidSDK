package com.explara.explara_ticketing_sdk_ui.tickets.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.explara.explara_ticketing_sdk.tickets.dto.TicketDate;
import com.explara.explara_ticketing_sdk_ui.R;

import java.util.List;

/**
 * Created by anudeep on 31/08/15.
 */
public class TicketDatesAdapter extends RecyclerView.Adapter<TicketDatesAdapter.ViewHolder> {
    private List<TicketDate> mList;
    private int mSelectedDatePosition = 0;

    public interface TicketDatesListener {
        void onDateClicked(int position);
    }

    private TicketDatesListener mTicketDatesListener;

    public TicketDatesAdapter(List<TicketDate> collectionList, TicketDatesListener ticketDatesListener) {
        mList = collectionList;
        this.mTicketDatesListener = ticketDatesListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_date_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView, mTicketDatesListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TicketDate ticketDate = this.mList.get(position);
        String[] ticketdates = ticketDate.ticketDate.split(" ");
        holder.mTicketdate.setText(ticketDate.day + "\n " + ticketdates[0]);
        if (position == mSelectedDatePosition) {
            holder.mTicketdate.setTextColor(holder.mTicketdate.getContext().getResources().getColor(R.color.colorPrimary));
            holder.mUnderLine.setVisibility(View.VISIBLE);
        } else {
            holder.mTicketdate.setTextColor(holder.mTicketdate.getContext().getResources().getColor(R.color.separator_line));
            holder.mUnderLine.setVisibility(View.GONE);
        }

        holder.mTicketdate.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTicketdate;
        private View mUnderLine;
        private TicketDatesListener ticketDatesListener;

        public ViewHolder(View itemView, TicketDatesListener ticketDatesListener) {
            super(itemView);
            this.ticketDatesListener = ticketDatesListener;
            //itemView.setOnClickListener(this);
            mTicketdate = (TextView) itemView.findViewById(R.id.ticket_date);
            mUnderLine = (View) itemView.findViewById(R.id.text_underline);
            mTicketdate.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ticketDatesListener.onDateClicked((Integer) v.getTag());
        }
    }

    public void updateSelectedPosition(int position) {
        mSelectedDatePosition = position;
    }
}
