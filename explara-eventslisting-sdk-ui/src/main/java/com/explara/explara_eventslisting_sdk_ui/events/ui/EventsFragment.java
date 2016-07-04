package com.explara.explara_eventslisting_sdk_ui.events.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.explara.explara_eventslisting_sdk.events.EventsManger;
import com.explara.explara_eventslisting_sdk.events.dto.CategoriesResponseDto;
import com.explara.explara_eventslisting_sdk.events.dto.CollectionEventsDto;
import com.explara.explara_eventslisting_sdk.events.dto.OrganizerEventsDto;
import com.explara.explara_eventslisting_sdk_ui.R;
import com.explara.explara_eventslisting_sdk_ui.common.BaseFragmentWithBottomSheet;
import com.explara.explara_eventslisting_sdk_ui.common.EventBaseFragment;
import com.explara.explara_eventslisting_sdk_ui.utils.EventConstantKeys;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.Log;
import com.explara_core.utils.PreferenceManager;


/**
 * Created by Debasish on 04/09/15.
 */

public class EventsFragment extends EventBaseFragment implements TabLayout.OnTabSelectedListener {
    private static final String TAG = EventsFragment.class.getSimpleName();
    private ViewPager mEventsViewPager;
    private TabLayout mTabLayout;
    public EventsPagerAdapter mEventsPagerAdapter;
    private int mCollectionPosition;
    private String mCategoryName = null;
    private String mOrganzierId = null;
    private TextView mNoEvents;
    private String mSourcePage;
    private ProgressBar mProgressBar;
    CollectionEventFragment.FragmentListener mFragmentListener;
    CollectionEventFragment.CityNameClickListener mCityNameClickListener;

    // For collection
    public static EventsFragment newInstance(int collectionId, String sourcePage) {
        EventsFragment eventsFragment = new EventsFragment();
        Bundle bundle = new Bundle(1);
        bundle.putInt(EventConstantKeys.EventKeys.COLLECTION_POSITION, collectionId);
        bundle.putString(ConstantKeys.SOURCE_PAGE.SOURCE_PAGE_KEY, sourcePage);
        eventsFragment.setArguments(bundle);
        return eventsFragment;
    }

    // For category
    public static EventsFragment newInstance(String categoryName) {
        EventsFragment eventsFragment = new EventsFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString(EventConstantKeys.EventKeys.COLLECTION_CATEGORY_NAME, categoryName);
        eventsFragment.setArguments(bundle);
        return eventsFragment;
    }

