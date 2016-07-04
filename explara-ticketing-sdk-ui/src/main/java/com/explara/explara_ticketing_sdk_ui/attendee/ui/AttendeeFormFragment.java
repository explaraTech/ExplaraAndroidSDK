package com.explara.explara_ticketing_sdk_ui.attendee.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.explara.explara_ticketing_sdk.attendee.AttendeeManager;
import com.explara.explara_ticketing_sdk.attendee.dto.AttendeeDetailsResponseDto;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk.tickets.dto.Order;
import com.explara.explara_ticketing_sdk.utils.Callback;
import com.explara.explara_ticketing_sdk.utils.ExplaraError;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara.explara_ticketing_sdk_ui.common.TicketingBaseFragment;
import com.explara_core.utils.Constants;
import com.explara_core.utils.Log;
import com.explara_core.utils.PreferenceManager;
import com.explara_core.utils.Utility;
import com.explara_core.utils.WidgetsColorUtil;

import java.util.HashMap;

/**
 * Created by anudeep on 08/01/16.
 */
public class AttendeeFormFragment extends TicketingBaseFragment implements TabLayout.OnTabSelectedListener {

    private static final String TAG = AttendeeFormFragment.class.getSimpleName();
    private String mEventId;
    private ProgressBar mProgressBar;
    public ViewPager mEventsViewPager;
    private TabLayout mTabLayout;
    public AttendeePagerAdapter mAttendeePagerAdapter;
    private TextView mErrorText;
    private MaterialDialog mMaterialDialog;
    private View mSeparator;
    public Button mCheckoutBtn;
    private RelativeLayout mPaymentSelectedLayout;
    public TextView mWalletChangeText;
    private int mErrorInAttendeeNo = 0;
    private boolean mIsSaveAttendeeCalled = false;

    public AttendeeFormFragment() {

    }

