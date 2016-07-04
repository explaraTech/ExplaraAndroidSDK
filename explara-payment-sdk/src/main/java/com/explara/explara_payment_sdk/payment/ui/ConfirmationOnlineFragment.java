package com.explara.explara_payment_sdk.payment.ui;

import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.explara.explara_payment_sdk.R;
import com.explara.explara_payment_sdk.common.PaymentBaseFragment;
import com.explara.explara_payment_sdk.payment.PaymentManager;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara_core.login.ui.LoginActivity;
import com.explara_core.utils.AppUtility;
import com.explara_core.utils.Constants;
import com.explara_core.utils.PreferenceManager;
import com.explara_core.utils.Utility;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;

import io.branch.referral.util.ShareSheetStyle;

/**
 * Created by debasishpanda on 12/09/15.
 */
public class ConfirmationOnlineFragment extends PaymentBaseFragment {

    private static final String TAG = ConfirmationOnlineFragment.class.getSimpleName();
    private String mEventId;
    private Button mEmailTickets;
    private Button mShareButton;
    private Button mMyTickets;
    private TextView mMyTicketTextView;
    private ConfirmationOnlineFragment mConfirmationOnlineFragment;

    private Button mAddToCalenderButton;

    public ConfirmationOnlineFragment() {

    }

    public static ConfirmationOnlineFragment getInstance(Intent intent) {
        ConfirmationOnlineFragment paymentFragment = new ConfirmationOnlineFragment();
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
        View view = inflater.inflate(R.layout.activity_booking_confirmed, container, false);
        extractArguments();
        initViews(view);
        analyticsForEcommerceData();
        googleAnalyticsSendScreenName();
        //analyticsForEcommerceData();
        //googleAnalyticsSendScreenName();
        return view;

    }

