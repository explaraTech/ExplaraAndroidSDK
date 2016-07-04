package com.explara.explara_ticketing_sdk_ui.tickets.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk.tickets.dto.MultiSessionDto;
import com.explara.explara_ticketing_sdk.tickets.dto.TicketDatesDto;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara.explara_ticketing_sdk_ui.attendee.ui.AttendeeFormActivity;
import com.explara.explara_ticketing_sdk_ui.common.BaseFragmentWithBottomSheet;
import com.explara.explara_ticketing_sdk_ui.utils.TicketingUiConstantKeys;
import com.explara_core.utils.AppUtility;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.PreferenceManager;
import com.explara_core.utils.Utility;
import com.explara_core.utils.WidgetsColorUtil;

/**
 * Created by anudeep on 12/09/15.
 */
public class TicketsDetailsWithMultipleSessionFragment extends BaseFragmentWithBottomSheet implements TicketDatesAdapter.TicketDatesListener, TicketMonthsAdapter.TicketMonthsListener {

    private static final String TAG = TicketsDetailsWithMultipleSessionFragment.class.getSimpleName();
    private String mEventId;
    private String mCurrency;
    private String mIsAttendeeFormEnabled;
    private RecyclerView mMonthRecyclerView;
    private RecyclerView mDateRecuclerView;
    public ViewPager mTicketDetailsViewPager;
    private TicketMonthsAdapter mTicketMonthsAdapter;
    private TicketDatesAdapter mTicketDatesAdapter;
    private TicketDatesWithMultipleSessionPagerAdapter mTicketDatesWithMultipleSessionPagerAdapter;
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
    //public MaterialDialog mMaterialDialog;
    public boolean isDialogShown = false;
    private TextView mSelectedSessionTextView;
    private TextView mBuyerNameTextView;
    //private TextView mBuyerEmail;
    private TextView mBuyerDetailsChangeText;
    private TextView mWalletChangeText;
    private LinearLayout mInlineTabsLayout;
    private RelativeLayout mBuyerDetailsTabLayout;
    private RelativeLayout mPaymentSelectedLayout;
    private ImageView mInfoIcon;
    private View mSeparatorLine;
    public String mBuyerName;
    public String mBuyerEmail;
    public String mBuyerMobile;