    public static EventsFragment newInstanceForSdk(String organizerId) {
        EventsFragment eventsFragment = new EventsFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString(EventConstantKeys.EventKeys.ORGANIZER_ID, organizerId);
        eventsFragment.setArguments(bundle);
        return eventsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.events_layout_fragment, container, false);
        extractArguments();
        intiViews(view);
        return view;
    }


    private void extractArguments() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCollectionPosition = bundle.getInt(EventConstantKeys.EventKeys.COLLECTION_POSITION);
            mCategoryName = bundle.getString(EventConstantKeys.EventKeys.COLLECTION_CATEGORY_NAME);
            mSourcePage = bundle.getString(ConstantKeys.SOURCE_PAGE.SOURCE_PAGE_KEY);
            mOrganzierId = bundle.getString(EventConstantKeys.EventKeys.ORGANIZER_ID);
        }
    }

    private void intiViews(View view) {
        mEventsViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) view.findViewById(R.id.events_tabs);
        //mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        if (Constants.EXPLARA_ONLY) {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
        mNoEvents = (TextView) view.findViewById(R.id.error_msg_city);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Constants.EXPLARA_ONLY) {
            if (TextUtils.isEmpty(mCategoryName)) {
                downloadCollectionEvents();
            } else {
                downloadCategories(PreferenceManager.getInstance(getContext()).getSelectedCity());
            }
        } else {
            downloadOrganizerEvents(mOrganzierId);
        }
        //downloadEventsWithTopics();
        //Log.d("count",""+CollectionsManager.newInstance().getCollectionEvent(mCollectionPosition).getCollectionEvents());
    }

    public void downloadOrganizerEvents(String accountId) {
        EventsManger.getInstance().downloadOrganizerEvents(getContext(), accountId, new EventsManger.DownloadOrganizerEventsListener() {
            @Override
            public void onOrganizerEventsDownloaded(OrganizerEventsDto organizerEventsDto) {
                if (getActivity() != null && organizerEventsDto != null) {
                    loadOrganizerEvents();
                }
            }

            @Override
            public void onOrganizerEventsDownloadFailed(VolleyError volleyError) {
                if (getActivity() != null) {
                    mEventsViewPager.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.GONE);
                    volleyError.printStackTrace();
                }
            }
        }, TAG);
    }

    public void downloadCollectionEvents() {

        String collectionId = null;

        if (ConstantKeys.SOURCE_PAGE.FROM_NOTIFICATION_SCREEN.equals(mSourcePage)) {
            // Here mCollectionPosition is collection Id
            collectionId = Integer.toString(mCollectionPosition);
        } else {
            EventsManger.getInstance().mAppCallBackListener.getSelectedCollectionIdFromPosition(mCollectionPosition);
            collectionId = EventsManger.getInstance().mSelectedSessionId;
            // Here mCollectionPosition is actual collection position
            //collectionId = CollectionsManager.newInstance().homeScreenDto.collections.get(mCollectionPosition).id;
        }

        EventsManger.getInstance().downloadCollectionEvents(getContext(), collectionId, new EventsManger.CollectionEventDownloadListener() {
            @Override
            public void onCollectionEventDownload(CollectionEventsDto collectionEventsDto) {
                loadEventsFromCollectionList();
            }

            @Override
            public void onCollectionEventDownloadFailed(VolleyError volleyError) {
                mEventsViewPager.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);

                if (volleyError.getMessage().contains(getResources().getString(R.string.volley_unknownhost_exception))) {
                    handleNetworkConnection();
                } else {
                    handleErrors();
                }

                // mNoEvents.setVisibility(View.VISIBLE);
            }
        }, TAG);


    }

    private void handleErrors() {
        mProgressBar.setVisibility(View.GONE);
        mEventsViewPager.setVisibility(View.GONE);
        mNoEvents.setVisibility(View.VISIBLE);
    }

    public void downloadCategories(final String cityName) {
        mEventsViewPager.setVisibility(View.GONE);
        mEventsPagerAdapter = null;
        mEventsViewPager.setAdapter(mEventsPagerAdapter);
        getView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        EventsManger.getInstance().downloadEventsBasedOnCategory(getActivity().getApplicationContext(), mCategoryName, cityName, new EventsManger.EventsDownloadListener() {
            @Override
            public void onEventsDownloaded(CategoriesResponseDto collectionsDto) {

                if (getActivity() != null && EventsManger.getInstance().categoriesResponseDtoMap.get(mCategoryName) != null
                        && EventsManger.getInstance().categoriesResponseDtoMap.get(mCategoryName).getSubCategoryEvents() != null
                        && !EventsManger.getInstance().categoriesResponseDtoMap.get(mCategoryName).getSubCategoryEvents().isEmpty()) {
                    getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
                    mNoEvents.setVisibility(View.GONE);
                    mEventsPagerAdapter = new EventsPagerAdapter(getChildFragmentManager(), mCategoryName, cityName, getContext().getApplicationContext());
                    mEventsViewPager.setVisibility(View.VISIBLE);
                    mEventsViewPager.setAdapter(mEventsPagerAdapter);
                    mTabLayout.setupWithViewPager(mEventsViewPager);
                    mEventsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
                    mTabLayout.setOnTabSelectedListener(EventsFragment.this);
                } else {
                    displayErrorMessage();
                }

            }

            @Override
            public void onEventsDownloadFailed(VolleyError volleyError) {
                if (getActivity() != null) {
                    Log.d(TAG, "" + volleyError);
                    if (volleyError.getMessage().contains(getResources().getString(R.string.volley_unknownhost_exception))) {
                        handleNetworkConnection();
                    }
                }
            }
        }, TAG);
    }

    private void displayErrorMessage() {
        if (getActivity() != null) {
            mNoEvents.setVisibility(View.VISIBLE);
            mEventsViewPager.setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
        }
    }

    private void loadOrganizerEvents() {
        getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
        mEventsViewPager.setVisibility(View.VISIBLE);
        mEventsPagerAdapter = new EventsPagerAdapter(getChildFragmentManager());
        mEventsViewPager.setAdapter(mEventsPagerAdapter);
        mTabLayout.setupWithViewPager(mEventsViewPager);
        mEventsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(this);
    }

    private void loadEventsFromCollectionList() {
        if (getActivity() != null) {
            getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
            mEventsViewPager.setVisibility(View.VISIBLE);
            mEventsPagerAdapter = new EventsPagerAdapter(getChildFragmentManager(), mCollectionPosition, mSourcePage, getContext().getApplicationContext());
            mEventsViewPager.setAdapter(mEventsPagerAdapter);
            mTabLayout.setupWithViewPager(mEventsViewPager);
            mEventsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
            mTabLayout.setOnTabSelectedListener(this);
        }
    }

    @Override
    public void refresh() {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mEventsViewPager.setCurrentItem(tab.getPosition());
        if (!TextUtils.isEmpty(mCategoryName)) {
            if (EventsManger.getInstance().categoriesResponseDtoMap.get(mCategoryName) != null && EventsManger.getInstance().categoriesResponseDtoMap.get(mCategoryName).getSubCategory() != null) {
                String category = EventsManger.getInstance().categoriesResponseDtoMap.get(mCategoryName).getSubCategory()[tab.getPosition()];
                googleAnalyticsSendScreenName(category);
            }


        }
        clearFilter();
    }


    @Override
    public void onTabUnselected(TabLayout.Tab tab) {


    }


    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void googleAnalyticsSendScreenName(String category) {
        if (EventsManger.getInstance().mAnalyticsListener != null) {
            EventsManger.getInstance().mAnalyticsListener.sendScreenName(getActivity().getApplicationContext().getString(R.string.category_name) + "" + category, getActivity().getApplication(), getContext());
        }
        //AnalyticsHelper.sendScreenName(getActivity().getApplicationContext().getString(R.string.category_name) + "" + category, getActivity().getApplication(), getContext());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mEventsViewPager = null;
        mEventsPagerAdapter = null;
        mTabLayout = null;

        if (getActivity() != null) {
            // VolleyManager.newInstance(getActivity()).cancelRequest(TAG);
        }
    }

    public BaseFragmentWithBottomSheet getFragment() {
        if (mEventsPagerAdapter != null && mEventsViewPager != null) {
            return mEventsPagerAdapter.getFragment(mEventsViewPager.getCurrentItem());
        }
        return null;
    }

    private void clearFilter() {


        String clicked = "clear";
        PreferenceManager.getInstance(getActivity()).setFilterTimeSelected(clicked);
        PreferenceManager.getInstance(getActivity()).setFilterPrice(clicked);
        PreferenceManager.getInstance(getActivity()).setFilterOptionSortBy(clicked);

    }


    private void handleNetworkConnection() {

        mNoEvents.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mNoEvents.setText(getResources().getString(R.string.internet_check_msg));
        Constants.createToastWithMessage(getActivity().getApplicationContext(), getActivity().getResources().getString(R.string.internet_check_msg));
        //  mErrorText.setVisibility(View.VISIBLE);
        //mErrorText.setText(getResources().getString(R.string.internet_check_msg));
    }
}
