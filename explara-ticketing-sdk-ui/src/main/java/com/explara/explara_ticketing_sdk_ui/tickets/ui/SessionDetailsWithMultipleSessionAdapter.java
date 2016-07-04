package com.explara.explara_ticketing_sdk_ui.tickets.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk.tickets.dto.Cart;
import com.explara.explara_ticketing_sdk.tickets.dto.DiscountResponse;
import com.explara.explara_ticketing_sdk.tickets.dto.SelectedSessionDetailsDto;
import com.explara.explara_ticketing_sdk.tickets.dto.SessionsDesc;
import com.explara.explara_ticketing_sdk.tickets.dto.SessionsDetails;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara_core.utils.Constants;
import com.explara_core.utils.Utility;
import com.explara_core.utils.WidgetsColorUtil;

import java.util.Collection;
import java.util.List;

/**
 * Created by anudeep on 31/08/15.
 */
public class SessionDetailsWithMultipleSessionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SessionTimingsAdapter.SessionTimeOnClickListener {
    private List<SessionsDetails> mList;
    private static final int HEADER_TYPE = 0;
    private static final int ITEM_TYPE = 1;
    private static final int FOOTER_TYPE = 2;
    private static final int ITEM_TYPE_COUNT_INCREAMENT = 2;
    private boolean mShouldShowDiscountUI = false;
    private String mCouponCode = "";
    private CalculatePriceOnSelectingSession mCalculatePriceOnSelectingSession;
    public Boolean mApplyDiscountCodeAction = false;
    private String mDiscountFees = "";
    private String mProcessingFees = "";
    private String mServiceFees = "";
    //private double mTotal = 0;
    public Boolean isCartChanged = false;
    public Boolean hideFareBreakDownLayout = false;
    public Boolean isCartCalculationDone = false;
    private Boolean couponLayoutVisible = false;
    private Boolean fareBreakdownVisible = false;
    private String mSelectedSessionId;
    private String mSelectedSessionName;
    private int mSessionSelectedQuantity = 0;
    private int mSelectedSessionTimePosition = 0;
    private int mPreviousSelectedSessionPosition = 999;
    private SessionTimingsAdapter mSessionTimingsAdapter;
    //private String mPreviousGrandTotal = "0";
    public MaterialDialog mMaterialDialog;
    private String mCurrency;
    private String mSelectedSessionDate;


    // private CheckBox mLastChecked = null;
    // private int mLastCheckedPos = 0;


    @Override
    public void onSessionTimeClicked(int position) {
        mSelectedSessionTimePosition = position;
        mSessionTimingsAdapter.updateSelectedPosition(position);
        //this.notifyItemChanged(mList.get(mSelectedSessionTimePosition).sessions.size());
        notifyDataSetChanged();

    }

   /* public void reset() {
        for (SessionsDetails sessionsDetails : mList) {
            for (SessionsDesc sessionsDesc : sessionsDetails.sessions) {
                sessionsDesc.userSelectedTicketQuantity = 0;
            }
        }

    }*/

   /* public interface CalculatePriceOnQuantitySelectListener {
        void onSpinnerItemSelected(String sessionId, int selectedQuantity, String couponCode, int mSelectedSessionTimePosition);
    }*/

    public interface CalculatePriceOnSelectingSession {
        void onSessionSelected(int mSelectedSessionTimePosition);

        void updateSelectedSessionText();
    }

    //private final TicketsDetailsWithDatesFragment.TicketDatesListener ticketDatesListener;