    public static TicketsDetailsWithMultipleSessionFragment getInstance(Intent intent) {
        TicketsDetailsWithMultipleSessionFragment ticketsDetailsWithMultipleSessionFragment = new TicketsDetailsWithMultipleSessionFragment();
        if (intent != null && intent.hasExtra(Constants.EVENT_ID)) {
            String currency = intent.getStringExtra(Constants.CURRENCY);
            String eventId = intent.getStringExtra(Constants.EVENT_ID);
            String isAttendeeFormEnabled = intent.getStringExtra(TicketingUiConstantKeys.TicketingKeys.IS_ATTENDEE_FORM_ENABLED);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.CURRENCY, currency);
            bundle.putString(Constants.EVENT_ID, eventId);
            bundle.putString(TicketingUiConstantKeys.TicketingKeys.IS_ATTENDEE_FORM_ENABLED, isAttendeeFormEnabled);
            ticketsDetailsWithMultipleSessionFragment.setArguments(bundle);
        }
        return ticketsDetailsWithMultipleSessionFragment;
    }

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ticket_detail_with_dates_multiple_session, container, false);
        extractArguments();
        initViews(view);
        return view;
    }*/

    @Override
    protected void addContainerLayout(FrameLayout containerLayout, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.activity_ticket_detail_with_dates_multiple_session, containerLayout, false);
        containerLayout.addView(view);
        extractArguments();
        initViews(view);
    }

    @Override
    protected void addBottomLayout(FrameLayout slidingLayout, LayoutInflater inflater) {

    }

    private void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEventId = args.getString(Constants.EVENT_ID);
            mCurrency = args.getString(Constants.CURRENCY);
            mIsAttendeeFormEnabled = args.getString(TicketingUiConstantKeys.TicketingKeys.IS_ATTENDEE_FORM_ENABLED);
        }
    }

    private void initViews(View view) {
        mLinearLayout = (LinearLayout) view.findViewById(R.id.root_linear_layout);
        mErrorText = (TextView) view.findViewById(R.id.error_img);
        mNoSessionsText = (TextView) view.findViewById(R.id.no_sessions_text);
        mProgressbar = (ProgressBar) view.findViewById(R.id.progressBar);
        //mProgressbar.getIndeterminateDrawable().setColorFilter(getActivity().getResources().getColor(R.color.accentColor), PorterDuff.Mode.SRC_IN);
        WidgetsColorUtil.setProgressBarTintColor(mProgressbar, getResources().getColor(R.color.accentColor));
        mSelectedSessionTextView = (TextView) view.findViewById(R.id.select_sessions_text);
        mLinearLayoutManager1 = new LinearLayoutManager(getActivity());
        mLinearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        mInfoIcon = (ImageView) view.findViewById(R.id.info_icon_multiple_session);
        mInfoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpScreenDailogForMultipleSessionFragment helpScreenDailogForMultipleSessionFragment = new HelpScreenDailogForMultipleSessionFragment();
                helpScreenDailogForMultipleSessionFragment.show(getActivity().getFragmentManager(), "Help Screen");
            }
        });
        // for month recycler view
        mMonthRecyclerView = (RecyclerView) view.findViewById(R.id.month_recyclerview);
        mMonthRecyclerView.setLayoutManager(mLinearLayoutManager1);
        mMonthRecyclerView.setHasFixedSize(true);
        //mTicketMonthsAdapter = new PackageMonthsAdapter(new ArrayList<String>(),mTicketDatesListener);
        //mMonthRecyclerView.setAdapter(mTicketMonthsAdapter);

        // for date recycler view
        linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mDateRecuclerView = (RecyclerView) view.findViewById(R.id.dates_recyclerview);
        mDateRecuclerView.setLayoutManager(linearLayoutManager2);
        mDateRecuclerView.setHasFixedSize(true);

        //mTicketDatesAdapter = new PackageDatesAdapter(new ArrayList<TicketDate>(),mTicketDatesListener);
        //mDateRecuclerView.setAdapter(mTicketDatesAdapter);

        mTotalText = (TextView) view.findViewById(R.id.totalText);
        mTicketDetailsViewPager = (ViewPager) view.findViewById(R.id.ticketDetails_view_pager);

        // mButtonTotal = (Button) view.findViewById(R.id.btn_total);
        // mButtonTotal.setText("TOTAL\n " + " 0");

        // inline form text
        mInlineTabsLayout = (LinearLayout) view.findViewById(R.id.inline_buyer_payment_details);

        if ("yes".equals(mIsAttendeeFormEnabled)) {
            mInlineTabsLayout.setVisibility(View.GONE);
        } else {
            storePreferenceDataInBuyerDetailsDto();
            if (Constants.EXPLARA_ONLY) {
                // storing data in Transaction Dto
                //PaymentManager.getInstance().storeBuyerDetailsInTransactionDto(getContext());
                TicketsManager.getInstance().mTicketListingCallBackListnener.storeDataInTransactionDtoFromPerference(getContext());
            }

            // Auto fill the buyer form
            mBuyerDetailsTabLayout = (RelativeLayout) view.findViewById(R.id.buyer_details_tab);
            mBuyerDetailsTabLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSlidingLayout.removeAllViews();
                    showBottomSheetFrgament(TicketsDetailsWithMultipleSessionFragment.this, InlineBuyerFormFragment.newInstance(mEventId, ConstantKeys.InlineFormKeys.conferencePage), getResources().getDimension(R.dimen.buyer_form_height));
                }
            });
            mPaymentSelectedLayout = (RelativeLayout) view.findViewById(R.id.payment_details_tab);
            mPaymentSelectedLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleProceedToPayClick(false);
                }
            });
            mSeparatorLine = (View) view.findViewById(R.id.separator_1);
            mBuyerNameTextView = (TextView) view.findViewById(R.id.buyername_text);
            if (!TextUtils.isEmpty(PreferenceManager.getInstance(getContext()).getUserName())) {
                mBuyerNameTextView.setText(PreferenceManager.getInstance(getContext()).getUserName());
            } else {
                mBuyerDetailsTabLayout.setVisibility(View.GONE);
                mSeparatorLine.setVisibility(View.GONE);
            }
