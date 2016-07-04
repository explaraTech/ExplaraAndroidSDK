package com.explara.explara_ticketing_sdk_ui.tickets.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk.tickets.dto.CartCalculationObject;
import com.explara.explara_ticketing_sdk.tickets.dto.Discount;
import com.explara.explara_ticketing_sdk.tickets.dto.DiscountResponse;
import com.explara.explara_ticketing_sdk.tickets.dto.Ticket;
import com.explara.explara_ticketing_sdk.tickets.dto.UpdatePaymentStatusResponseDto;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara.explara_ticketing_sdk_ui.attendee.ui.AttendeeFormActivity;
import com.explara.explara_ticketing_sdk_ui.common.BaseFragmentWithBottomSheet;
import com.explara.explara_ticketing_sdk_ui.utils.TicketingUiConstantKeys;
import com.explara_core.utils.AppUtility;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.Log;
import com.explara_core.utils.PreferenceManager;
import com.explara_core.utils.WidgetsColorUtil;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by anudeep on 12/09/15.
 */
public class TicketsDetailsFragment extends BaseFragmentWithBottomSheet implements TicketsListAdapter.CalculatePriceOnQuantitySelectListener {


    private static final String TAG = TicketsDetailsFragment.class.getSimpleName();
    public static final String SHOULD_LAUNCH_PREFFERED_WALLET = "Should_launch_preffered_wallet";
    private ListView mListView;
    private List<Ticket> mTicketList;
    private TicketsListAdapter mTicketsListAdapter;
    private TextView mDescountCounponText;
    private LinearLayout mApplyDiscountLayout;
    private Button mApplyCouponButton;
    private LinearLayout mFareBreakdownLayout;
    private TextView mFareBreakdownText;
    // private TextView mFareDummyMsg;
    public TextView mTotalText;
    boolean couponLayoutVisible = false;
    boolean fareBreakdownVisible = false;
    View mfooter;
    private Button mButtonTotal, mCheckoutButton;
    private TextView mFinalTotalCheckout;
    private String mTotal = "0";
    private double mSubTotal = 0;
    private String mDiscountFees = "";
    private String mProcessingFees = "";
    private String mServiceFees = "";
    public String mDiscountCodeApplied = "";
    private boolean mShouldShowDiscountUI = false;
    private boolean mApplyDiscountCodeAction = false;
    private ProgressBar mProgressbar;
    private TextView mErrorText;
    private TextView mNoTicketsText;
    private TextView mToolbarTitle;
    private String mEventID;
    private String mCurrency;
    private String mPackageId;
    private String mIsAttendeeFormEnabled;
    private TextView mBuyerNameTextView;
    private TextView mBuyerDetailsChangeText;
    private TextView mWalletChangeText;
    private LinearLayout mInlineTabsLayout;
    private RelativeLayout mBuyerDetailsTabLayout;
    private RelativeLayout mPaymentSelectedLayout;
    private View mSeparatorLine;
    public String mBuyerName;
    public String mBuyerEmail;
    public String mBuyerMobile;


    public static TicketsDetailsFragment getInstance(Intent intent) {

        TicketsDetailsFragment ticketsDetailsFragment = new TicketsDetailsFragment();
        if (intent != null && intent.hasExtra(TicketingUiConstantKeys.TicketingKeys.EVENT_ID)) {
            String currency = intent.getStringExtra(TicketingUiConstantKeys.TicketingKeys.CURRENCY);
            String eventId = intent.getStringExtra(TicketingUiConstantKeys.TicketingKeys.EVENT_ID);
            String packageId = intent.getStringExtra(TicketingUiConstantKeys.TicketingKeys.PACKAGE_ID);
            String isAttendeeFormEnabled = intent.getStringExtra(TicketingUiConstantKeys.TicketingKeys.IS_ATTENDEE_FORM_ENABLED);
            Bundle bundle = new Bundle();
            bundle.putString(TicketingUiConstantKeys.TicketingKeys.CURRENCY, currency);
            bundle.putString(TicketingUiConstantKeys.TicketingKeys.EVENT_ID, eventId);
            bundle.putString(TicketingUiConstantKeys.TicketingKeys.PACKAGE_ID, packageId);
            bundle.putString(TicketingUiConstantKeys.TicketingKeys.IS_ATTENDEE_FORM_ENABLED, isAttendeeFormEnabled);
            ticketsDetailsFragment.setArguments(bundle);
        }
        return ticketsDetailsFragment;
    }

