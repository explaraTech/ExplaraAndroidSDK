package com.explara.explara_ticketing_sdk_ui.tickets.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk.tickets.dto.DiscountResponse;
import com.explara.explara_ticketing_sdk.tickets.dto.MonthDetails;
import com.explara.explara_ticketing_sdk.tickets.dto.TicketDetailsWithTimingsDto;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara.explara_ticketing_sdk_ui.common.TicketingBaseFragment;
import com.explara_core.utils.AppUtility;
import com.explara_core.utils.Constants;
import com.explara_core.utils.WidgetsColorUtil;

/**
 * Created by anudeep on 12/09/15.
 */
public class TicketDetailsByDateFragment extends TicketingBaseFragment implements SessionDetailsAdapter.CalculatePriceOnQuantitySelectListener {
    public static final String SELECTED_MONTH_POSITION = "selected_month";
    private static final String TAG = TicketDetailsByDateFragment.class.getSimpleName();
    private String mEventId;
    private String mCurrency;
    private int mPosition;
    private int mSelectedMonthPosition;
    private RecyclerView mTicketDetailsRecyclerView;
    private SessionDetailsAdapter mSessionDetailsAdapter;
    private LinearLayout mLinearLayout;
    private TextView mErrorText;
    private TextView mNoSessionsText;
    private ProgressBar mProgressbar;
    private String mSelectedSessionDate;


    @Override
    public void onSpinnerItemSelected(String sessionId, int selectedQuantity, String couponCode, int mSelectedSessionTimePosition) {
        getSessionCartDetails(sessionId, selectedQuantity, couponCode, mSelectedSessionTimePosition);
    }

    @Override
    public void updateSelectedSessionText() {
        ((TicketsDetailsWithDatesFragment) getParentFragment()).setTextForSelectedSessions();
    }

