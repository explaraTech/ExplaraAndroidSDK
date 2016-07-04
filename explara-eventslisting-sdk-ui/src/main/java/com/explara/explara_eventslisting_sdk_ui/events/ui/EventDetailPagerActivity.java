package com.explara.explara_eventslisting_sdk_ui.events.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.explara.explara_eventslisting_sdk.events.EventsManger;
import com.explara.explara_eventslisting_sdk.events.dto.SaveTopicsResponse;
import com.explara.explara_eventslisting_sdk_ui.R;
import com.explara.explara_eventslisting_sdk_ui.common.BaseFragmentWithBottomSheet;
import com.explara.explara_eventslisting_sdk_ui.utils.EventConstantKeys;
import com.explara_core.common.BaseWithOutNavActivity;
import com.explara_core.communication.dto.BuyTicketDataDto;
import com.explara_core.utils.AppUtility;
import com.explara_core.utils.Constants;
import com.explara_core.utils.FragmentHelper;
import com.explara_core.utils.Log;
import com.explara_core.utils.PreferenceManager;
import com.explara_core.utils.Utility;

import io.branch.referral.util.ShareSheetStyle;

/**
 * Created by anudeep on 15/09/15.
 */
public class EventDetailPagerActivity extends BaseWithOutNavActivity implements EventsDetailPagerFragment.EventsDetailFragmentListener {

