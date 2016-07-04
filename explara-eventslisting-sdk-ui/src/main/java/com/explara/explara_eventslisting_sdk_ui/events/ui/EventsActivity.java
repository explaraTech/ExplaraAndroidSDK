package com.explara.explara_eventslisting_sdk_ui.events.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.MenuItem;

import com.explara.explara_eventslisting_sdk.events.EventsManger;
import com.explara.explara_eventslisting_sdk_ui.R;
import com.explara.explara_eventslisting_sdk_ui.common.BaseFragmentWithBottomSheet;
import com.explara.explara_eventslisting_sdk_ui.utils.EventConstantKeys;
import com.explara_core.common.BaseWithOutNavActivity;
import com.explara_core.database.DataBaseManager;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.FragmentHelper;
import com.explara_core.utils.Log;
import com.explara_core.utils.PreferenceManager;

/**
 * Created by Debasish on 04/09/15.
 */

public class EventsActivity extends BaseWithOutNavActivity implements CollectionEventFragment.FragmentListener {
    private static final String TAG = EventsActivity.class.getSimpleName();
    public String categoryName;
    public String categoryId;
    public String collectionName;
    public String categoryTitle;
    private String mSourcePage;
    private String mCollectionId;
    private String mOrganizerId;
    private int mCollectionPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Save Currency Details in Db only
        DataBaseManager.getInstance(this).saveAllCurrencWithCountriesInDb(this);
    }

    @Override
    protected void addContentFragment() {
        displayBackButton();
        EventsFragment eventsFragment = null;
        // For Sdk
        mOrganizerId = getIntent().getStringExtra(EventConstantKeys.EventKeys.ORGANIZER_ID);
        // For collection
        mSourcePage = getIntent().getStringExtra(EventConstantKeys.EventKeys.SOURCE_PAGE_KEY);
        collectionName = getIntent().getStringExtra(EventConstantKeys.EventKeys.COLLECTION_NAME);
        // Only for notification.Here collection id is passed in intent.
        mCollectionId = getIntent().getExtras().getString(EventConstantKeys.EventKeys.COLLECTION_POSITION, String.valueOf(30));
        //Here selected collectio position is passed in intent.
        mCollectionPosition = getIntent().getExtras().getInt(EventConstantKeys.EventKeys.COLLECTION_POSITION, 0);
        // For category
        categoryId = getIntent().getStringExtra(EventConstantKeys.EventKeys.CATEGORY_ID);
        categoryName = getIntent().getStringExtra(EventConstantKeys.EventKeys.CATEGORY_NAME);

        setToolBarTitle();

        if (!TextUtils.isEmpty(mOrganizerId)) {
            eventsFragment = EventsFragment.newInstanceForSdk(mOrganizerId);
        } else if (!TextUtils.isEmpty(categoryId)) {
            eventsFragment = EventsFragment.newInstance(categoryId);
        } else {
            if (ConstantKeys.SOURCE_PAGE.FROM_NOTIFICATION_SCREEN.equals(mSourcePage)) {
                // Only for notication
                eventsFragment = EventsFragment.newInstance(Integer.parseInt(mCollectionId), getIntent().getStringExtra(ConstantKeys.SOURCE_PAGE.SOURCE_PAGE_KEY));
            } else {
                eventsFragment = EventsFragment.newInstance(mCollectionPosition, getIntent().getStringExtra(ConstantKeys.SOURCE_PAGE.SOURCE_PAGE_KEY));
            }

        }

        FragmentHelper.replaceContentFragment(this, R.id.fragment_container, eventsFragment);
    }

    @Override
    public void onEventClicked(String eventId, int eventPosition, int clickPosition) {
        Log.d(TAG, "EventId" + eventId + "--EventsPos=" + eventPosition + "--ClickPos=" + clickPosition);
        Intent intent = new Intent(this, EventDetailPagerActivity.class);
        intent.putExtra(EventConstantKeys.EventKeys.EVENT_ID, eventId);
        intent.putExtra(EventConstantKeys.EventKeys.EVENT_POSITION, eventPosition);
        intent.putExtra(EventConstantKeys.EventKeys.CATEGORIES_NAME, categoryId);
        if (ConstantKeys.SOURCE_PAGE.FROM_NOTIFICATION_SCREEN.equals(mSourcePage)) {
            intent.putExtra(EventConstantKeys.EventKeys.COLLECTION_POSITION, Integer.valueOf(mCollectionId));
        } else {
            intent.putExtra(EventConstantKeys.EventKeys.COLLECTION_POSITION, getIntent().getExtras().getInt(EventConstantKeys.EventKeys.COLLECTION_POSITION, 0));
        }
        intent.putExtra(ConstantKeys.SOURCE_PAGE.SOURCE_PAGE_KEY, mSourcePage);
        intent.putExtra(EventConstantKeys.EventKeys.EVENT_CLICKED_POSITION, clickPosition);
        startActivity(intent);
    }


    @Override
    public void onEventsTopicsClick(String topicName, String topicId, int sourcePage) {
        if (EventsManger.getInstance().mAnalyticsListener != null) {
            EventsManger.getInstance().mAnalyticsListener.sendScreenName(getString(R.string.event_screen_topic) + " :- " + topicId, getApplication(), getApplicationContext());
        }
        //AnalyticsHelper.sendScreenName(getString(R.string.event_screen_topic) + " :- " + topicId, getApplication(), getApplicationContext());

        Intent intent = new Intent();
        intent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME, "com.explara.android.topics.ui.TopicsActivity"));
        intent.putExtra(EventConstantKeys.EventKeys.TOPIC_NAME, topicName);
        intent.putExtra(EventConstantKeys.EventKeys.TOPIC, topicId);
        intent.putExtra(ConstantKeys.SOURCE_PAGE.FROM_CATEGORIES_SCREEN, sourcePage);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setToolBarTitle() {
        //super.setToolBarTitle();
        if (Constants.EXPLARA_ONLY) {
            if (!TextUtils.isEmpty(collectionName)) {
                getSupportActionBar().setTitle(collectionName);
            } else {
                if (!TextUtils.isEmpty(categoryName))
                    getSupportActionBar().setTitle(categoryName);
            }
        } else {
            getSupportActionBar().setTitle("Events");
        }
    }

    @Override
    public void onBackPressed() {

        String clicked = "clear";
        PreferenceManager.getInstance(getApplicationContext()).setFilterTimeSelected(clicked);
        PreferenceManager.getInstance(getApplicationContext()).setFilterPrice(clicked);
        PreferenceManager.getInstance(getApplicationContext()).setFilterOptionSortBy(clicked);

        Fragment fragment = FragmentHelper.getFragment(this, R.id.fragment_container);
        if (fragment instanceof EventsFragment) {
            EventsFragment eventFragment = ((EventsFragment) fragment);
            BaseFragmentWithBottomSheet baseFragmentWithBottomSheet = eventFragment.getFragment();
            if (baseFragmentWithBottomSheet != null) {
                if (baseFragmentWithBottomSheet.shouldHideOnBackpress()) {
                    baseFragmentWithBottomSheet.hideShowBottomSlideContainer();
                    return;
                }
            }
        }

        if (getIntent().getExtras().getBoolean(ConstantKeys.BundleKeys.FROM_NOTIFICATION, false)) {
            if (getIntent().getExtras().getBoolean(ConstantKeys.SOURCE_PAGE.FROM_NOTIFICATION_ACTIVITY, false)) {
                super.onBackPressed();
            } else {
                launchHome();
            }
        } else {
            super.onBackPressed();
        }
    }
}