    public void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEventId = args.getString(Constants.EVENT_ID);
        }
    }

   /* @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getOrderDetailsFromOrderNo();
    } */

    public void initViews(View view) {
        PaymentManager.getInstance().mPaymentCallBackListener.getEventDetailsFromEventId(mEventId);
        mAddToCalenderButton = (Button) view.findViewById(R.id.add_to_calender);
        mAddToCalenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spanned titleSpanned = Html.fromHtml(PaymentManager.getInstance().mEventDetails.eventTitle);
                String eventTitle = titleSpanned.toString();
                Spanned descSpanned = Html.fromHtml(PaymentManager.getInstance().mEventDetails.description);
                String eventDesc = descSpanned.toString();
                String location = PaymentManager.getInstance().mEventDetails.city;

                Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setType("vnd.android.cursor.item/event");
                calIntent.putExtra(CalendarContract.Events.CALENDAR_COLOR, getResources().getColor(R.color.accentColor));
                calIntent.putExtra(CalendarContract.Events.CALENDAR_DISPLAY_NAME, "Calender For Explara");
                calIntent.putExtra(CalendarContract.Events.TITLE, eventTitle);
                calIntent.putExtra(CalendarContract.Events.DESCRIPTION, eventDesc);
                calIntent.putExtra(CalendarContract.Events.EVENT_COLOR, getResources().getColor(R.color.accentColor));
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                if (location != null) {
                    calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, location);
                }
                startActivity(calIntent);
            }
        });
        mEmailTickets = (Button) view.findViewById(R.id.email_tickets);
        mEmailTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmailTicketDialogFragment emailTicketDialogFragment = EmailTicketDialogFragment.getInstance(mEventId);
                emailTicketDialogFragment.show(getActivity().getSupportFragmentManager(), "Email Ticket");
            }
        });
        mMyTickets = (Button) view.findViewById(R.id.my_ticket);
        mMyTickets.setVisibility(Constants.EXPLARA_ONLY ? View.VISIBLE : View.GONE);
        mMyTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToMyTicketPage();
                //Intent intent = new Intent(v.getContext(),TicketsActivity.class);
                //startActivity(intent);
            }
        });
        mMyTicketTextView = (TextView) view.findViewById(R.id.my_ticket_text);
        if (Constants.EXPLARA_ONLY) {
            setSpanableText();
        } else {
            mMyTicketTextView.setVisibility(View.INVISIBLE);
        }
        mShareButton = (Button) view.findViewById(R.id.share_event);
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.EXPLARA_ONLY) {
                    ShareSheetStyle shareSheetStyle = new ShareSheetStyle(getContext().getApplicationContext(), "You Need to Check This Out!", "Hey! I just found this ah!mazing event on Explara, take a look:");
                    Spanned spanned = Html.fromHtml(PaymentManager.getInstance().mEventDetails.eventTitle);
                    String eventTitle = spanned.toString();
                    Spanned descSpanned = Html.fromHtml(PaymentManager.getInstance().mEventDetails.description);
                    String eventDesc = descSpanned.toString();
                    String url = PaymentManager.getInstance().mEventDetails.largeImage;
                    String desktopUrl = PaymentManager.getInstance().mEventDetails.eventUrl;
                    AppUtility.branchShare(shareSheetStyle, getActivity(), eventTitle, eventDesc, url, mEventId, desktopUrl);
                } else {
                    Intent share = new Intent(android.content.Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_SUBJECT, PaymentManager.getInstance().mEventDetails.eventTitle);
                    share.putExtra(Intent.EXTRA_TEXT, PaymentManager.getInstance().mEventDetails.eventUrl);
                    startActivity(Intent.createChooser(share, "Share this event!"));
                }
            }
        });
    }


    public void setSpanableText() {
        SpannableString spannableString = new SpannableString("My Tickets");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                redirectToMyTicketPage();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        spannableString.setSpan(clickableSpan, 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append("You can access the ticket anytime from ");
        sb.append(spannableString);
        sb.append(" section on Navigation.");

        // TextView textView = (TextView) findViewById(R.id.hello);
        mMyTicketTextView.setText(sb);
        mMyTicketTextView.setTextColor(Color.BLACK);
        mMyTicketTextView.setMovementMethod(LinkMovementMethod.getInstance());
        mMyTicketTextView.setHighlightColor(getActivity().getResources().getColor(R.color.accentColor));

    }

    public void analyticsForEcommerceData() {

        //Double ticketPrice = Double.parseDouble(PaymentManager.getInstance().mOrder.getTicketCost());
        Double grandTotal = Double.parseDouble(TicketsManager.getInstance().getGrandTotal());
        Product product = new Product().setPrice(grandTotal).setId(TicketsManager.getInstance().mOrder.getOrderNo());

        ProductAction productAction = new ProductAction(ProductAction.ACTION_PURCHASE)
                .setTransactionRevenue(grandTotal)
                .setTransactionId(TicketsManager.getInstance().mOrder.getOrderNo());
        if (PaymentManager.getInstance().mAnalyticsListener != null) {
            PaymentManager.getInstance().mAnalyticsListener.sendEcommerce(getString(R.string.transaction), (Application) getActivity().getApplicationContext(), product, productAction, getContext());
        }
        //AnalyticsHelper.sendEcommerce(getString(R.string.transaction), (Application) getActivity().getApplicationContext(), product, productAction, getContext());
        String currency = TicketsManager.getInstance().mDiscountResponse.getCart().getCurrency();
        Utility.sendRevenuetoAdjust(grandTotal, currency);
    }

    private void googleAnalyticsSendScreenName() {
        if (PaymentManager.getInstance().mAnalyticsListener != null) {
            PaymentManager.getInstance().mAnalyticsListener.sendScreenName(getString(R.string.booking_confirmed_screen), getActivity().getApplication(), getContext());
        }
        //AnalyticsHelper.sendScreenName(getString(R.string.booking_confirmed_screen), getActivity().getApplication(), getContext());
    }

    public void redirectToMyTicketPage() {
        if (PreferenceManager.getInstance(getContext()).isLogin()) {
            PaymentManager.getInstance().mAppCallBackListener.launchTicketPage(getContext());
            /*//intent = new Intent(getActivity(), TicketsActivity.class);
            intent = new Intent();
            intent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME, getString(R.string.my_tickets_activity)));*/
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra(Constants.SOURCE, Constants.LOGIN);
            intent.putExtra(Constants.REDIRECT, Constants.CONFIRMATION_PAGE);
            startActivity(intent);
        }

    }

    @Override
    public void refresh() {

    }
}