//            mBuyerEmail = (TextView)view.findViewById(R.id.buyeremail_text);
//            if(!TextUtils.isEmpty(PreferenceManager.getInstance(getContext()).getEmail())){
//                mBuyerEmail.setText(PreferenceManager.getInstance(getContext()).getEmail());
//            }

            mWalletChangeText = (TextView) view.findViewById(R.id.payment_mode_change_text);
            mWalletChangeText.setVisibility(TicketsManager.getInstance().isBuyerDetailsFilled() ? View.VISIBLE : View.GONE);
            mWalletChangeText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleProceedToPayClick(false);
                }
            });

            mBuyerDetailsChangeText = (TextView) view.findViewById(R.id.buyerdetails_change_text);
            mBuyerDetailsChangeText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSlidingLayout.removeAllViews();
                    showBottomSheetFrgament(TicketsDetailsWithMultipleSessionFragment.this, InlineBuyerFormFragment.newInstance(mEventId, ConstantKeys.InlineFormKeys.conferencePage), getResources().getDimension(R.dimen.buyer_form_height));
                }
            });


            // Load perferred walet section with image (Paytm and Citrus)
            handlePaymentOptionSection(view);
        }

        mFinalTotalCheckout = (TextView) view.findViewById(R.id.final_price_checkout_text);
        mFinalTotalCheckout.setText(AppUtility.getCurrencyFormatedString(getActivity(), mCurrency, 0.0 + ""));

        mCheckoutButton = (Button) view.findViewById(R.id.checkout_btn);
        mCheckoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleProceedToPayClick(PreferenceManager.getInstance(getContext()).isPreferredWalletOptionSelected());
            }
        });

        // Setting of attendee quantity spinner
        mAttendeeQuantity = (Spinner) view.findViewById(R.id.attendee_quantity);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_text,
                AppUtility.getQuantity(1, 10)) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                return view;
            }
        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAttendeeQuantity.setAdapter(dataAdapter);
        //mAttendeeQuantity.setOnItemSelectedListener(null);
        mAttendeeQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Utility.isNetworkAvailable(getActivity())) {
                    MultiSessionDto multiSessionDto = new MultiSessionDto();
                    multiSessionDto.attendeeQuantity = position;
                    TicketsManager.getInstance().mMultiSessionDto = multiSessionDto;
                    // Check if any session is selected or not
                    if (!TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.isEmpty() && TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.size() > 0) {
                        showMaterialDialog();
                        if (mTicketDatesWithMultipleSessionPagerAdapter != null) {
                            TicketDetailsByDateMultipleSessionFragment ticketDetailsByDateMultipleSessionFragment = mTicketDatesWithMultipleSessionPagerAdapter.getFragment(mTicketDetailsViewPager.getCurrentItem());
                            SessionDetailsWithMultipleSessionAdapter sessionDetailsWithMultipleSessionAdapter = ticketDetailsByDateMultipleSessionFragment.getAdapter();
                            sessionDetailsWithMultipleSessionAdapter.calculateCart();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.internet_check_msg), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void handleProceedToPayClick(boolean launchDefaultPayment) {
        if (Utility.isNetworkAvailable(getActivity().getApplicationContext())) {
            if (!TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.isEmpty() && TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.size() > 0) {
                if (TicketsManager.getInstance().mMultiSessionDto.attendeeQuantity > 0) {
                    if ("yes".equals(mIsAttendeeFormEnabled)) {
                        //Intent intent = new Intent(getActivity(), BuyerDetailActivity.class);
                        Intent intent = new Intent(getActivity(), AttendeeFormActivity.class);
                        intent.putExtra(Constants.EVENT_ID, mEventId);
                        startActivity(intent);
                    } else {
                        // if(((InlineBuyerFormFragment)FragmentHelper.getChildFragment(TicketsDetailsFragment.this,mSlidingLayout.getId())).checkAllBuyerDetailsFilled()){
                        if (TicketsManager.getInstance().isBuyerDetailsFilled()) {
                            showMaterialDialog();
                            // Generate order no and save attendee form
                            generateOrderNoForMultiSession(launchDefaultPayment, mEventId, TAG);
                        } else {
                            mSlidingLayout.removeAllViews();
                            showBottomSheetFrgament(TicketsDetailsWithMultipleSessionFragment.this, InlineBuyerFormFragment.newInstance(mEventId, ConstantKeys.InlineFormKeys.conferencePage), getResources().getDimension(R.dimen.enquiry_form_height));
                        }
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Please select number of attendee ", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Please select a session", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.internet_check_msg), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getAllDatesAndTimes();
        if (Constants.EXPLARA_ONLY) {
            //loadBalanceForPrefferedWallet(TAG);

            //Get wallet balance from
            TicketsManager.getInstance().mTicketListingCallBackListnener.loadPreferredWalletBalanceListener(getContext(), TAG);
            setPreferredWalletBalance();
        }
        if (isFirstTime()) {
            helpScreenView();

        }
    }


    private boolean isFirstTime() {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("FirstTime", false);
        if (!ranBefore) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("FirstTime", true);
            editor.commit();
        }
        return !ranBefore;
    }


    public void helpScreenView() {

        HelpScreenDailogForMultipleSessionFragment helpScreenDailogForMultipleSessionFragment = new HelpScreenDailogForMultipleSessionFragment();
        helpScreenDailogForMultipleSessionFragment.show(getActivity().getFragmentManager(), "Help Screen");
    }



    /*private void loadBalanceForPrefferedWallet() {
        PreferenceManager preferenceManager = PreferenceManager.getInstance(getContext());
        if (preferenceManager.isPreferredWalletOptionSelected()) {
            int preferredWalletOption = preferenceManager.getPreferredWalletOption();
            if (preferredWalletOption == PreferenceManager.PAY_TM_PREFERRED_WALLET) {

                PaymentManager.getInstance().getPayTmUserDetails(getContext(), preferenceManager.getPayTmAccessToken(), new PaymentManager.FetchPaytmProfileListener() {
                    @Override
                    public void onFetchPaytmUserProfile(PayTmUserProfile payTmUserProfile, TextView textView) {
                        if (getActivity() != null)
                            textView.setText(String.format("Balance -  %s %s", getContext().getString(R.string.rupee_symbol), payTmUserProfile.WALLETBALANCE));
                    }

                    @Override
                    public void onFetchpaytmUserProfileFailed(VolleyError volleyError) {

                    }
                }, TAG, (TextView) getView().findViewById(R.id.payment_mode_name));
            } else if (preferredWalletOption == PreferenceManager.CITRUS_PREFFRED_WALLET) {
                CitrusClient.getInstance(getContext()).getBalance(new Callback<Amount>() {
                    @Override
                    public void success(Amount amount) {
                        if (getActivity() != null)
                            setBalance(getView(), String.format("Balance -  %s %s", getContext().getString(R.string.rupee_symbol), amount.getValueAsFormattedDouble("#.00")));

                    }

                    @Override
                    public void error(CitrusError citrusError) {
                        Logger.d(TAG + " get Balance failure " + citrusError.getMessage());
                    }
                });
            }
        }

    }*/

    private void getAllDatesAndTimes() {
        TicketsManager.getInstance().getAllDatesAndTimes(getActivity(), mEventId, true, new TicketsManager.TicketsDatesListener() {
            @Override
            public void onTicketDatesCalculated(TicketDatesDto ticketDatesDto) {
                if (getActivity() != null && ticketDatesDto != null) {
                    if (ticketDatesDto.sessionDates.dates != null && ticketDatesDto.sessionDates.dates.size() > 0) {
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
            public void onTicketDatesCalculationFailed(VolleyError volleyError) {
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
        mTicketMonthsAdapter = new TicketMonthsAdapter(ticketDatesDto.sessionDates.months, this);
        mMonthRecyclerView.setAdapter(mTicketMonthsAdapter);


        mTicketDatesAdapter = new TicketDatesAdapter(ticketDatesDto.sessionDates.dates.subList(ticketDatesDto.sessionDates.months.get(mSelectedMonthPosition).startPosition, ticketDatesDto.sessionDates.months.get(mSelectedMonthPosition).endPosition + 1), this);
        mDateRecuclerView.setAdapter(mTicketDatesAdapter);

      /*  mDateRecuclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //super.onScrolled(recyclerView, dx, dy);
                if(dy > 0) //check for scroll down
                {
                    //int visibleItemCount = mLinearLayoutManager1.getChildCount();
                    //int totalItemCount = mLinearLayoutManager1.getItemCount();
                    int lastVisibleItemPosition = mLinearLayoutManager1.findLastCompletelyVisibleItemPosition();
                    if(lastVisibleItemPosition)

                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        }); */

        mTicketDatesWithMultipleSessionPagerAdapter = new TicketDatesWithMultipleSessionPagerAdapter(getChildFragmentManager(), mEventId, mSelectedMonthPosition, mCurrency);
        mTicketDetailsViewPager.setAdapter(mTicketDatesWithMultipleSessionPagerAdapter);
        mTicketDetailsViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateDateRecyclerView(position);

//                mTotalText.setText("Total");
//                mFinalTotalCheckout.setText(getCurrencyFormatedString(getActivity(), mCurrency, 0.0 + ""));
//                TicketsManager.getInstance().mDiscountResponse = null;
//                TicketsManager.getInstance().selectedSessionDetailsDto = null;
//                mTicketDatesWithMultipleSessionPagerAdapter.getFragment(position).reset();
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
        //((LinearLayoutManager)mDateRecuclerView.getLayoutManager()).scrollToPosition(position);
        mDateRecuclerView.scrollToPosition(position);
    }


    @Override
    public void onMonthClicked(int position, int startPosition) {
        mSelectedMonthPosition = position;

        TicketDatesDto ticketDatesDto = TicketsManager.getInstance().mTicketDatesDto;
        mTicketDatesAdapter = new TicketDatesAdapter(ticketDatesDto.sessionDates.dates.subList(ticketDatesDto.sessionDates.months.get(mSelectedMonthPosition).startPosition, ticketDatesDto.sessionDates.months.get(mSelectedMonthPosition).endPosition + 1), this);
        mDateRecuclerView.setAdapter(mTicketDatesAdapter);

        mTicketDatesWithMultipleSessionPagerAdapter = new TicketDatesWithMultipleSessionPagerAdapter(getChildFragmentManager(), mEventId, mSelectedMonthPosition, mCurrency);
        mTicketDetailsViewPager.setAdapter(mTicketDatesWithMultipleSessionPagerAdapter);

        updateMonthRecyclerView(position);
    }

    public void updateMonthRecyclerView(int position) {
        mTicketMonthsAdapter.updateSelectedPosition(position);
        mTicketMonthsAdapter.notifyDataSetChanged();
        mMonthRecyclerView.scrollToPosition(position);
    }


    public void populateBuyerData(String buyerName, String buyerEmail, String buyerPhone) {
        mBuyerName = buyerName;
        mBuyerEmail = buyerEmail;
        mBuyerMobile = buyerPhone;

        if (mBuyerNameTextView != null) {
            mBuyerDetailsTabLayout.setVisibility(View.VISIBLE);
            mSeparatorLine.setVisibility(View.VISIBLE);
            mBuyerNameTextView.setText(buyerName);
            if (PreferenceManager.getInstance(getContext()).isPreferredWalletOptionSelected()) {
                mWalletChangeText.setVisibility(View.VISIBLE);
            }
        } else {
            mBuyerDetailsTabLayout.setVisibility(View.GONE);
            mSeparatorLine.setVisibility(View.GONE);
            mWalletChangeText.setVisibility(View.GONE);
        }
    }

    public void setTextForSelectedSessions() {
        if (!TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.isEmpty() && TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.size() > 0) {
            int selectedSessions = TicketsManager.getInstance().mSelectedSessionDetailsDtoMap.size();
            if (selectedSessions == 1) {
                mSelectedSessionTextView.setText(selectedSessions + " session selected.");
            } else {
                mSelectedSessionTextView.setText(selectedSessions + " sessions selected.");
            }
        } else {
            mSelectedSessionTextView.setText("Multiple sessions can be selected.");
        }
    }

    public void setPreferredWalletBalance() {
        TextView balanceText = (TextView) getView().findViewById(R.id.payment_mode_name);
        if (TicketsManager.getInstance().mWalletbalance != null) {
            balanceText.setText(String.format("Balance -  %s %s", getContext().getString(R.string.rupee_symbol), TicketsManager.getInstance().mWalletbalance));
        }
    }

}