    private final static String TAG = EventDetailPagerActivity.class.getSimpleName();
    private String eventId;
    private String mEventName;
    private Menu mMenu;
    protected String mContactNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventId = getIntent().getStringExtra(EventConstantKeys.EventKeys.EVENT_ID);
    }

    @Override
    protected void addContentFragment() {
        removeContainerPadding();
        displayBackButton();
        int eventPosition = getIntent().getIntExtra(EventConstantKeys.EventKeys.EVENT_POSITION, 0);
        String eventId = getIntent().getStringExtra(EventConstantKeys.EventKeys.EVENT_ID);
        String sourcePage = getIntent().getStringExtra(EventConstantKeys.EventKeys.SOURCE_PAGE_KEY);
        int collectionPosition = getIntent().getIntExtra(EventConstantKeys.EventKeys.COLLECTION_POSITION, 0);
        int eventCLickedPosition = getIntent().getIntExtra(EventConstantKeys.EventKeys.EVENT_CLICKED_POSITION, 0);
        FragmentHelper.replaceContentFragment(this, R.id.fragment_container, EventsDetailPagerFragment.getInstance(eventId, eventPosition, collectionPosition,
                getIntent().getStringExtra(EventConstantKeys.EventKeys.CATEGORIES_NAME), eventCLickedPosition, sourcePage));
    }

    @Override
    public void onTopicsListItemClick(String topic) {
        EventsManger.getInstance().mAppCallBackListener.launchTopicsPage(this, topic);
        /*Intent intent = new Intent(EventDetailPagerActivity.this, TopicsActivity.class);
        intent.putExtra(EventConstantKeys.EventKeys.TOPIC, topic);
        startActivity(intent);*/
    }

    @Override
    public void onShowMoreTextClicked(String textDescription, String title) {
        Intent intent = new Intent(EventDetailPagerActivity.this, ShowMoreDetailsActivity.class);
        intent.putExtra(EventConstantKeys.EventKeys.DESCRIPTION, textDescription);
        intent.putExtra(EventConstantKeys.EventKeys.NAME, title);
        startActivity(intent);
    }

    @Override
    public void onInvitesButtonClick(String eventTitle, String eventUrl) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, eventTitle + " " + eventUrl);
        startActivity(share);
    }

    @Override
    public void onGetTicketsButton(String event, String title, String date, String time, String currency) {
        googleAnalyticsSendAnEvent();

        BuyTicketDataDto buyTicketDataDto = new BuyTicketDataDto();
        buyTicketDataDto.eventId = event;
        buyTicketDataDto.eventSessionType = EventsManger.getInstance().eventsDetailDtoMap.get(event).events.getEventSessionType();
        buyTicketDataDto.currency = currency;
        buyTicketDataDto.isAttendeeFormEnabled = EventsManger.getInstance().eventsDetailDtoMap.get(event).events.getIsAttendeeFormEnabled();
        EventsManger.getInstance().mEventListingCallBackListener.onBuyTicketButtonClicked(this, buyTicketDataDto);

        /*//TicketsManager.newInstance().cleanSelectedSessionData();
        Intent intent = AppUtility.generateTicketPageIntent(this, event);
        intent.putExtra(Constants.EVENT_ID, event);
        intent.putExtra(Constants.CURRENCY, currency.equals("$") ? "USD" : (currency.equals("&#8377;") ? "INR" : currency));
        startActivity(intent);*/
    }

    @Override
    public void onGetRespForm(String eventId) {
        EventsManger.getInstance().mAppCallBackListener.launchRsvpFormPage(this, eventId);
        /*Intent intent = new Intent(getBaseContext(), RsvpFormActivity.class);
        intent.putExtra(Constants.EVENT_ID, eventId);
        startActivity(intent);*/
    }

    @Override
    public void isEventFavorited(boolean isfavorite) {
        if (!TextUtils.isEmpty(PreferenceManager.getInstance(this).getAccessToken())) {
            if (mMenu != null) {
                MenuItem favMenuItem = mMenu.findItem(R.id.fav);
                if (isfavorite) {
                    favMenuItem.setIcon(R.drawable.favorited);
                } else {
                    favMenuItem.setIcon(R.drawable.like_outline_256);
                }
                favMenuItem.setVisible(true);
            }
        }
    }

    @Override
    public void onSetEventName(Spanned eventName) {
        mEventName = eventName.toString();
        setToolBarTitle();
        googleAnalyticsSendScreenName(mEventName);
    }

    @Override
    public void enableDialer(String mobileNumber) {
        mContactNumber = mobileNumber;
        MenuItem item = null;
        if (mMenu != null) {
            item = mMenu.findItem(R.id.dialer);
        }
        if (mobileNumber != null && !mobileNumber.trim().isEmpty()) {
            if (item != null) {
                item.setVisible(true);
            }
        } else {
            if (item != null) {
                item.setVisible(false);
            }
        }
    }

    @Override
    public void onEnquiryButtonClicked(String eventId) {
        EventsManger.getInstance().mAppCallBackListener.launchEnquiryPage(this, eventId);
        /*Intent intent = new Intent(this, EnquiryActivity.class);
        intent.putExtra("eventId", eventId);
        startActivity(intent);*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_details_of_event, menu);
        // Hiding tool bar icons for Sdk
        mMenu.findItem(R.id.share).setVisible(Constants.EXPLARA_ONLY ? true : false);
        mMenu.findItem(R.id.fav).setVisible(Constants.EXPLARA_ONLY ? true : false);
        mMenu.findItem(R.id.dialer).setVisible(Constants.EXPLARA_ONLY ? true : false);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mMenu = menu;
        handleFavMenu(TextUtils.isEmpty(PreferenceManager.getInstance(this).getAccessToken()));
        return super.onPrepareOptionsMenu(mMenu);
    }

    private void handleFavMenu(boolean isLoggedIn) {
        mMenu.findItem(R.id.fav).setVisible(false);
        if (isLoggedIn) {
            mMenu.removeItem(R.id.fav);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.share) {
            ShareSheetStyle shareSheetStyle = new ShareSheetStyle(getApplicationContext(), "Check this out!", "Hey! I just found this event on Explara, take a look:   ");
            Spanned spanned = Html.fromHtml(EventsManger.getInstance().eventsDetailDtoMap.get(getEventId()).events.getTitle());
            String title = spanned.toString();
            Spanned descSpanned = Html.fromHtml(EventsManger.getInstance().eventsDetailDtoMap.get(getEventId()).events.getTextDescription());
            String desc = descSpanned.toString();
            String url = EventsManger.getInstance().eventsDetailDtoMap.get(getEventId()).events.getLargeImage();
            String desktopUrl = EventsManger.getInstance().eventsDetailDtoMap.get(getEventId()).events.getUrl();
            AppUtility.branchShare(shareSheetStyle, this, title, desc, url, getEventId(), desktopUrl);
        }

        if (item.getItemId() == R.id.dialer) {
            callOrganizer(mContactNumber);
            return true;
        }

        if (item.getItemId() == R.id.fav) {
            favEvent();
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return true;
    }

    protected void callOrganizer(String mobileNumber) {
        if (mobileNumber != null && !mobileNumber.trim().isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mobileNumber));
            startActivity(intent);
        }
    }

    private void favEvent() {
        setResult(500);
        if (Utility.isNetworkAvailable(getApplicationContext())) {
            EventsManger.getInstance().saveEventFavTopic(this.getApplicationContext(), new EventsManger.SavEventFavListener() {
                @Override
                public void onSaveEventFavSuccess(SaveTopicsResponse saveTopicsResponse) {
                    if (saveTopicsResponse != null) {
                        Log.d(TAG, "" + saveTopicsResponse);
                        if (saveTopicsResponse.status.equals("Event liked successfully")) {
                            isEventFavorited(true);
                        } else if (saveTopicsResponse.status.equals("Event unliked successfully")) {
                            isEventFavorited(false);
                        }
                    }
                }

                @Override
                public void onSavEventFailed() {
                    Toast.makeText(EventDetailPagerActivity.this.getApplicationContext(), "Event fav failed", Toast.LENGTH_LONG).show();

                }
            }, getEventId(), TAG);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.internet_check_msg), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void setToolBarTitle() {
        getSupportActionBar().setTitle(mEventName);
    }


    protected String getEventId() {
        EventsDetailPagerFragment eventsDetailFragment = (EventsDetailPagerFragment) FragmentHelper.getFragment(this, R.id.fragment_container);
        return eventId = eventsDetailFragment.getCurrentFragment().mEventId;
    }

    protected void googleAnalyticsSendScreenName(String eventName) {
        if (EventsManger.getInstance().mAnalyticsListener != null) {
            EventsManger.getInstance().mAnalyticsListener.sendScreenName(getString(R.string.event_detail_screen) + " :- " + eventName, getApplication(), getApplicationContext());
        }
        //AnalyticsHelper.sendScreenName(getString(R.string.event_detail_screen) + " :- " + eventName, getApplication(), getApplicationContext());
    }

    public void googleAnalyticsSendAnEvent() {
        if (EventsManger.getInstance().mAnalyticsListener != null) {
            EventsManger.getInstance().mAnalyticsListener.sendAnEvent(getResources().getString(R.string.event_category_buy_ticket), getResources().getString(R.string.event_action_buy_ticket), getApplication(), getApplicationContext());
        }
        //AnalyticsHelper.sendAnEvent(getResources().getString(R.string.event_category_buy_ticket), getResources().getString(R.string.event_action_buy_ticket), getApplication(), getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = FragmentHelper.getFragment(this, R.id.fragment_container);
        if (fragment instanceof EventsDetailPagerFragment) {
            EventsDetailPagerFragment eventFragment = ((EventsDetailPagerFragment) fragment);
            BaseFragmentWithBottomSheet baseFragmentWithBottomSheet = eventFragment.getCurrentFragment();
            if (baseFragmentWithBottomSheet != null) {
                if (baseFragmentWithBottomSheet.shouldHideOnBackpress()) {
                    baseFragmentWithBottomSheet.hideShowBottomSlideContainer();
                    return;
                }
            }
        }
        super.onBackPressed();
    }

}
