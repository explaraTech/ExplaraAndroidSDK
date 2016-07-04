package com.explara.explara_eventslisting_sdk_ui.events.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.explara.explara_eventslisting_sdk.events.EventsManger;
import com.explara.explara_eventslisting_sdk.events.dto.CollectionEventsDto;
import com.explara.explara_eventslisting_sdk_ui.R;
import com.explara.explara_eventslisting_sdk_ui.common.EventBaseFragment;
import com.explara.explara_eventslisting_sdk_ui.utils.EventConstantKeys;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;

import java.util.List;

/**
 * Created by anudeep on 15/09/15.
 */
public class EventsDetailPagerFragment extends EventBaseFragment {

    private static final String TAG = EventsDetailPagerFragment.class.getSimpleName();
    private String mEventId;
    private int mCollectionsPos;
    private int mEventPosition;
    private String mCategoryName;
    private int mEventClickedPosition;
    private ViewPager mViewPager;
    private String mSourcePage;
    private EventsDetailFragmentListener mFragmentListener;

    public interface GetFragmentListener {
        EventDetailFragment getFragment(int pos);
    }

    public interface EventsDetailFragmentListener {
        void onTopicsListItemClick(String topic);

        void onShowMoreTextClicked(String textDescription, String title);

        void onInvitesButtonClick(String eventTitle, String eventUrl);

        void onGetTicketsButton(String event, String title, String date, String time, String currency);

        void onGetRespForm(String eventId);

        void isEventFavorited(boolean isfavorite);

        void onSetEventName(Spanned eventName);

        void enableDialer(String org_mobNumber);

        void onEnquiryButtonClicked(String eventId);
    }

    public static EventsDetailPagerFragment getInstance(String eventId, int eventPosition, int collectionPosition, String categoryName, int clickedEventPosition, String sourcePage) {
        EventsDetailPagerFragment eventsDetailPagerFragment = new EventsDetailPagerFragment();
        Bundle bundle = new Bundle(6);
        bundle.putString(EventConstantKeys.EventKeys.EVENT_ID, eventId);
        bundle.putInt(EventConstantKeys.EventKeys.EVENT_POSITION, eventPosition);
        bundle.putInt(EventConstantKeys.EventKeys.COLLECTION_POSITION, collectionPosition);
        bundle.putString(EventConstantKeys.EventKeys.CATEGORIES_NAME, categoryName);
        bundle.putString(EventConstantKeys.EventKeys.SOURCE_PAGE_KEY, sourcePage);
        bundle.putInt(EventConstantKeys.EventKeys.EVENT_CLICKED_POSITION, clickedEventPosition);
        eventsDetailPagerFragment.setArguments(bundle);
        return eventsDetailPagerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.events_detail_pager_fragment, container, false);
        extractArguments();
        intiViews(view);
        return view;
    }

    private void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEventId = args.getString(EventConstantKeys.EventKeys.EVENT_ID);
            mEventPosition = args.getInt(EventConstantKeys.EventKeys.EVENT_POSITION);
            mCollectionsPos = args.getInt(EventConstantKeys.EventKeys.COLLECTION_POSITION);
            mCategoryName = args.getString(EventConstantKeys.EventKeys.CATEGORIES_NAME);
            mEventClickedPosition = args.getInt(EventConstantKeys.EventKeys.EVENT_CLICKED_POSITION);
            mSourcePage = args.getString(ConstantKeys.SOURCE_PAGE.SOURCE_PAGE_KEY);
        }
    }


    private void intiViews(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                handlePageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (Constants.EXPLARA_ONLY) {
            if (TextUtils.isEmpty(mCategoryName)) {
                String collectionId = null;
                if (ConstantKeys.SOURCE_PAGE.FROM_COLLECTION_SCREEN.equals(mSourcePage)) {
                    EventsManger.getInstance().mAppCallBackListener.getSelectedCollectionIdFromPosition(mCollectionsPos);
                    collectionId = EventsManger.getInstance().mSelectedSessionId;
                    //collectionId = CollectionsManager.newInstance().homeScreenDto.collections.get(mCollectionsPos).id;
                } else {
                    collectionId = String.valueOf(mCollectionsPos);
                }
                List<CollectionEventsDto.Events> events = EventsManger.getInstance().mCollectionEventsDtoMap.get(collectionId).collections.collectionEvents.get(mEventPosition).events;
                mViewPager.setAdapter(new EventsDetailPagerAdapter(getChildFragmentManager(), events));
            } else {
                List<CollectionEventsDto.Events> categoryEventsWithTopics = EventsManger.getInstance().getCategoryEventsWithTopics(mCategoryName, mEventPosition);
                mViewPager.setAdapter(new CategoryEventDetailPagerAdapter(getChildFragmentManager(), mCategoryName, mEventPosition, categoryEventsWithTopics));
            }
        } else {
            List<CollectionEventsDto.Events> events = EventsManger.getInstance().mOrganizerEventsDto.allEvents.get(mEventPosition).events;
            mViewPager.setAdapter(new EventsDetailPagerAdapter(getChildFragmentManager(), events));
        }
        mViewPager.setCurrentItem(mEventClickedPosition);
        if (mEventClickedPosition == 0) {
            handlePageSelected(0);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentListener = (EventsDetailFragmentListener) activity;
    }

    private void handlePageSelected(int pos) {
        GetFragmentListener fragmentListener = (GetFragmentListener) mViewPager.getAdapter();
        EventDetailFragment eventDetailFragment = fragmentListener.getFragment(pos);
        if (eventDetailFragment != null) {
            eventDetailFragment.setUserVisibleHint(true);
            eventDetailFragment.setTitle();
        }
    }

    public EventDetailFragment getCurrentFragment() {
        GetFragmentListener fragmentListener = (GetFragmentListener) mViewPager.getAdapter();
        if (fragmentListener != null)
            return fragmentListener.getFragment(mViewPager.getCurrentItem());
        return null;
    }

    @Override
    public void refresh() {

    }

}