    @Override
    public void onSpinnerItemSelected() {
        /*if(mTicketsListAdapter.mMaterialDialog != null){
            mTicketsListAdapter.mMaterialDialog.dismiss();
        }*/

        new AsyncTask<Void, Void, Boolean>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mTotalText.setText("Calculating..");
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                Gson gson = new Gson();
                String oldJsonStr = gson.toJson(TicketsManager.getInstance().cartObj);

                //Log.i("oldJsonStr",oldJsonStr);
                // Prepare cartdetails object for Cart API call
                TicketsManager.getInstance().onQuantityChanged(mEventID, mPackageId, getContext(), mDiscountCodeApplied);

                String newJsonStr = gson.toJson(TicketsManager.getInstance().cartObj);
                //Log.i("newJsonStr",newJsonStr);

//                if (checkCartChanged(oldJsonStr, newJsonStr)) {
//
//                }

                return checkCartChanged(oldJsonStr, newJsonStr);
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                if (aVoid) {
                    getTotalPriceDetails();
                } else {
                    mTotalText.setText("Total");
                }
            }
        }.execute();
    }

   /* @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ticket_detail, container, false);
        mfooter = inflater.inflate(R.layout.ticket_detail_footer, mListView, false);
        extractArguments();
        initViews(view);
        return view;
    } */

    @Override
    protected void addContainerLayout(FrameLayout containerLayout, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.activity_ticket_detail, containerLayout, false);
        containerLayout.addView(view);
        mfooter = inflater.inflate(R.layout.ticket_detail_footer, mListView, false);
        extractArguments();
        initViews(view);
    }

    @Override
    protected void addBottomLayout(FrameLayout slidingLayout, LayoutInflater inflater) {

    }

    private void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEventID = args.getString(TicketingUiConstantKeys.TicketingKeys.EVENT_ID);
            mCurrency = args.getString(TicketingUiConstantKeys.TicketingKeys.CURRENCY);
            mPackageId = args.getString(TicketingUiConstantKeys.TicketingKeys.PACKAGE_ID);
            mIsAttendeeFormEnabled = args.getString(TicketingUiConstantKeys.TicketingKeys.IS_ATTENDEE_FORM_ENABLED);
        }
    }


    private void googleAnalyticsSendScreenName() {
        if (TicketsManager.getInstance().mAnalyticsListener != null) {
            TicketsManager.getInstance().mAnalyticsListener.sendScreenName(getResources().getString(R.string.ticket_detail), getActivity().getApplication(), getContext());
        }
        //AnalyticsHelper.sendScreenName(getResources().getString(R.string.ticket_detail), getActivity().getApplication(), getContext());
    }


    private void initViews(View view) {
        // mLinearLayout = (LinearLayout) view.findViewById(R.id.root_linear_layout);
        mProgressbar = (ProgressBar) view.findViewById(R.id.progressBar);
        //mProgressbar.getIndeterminateDrawable().setColorFilter(getActivity().getResources().getColor(R.color.accentColor), PorterDuff.Mode.SRC_IN);
        WidgetsColorUtil.setProgressBarTintColor(mProgressbar, getResources().getColor(R.color.accentColor));
        mErrorText = (TextView) view.findViewById(R.id.error_img);
        mNoTicketsText = (TextView) view.findViewById(R.id.no_tickets_text);
        //mFareDummyMsg = (TextView) view.findViewById(R.id.dummy_fare_message);
        mTotalText = (TextView) view.findViewById(R.id.totalText);
        mListView = (ListView) view.findViewById(R.id.list);
        mListView.addFooterView(mfooter, null, false);
        googleAnalyticsSendScreenName();
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
                    showBottomSheetFrgament(TicketsDetailsFragment.this, InlineBuyerFormFragment.newInstance(mEventID, ConstantKeys.InlineFormKeys.ticketingPage), getResources().getDimension(R.dimen.buyer_form_height));
                }
            });
            mPaymentSelectedLayout = (RelativeLayout) view.findViewById(R.id.payment_details_tab);
            mPaymentSelectedLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleProceedToPayClick(false);
                }
            });
            mSeparatorLine = view.findViewById(R.id.separator_1);
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
                    showBottomSheetFrgament(TicketsDetailsFragment.this, InlineBuyerFormFragment.newInstance(mEventID, ConstantKeys.InlineFormKeys.ticketingPage), getResources().getDimension(R.dimen.buyer_form_height));
                }
            });

            // Load perferred wallet section with image (Paytm and Citrus)
            handlePaymentOptionSection(view);

        }

        // For the footer and code discount part
        mDescountCounponText = (TextView) view.findViewById(R.id.discount_coupon_text);
        mApplyDiscountLayout = (LinearLayout) view.findViewById(R.id.apply_discount_layout);
        mApplyCouponButton = (Button) view.findViewById(R.id.apply_coupon_button);
        mFareBreakdownLayout = (LinearLayout) view.findViewById(R.id.fare_breakdown_layout);
        mFareBreakdownLayout.setVisibility(View.GONE);
        mFareBreakdownText = (TextView) view.findViewById(R.id.fare_breakdown_text);

        mDescountCounponText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (couponLayoutVisible) {
                    mApplyDiscountLayout.setVisibility(View.GONE);
                    mDescountCounponText.setText("+ Discount Coupon");
                    couponLayoutVisible = false;

                } else {
                    mApplyDiscountLayout.setVisibility(View.VISIBLE);
                    mDescountCounponText.setText("- Discount Coupon");
                    couponLayoutVisible = true;
                }
                mFareBreakdownLayout.setVisibility(View.GONE);
                mFareBreakdownText.setText("+ Fare Breakdown");
            }
        });


        // red tick button
        mApplyCouponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mApplyDiscountLayout.getVisibility() == View.VISIBLE) {

                    EditText couponEditText = (EditText) mApplyDiscountLayout.findViewById(R.id.discount_edit_text);
                    if (couponEditText != null) {
                        String coupon = couponEditText.getText().toString();
                        if (!coupon.trim().isEmpty()) {
                            /*if(mTicketsListAdapter != null){
                                mTicketsListAdapter.showMaterialDialog(getView().getContext());
                            }*/
                            applyCouponDiscount(coupon);
                        }
                    }
                }
            }
        });

        mFareBreakdownText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fareBreakdownVisible) {
                    mFareBreakdownLayout.setVisibility(View.GONE);
                    mFareBreakdownText.setText("+ Fare Breakdown");
                    fareBreakdownVisible = false;
                } else {
                    mFareBreakdownLayout.setVisibility(View.VISIBLE);
                    mFareBreakdownText.setText("- Fare Breakdown");
                    fareBreakdownVisible = true;
                    //updateFareBreakdownLayout();
                }
                mApplyDiscountLayout.setVisibility(View.GONE);
                mDescountCounponText.setText("+ Discount Coupon");
            }
        });

        //mButtonTotal = (Button) view.findViewById(R.id.btn_total);
        //mButtonTotal.setText("TOTAL\n " + " 0");

        mFinalTotalCheckout = (TextView) view.findViewById(R.id.final_price_checkout_text);
        mFinalTotalCheckout.setText(AppUtility.getCurrencyFormatedString(getContext(), mCurrency, 0 + ""));
        mCheckoutButton = (Button) view.findViewById(R.id.checkout_btn);

        mCheckoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleProceedToPayClick(PreferenceManager.getInstance(getContext()).isPreferredWalletOptionSelected());
            }
        });

    }

    private void handleProceedToPayClick(boolean launchDefaultPayment) {
        if (TicketsManager.getInstance().cartObj.tickets != null && !TicketsManager.getInstance().cartObj.tickets.isEmpty()) {
            if ("yes".equals(mIsAttendeeFormEnabled)) {
                // Redirect to attendeeform Page
                Intent intent = new Intent(getActivity(), AttendeeFormActivity.class);
                intent.putExtra(Constants.EVENT_ID, mEventID);
                startActivity(intent);
            } else {
                // if(((InlineBuyerFormFragment)FragmentHelper.getChildFragment(TicketsDetailsFragment.this,mSlidingLayout.getId())).checkAllBuyerDetailsFilled()){
                if (TicketsManager.getInstance().isBuyerDetailsFilled()) {
                    showMaterialDialog();
                    // Generate order no and save attendee form
                    generateOrderNo(launchDefaultPayment, mEventID, TAG);
                } else {
                    mSlidingLayout.removeAllViews();
                    showBottomSheetFrgament(TicketsDetailsFragment.this, InlineBuyerFormFragment.newInstance(mEventID, ConstantKeys.InlineFormKeys.ticketingPage), getResources().getDimension(R.dimen.enquiry_form_height));
                }
            }
        } else {
            if (isAllPayAnyAmountTickets()) {
                Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid amount ", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Please select a ticket ", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mPackageId != null) {
            downloadPackageDetails();
        } else {
            downTicketDetailsForEvents();
        }
        if (Constants.EXPLARA_ONLY) {
            //loadBalanceForPrefferedWallet(TAG);
            //Get wallet balance from
            TicketsManager.getInstance().mTicketListingCallBackListnener.loadPreferredWalletBalanceListener(getContext(), TAG);
            setPreferredWalletBalance();
        }
    }


    private void applyCouponDiscount(String code) {
        mApplyDiscountCodeAction = true;
        mDiscountCodeApplied = code;
        onSpinnerItemSelected();
    }

    private void getTotalPriceDetails() {
        TicketsManager.getInstance().getAllCartCalculation(getActivity(), new TicketsManager.CartCalculationListener() {

            @Override
            public void onCartCalculated(DiscountResponse discountResponse) {
                if (getActivity() != null && discountResponse != null) {
                    mTotalText.setText("Total");
                    if (Constants.STATUS_SUCCESS.equals(discountResponse.getStatus())) {
                        //mFareBreakdownLayout.setVisibility(View.VISIBLE);
                        if (discountResponse.getCart().getDiscount().equals("0")) {
                            if (mApplyDiscountCodeAction)
                                Constants.createToastWithMessage(getActivity(), "Discount coupon not valid");
                            updatePriceCalculation(discountResponse);
                        } else {
                            updatePriceCalculation(discountResponse);
                        }
                    }
                    mApplyDiscountCodeAction = false;
                }
            }

            @Override
            public void onCartCalculationFailed(VolleyError volleyError) {
                if (getActivity() != null) {
                    mTotalText.setText("Total");
                    //Toast.makeText(getActivity(), "Cart details download failed", Toast.LENGTH_SHORT).show();
                    Log.d("Cart Details download:", "Failed");
                    //Again Calling to calculate the cart details
                    getTotalPriceDetails();
                    mApplyDiscountCodeAction = false;
                }
            }
        });
    }

    private void downloadPackageDetails() {
        TicketsManager.getInstance().downloadPackageDetails(getActivity(), mPackageId, new TicketsManager.PackageDetailDownloadListener() {
            @Override
            public void onPackageDetailDownloadListener(com.explara.explara_ticketing_sdk.tickets.dto.TicketDetailResponse ticketDetailResponse) {
                if (getActivity() != null && ticketDetailResponse != null) {
                    showAllCategoriesWithTickets();
                }
            }

            @Override
            public void onPackageDetailDownloadFailed(VolleyError volleyError) {
                if (getActivity() != null) {
                    mProgressbar.setVisibility(View.GONE);
                    mErrorText.setVisibility(View.VISIBLE);
                    // mLinearLayout.setVisibility(View.GONE);
                }
            }
        });
    }


    private void downTicketDetailsForEvents() {
        TicketsManager.getInstance().downloadDetailTickets(getActivity(), mEventID, new TicketsManager.TicketsDetailDownloadListener() {
            @Override
            public void onTicketsDetailDownloadListener(com.explara.explara_ticketing_sdk.tickets.dto.TicketDetailResponse ticketDetailResponse) {
                if (getActivity() != null) {
                    showAllCategoriesWithTickets();
                }
            }

            @Override
            public void onTicketDetailDownloadFailed(VolleyError volleyError) {
                if (getActivity() != null) {
                    mProgressbar.setVisibility(View.GONE);
                    mErrorText.setVisibility(View.VISIBLE);
                    // mLinearLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showAllCategoriesWithTickets() {
        mProgressbar.setVisibility(View.GONE);
        // mLinearLayout.setVisibility(View.VISIBLE);

        mTicketList = TicketsManager.getInstance().getTicketsList();
        if (mTicketList.isEmpty() || mTicketList.size() == 0) {
            mErrorText.setVisibility(View.GONE);
            mNoTicketsText.setVisibility(View.VISIBLE);
        } else {
            if (mTicketList != null) {
                for (Ticket ticket : mTicketList) {
                    if (ticket.getDiscounts() != null) {
                        for (Discount discount : ticket.getDiscounts()) {
                            if (discount.getDiscountType().equals("code")) {
                                mShouldShowDiscountUI = true;
                            }
                        }
                    }
                }
            }
            mListView.setVisibility(View.VISIBLE);
            mTicketsListAdapter = new TicketsListAdapter(getContext(), 0, mTicketList, this);
            mListView.setAdapter(mTicketsListAdapter);
            if (mShouldShowDiscountUI) {
                mDescountCounponText.setVisibility(View.VISIBLE);
            } else {
                mDescountCounponText.setVisibility(View.GONE);
            }
        }
    }

    private void updatePriceCalculation(DiscountResponse response) {
        if (response.getStatus().equals("success")) {
            com.explara.explara_ticketing_sdk.tickets.dto.Cart cart = response.getCart();
            if (!cart.getProcessingFee().equals("0")) {
                mProcessingFees = AppUtility.getCurrencyFormatedString(getContext(), mCurrency, String.valueOf(cart.getProcessingFee()));
            } else {
                mProcessingFees = "";
            }

            if (!cart.getServiceTax().equals("0")) {
                mServiceFees = AppUtility.getCurrencyFormatedString(getContext(), mCurrency, String.valueOf(cart.getServiceTax()));
            } else {
                mServiceFees = "";
            }
            if (!cart.getDiscount().equals("0")) {
                mDiscountFees = AppUtility.getCurrencyFormatedString(getContext(), mCurrency, String.valueOf(cart.getDiscount()));
            } else {
                mDiscountFees = "";
            }
            mTotal = cart.getGrandTotal();//Double.parseDouble(cart.getGrandTotal());
            mFinalTotalCheckout.setText(AppUtility.getCurrencyFormatedString(getContext(), mCurrency, mTotal + ""));
            // double discount = Double.parseDouble(cart.getDiscount());

            updateFareBreakdownLayout();
        }
    }

    private void updateFareBreakdownLayout() {

        // if (mFareBreakdownLayout.getVisibility() == View.VISIBLE) {

        // clear all previous child elements
        mFareBreakdownLayout.removeAllViews();

        CartCalculationObject cartObj = TicketsManager.getInstance().cartObj;
        if (cartObj != null) {
            for (CartCalculationObject.CartTicket cartTicket : cartObj.tickets) {
                LinearLayout fareBreakdownItemLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.fare_breakdown_layout_item, mFareBreakdownLayout, false);
                ViewGroup.LayoutParams params = fareBreakdownItemLayout.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                TextView descTextView = (TextView) fareBreakdownItemLayout.findViewById(R.id.fare_item_breakdown_description);
                TextView descTextValue = (TextView) fareBreakdownItemLayout.findViewById(R.id.fare_item_breakdown_value);

                descTextView.setText(cartTicket.ticketName + " x" + cartTicket.quantity);
                descTextValue.setText(AppUtility.getCurrencyFormatedString(getContext(), mCurrency, String.valueOf(cartTicket.ticketPrice)));

                mFareBreakdownLayout.addView(fareBreakdownItemLayout);
            }
        }


        if (!mDiscountFees.isEmpty() && mDiscountFees.length() > 1) {
            LinearLayout fareBreakdownItemLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.fare_breakdown_layout_item, mFareBreakdownLayout, false);
            ViewGroup.LayoutParams params = fareBreakdownItemLayout.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            TextView descTextView = (TextView) fareBreakdownItemLayout.findViewById(R.id.fare_item_breakdown_description);
            TextView descTextValue = (TextView) fareBreakdownItemLayout.findViewById(R.id.fare_item_breakdown_value);

            descTextView.setText("Discount ");
            descTextValue.setText(mDiscountFees);

            mFareBreakdownLayout.addView(fareBreakdownItemLayout);
        }

        if (!mProcessingFees.isEmpty() && mProcessingFees.length() > 1) {
            LinearLayout fareBreakdownItemLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.fare_breakdown_layout_item, mFareBreakdownLayout, false);
            ViewGroup.LayoutParams params = fareBreakdownItemLayout.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            TextView descTextView = (TextView) fareBreakdownItemLayout.findViewById(R.id.fare_item_breakdown_description);
            TextView descTextValue = (TextView) fareBreakdownItemLayout.findViewById(R.id.fare_item_breakdown_value);

            descTextView.setText("Processing Fees ");
            descTextValue.setText(mProcessingFees);

            mFareBreakdownLayout.addView(fareBreakdownItemLayout);
        }

        if (!mServiceFees.isEmpty() && mServiceFees.length() > 1) {
            LinearLayout fareBreakdownItemLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.fare_breakdown_layout_item, mFareBreakdownLayout, false);
            ViewGroup.LayoutParams params = fareBreakdownItemLayout.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            TextView descTextView = (TextView) fareBreakdownItemLayout.findViewById(R.id.fare_item_breakdown_description);
            TextView descTextValue = (TextView) fareBreakdownItemLayout.findViewById(R.id.fare_item_breakdown_value);

            descTextView.setText("Service Fees ");
            descTextValue.setText(mServiceFees);

            mFareBreakdownLayout.addView(fareBreakdownItemLayout);
        }
        // }

    }


    public boolean checkCartChanged(String oldJsonStr, String newJsonStr) {
        return !oldJsonStr.equals(newJsonStr);
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

    private boolean isAllPayAnyAmountTickets() {
        boolean valid = false;
        if (TicketsManager.getInstance().mTicketDetailResponse != null && TicketsManager.getInstance().mTicketDetailResponse.getTickets() != null && TicketsManager.getInstance().mTicketDetailResponse.getTickets().size() > 0) {
            for (Ticket ticket : TicketsManager.getInstance().mTicketDetailResponse.getTickets()) {
                if (ticket.getPrice() == null) {
                    valid = true;
                } else {
                    valid = false;
                    break;
                }
            }
        }
        return valid;
    }

    public void setPreferredWalletBalance() {
        TextView balanceText = (TextView) getView().findViewById(R.id.payment_mode_name);
        if (TicketsManager.getInstance().mWalletbalance != null) {
            balanceText.setText(String.format("Balance -  %s %s", getContext().getString(R.string.rupee_symbol), TicketsManager.getInstance().mWalletbalance));
        }
    }


    @Override
    public void refresh() {

    }


    // For sdk updatePaymentStatus
    private void updatePaymentStatus(String publisherAccessToken, String orderNo, String status, String referenceNo) {
        TicketsManager.getInstance().updatePaymentStatus(getContext(), publisherAccessToken, orderNo, status, referenceNo, new TicketsManager.UpdatePaymentStatusListener() {
            @Override
            public void onPaymentStatusUpdated(UpdatePaymentStatusResponseDto updatePaymentStatusResponseDto) {
                if (getActivity() != null && updatePaymentStatusResponseDto != null) {

                }
            }

            @Override
            public void onPaymentStatusUpdateFailed(VolleyError volleyError) {
                if (getActivity() != null) {
                    volleyError.printStackTrace();
                }
            }
        });
    }



}