    public SessionDetailsWithMultipleSessionAdapter(List<SessionsDetails> sessionsList, CalculatePriceOnSelectingSession mCalculatePriceOnSelectingSession, String mCurrency, String mSelectedSessionDate) {
        mList = sessionsList;
        this.mCalculatePriceOnSelectingSession = mCalculatePriceOnSelectingSession;
        this.mCurrency = mCurrency;
        this.mSelectedSessionDate = mSelectedSessionDate;
        //this.ticketDatesListener = ticketDatesListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == HEADER_TYPE) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_timing_item, parent, false);
            ViewHolder1 viewHolder = new ViewHolder1(itemView);
            return viewHolder;
        } else if (viewType == FOOTER_TYPE) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_detail_footer, parent, false);
            ViewHolder3 viewHolder = new ViewHolder3(itemView);
            return viewHolder;
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_details_item_multiple_session, parent, false);
            ViewHolder2 viewHolder = new ViewHolder2(itemView);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == HEADER_TYPE) {
            mSessionTimingsAdapter = new SessionTimingsAdapter(mList, this, mSelectedSessionTimePosition);
            ((ViewHolder1) holder).mSessionTimingsRecycler.setAdapter(mSessionTimingsAdapter);
        } else if (getItemViewType(position) == FOOTER_TYPE) {
            displayFooterDetails(holder, position);
        } else {
            displaySessionDetails(holder, position);
        }
    }

    private void displayFooterDetails(final RecyclerView.ViewHolder holder, int position) {
        if (hideFareBreakDownLayout) {
            ((ViewHolder3) holder).mFareBreakdownLayout.removeAllViews();
            ((ViewHolder3) holder).mFareBreakdownLayout.setVisibility(View.GONE);
            ((ViewHolder3) holder).mFareBreakdownText.setText("+ Fare Breakdown");

            ((ViewHolder3) holder).mApplyDiscountLayout.setVisibility(View.GONE);
            ((ViewHolder3) holder).mDescountCounponText.setText("+ Discount Coupon");

        }

       /* if (mList.get(mSelectedSessionTimePosition).isCodeDiscountAvailable.equals("yes")) {
            ((ViewHolder3) holder).mDescountCounponText.setVisibility(View.VISIBLE);
        } else {
            ((ViewHolder3) holder).mDescountCounponText.setVisibility(View.GONE);
            ((ViewHolder3) holder).mApplyDiscountLayout.setVisibility(View.GONE);
        }*/

        if (TicketsManager.getInstance().mTicketDetailsWithTimingsMap.get(mSelectedSessionDate).isCodeAvailable.equals("yes")) {
            ((ViewHolder3) holder).mDescountCounponText.setVisibility(View.VISIBLE);
        } else {
            ((ViewHolder3) holder).mDescountCounponText.setVisibility(View.GONE);
            ((ViewHolder3) holder).mApplyDiscountLayout.setVisibility(View.GONE);
        }


        ((ViewHolder3) holder).mDescountCounponText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (couponLayoutVisible) {
                    ((ViewHolder3) holder).mApplyDiscountLayout.setVisibility(View.GONE);
                    ((ViewHolder3) holder).mDescountCounponText.setText("+ Discount Coupon");
                    couponLayoutVisible = false;

                } else {
                    ((ViewHolder3) holder).mApplyDiscountLayout.setVisibility(View.VISIBLE);
                    ((ViewHolder3) holder).mDescountCounponText.setText("- Discount Coupon");
                    couponLayoutVisible = true;
                }
                ((ViewHolder3) holder).mFareBreakdownLayout.setVisibility(View.GONE);
                ((ViewHolder3) holder).mFareBreakdownText.setText("+ Fare Breakdown");
            }
        });

        ((ViewHolder3) holder).mApplyCouponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isNetworkAvailable(((ViewHolder3) holder).itemView.getContext())) {
                    if (((ViewHolder3) holder).mApplyDiscountLayout.getVisibility() == View.VISIBLE) {
                        EditText couponEditText = (EditText) ((ViewHolder3) holder).mApplyDiscountLayout.findViewById(R.id.discount_edit_text);
                        if (couponEditText != null) {
                            String coupon = couponEditText.getText().toString();
                            if (!coupon.trim().isEmpty()) {
                                mCouponCode = coupon;
                                // Storing data in Dto
                                if (TicketsManager.getInstance().mMultiSessionDto != null) {
                                    TicketsManager.getInstance().mMultiSessionDto.discountCodeUsed = coupon;
                                }
//                                if (TicketsManager.getInstance().selectedSessionDetailsDto != null) {
//                                    TicketsManager.getInstance().selectedSessionDetailsDto.discountUsed = coupon;
//                                }
                                // storing previous grand total value
//                                if (TicketsManager.getInstance().mDiscountResponse != null) {
//                                    mPreviousGrandTotal = TicketsManager.getInstance().mDiscountResponse.getCart().getGrandTotal();
//                                }

                                mApplyDiscountCodeAction = true;
                                if (!TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.isEmpty() && TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.size() > 0) {
                                    ((ViewHolder3) holder).mApplyCouponButton.setVisibility(View.GONE);
                                    ((ViewHolder3) holder).mProgressBar.setVisibility(View.VISIBLE);
                                    showMaterialDialog(((ViewHolder3) holder).itemView.getContext());
                                    mCalculatePriceOnSelectingSession.onSessionSelected(mSelectedSessionTimePosition);
                                    //mCalculatePriceOnQuantitySelectListener.onSpinnerItemSelected(mSelectedSessionId, mSessionSelectedQuantity, mCouponCode, mSelectedSessionTimePosition);
                                } else {
                                    Constants.createToastWithMessage(((ViewHolder3) holder).itemView.getContext(), "Select a session");
                                }
                            } else {
                                Constants.createToastWithMessage(((ViewHolder3) holder).itemView.getContext(), "Discount coupon is empty");
                            }
                        }
                    }
                } else {
                    Toast.makeText(((ViewHolder3) holder).itemView.getContext().getApplicationContext(), ((ViewHolder3) holder).itemView.getContext().getString(R.string.internet_check_msg), Toast.LENGTH_SHORT).show();
                }

            }
        });

        ((ViewHolder3) holder).mFareBreakdownText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fareBreakdownVisible) {
                    ((ViewHolder3) holder).mFareBreakdownLayout.setVisibility(View.GONE);
                    ((ViewHolder3) holder).mFareBreakdownText.setText("+ Fare Breakdown");
                    fareBreakdownVisible = false;
                } else {
                    ((ViewHolder3) holder).mFareBreakdownLayout.setVisibility(View.VISIBLE);
                    ((ViewHolder3) holder).mFareBreakdownText.setText("- Fare Breakdown");
                    fareBreakdownVisible = true;
                    //updateFareBreakdownLayout();
                }
                ((ViewHolder3) holder).mApplyDiscountLayout.setVisibility(View.GONE);
                ((ViewHolder3) holder).mDescountCounponText.setText("+ Discount Coupon");
            }
        });


