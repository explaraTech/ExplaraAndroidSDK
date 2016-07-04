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
public class PackageDatesAdapter extends RecyclerView.Adapter<PackageDatesAdapter.ViewHolder> {
    private List<TicketDate> mList;
    private int mSelectedDatePosition = 0;

    public interface TicketDatesListener {
        void onDateClicked(int position);
    }

    private TicketDatesListener mTicketDatesListener;

    public PackageDatesAdapter(List<TicketDate> collectionList, TicketDatesListener ticketDatesListener) {
        mList = collectionList;
        this.mTicketDatesListener = ticketDatesListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_date_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView, mTicketDatesListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TicketDate ticketDate = this.mList.get(position);
        String[] ticketdates = ticketDate.ticketDate.split(" ");
        holder.mTicketDay.setText(ticketdates[0]);
        holder.mTicketDate.setText(ticketDate.day);

        if (position == mSelectedDatePosition) {
            holder.itemView.setBackgroundColor(holder.mTicketDate.getContext().getResources().getColor(R.color.selected_background));
            holder.mTicketDate.setTextColor(holder.mTicketDate.getContext().getResources().getColor(R.color.selected_text_color));
            holder.mTicketDay.setTextColor(holder.mTicketDay.getContext().getResources().getColor(R.color.selected_text_color));
        } else {
            holder.itemView.setBackgroundColor(holder.mTicketDate.getContext().getResources().getColor(R.color.normal_background_date));
            holder.mTicketDate.setTextColor(holder.mTicketDate.getContext().getResources().getColor(R.color.normal_text_color));
            holder.mTicketDay.setTextColor(holder.mTicketDay.getContext().getResources().getColor(R.color.normal_text_color));
        }

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTicketDay;
        private TextView mTicketDate;

        private TicketDatesListener ticketDatesListener;

        public ViewHolder(View itemView, TicketDatesListener ticketDatesListener) {
            super(itemView);
            this.ticketDatesListener = ticketDatesListener;
            //itemView.setOnClickListener(this);
            mTicketDay = (TextView) itemView.findViewById(R.id.ticket_day);
            mTicketDate = (TextView) itemView.findViewById(R.id.ticket_date);
            itemView.setOnClickListener(this);
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
