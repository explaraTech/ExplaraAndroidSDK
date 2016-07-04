package com.explara.explara_ticketing_sdk_ui.tickets.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk.tickets.dto.Discount;
import com.explara.explara_ticketing_sdk.tickets.dto.Ticket;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara_core.utils.AppUtility;
import com.explara_core.utils.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by anudeep on 12/09/15.
 */
public class TicketsListAdapter extends ArrayAdapter<Ticket> {

    public static final int HEADER_TYPE = 0;
    public static final int ITEM_TYPE = 1;
    public static final int ITEM_TYPE_COUNT = 2;
    private LayoutInflater mInflater;
    private CalculatePriceOnQuantitySelectListener mCalculatePriceOnQuantitySelectListener;
    private boolean isFirstLoading = true;
    //public MaterialDialog mMaterialDialog;
    //private int mCheck = 0;

    public interface CalculatePriceOnQuantitySelectListener {
        void onSpinnerItemSelected();
    }

    public TicketsListAdapter(Context context, int resource, List<Ticket> ticketList, CalculatePriceOnQuantitySelectListener quantitySelectListener) {
        super(context, resource, ticketList);
        mInflater = LayoutInflater.from(context);
        mCalculatePriceOnQuantitySelectListener = quantitySelectListener;
    }

    public TicketsListAdapter(Context context, int resource) {
        super(context, resource);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        final ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
            Log.d("convertView:", "1");
        } else {
            holder = new ViewHolder();
            if (getItemViewType(position) == HEADER_TYPE) {
                convertView = mInflater.inflate(R.layout.category_item, parent, false);
                holder.mCategoryTitle = (TextView) convertView.findViewById(R.id.category_section_title);
            } else {
                convertView = mInflater.inflate(R.layout.ticket_list_details_item, parent, false);
                holder.mTitle = (TextView) convertView.findViewById(R.id.ticket_title);
                holder.mDescription = (TextView) convertView.findViewById(R.id.ticket_description);
                holder.mDiscountDescription = (TextView) convertView.findViewById(R.id.discount_description);
                holder.mTicketEndInfo = (TextView) convertView.findViewById(R.id.end_time_description);
                holder.mPrice = (TextView) convertView.findViewById(R.id.ticket_price);
                //holder.mTicketVenue = (TextView) convertView.findViewById(R.id.ticket_venue);
                holder.mTicketQuantity = (Spinner) convertView.findViewById(R.id.ticket_quantity);
                holder.mSoldOutText = (TextView) convertView.findViewById(R.id.sold_out_text_view);
                holder.mComingSoonText = (TextView) convertView.findViewById(R.id.coming_soon_text_view);
                holder.mAnyAmount = (EditText) convertView.findViewById(R.id.pay_any_amount_edit_text);
                holder.mAnyAmountLayout = (TextInputLayout) convertView.findViewById(R.id.pay_any_amount_input);
            }
            Log.d("convertView:", "2");
            convertView.setTag(holder);
        }

