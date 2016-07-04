package com.explara.explara_ticketing_sdk_ui.tickets.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.explara.explara_ticketing_sdk.tickets.dto.MonthDetails;
import com.explara.explara_ticketing_sdk_ui.R;

import java.util.List;

/**
 * Created by anudeep on 31/08/15.
 */
public class PackageMonthsAdapter extends RecyclerView.Adapter<PackageMonthsAdapter.ViewHolder> {
    private List<MonthDetails> mList;
    private int mSelectedMonthPosition = 0;

    public interface TicketMonthsListener {
        void onMonthClicked(int position, int startPosition);
    }

    private TicketMonthsListener mTicketMonthsListener;


    public PackageMonthsAdapter(List<MonthDetails> collectionList, TicketMonthsListener ticketMonthsListener) {
        mList = collectionList;
        this.mTicketMonthsListener = ticketMonthsListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_month_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView, mTicketMonthsListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //SessionDates sessionDates = this.mList.get(position);
        String[] ticketMonth = mList.get(position).monthName.split(" ");
        holder.mTicketMonth.setText(ticketMonth[0]);
        holder.mTicketYear.setText(ticketMonth[1]);

        if (position == mSelectedMonthPosition) {
            holder.itemView.setBackgroundColor(holder.mTicketMonth.getContext().getResources().getColor(R.color.selected_background));
            holder.mTicketMonth.setTextColor(holder.mTicketMonth.getContext().getResources().getColor(R.color.selected_text_color));
            holder.mTicketYear.setTextColor(holder.mTicketMonth.getContext().getResources().getColor(R.color.selected_text_color));
        } else {
            holder.itemView.setBackgroundColor(holder.mTicketMonth.getContext().getResources().getColor(R.color.normal_background_month));
            holder.mTicketMonth.setTextColor(holder.mTicketMonth.getContext().getResources().getColor(R.color.normal_text_color));
            holder.mTicketYear.setTextColor(holder.mTicketYear.getContext().getResources().getColor(R.color.normal_text_color));
        }
        holder.itemView.setTag(R.id.ticket_month_title, position);
        holder.itemView.setTag(R.id.month_item_layout, mList.get(position).startPosition);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTicketMonth;
        public TextView mTicketYear;
        private TicketMonthsListener ticketMonthsListener;

        public ViewHolder(View itemView, TicketMonthsListener ticketMonthsListener) {
            super(itemView);
            this.ticketMonthsListener = ticketMonthsListener;
            mTicketMonth = (TextView) itemView.findViewById(R.id.ticket_month_title);
            mTicketYear = (TextView) itemView.findViewById(R.id.ticket_year_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ticketMonthsListener.onMonthClicked((Integer) v.getTag(R.id.ticket_month_title), (Integer) v.getTag(R.id.month_item_layout));
        }
    }

    public void updateSelectedPosition(int position) {
        mSelectedMonthPosition = position;
    }
}
