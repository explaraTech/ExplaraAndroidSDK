package com.explara.explara_ticketing_sdk_ui.tickets.ui;

import android.content.Context;
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

import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk.tickets.dto.MonthDetails;
import com.explara.explara_ticketing_sdk.tickets.dto.PackageDetailsWithTimingsDto;
import com.explara.explara_ticketing_sdk.utils.Callback;
import com.explara.explara_ticketing_sdk.utils.ExplaraError;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara.explara_ticketing_sdk_ui.common.TicketingBaseFragment;
import com.explara_core.utils.Constants;
import com.explara_core.utils.WidgetsColorUtil;

/**
 * Created by anudeep on 12/09/15.
 */
public class PackagesByDateFragment extends TicketingBaseFragment {
    public static final String SELECTED_MONTH_POSITION = "selected_month";
    private static final String TAG = PackagesByDateFragment.class.getSimpleName();
    private String mEventId;
    private int mPosition;
    private int mSelectedMonthPosition;
    private RecyclerView mTicketDetailsRecyclerView;
    private PackagesItemAdapter mPackagesItemAdapter;
    private LinearLayout mLinearLayout;
    private TextView mErrorText;
    private TextView mNoSessionsText;
    private ProgressBar mProgressbar;
    private String mSelectedSessionDate;

    public interface PackageSelectedListener {
        void onPackageSelected(String packageId);
    }

    private PackageSelectedListener mPackageSelectedListener;

    public static PackagesByDateFragment newInstance(int position, String eventId, int mSelectedMonthPos) {
        PackagesByDateFragment packagesByDateFragment = new PackagesByDateFragment();
        Bundle bundle = new Bundle(3);
        bundle.putInt(Constants.POSITION, position);
        bundle.putString(Constants.EVENT_ID, eventId);
        bundle.putInt(SELECTED_MONTH_POSITION, mSelectedMonthPos);
        packagesByDateFragment.setArguments(bundle);
        return packagesByDateFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPackageSelectedListener = (PackageSelectedListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_packages_by_dates, container, false);
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
        TicketsManager.getInstance().getPackageDetailsByDate(getActivity(), mEventId, mSelectedSessionDate, mPosition, new Callback<PackageDetailsWithTimingsDto>() {
            @Override
            public void success(PackageDetailsWithTimingsDto packageDetailsWithTimingsDto) {
                if (getActivity() != null && packageDetailsWithTimingsDto != null) {
                    if (packageDetailsWithTimingsDto.sessions != null && packageDetailsWithTimingsDto.sessions.size() > 0) {
                        mLinearLayout.setVisibility(View.VISIBLE);
                        mProgressbar.setVisibility(View.GONE);
                        mErrorText.setVisibility(View.GONE);
                        mNoSessionsText.setVisibility(View.GONE);
                        displayAllSessionDetails(packageDetailsWithTimingsDto);
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

    public void displayAllSessionDetails(PackageDetailsWithTimingsDto packageDetailsWithTimingsDto) {
        mPackagesItemAdapter = new PackagesItemAdapter(packageDetailsWithTimingsDto.sessions, mPackageSelectedListener, mSelectedSessionDate);
        mTicketDetailsRecyclerView.setAdapter(mPackagesItemAdapter);
    }

    @Override
    public void refresh() {

    }

    public PackagesItemAdapter getAdapter() {
        return mPackagesItemAdapter;
    }

}