    public static TicketDetailsByDateFragment getInstance(int position, String eventId, int mSelectedMonthPos, String currency) {
        TicketDetailsByDateFragment ticketsDetailsWithDatesFragment = new TicketDetailsByDateFragment();
        Bundle bundle = new Bundle(3);
        bundle.putInt(Constants.POSITION, position);
        bundle.putString(Constants.EVENT_ID, eventId);
        bundle.putString(Constants.CURRENCY, currency);
        bundle.putInt(SELECTED_MONTH_POSITION, mSelectedMonthPos);
        ticketsDetailsWithDatesFragment.setArguments(bundle);
        return ticketsDetailsWithDatesFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ticket_details_by_dates, container, false);
        extractArguments();
        initViews(view);
        //storeAllSelectedData();
        return view;
    }

    private void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEventId = args.getString(Constants.EVENT_ID);
            mPosition = args.getInt(Constants.POSITION);
            mCurrency = args.getString(Constants.CURRENCY);
            mSelectedMonthPosition = args.getInt(SELECTED_MONTH_POSITION);
        }
    }

    private void initViews(View view) {
        mLinearLayout = (LinearLayout) view.findViewById(R.id.root_linear_layout);
        mErrorText = (TextView) view.findViewById(R.id.error_img);
        mNoSessionsText = (TextView) view.findViewById(R.id.no_sessions_text);
        mProgressbar = (ProgressBar) view.findViewById(R.id.progressBar);
        //mProgressbar.getIndeterminateDrawable().setColorFilter(getActivity().getResources().getColor(R.color.accentColor), PorterDuff.Mode.SRC_IN);
        WidgetsColorUtil.setProgressBarTintColor(mProgressbar, getResources().getColor(R.color.accentColor));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // for month recycler view
        mTicketDetailsRecyclerView = (RecyclerView) view.findViewById(R.id.ticketdetailswithtimings_recyclerview);
        mTicketDetailsRecyclerView.setLayoutManager(linearLayoutManager);
        mTicketDetailsRecyclerView.setHasFixedSize(true);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getTicketDetailsByDate();
    }

    private void getTicketDetailsByDate() {

        MonthDetails monthDetails = TicketsManager.getInstance().mTicketDatesDto.sessionDates.months.get(mSelectedMonthPosition);
        mSelectedSessionDate = TicketsManager.getInstance().mTicketDatesDto.sessionDates.dates.get(monthDetails.startPosition + mPosition).ticketDate + " " + TicketsManager.getInstance().mTicketDatesDto.sessionDates.dates.get(monthDetails.startPosition + mPosition).year;
        // String in Dto for gobal access


        TicketsManager.getInstance().getTicketDetailsByDate(getActivity(), mEventId, mSelectedSessionDate, mPosition, false, new TicketsManager.SessionDetailsListener() {
            @Override
            public void onSessionDetailsDownloaded(TicketDetailsWithTimingsDto ticketDetailsWithTimingsDto) {
                if (getActivity() != null && ticketDetailsWithTimingsDto != null) {
                    if (ticketDetailsWithTimingsDto.sessionDates != null && ticketDetailsWithTimingsDto.sessionDates.size() > 0) {
                        mLinearLayout.setVisibility(View.VISIBLE);
                        mProgressbar.setVisibility(View.GONE);
                        mErrorText.setVisibility(View.GONE);
                        mNoSessionsText.setVisibility(View.GONE);
                        displayAllSessionDetails(ticketDetailsWithTimingsDto);
                    } else {
                        mNoSessionsText.setVisibility(View.VISIBLE);
                        mProgressbar.setVisibility(View.GONE);
                        mErrorText.setVisibility(View.GONE);
                        mLinearLayout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onSessionDetailsDownloadFailed(VolleyError volleyError) {
                if (getActivity() != null) {
                    mProgressbar.setVisibility(View.GONE);
                    mErrorText.setVisibility(View.VISIBLE);
                    mNoSessionsText.setVisibility(View.GONE);
                    mLinearLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    public void displayAllSessionDetails(TicketDetailsWithTimingsDto ticketDetailsWithTimingsDto) {
        mSessionDetailsAdapter = new SessionDetailsAdapter(ticketDetailsWithTimingsDto.sessionDates, this, mCurrency);
        mTicketDetailsRecyclerView.setAdapter(mSessionDetailsAdapter);
    }

    @Override
    public void refresh() {

    }


    public void getSessionCartDetails(final String sessionId, final int selectedQuantity, final String couponCode, final int mSelectedSessionTimePosition) {
        TicketsManager.getInstance().getSessionCartDetails(getActivity(), sessionId, selectedQuantity, couponCode, new TicketsManager.SessionCartListener() {
            @Override
            public void onSessionCartDownloaded(DiscountResponse discountResponse) {
                if (getActivity() != null && discountResponse != null) {
                    TicketsDetailsWithDatesFragment.mTotalText.setText("Total");
                    if (discountResponse.getStatus().equals(Constants.STATUS_SUCCESS)) {
                        // Showing total value
                        double total = Double.parseDouble(discountResponse.getCart().getGrandTotal());
                        TicketsDetailsWithDatesFragment.mFinalTotalCheckout.setText(AppUtility.getCurrencyFormatedString(getActivity(), discountResponse.getCart().getCurrency(), total + ""));
                        if (mSessionDetailsAdapter.mMaterialDialog != null) {
                            mSessionDetailsAdapter.mMaterialDialog.dismiss();
                        }
                        mSessionDetailsAdapter.isCartChanged = true;
                        mSessionDetailsAdapter.notifyItemChanged(TicketsManager.getInstance().mTicketDetailsWithTimingsMap.get(mSelectedSessionDate).sessionDates.get(mSelectedSessionTimePosition).sessions.size() + 1);
                    } else {
                        Constants.createToastWithMessage(getActivity(), discountResponse.getMessage());
                    }
//                    mSessionDetailsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onSessionCartDownloadFailed(VolleyError volleyError) {
                TicketsDetailsWithDatesFragment.mTotalText.setText("Total");
                if (mSessionDetailsAdapter.mMaterialDialog != null) {
                    mSessionDetailsAdapter.mMaterialDialog.dismiss();
                }
                Constants.createToastWithMessage(getActivity(), "Cart generation failed");
                mSessionDetailsAdapter.mApplyDiscountCodeAction = false;
            }
        });
    }

   /* public void storeAllSelectedData(){
        TicketsDetailsWithDatesFragment ticketsDeFragment = (TicketsDetailsWithDatesFragment) getParentFragment();
        TicketsManager.getInstance().selectedSessionDetailsDto = new SelectedSessionDetailsDto();
        TicketsManager.getInstance().selectedSessionDetailsDto.monthPosition = mSelectedMonthPosition;
        int selectedDatePositon = ticketsDeFragment.mTicketDetailsViewPager.getCurrentItem();
        TicketsManager.getInstance().selectedSessionDetailsDto.datePosition = selectedDatePositon;
        MonthDetails selMonthDetails = TicketsManager.getInstance().mTicketDatesDto.sessionDates.months.get(mSelectedMonthPosition);
        mSelectedSessionDate = TicketsManager.getInstance().mTicketDatesDto.sessionDates.dates.get(selMonthDetails.startPosition + selectedDatePositon).ticketDate + " " + TicketsManager.getInstance().mTicketDatesDto.sessionDates.dates.get(selMonthDetails.startPosition + selectedDatePositon).year;
        TicketsManager.getInstance().selectedSessionDetailsDto.dateSelected = mSelectedSessionDate;
    }*/

    public void reset() {
        if (mSessionDetailsAdapter != null) {
            mSessionDetailsAdapter.reset();
            mSessionDetailsAdapter.notifyDataSetChanged();
        }
    }


}