        // Fetch the category
        if (getItemViewType(position) == HEADER_TYPE) {
            holder.mCategoryTitle.setText(Html.fromHtml(getItem(position).categoryName));
        } else {
            final Ticket ticket = getItem(position);
            if (ticket != null) {
                holder.mTitle.setText(Html.fromHtml(getText(ticket.getName())));
                holder.mDescription.setText("");
                holder.mDescription.setVisibility(View.GONE);
                String description = getText(ticket.getDescription());
                if (!TextUtils.isEmpty(description)) {
                    holder.mDescription.setText(Html.fromHtml(getText(ticket.getDescription())));
                    holder.mDescription.setVisibility(View.VISIBLE);
                }
                if (ticket.getEndDate() != null) {
                    String dateString = "";
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");
                    SimpleDateFormat humanReadableDateStr = new SimpleDateFormat("dd MMM, yyyy");
                    if (ticket.getEndTime() != null) {
                        dateString += ticket.getEndTime() + " " + ticket.getEndDate();
                    } else {
                        dateString += ticket.getEndDate();
                    }

                    try {
                        Date date = formatter.parse(dateString);
                        holder.mTicketEndInfo.setText("Ends on " +
                                AppUtility.convert24HoursTo12Hours(date.getHours()) +
                                (date.getMinutes() > 0 ? (":" + date.getMinutes()) : "") +
                                (date.getHours() >= 12 ? " pm" : " am") + ", " + humanReadableDateStr.format(date));
                        holder.mTicketEndInfo.setVisibility(View.VISIBLE);
                    } catch (Exception e) {

                    }
                }
                holder.mDiscountDescription.setVisibility(View.GONE);
                // Setting discount description if available
                if (ticket.getDiscounts() != null) {
                    for (Discount discount : ticket.getDiscounts()) {
                        if (discount.getDiscountType().equals("flat")) {
                            holder.mDiscountDescription.setText(discount.getDiscountDescription());
                            holder.mDiscountDescription.setVisibility(View.VISIBLE);
                        } else {
                            holder.mDiscountDescription.setVisibility(View.GONE);
                        }
                    }
                }

                if (ticket.getPrice() != null) {
                    holder.mPrice.setText(AppUtility.getCurrencyFormatedString(getContext(), ticket.getCurrency(), ticket.getPrice()));

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                            R.layout.spinner_text,
                            getQuantity(ticket.getMinQuantity(), ticket.getMaxQuantity())) {
                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView text = (TextView) view.findViewById(android.R.id.text1);
                            text.setTextColor(Color.BLACK);
                            return view;
                        }
                    };
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    holder.mTicketQuantity.setAdapter(dataAdapter);

                    final Spinner spinnerQuantity = holder.mTicketQuantity;
                    // Log.d("TList", "List Position: " + position);
                    holder.mTicketQuantity.setOnItemSelectedListener(null);
                    holder.mTicketQuantity.setSelection(getItem(position).userSelectedTicketQuantity);
                    // Hide spinner for ticket which are
                    holder.mTicketQuantity
                            .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> arg0,
                                                           View arg1, int arg2, long arg3) {
                                    if (isFirstLoading) {
                                        if (arg2 > 0) {
                                            // TicketsDetailsFragment.mTotalText.setText("Calculating...");
                                            TicketsManager.getInstance().tickets.get(position).userSelectedTicketQuantity = Integer.valueOf((String) spinnerQuantity.getItemAtPosition(arg2));
                                            // calling Cart Details Interface method
                                            mCalculatePriceOnQuantitySelectListener.onSpinnerItemSelected();
                                            isFirstLoading = false;
                                        }
                                    } else {

                                        TicketsManager.getInstance().tickets.get(position).userSelectedTicketQuantity = Integer.valueOf((String) spinnerQuantity.getItemAtPosition(arg2));
                                        // calling Cart Details Interface method
                                        mCalculatePriceOnQuantitySelectListener.onSpinnerItemSelected();

                                        //TicketsDetailsFragment.mTotalText.setText("Calculating...");
                                        isFirstLoading = false;
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {

                                }
                            });
                } else {
                    holder.mAnyAmount.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            holder.mAnyAmountLayout.setError(null);
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String enterAnyAmount = s.toString();
                            if (!TextUtils.isEmpty(enterAnyAmount)) {
                                if (Integer.parseInt(enterAnyAmount) > 0) {
                                    TicketsManager.getInstance().tickets.get(position).userSelectedTicketQuantity = 1;
                                    TicketsManager.getInstance().tickets.get(position).setPrice(enterAnyAmount);
                                    // calling Cart Details Interface method
                                    mCalculatePriceOnQuantitySelectListener.onSpinnerItemSelected();
                                } else {
                                    holder.mAnyAmountLayout.setError("Enter a vaild amount");
                                    resetTicketPriceNQuantity(position, enterAnyAmount);
                                }
                            } else {
                                resetTicketPriceNQuantity(position, enterAnyAmount);
                            }

                        }
                    });

                }


                // Test if desciption is empty
                if (holder.mDescription.getText() == "") {
                    holder.mDescription.setVisibility(View.GONE);
                }
                if (holder.mTicketEndInfo.getText() == "") {
                    holder.mTicketEndInfo.setVisibility(View.GONE);
                }

                switch (ticket.getTicketStatus().toUpperCase()) {
                    case "PUBLISHED":
                        if (ticket.getPrice() != null) {
                            holder.mTicketQuantity.setVisibility(View.VISIBLE);
                            holder.mPrice.setVisibility(View.VISIBLE);
                            holder.mAnyAmountLayout.setVisibility(View.GONE);
                        } else {
                            holder.mTicketQuantity.setVisibility(View.GONE);
                            holder.mPrice.setVisibility(View.GONE);
                            holder.mAnyAmountLayout.setVisibility(View.VISIBLE);
                        }
                        holder.mSoldOutText.setVisibility(View.GONE);
                        holder.mComingSoonText.setVisibility(View.GONE);
                        break;
                    case "SOLD":
                        holder.mTicketQuantity.setVisibility(View.GONE);
                        holder.mPrice.setVisibility(View.GONE);
                        holder.mAnyAmountLayout.setVisibility(View.GONE);
                        holder.mSoldOutText.setVisibility(View.VISIBLE);
                        holder.mComingSoonText.setVisibility(View.GONE);
                        break;
                    case "UPCOMING":
                        holder.mTicketQuantity.setVisibility(View.GONE);
                        holder.mPrice.setVisibility(View.GONE);
                        holder.mAnyAmountLayout.setVisibility(View.GONE);
                        holder.mSoldOutText.setVisibility(View.GONE);
                        holder.mComingSoonText.setVisibility(View.VISIBLE);
                        break;
                    case "EXPIRED":
                        convertView.setVisibility(View.GONE);
                        break;
                }
            }
        }
        return convertView;
    }

    private void resetTicketPriceNQuantity(int position, String enterAnyAmount) {
        TicketsManager.getInstance().tickets.get(position).userSelectedTicketQuantity = 0;
        TicketsManager.getInstance().tickets.get(position).setPrice(null);
        // calling Cart Details Interface method
        mCalculatePriceOnQuantitySelectListener.onSpinnerItemSelected();
    }

    class ViewHolder {
        private TextView mCategoryTitle;
        private TextView mTitle;
        private TextView mDescription;
        private TextView mDiscountDescription;
        private TextView mPrice;
        //private TextView mTicketVenue;
        private Spinner mTicketQuantity;
        private TextView mSoldOutText;
        private TextView mComingSoonText;
        private TextView mTicketEndInfo;
        private EditText mAnyAmount;
        private TextInputLayout mAnyAmountLayout;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).itemType;

    }

    @Override
    public int getViewTypeCount() {
        return ITEM_TYPE_COUNT;
    }

    private String[] getQuantity(int min, int max) {
        String[] quan = new String[max + 1];
        for (int i = 0; i <= max; i++) {
            quan[i] = i + "";
        }
        return quan;
    }

    private String getText(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return "";
        }
        return msg;
    }


}
