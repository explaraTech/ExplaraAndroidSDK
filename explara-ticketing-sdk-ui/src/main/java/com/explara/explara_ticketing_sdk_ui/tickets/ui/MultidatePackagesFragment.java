package com.explara.explara_ticketing_sdk_ui.tickets.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk.tickets.dto.TicketDatesDto;
import com.explara.explara_ticketing_sdk.utils.Callback;
import com.explara.explara_ticketing_sdk.utils.ExplaraError;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara.explara_ticketing_sdk_ui.common.TicketingBaseFragment;
import com.explara_core.utils.Constants;
import com.explara_core.utils.WidgetsColorUtil;

/**
 * Created by Debasish on 12/09/15.
 */
public class MultidatePackagesFragment extends TicketingBaseFragment implements PackageDatesAdapter.TicketDatesListener, PackageMonthsAdapter.TicketMonthsListener {

    private static final String TAG = MultidatePackagesFragment.class.getSimpleName();
    private String mEventId;
    private String mCurrency;
    private RecyclerView mMonthRecyclerView;
    private RecyclerView mDateRecuclerView;
    public ViewPager mTicketDetailsViewPager;
    private PackageMonthsAdapter mTicketMonthsAdapter;
    private PackageDatesAdapter mTicketDatesAdapter;
    private MultidatePagerAdapter mTicketDatesWithMultipleSessionPagerAdapter;
    private Button mButtonTotal;
    private Button mCheckoutButton;
    public static TextView mTotalText;
    public static TextView mFinalTotalCheckout;
    public LinearLayout mLinearLayout;
    private TextView mErrorText;
    private TextView mNoSessionsText;
    private ProgressBar mProgressbar;
    private Double defaultPrice = 0.0;
    private Spinner mAttendeeQuantity;
    LinearLayoutManager mLinearLayoutManager1;
    LinearLayoutManager linearLayoutManager2;
    private int mSelectedMonthPosition = 0;