    public static AttendeeFormFragment newInstance(Intent intent) {
        AttendeeFormFragment attendeeFormFragment = new AttendeeFormFragment();
        String eventId = intent.getStringExtra(Constants.EVENT_ID);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EVENT_ID, eventId);
        //args.putInt(ATTENDEE_POSITION, position);
        attendeeFormFragment.setArguments(bundle);
        return attendeeFormFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.multiple_attendee_fragment, container, false);
        initViews(view);
        extractArguments();
        return view;
    }

    private void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEventId = args.getString(Constants.EVENT_ID);
        }
    }

    private void initViews(View view) {
        mEventsViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) view.findViewById(R.id.events_tabs);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        //mProgressBar.getIndeterminateDrawable().setColorFilter(getActivity().getResources().getColor(R.color.accentColor), PorterDuff.Mode.SRC_IN);
        WidgetsColorUtil.setProgressBarTintColor(mProgressBar, getResources().getColor(R.color.accentColor));
        mErrorText = (TextView) view.findViewById(R.id.error_msg);
        mSeparator = (View) view.findViewById(R.id.separator_2);
        mPaymentSelectedLayout = (RelativeLayout) view.findViewById(R.id.payment_details_tab);
        mWalletChangeText = (TextView) view.findViewById(R.id.payment_mode_change_text);
        mWalletChangeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToPaymentSelectionActivity(false, mEventId, TAG);
            }
        });
        mCheckoutBtn = (Button) view.findViewById(R.id.proceed_btn);
        mCheckoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isNetworkAvailable(getContext())) {
                    saveAttendeeDetailsData();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.internet_check_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveAttendeeDetailsData() {
        showMaterialDialog();
        if (isAllAttendeeFormFilled()) {
            if (!mIsSaveAttendeeCalled) {
                //Get data from the firstAttedeeform and Storing name,emailId,Phone no in Preference for both login and without login
                getBuyerDataFromFirstAttendeeForm();
                saveAttendeeFormData(mEventId, TAG);
                if (Constants.STATUS_SUCCESS.equals(AttendeeManager.getInstance().attendeeDetailsResponseDto.status)) {
                    mWalletChangeText.setVisibility(View.VISIBLE);
                    mIsSaveAttendeeCalled = true;
                } else {
                    mWalletChangeText.setVisibility(View.GONE);
                }
            } else {
                navigateToPaymentSelectionActivity(PreferenceManager.getInstance(getContext()).isPreferredWalletOptionSelected(), mEventId, TAG);
            }
        } else {
            dismissMaterialDialog();
            mWalletChangeText.setVisibility(View.GONE);
            mEventsViewPager.setCurrentItem(mErrorInAttendeeNo);
        }
    }

    private void getBuyerDataFromFirstAttendeeForm() {
        AttendeeFieldItemsFragment attendeeFieldItemsFragment = (AttendeeFieldItemsFragment) mAttendeePagerAdapter.getFragment(0);
        AttendeeCustomLayout mAttendeeBaseLayout = attendeeFieldItemsFragment.mAttendeeBaseLayout;
        HashMap<String, String> buyerDataFromFirstAttendeeForm = mAttendeeBaseLayout.getBuyerDataFromFirstAttendeeForm();
        storeBuyerDetailsInPreference(buyerDataFromFirstAttendeeForm.get("Name"), buyerDataFromFirstAttendeeForm.get("Email"), buyerDataFromFirstAttendeeForm.get("Mobile"));
    }

    private boolean isAllAttendeeFormFilled() {
        boolean proceedToCheckout = true;
        int attendeCount = AttendeeManager.getInstance().attendeeDetailsResponseDto.attendeeform.size();
        int i = 0;
        for (; i < attendeCount; i++) {
            AttendeeFieldItemsFragment attendeeFieldItemsFragment = (AttendeeFieldItemsFragment) mAttendeePagerAdapter.getFragment(i);
            AttendeeCustomLayout attendeeBaseLayout = attendeeFieldItemsFragment.mAttendeeBaseLayout;
            attendeeFieldItemsFragment.mAttendeeBaseLayout.dismissKeyboard();
            if (!attendeeBaseLayout.checkFormDataFilled()) {
                proceedToCheckout = false;
                mErrorInAttendeeNo = i;
                break;
            } else {
                attendeeBaseLayout.prepareAttendeeFormData(i + 1);
            }
        }
        return proceedToCheckout;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TicketsManager.getInstance().mTicketListingCallBackListnener.getEventSessionTypeFromEventId(mEventId);
        if ("theater".equals(TicketsManager.getInstance().mEventSessionType)) {
            generateOrderNoForSession();
        } else if ("conference".equals(TicketsManager.getInstance().mEventSessionType)) {
            generateOrderNoForMultiSession();
        } else {
            generateOrderNo();
        }
    }

    public void generateOrderNo() {
        TicketsManager.getInstance().generateOrderNo(getActivity().getApplicationContext(), mEventId, new TicketsManager.GenerateOrderListener() {
            @Override
            public void onOrderGenerated(Order order) {
                if (getActivity() != null && order != null) {
                    Log.d("Order No: ", order.getOrderNo());
                    downlodAttendeeForm();
                }
            }

            @Override
            public void onOrderGeneratedFailed() {
                if (getActivity() != null) {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity().getApplicationContext(), "Order generation failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    }

    public void generateOrderNoForMultiSession() {
        TicketsManager.getInstance().generateOrderNoForMultiSession(getActivity().getApplicationContext(), mEventId, new TicketsManager.GenerateMultipleSessionOrderListener() {
            @Override
            public void onMultipleSessionOrderGenerated(Order order) {
                if (getActivity() != null && order != null) {
                    Log.d("Order No: ", order.getOrderNo());
                    downlodAttendeeForm();
                }
            }

            @Override
            public void onMultipleSessionOrderGenerateFailed() {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Order generation failed", Toast.LENGTH_SHORT).show();
            }
        }, TAG);

    }

    public void generateOrderNoForSession() {
        TicketsManager.getInstance().generateOrderNoForSession(getActivity().getApplicationContext(), mEventId, new TicketsManager.GenerateSessionOrderListener() {
            @Override
            public void onSessionOrderGenerated(Order order) {
                if (getActivity() != null && order != null) {
                    Log.d("Order No: ", order.getOrderNo());
                    downlodAttendeeForm();
                }
            }

            @Override
            public void onSessionOrderGenerateFailed() {
                if (getActivity() != null) {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity().getApplicationContext(), "Order generation failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void downlodAttendeeForm() {
        mEventsViewPager.setVisibility(View.GONE);
        mAttendeePagerAdapter = null;
        mEventsViewPager.setAdapter(mAttendeePagerAdapter);

        AttendeeManager.getInstance().downloadAttendeeFormData(getActivity().getApplicationContext(), new Callback<AttendeeDetailsResponseDto>() {
            @Override
            public void success(AttendeeDetailsResponseDto attendeeDetailsResponseDto) {
                if (getActivity() != null && attendeeDetailsResponseDto != null) {
                    mProgressBar.setVisibility(View.GONE);
                    mErrorText.setVisibility(View.GONE);
                    if (attendeeDetailsResponseDto.status.equals(Constants.STATUS_SUCCESS)) {
                        mCheckoutBtn.setVisibility(View.VISIBLE);
                        if (TicketsManager.getInstance().mTotal != 0) {
                            handlePaymentOptionSection(getView());
                            //loadBalanceForPrefferedWallet(TAG);
                            TicketsManager.getInstance().mTicketListingCallBackListnener.loadPreferredWalletBalanceListener(getContext(), TAG);
                        }
                        mAttendeePagerAdapter = new AttendeePagerAdapter(getChildFragmentManager(), attendeeDetailsResponseDto);
                        mEventsViewPager.setVisibility(View.VISIBLE);
                        mEventsViewPager.setOffscreenPageLimit(10);
                        mEventsViewPager.setAdapter(mAttendeePagerAdapter);
                        mTabLayout.setupWithViewPager(mEventsViewPager);
                        mEventsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
                        mTabLayout.setOnTabSelectedListener(AttendeeFormFragment.this);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), attendeeDetailsResponseDto.message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void error(ExplaraError error) {
                if (getActivity() != null) {
                    mProgressBar.setVisibility(View.GONE);
                    mErrorText.setVisibility(View.VISIBLE);
                    error.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "Fetching attendee form fields failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    }

    @Override
    public void refresh() {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mEventsViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
