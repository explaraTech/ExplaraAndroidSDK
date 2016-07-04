package com.explara.explara_payment_sdk.payment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.explara.explara_payment_sdk.R;
import com.explara.explara_payment_sdk.common.PaymentBaseFragment;
import com.explara.explara_payment_sdk.payment.PaymentManager;
import com.explara.explara_payment_sdk.payment.dto.TicketInfo;
import com.explara.explara_payment_sdk.utils.GlobalVar;
import com.explara_core.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by debasishpanda on 12/09/15.
 */
public class ConfirmationOfflineFragment extends PaymentBaseFragment {

    private static final String TAG = ConfirmationOfflineFragment.class.getSimpleName();
    private String mEventID;
    private TextView mEventTitle;
    private TextView mEventDate;
    private NetworkImageView mEvnetBigImage;
    private TextView mOrderNoText;
    private TextView mOrderDateText;
    private TextView mNameText;
    private TextView mEmailText;
    private TextView mPhoneText;
    private TextView mInstructionText;
    private TextView mTicketTypeText;
    private Button mShareButton;

    private TextView mTicketPriceTitleText;
    private TextView mTicketPriceText;
    private TextView mTicketCategoryTitleText;
    private TextView mTicketCategoryText;



    public ConfirmationOfflineFragment() {

    }

    public static ConfirmationOfflineFragment getInstance(Intent intent) {
        ConfirmationOfflineFragment paymentFragment = new ConfirmationOfflineFragment();
        String eventId = intent.getStringExtra(Constants.EVENT_ID);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EVENT_ID, eventId);
        paymentFragment.setArguments(bundle);
        return paymentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_booking_pending_layout, container, false);
        extractArguments();
        initViews(view);
        setWizRocket();
        googleAnalyticsSendScreenName();
        return view;
    }

    public void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEventID = args.getString(Constants.EVENT_ID);
        }
    }


    public void initViews(View view) {
        PaymentManager.getInstance().mPaymentCallBackListener.getEventDetailsFromEventId(mEventID);
        mEventTitle = (TextView) view.findViewById(R.id.event_title);
        mEventTitle.setText(PaymentManager.getInstance().mEventDetails.eventTitle);
        mEventDate = (TextView) view.findViewById(R.id.event_date);
        mOrderNoText = (TextView) view.findViewById(R.id.text_order_no);
        mOrderDateText = (TextView) view.findViewById(R.id.text_order_date);
        mNameText = (TextView) view.findViewById(R.id.text_name);
        mEmailText = (TextView) view.findViewById(R.id.text_email);
        mPhoneText = (TextView) view.findViewById(R.id.text_phone);
        mInstructionText = (TextView) view.findViewById(R.id.text_instruction);
        mTicketTypeText = (TextView) view.findViewById(R.id.text_ticket_type);

        mTicketPriceTitleText = (TextView) view.findViewById(R.id.ticket_price_title);
        mTicketPriceText = (TextView) view.findViewById(R.id.text_ticket_price);
        mTicketCategoryTitleText = (TextView) view.findViewById(R.id.ticket_category_title);
        mTicketCategoryText = (TextView) view.findViewById(R.id.text_ticket_category);

        mShareButton = (Button) view.findViewById(R.id.share_event);
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_SUBJECT, PaymentManager.getInstance().mEventDetails.eventTitle);
                share.putExtra(Intent.EXTRA_TEXT, PaymentManager.getInstance().mEventDetails.eventUrl);
                startActivity(Intent.createChooser(share, "Share this event!"));
            }
        });
        setAllValues();
    }

    public void setAllValues() {
        if (PaymentManager.getInstance().mCheckoutOfflineResponse != null) {

            try {
                mOrderNoText.setText(": " + PaymentManager.getInstance().mCheckoutOfflineResponse.getOfflineResponse().getOrder().getOrderNo());
                mOrderDateText.setText(": " + formateDate(PaymentManager.getInstance().mCheckoutOfflineResponse.getOfflineResponse().getOrder().getDatePurchased().getDate()));
                TicketInfo ticketInfo = PaymentManager.getInstance().mCheckoutOfflineResponse.getOfflineResponse().getTicketInfo().get(0);
                mNameText.setText(": " + PaymentManager.getInstance().mCheckoutOfflineResponse.getOfflineResponse().getOrder().getBuyerName());
                mEmailText.setText(": " + PaymentManager.getInstance().mCheckoutOfflineResponse.getOfflineResponse().getOrder().getBuyerEmailId());
                mPhoneText.setText(": " + PaymentManager.getInstance().mCheckoutOfflineResponse.getOfflineResponse().getOrder().getBuyerTelephone());
                mTicketTypeText.setText(": " + ticketInfo.getTicketType().getTypeName());
                mEventDate.setText(formateDate(PaymentManager.getInstance().mCheckoutOfflineResponse.getOfflineResponse().getEvent().getStartDate().getDate().toString()));

                String priceText = null;
                String priceValueStr = PaymentManager.getInstance().mCheckoutOfflineResponse.getOfflineResponse().getOrder().getPaidAmount();
                priceValueStr = (priceValueStr == null ? "0" : priceValueStr);
                switch (ticketInfo.getTicketType().getCurrency()) {
                    case "INR":
                        priceText = this.getResources().getString(R.string.rupess, priceValueStr);
                        break;
                    case "USD":
                        priceText = "$ " + priceValueStr;
                        break;
                    default:
                        priceText = ticketInfo.getTicketType().getCurrency() + " " + priceValueStr;

                }

                if (priceText != null) {
                    mTicketPriceText.setText(": " + priceText);
                } else {
                    mTicketPriceTitleText.setVisibility(View.GONE);
                    mTicketPriceText.setVisibility(View.GONE);
                }

                if (ticketInfo.getCategoryName() != null && ticketInfo.getCategoryName().getName() != null) {
                    mTicketCategoryText.setText(": " + ticketInfo.getCategoryName().getName());
                } else {
                    mTicketCategoryTitleText.setVisibility(View.GONE);
                    mTicketCategoryText.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (GlobalVar.INSTRUCTION != "false") {
                mInstructionText.setText(": " + GlobalVar.INSTRUCTION);
            } else {
                mInstructionText.setText(": " + "No instructions found. Please check your email for more pending ticket details");
            }

        }
    }

    public void setWizRocket() {
//        if (Constants.WIZ_ROCKET_API != null) Constants.WIZ_ROCKET_API.event.push(TAG);
    }

    private String formateDate(String orderDateStr) {
        String inputFormat = "yyyy-MM-dd hh:mm:ss";
        //String outputFormat = "EEE, dd MMM, yyyy";
        String outputFormat = "dd MMM yyyy";
        String finalDateString = "";
        String tag = "eventTime";
        SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);
        String dateString = orderDateStr;
        try {
            Date orderDate = formatter.parse(dateString);
            SimpleDateFormat newFormat = new SimpleDateFormat(outputFormat);
            finalDateString = newFormat.format(orderDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalDateString;
    }

    private void googleAnalyticsSendScreenName() {
        if (PaymentManager.getInstance().mAnalyticsListener != null) {
            PaymentManager.getInstance().mAnalyticsListener.sendScreenName(getString(R.string.booking_pending_screen), getActivity().getApplication(), getContext());
        }
        //AnalyticsHelper.sendScreenName(getString(R.string.booking_pending_screen), getActivity().getApplication(), getContext());
    }

    @Override
    public void refresh() {

    }
}