    public static MultidatePackagesFragment newInstance(String eventId, String currency) {
        MultidatePackagesFragment multidatePackagesFragment = new MultidatePackagesFragment();
        if (eventId != null) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.CURRENCY, currency);
            bundle.putString(Constants.EVENT_ID, eventId);
            multidatePackagesFragment.setArguments(bundle);
        }
        return multidatePackagesFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_multidate_session, container, false);
        extractArguments();
        initViews(view);
        return view;
    }

    private void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEventId = args.getString(Constants.EVENT_ID);
            mCurrency = args.getString(Constants.CURRENCY);
        }
    }

    private void initViews(View view) {
        mLinearLayout = (LinearLayout) view.findViewById(R.id.root_linear_layout);
        mErrorText = (TextView) view.findViewById(R.id.error_img);
        mNoSessionsText = (TextView) view.findViewById(R.id.no_sessions_text);
        mProgressbar = (ProgressBar) view.findViewById(R.id.progressBar);
        WidgetsColorUtil.setProgressBarTintColor(mProgressbar, getResources().getColor(R.color.accentColor));

        // for month recycler view
        mLinearLayoutManager1 = new LinearLayoutManager(getActivity());
        mLinearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMonthRecyclerView = (RecyclerView) view.findViewById(R.id.month_recyclerview);
        mMonthRecyclerView.setLayoutManager(mLinearLayoutManager1);
        mMonthRecyclerView.setHasFixedSize(true);

        // for date recycler view
        linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mDateRecuclerView = (RecyclerView) view.findViewById(R.id.dates_recyclerview);
        mDateRecuclerView.setLayoutManager(linearLayoutManager2);
        mDateRecuclerView.setHasFixedSize(true);
        mTicketDetailsViewPager = (ViewPager) view.findViewById(R.id.ticketDetails_view_pager);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getAllDatesAndTimes();
    }

    private void getAllDatesAndTimes() {
        TicketsManager.getInstance().getAllPackagesDatesAndTimes(getActivity(), mEventId, new Callback<TicketDatesDto>() {
            @Override
            public void success(TicketDatesDto ticketDatesDto) {
                if (getActivity() != null && ticketDatesDto != null) {
                    if ("success".equals(ticketDatesDto.status) && ticketDatesDto.sessionDates.dates != null && ticketDatesDto.sessionDates.dates.size() > 0) {
                        mLinearLayout.setVisibility(View.VISIBLE);
                        mProgressbar.setVisibility(View.GONE);
                        mErrorText.setVisibility(View.GONE);
                        mNoSessionsText.setVisibility(View.GONE);
                        displayAllDatesWithTickets(ticketDatesDto);
                    } else {
                        mNoSessionsText.setVisibility(View.VISIBLE);
                        mProgressbar.setVisibility(View.GONE);
                        mErrorText.setVisibility(View.GONE);
                        mLinearLayout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void error(ExplaraError error) {
                if (getActivity() != null) {
                    mProgressbar.setVisibility(View.GONE);
                    mErrorText.setVisibility(View.VISIBLE);
                    mNoSessionsText.setVisibility(View.GONE);
                    mLinearLayout.setVisibility(View.GONE);
                }
            }
        });
    }


    public void displayAllDatesWithTickets(TicketDatesDto ticketDatesDto) {
        mTicketMonthsAdapter = new PackageMonthsAdapter(ticketDatesDto.sessionDates.months, this);
        mMonthRecyclerView.setAdapter(mTicketMonthsAdapter);

        mTicketDatesAdapter = new PackageDatesAdapter(ticketDatesDto.sessionDates.dates.subList(ticketDatesDto.sessionDates.months.get(mSelectedMonthPosition).startPosition, ticketDatesDto.sessionDates.months.get(mSelectedMonthPosition).endPosition + 1), this);
        mDateRecuclerView.setAdapter(mTicketDatesAdapter);

        mTicketDatesWithMultipleSessionPagerAdapter = new MultidatePagerAdapter(getChildFragmentManager(), mEventId, mSelectedMonthPosition);
        mTicketDetailsViewPager.setAdapter(mTicketDatesWithMultipleSessionPagerAdapter);
        mTicketDetailsViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateDateRecyclerView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void refresh() {

    }


    @Override
    public void onDateClicked(int position) {
        updateDateRecyclerView(position);
        mTicketDetailsViewPager.setCurrentItem(position);
    }

    public void updateDateRecyclerView(int position) {
        mTicketDatesAdapter.updateSelectedPosition(position);
        mTicketDatesAdapter.notifyDataSetChanged();
        mDateRecuclerView.scrollToPosition(position);
    }


    @Override
    public void onMonthClicked(int position, int startPosition) {
        mSelectedMonthPosition = position;
        TicketDatesDto ticketDatesDto = TicketsManager.getInstance().mTicketDatesDto;

        mTicketDatesAdapter = new PackageDatesAdapter(ticketDatesDto.sessionDates.dates.subList(ticketDatesDto.sessionDates.months.get(mSelectedMonthPosition).startPosition, ticketDatesDto.sessionDates.months.get(mSelectedMonthPosition).endPosition + 1), this);
        mDateRecuclerView.setAdapter(mTicketDatesAdapter);

        mTicketDatesWithMultipleSessionPagerAdapter = new MultidatePagerAdapter(getChildFragmentManager(), mEventId, mSelectedMonthPosition);
        mTicketDetailsViewPager.setAdapter(mTicketDatesWithMultipleSessionPagerAdapter);

        updateMonthRecyclerView(position);
    }

    public void updateMonthRecyclerView(int position) {
        mTicketMonthsAdapter.updateSelectedPosition(position);
        mTicketMonthsAdapter.notifyDataSetChanged();
        mMonthRecyclerView.scrollToPosition(position);
    }

}