//        if(isCartChanged && !isCartCalculationDone){
//            populateSessionCartDetails(holder);
//            isCartChanged = false;
//            isCartCalculationDone = true;
//
//        }
        // update all fare breakdown
        if (TicketsManager.getInstance().mDiscountResponse != null && TicketsManager.getInstance().mDiscountResponse.getCart() != null) {
            updatePriceCalculation(((ViewHolder3) holder).itemView.getContext(), (ViewHolder3) holder, TicketsManager.getInstance().mDiscountResponse);
        }

        if (isCartChanged) {
//            if(mMaterialDialog != null && mMaterialDialog.isShowing()){
//                mMaterialDialog.dismiss();
//            }
            ((ViewHolder3) holder).mApplyCouponButton.setVisibility(View.VISIBLE);
            ((ViewHolder3) holder).mProgressBar.setVisibility(View.GONE);
            populateSessionCartDetails(holder);
            //isCartChanged = false;
        }
    }

    private void displaySessionDetails(final RecyclerView.ViewHolder holder, final int position) {
        //final SessionsDesc details = mList.get(0).sessions.get(position-1);
        final SessionsDesc details = mList.get(mSelectedSessionTimePosition).sessions.get(position - 1);
        ((ViewHolder2) holder).mSessionTitle.setText(Html.fromHtml(details.name));
        String session_time = null;
        if (!details.venue.isEmpty() && details.venue != null) {
            session_time = details.startTime + " - " + details.endTime + " (" + details.venue + ") ";
        } else {
            session_time = details.startTime + " - " + details.endTime;
        }
        ((ViewHolder2) holder).mSessionTimings.setText(session_time);
        if (!details.description.isEmpty() && details.description != null) {
            ((ViewHolder2) holder).mSessionDesc.setText(Html.fromHtml(details.description).toString());
        } else {
            ((ViewHolder2) holder).mSessionDesc.setText(null);
        }
        ((ViewHolder2) holder).mSessionPrice.setText(getCurrencyFormatedString(((ViewHolder2) holder).itemView.getContext(), details.currency, String.valueOf(details.price)));


        ((ViewHolder2) holder).mSessionCheck.setChecked(details.isSelected());
        ((ViewHolder2) holder).mSessionCheck.setTag(new Integer(position));

        if (!TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.isEmpty() && TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.size() > 0) {
            Collection<SelectedSessionDetailsDto> allSelectedSessions = TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.values();
            for (SelectedSessionDetailsDto selectedSessionDetailsDto : allSelectedSessions) {
                if (details.id.equals(selectedSessionDetailsDto.sessionId)) {
                    ((ViewHolder2) holder).mSessionCheck.setChecked(true);
                }
            }
        }

        ((ViewHolder2) holder).mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewHolder2) holder).mSessionCheck.performClick();
            }
        });

        ((ViewHolder2) holder).mSessionCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isNetworkAvailable(((ViewHolder2) holder).itemView.getContext())) {
                    //if(TicketsManager.getInstance().mMultiSessionDto.attendeeQuantity > 0){
                    showMaterialDialog(((ViewHolder2) holder).itemView.getContext());
                    onMultipleSessionSelected(v, details);
                    // }else{
                    // ((CheckBox)v).setChecked(false);
                    // Toast.makeText(((ViewHolder2) holder).itemView.getContext().getApplicationContext(), ((ViewHolder2) holder).itemView.getContext().getString(R.string.select_attendee_quantity), Toast.LENGTH_SHORT).show();
                    // }
                } else {
                    //((CheckBox)v).setChecked(false);
                    Toast.makeText(((ViewHolder2) holder).itemView.getContext().getApplicationContext(), ((ViewHolder2) holder).itemView.getContext().getString(R.string.internet_check_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onMultipleSessionSelected(View v, SessionsDesc details) {
        CheckBox cb = (CheckBox) v;
        int clickedPos = ((Integer) cb.getTag()).intValue();
        if (cb.isChecked()) {

            if (TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.containsKey(details.sessionTime)) {
                //SelectedSessionDetailsDto selectedSessionDetailsDto = TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.get(details.sessionTime);
                if (!TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.get(details.sessionTime).sessionsDesc.id.equals(details.id)) {
                    //selectedSessionDetailsDto.checkBox.setSelected(false);
                    TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.get(details.sessionTime).sessionsDesc.setSelected(false);
                    // remove the select session object
                    TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.remove(details.sessionTime);

                }
            }
            // Storing data in Dto
            SelectedSessionDetailsDto selectedSessionDetailsDto = new SelectedSessionDetailsDto();
            selectedSessionDetailsDto.sessionId = details.id;
            selectedSessionDetailsDto.sessionPrice = details.price;
            selectedSessionDetailsDto.sessionName = details.name;
            selectedSessionDetailsDto.selectedQuantity = TicketsManager.getInstance().mMultiSessionDto.attendeeQuantity;
            selectedSessionDetailsDto.sessionsDesc = details;
            TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.put(details.sessionTime, selectedSessionDetailsDto);

            cb.setChecked(true);
            //Toast.makeText(((ViewHolder2) holder).itemView.getContext(),"checked",Toast.LENGTH_SHORT).show();
            // mLastChecked = cb;
            // mLastCheckedPos = clickedPos;
        } else {
            // mLastChecked = null;
            if (TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.containsKey(details.sessionTime)) {
                //SelectedSessionDetailsDto selectedSessionDetailsDto = TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.get(details.sessionTime);
                if (TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.get(details.sessionTime).sessionsDesc.id.equals(details.id)) {
                    //selectedSessionDetailsDto.checkBox.setSelected(false);
                    TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.get(details.sessionTime).sessionsDesc.setSelected(false);
                    // remove the select session object
                    TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.remove(details.sessionTime);
                }
            }
            cb.setChecked(false);

            //Toast.makeText(((ViewHolder2) holder).itemView.getContext(),"unchecked",Toast.LENGTH_SHORT).show();
        }

        mList.get(mSelectedSessionTimePosition).sessions.get(clickedPos - 1).setSelected(cb.isSelected());
        notifyDataSetChanged();
        // calling session cart details interface method
        mCalculatePriceOnSelectingSession.onSessionSelected(mSelectedSessionTimePosition);
        mCalculatePriceOnSelectingSession.updateSelectedSessionText();
    }


    @Override
    public int getItemCount() {
        return mList.get(mSelectedSessionTimePosition).sessions.size() + ITEM_TYPE_COUNT_INCREAMENT;
    }

    @Override
    public int getItemViewType(int position) {
        // return position % 2 * 2;
        if (position == 0) {
            return HEADER_TYPE;
        } else if (position == mList.get(mSelectedSessionTimePosition).sessions.size() + 1) {
            return FOOTER_TYPE;
        } else {
            return ITEM_TYPE;
        }
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        public RecyclerView mSessionTimingsRecycler;

        public ViewHolder1(View itemView) {
            super(itemView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mSessionTimingsRecycler = (RecyclerView) itemView.findViewById(R.id.session_timings_recyclerview);
            mSessionTimingsRecycler.setLayoutManager(linearLayoutManager);
            mSessionTimingsRecycler.setHasFixedSize(true);
        }
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder {
        public TextView mSessionTitle;
        public TextView mSessionTimings;
        public TextView mSessionDesc;
        public TextView mSessionPrice;
        // public Spinner mSessionTicketQuantity;
        public CheckBox mSessionCheck;
        public LinearLayout mLinearLayout;

        public ViewHolder2(View itemView) {
            super(itemView);
            mSessionTitle = (TextView) itemView.findViewById(R.id.session_title);
            mSessionTimings = (TextView) itemView.findViewById(R.id.session_timings);
            mSessionDesc = (TextView) itemView.findViewById(R.id.session_description);
            mSessionPrice = (TextView) itemView.findViewById(R.id.session_price);
            //   mSessionTicketQuantity = (Spinner) itemView.findViewById(R.id.ticket_quantity);
            mSessionCheck = (CheckBox) itemView.findViewById(R.id.chkBoxSession);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.session_detail_layout);
            ;
        }
    }

    public static class ViewHolder3 extends RecyclerView.ViewHolder {
        public TextView mFareBreakdownText;
        public TextView mDescountCounponText;
        public LinearLayout mApplyDiscountLayout;
        public Button mApplyCouponButton;
        public LinearLayout mFareBreakdownLayout;
        public ProgressBar mProgressBar;

        public ViewHolder3(View itemView) {
            super(itemView);
            mFareBreakdownText = (TextView) itemView.findViewById(R.id.fare_breakdown_text);
            mDescountCounponText = (TextView) itemView.findViewById(R.id.discount_coupon_text);
            mApplyDiscountLayout = (LinearLayout) itemView.findViewById(R.id.apply_discount_layout);
            mApplyCouponButton = (Button) itemView.findViewById(R.id.apply_coupon_button);
            mFareBreakdownLayout = (LinearLayout) itemView.findViewById(R.id.fare_breakdown_layout);
            mFareBreakdownLayout.setVisibility(View.GONE);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.discount_progressBar);
            //mProgressBar.getIndeterminateDrawable().setColorFilter(itemView.getResources().getColor(R.color.accentColor), PorterDuff.Mode.SRC_IN);
            WidgetsColorUtil.setProgressBarTintColor(mProgressBar, itemView.getResources().getColor(R.color.accentColor));
        }
    }

    private String getCurrencyFormatedString(Context context, String currencyName, String price) {

        String formattedPrice = "";
        switch (currencyName) {
            case "INR":
                formattedPrice = context.getResources().getString(
                        R.string.rupess, price);
                break;
            case "USD":
                formattedPrice = "$ " + price;
                break;
            default:
                formattedPrice = currencyName + " " + price;

        }

        return formattedPrice;
    }

    private String[] getQuantity(int min, int max) {
        String[] quan = new String[max + 1];
        for (int i = 0; i <= max; i++) {
            quan[i] = i + "";
        }
        return quan;
    }

    public void populateSessionCartDetails(RecyclerView.ViewHolder holder) {
        DiscountResponse discountResponse = TicketsManager.getInstance().mDiscountResponse;
        if (discountResponse != null) {
            Context context = ((ViewHolder3) holder).itemView.getContext();
            ViewHolder3 footerViewHolder = (ViewHolder3) holder;
            if (discountResponse.getStatus().equals(Constants.STATUS_SUCCESS)) {
                if (!discountResponse.getCart().getCodeApplied()) {
                    if (mApplyDiscountCodeAction) {
                        Constants.createToastWithMessage(context, "Discount coupon not valid");
                    }
                    updatePriceCalculation(context, footerViewHolder, discountResponse);
                } else {
                    updatePriceCalculation(context, footerViewHolder, discountResponse);
                }
                EditText couponEditText = (EditText) ((ViewHolder3) holder).mApplyDiscountLayout.findViewById(R.id.discount_edit_text);
                couponEditText.setText(null);
                mApplyDiscountCodeAction = false;
            }
        }

    }

    private void updatePriceCalculation(Context context, ViewHolder3 footerViewHolder, DiscountResponse response) {
        if (response.getStatus().equals("success")) {
            Cart cart = response.getCart();
            if (!cart.getProcessingFee().equals("0")) {
                mProcessingFees = getCurrencyFormatedString(context, response.getCart().getCurrency(), String.valueOf(cart.getProcessingFee()));
            } else {
                mProcessingFees = "";
            }

            if (!cart.getServiceTax().equals("0")) {
                mServiceFees = getCurrencyFormatedString(context, response.getCart().getCurrency(), String.valueOf(cart.getServiceTax()));
            } else {
                mServiceFees = "";
            }
            if (!cart.getDiscount().equals("0")) {
                mDiscountFees = getCurrencyFormatedString(context, response.getCart().getCurrency(), String.valueOf(cart.getDiscount()));
            } else {
                mDiscountFees = "";
            }

            //mTotal = Double.parseDouble(cart.getGrandTotal());
            //TicketsDetailsWithDatesFragment.mFinalTotalCheckout.setText(getCurrencyFormatedString(context, response.getCart().getCurrency(), mTotal + ""));
            // double discount = Double.parseDouble(cart.getDiscount());

            updateFareBreakdownLayout(context, footerViewHolder, response);
        }
    }

    private void updateFareBreakdownLayout(Context context, ViewHolder3 footerViewHolder, DiscountResponse response) {

        // if (mFareBreakdownLayout.getVisibility() == View.VISIBLE) {

        // clear all previous child elements
        footerViewHolder.mFareBreakdownLayout.removeAllViews();

       /* CartCalculationObject cartObj = TicketsManager.getInstance().cartObj;
        if (cartObj != null) {
            for (CartCalculationObject.CartTicket cartTicket : cartObj.tickets) {
                LinearLayout fareBreakdownItemLayout = (LinearLayout) ((Activity)context).getLayoutInflater().inflate(R.layout.fare_breakdown_layout_item, footerViewHolder.mFareBreakdownLayout, false);
                ViewGroup.LayoutParams params = fareBreakdownItemLayout.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                TextView descTextView = (TextView) fareBreakdownItemLayout.findViewById(R.id.fare_item_breakdown_description);
                TextView descTextValue = (TextView) fareBreakdownItemLayout.findViewById(R.id.fare_item_breakdown_value);

                descTextView.setText(cartTicket.ticketName + " x" + cartTicket.quantity);
                descTextValue.setText(getCurrencyFormatedString(context,response.getCart().getCurrency(), String.valueOf(cartTicket.ticketPrice)));

                footerViewHolder.mFareBreakdownLayout.addView(fareBreakdownItemLayout);
            }
        }*/

        if (response.getCart() != null) {
            if (!TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.isEmpty() && TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.size() > 0) {
                Collection<SelectedSessionDetailsDto> allSelectedSessions = TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.values();
                int selectedAttendeeQuantity = TicketsManager.getInstance().mMultiSessionDto.attendeeQuantity;
                for (SelectedSessionDetailsDto selectedSessionDetailsDto : allSelectedSessions) {
                    LinearLayout fareBreakdownItemLayout = (LinearLayout) ((Activity) context).getLayoutInflater().inflate(R.layout.fare_breakdown_layout_item, footerViewHolder.mFareBreakdownLayout, false);
                    ViewGroup.LayoutParams params = fareBreakdownItemLayout.getLayoutParams();
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                    TextView descTextView = (TextView) fareBreakdownItemLayout.findViewById(R.id.fare_item_breakdown_description);
                    TextView descTextValue = (TextView) fareBreakdownItemLayout.findViewById(R.id.fare_item_breakdown_value);

                    descTextView.setText(selectedSessionDetailsDto.sessionName + " x" + selectedAttendeeQuantity);
                    descTextValue.setText(getCurrencyFormatedString(context, response.getCart().getCurrency(), String.valueOf(Integer.parseInt(selectedSessionDetailsDto.sessionsDesc.price) * selectedAttendeeQuantity)));

                    footerViewHolder.mFareBreakdownLayout.addView(fareBreakdownItemLayout);
                }
            }
        }

        if (!mDiscountFees.isEmpty() && mDiscountFees.length() > 1) {
            LinearLayout fareBreakdownItemLayout = (LinearLayout) ((Activity) context).getLayoutInflater().inflate(R.layout.fare_breakdown_layout_item, footerViewHolder.mFareBreakdownLayout, false);
            ViewGroup.LayoutParams params = fareBreakdownItemLayout.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            TextView descTextView = (TextView) fareBreakdownItemLayout.findViewById(R.id.fare_item_breakdown_description);
            TextView descTextValue = (TextView) fareBreakdownItemLayout.findViewById(R.id.fare_item_breakdown_value);

            descTextView.setText("Discount ");
            descTextValue.setText(mDiscountFees);

            footerViewHolder.mFareBreakdownLayout.addView(fareBreakdownItemLayout);
        }

        if (!mProcessingFees.isEmpty() && mProcessingFees.length() > 1) {
            LinearLayout fareBreakdownItemLayout = (LinearLayout) ((Activity) context).getLayoutInflater().inflate(R.layout.fare_breakdown_layout_item, footerViewHolder.mFareBreakdownLayout, false);
            ViewGroup.LayoutParams params = fareBreakdownItemLayout.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            TextView descTextView = (TextView) fareBreakdownItemLayout.findViewById(R.id.fare_item_breakdown_description);
            TextView descTextValue = (TextView) fareBreakdownItemLayout.findViewById(R.id.fare_item_breakdown_value);

            descTextView.setText("Processing Fees ");
            descTextValue.setText(mProcessingFees);

            footerViewHolder.mFareBreakdownLayout.addView(fareBreakdownItemLayout);
        }

        if (!mServiceFees.isEmpty() && mServiceFees.length() > 1) {
            LinearLayout fareBreakdownItemLayout = (LinearLayout) ((Activity) context).getLayoutInflater().inflate(R.layout.fare_breakdown_layout_item, footerViewHolder.mFareBreakdownLayout, false);
            ViewGroup.LayoutParams params = fareBreakdownItemLayout.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            TextView descTextView = (TextView) fareBreakdownItemLayout.findViewById(R.id.fare_item_breakdown_description);
            TextView descTextValue = (TextView) fareBreakdownItemLayout.findViewById(R.id.fare_item_breakdown_value);

            descTextView.setText("Service Fees ");
            descTextValue.setText(mServiceFees);

            footerViewHolder.mFareBreakdownLayout.addView(fareBreakdownItemLayout);
        }
        // }

    }

    public void showMaterialDialog(Context context) {
        if (mMaterialDialog == null) {
            mMaterialDialog = new MaterialDialog.Builder(context)
                    //.title("Explara Login")
                    .content("Calculating..")
                    .cancelable(false)
                            //.iconRes(R.drawable.e_logo)
                    .progress(true, 0)
                    .build();
        }
        mMaterialDialog.show();
    }


    public void calculateCart() {
        mCalculatePriceOnSelectingSession.onSessionSelected(mSelectedSessionTimePosition);
    }


}
