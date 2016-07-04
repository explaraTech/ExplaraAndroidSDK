package com.explara.explara_eventslisting_sdk_ui.events.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.explara.explara_eventslisting_sdk.events.EventsManger;
import com.explara.explara_eventslisting_sdk.events.dto.CategoriesResponseDto;
import com.explara.explara_eventslisting_sdk.events.dto.CollectionEventsDto;
import com.explara.explara_eventslisting_sdk.events.dto.EventsWithTopicsDto;
import com.explara.explara_eventslisting_sdk.events.dto.FavouriteEventsDto;
import com.explara.explara_eventslisting_sdk.events.dto.SaveTopicsResponse;
import com.explara.explara_eventslisting_sdk_ui.R;
import com.explara.explara_eventslisting_sdk_ui.common.BaseFragmentWithBottomSheet;
import com.explara.explara_eventslisting_sdk_ui.utils.EventConstantKeys;
import com.explara.explara_eventslisting_sdk_ui.utils.EventsFilter;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.PreferenceManager;
import com.explara_core.utils.Utility;
import com.explara_core.utils.WidgetsColorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by Debasish on 04/09/15.
 */
public class CollectionEventFragment extends BaseFragmentWithBottomSheet implements EventAdapter.FavButtonClickListener, EventsFilter.FilterUpdateListener {

    public static final String TAG = CollectionEventFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private EventAdapter mEventAdapter;
    private ProgressBar mProgressbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mErrorText;
    private TextView mDefaultText;
    private TextView mCityTextView;
    private TextView mFilterResultEmptyText;
    private int mEventPosition;
    private int mCollectionPosition;
    private String mCategoryName;
    private int mSourcePage;
    private EventAdapter.FavButtonClickListener mFavButtonClickListener;
    private FragmentListener mFragmentListener;
    private String mCityName;
    private String mSourcePageName;
    private RelativeLayout mFilterLayout;
    private RelativeLayout mCityLayout;
    private TextView mDefaultOrgEventsText;

    public interface FragmentListener {
        void onEventClicked(String eventId, int count, int eventPosition);

        void onEventsTopicsClick(String topicName, String topicId, int sourcePageNo);
    }

    public interface CityNameClickListener {
        void onCityNameClickListener(String cityName);
    }

    public interface From {
        int CATEGORY = 1;
        int EVENT = 2;
        int FAV = 3;
        int ORG = 4;
    }

    // For collection events
    public static CollectionEventFragment getInstance(int collectionPosition, int eventPosition, int sourcePageNumber, String sourcePage) {
        CollectionEventFragment collectionEventFragment = new CollectionEventFragment();
        Bundle bundle = new Bundle(4);
        bundle.putInt(EventConstantKeys.EventKeys.COLLECTION_POSITION, collectionPosition);
        bundle.putInt(EventConstantKeys.EventKeys.EVENT_POSITION, eventPosition);
        bundle.putInt(EventConstantKeys.EventKeys.SOURCE_PAGE, sourcePageNumber);
        bundle.putString(ConstantKeys.SOURCE_PAGE.SOURCE_PAGE_KEY, sourcePage);
        collectionEventFragment.setArguments(bundle);
        return collectionEventFragment;
    }

    // For category events
    public static CollectionEventFragment getInstance(String categoryName, int eventPosition, int sourcePageNumber, String cityName, String sourcePage) {
        CollectionEventFragment collectionEventFragment = new CollectionEventFragment();
        Bundle bundle = new Bundle(5);
        bundle.putString(EventConstantKeys.EventKeys.CATEGORIES_NAME, categoryName);
        bundle.putString(EventConstantKeys.EventKeys.CITY_NAME, cityName);
        bundle.putInt(EventConstantKeys.EventKeys.EVENT_POSITION, eventPosition);
        bundle.putInt(EventConstantKeys.EventKeys.SOURCE_PAGE, sourcePageNumber);
        bundle.putString(ConstantKeys.SOURCE_PAGE.SOURCE_PAGE_KEY, sourcePage);
        collectionEventFragment.setArguments(bundle);
        return collectionEventFragment;
    }

    // For organizer events
    public static CollectionEventFragment getInstance(int eventPosition, int sourcePageNumber, String sourcePage) {
        CollectionEventFragment collectionEventFragment = new CollectionEventFragment();
        Bundle bundle = new Bundle(4);
        //bundle.putInt(EventConstantKeys.EventKeys.COLLECTION_POSITION, collectionPosition);
        bundle.putInt(EventConstantKeys.EventKeys.EVENT_POSITION, eventPosition);
        bundle.putInt(EventConstantKeys.EventKeys.SOURCE_PAGE, sourcePageNumber);
        bundle.putString(ConstantKeys.SOURCE_PAGE.SOURCE_PAGE_KEY, sourcePage);
        collectionEventFragment.setArguments(bundle);
        return collectionEventFragment;
    }

    // For fav events
    public static CollectionEventFragment getInstance(int sourcePageNumber) {
        CollectionEventFragment collectionEventFragment = new CollectionEventFragment();
        Bundle bundle = new Bundle(1);
        bundle.putInt(EventConstantKeys.EventKeys.SOURCE_PAGE, sourcePageNumber);
        collectionEventFragment.setArguments(bundle);
        return collectionEventFragment;
    }

    @Override
    protected void addContainerLayout(FrameLayout view, LayoutInflater inflater) {
        View layout = inflater.inflate(R.layout.collections_layout_fragment, view, false);
        view.addView(layout);
        extractArguments();
        initViews(layout);
    }

    @Override
    protected void addBottomLayout(FrameLayout slidingLayout, LayoutInflater inflater) {

    }

    public void reloadMyFavEvents(int position) {
        EventsManger.getInstance().mFavouriteEventsDto.events.remove(position);
        //mEventAdapter.notifyDataSetChanged();
        mEventAdapter.notifyItemRemoved(position);
        if (EventsManger.getInstance().mFavouriteEventsDto.events.size() == 0) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mProgressbar.setVisibility(View.GONE);
            mDefaultText.setVisibility(View.VISIBLE);
        }

    }

    private void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mCollectionPosition = args.getInt(EventConstantKeys.EventKeys.COLLECTION_POSITION);
            mEventPosition = args.getInt(EventConstantKeys.EventKeys.EVENT_POSITION);
            mCategoryName = args.getString(EventConstantKeys.EventKeys.CATEGORIES_NAME);
            mSourcePage = args.getInt(EventConstantKeys.EventKeys.SOURCE_PAGE);
            mCityName = args.getString(EventConstantKeys.EventKeys.CITY_NAME);
            mSourcePageName = args.getString(ConstantKeys.SOURCE_PAGE.SOURCE_PAGE_KEY);
        }
    }

    private void initViews(final View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setEnabled(false);
        view.findViewById(R.id.fab_action_button).setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpaceItemDecorator(5));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mProgressbar = (ProgressBar) view.findViewById(R.id.progressBar);
        //mProgressbar.getIndeterminateDrawable().setColorFilter(getActivity().getResources().getColor(R.color.accentColor), PorterDuff.Mode.SRC_IN);
        WidgetsColorUtil.setProgressBarTintColor(mProgressbar, getResources().getColor(R.color.accentColor));
        mErrorText = (TextView) view.findViewById(R.id.error_img);
        mDefaultText = (TextView) view.findViewById(R.id.default_text);
        mDefaultOrgEventsText = (TextView)view.findViewById(R.id.default_org_events_text);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(view.getResources().getColor(R.color.accentColor));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();
            }
        });

        // commenting for now.As crashing
        /*EventsManger.newInstance().prepareEventListingCustomizationDto();
        mFilterLayout = (RelativeLayout) view.findViewById(R.id.filter_layout);
        mCityLayout = (RelativeLayout) view.findViewById(R.id.city_layout);
        EventListingCustomizationDto eventListingCustomizationDto = EventsManger.newInstance().mEventListingCustomizationDto;
        mFilterLayout.setVisibility(eventListingCustomizationDto.showFilterLayout ? View.VISIBLE : View.GONE);
        mCityLayout.setVisibility(eventListingCustomizationDto.showCityLayout ? View.VISIBLE : View.GONE);
        if(!eventListingCustomizationDto.showFilterLayout && !eventListingCustomizationDto.showCityLayout){
            view.findViewById(R.id.filter_bottom_layout).setVisibility(View.GONE);
        }*/


        view.findViewById(R.id.filter_bottom_layout).setVisibility(TextUtils.isEmpty(mCategoryName) ? View.GONE : View.VISIBLE);
        view.findViewById(R.id.filter_text_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlidingLayout.removeAllViews();
                showBottomSheetFrgament(CollectionEventFragment.this, new FilterFragment(), getResources().getDimension(R.dimen.filter_height));
            }
        });
        mCityTextView = (TextView) view.findViewById(R.id.city_filter);
        mFilterResultEmptyText = (TextView) view.findViewById(R.id.no_filter_result);
        mCityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlidingLayout.removeAllViews();
                showBottomSheetFrgament(CollectionEventFragment.this, FilterLocationFragment.newInstance(mCategoryName), getResources().getDimension(R.dimen.filter_city_form_height));
            }
        });
        if (!TextUtils.isEmpty(mCityName)) {
            if (mSourcePage == From.CATEGORY) {
                PreferenceManager.getInstance(getContext()).setCategoryScreenSelectedCity(mCityName);
            }
            ((TextView) view.findViewById(R.id.city_filter)).setText(mCityName);
        }

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Constants.EXPLARA_ONLY) {
            if (mSourcePage == From.EVENT) {
                //downloadEventsWithTopics();
                String collectionId = null;
                if (ConstantKeys.SOURCE_PAGE.FROM_NOTIFICATION_SCREEN.equals(mSourcePageName)) {
                    collectionId = Integer.toString(mCollectionPosition);
                } else {
                    EventsManger.getInstance().mAppCallBackListener.getSelectedCollectionIdFromPosition(mCollectionPosition);
                    collectionId = EventsManger.getInstance().mSelectedSessionId;
                    //collectionId = CollectionsManager.newInstance().homeScreenDto.collections.get(mCollectionPosition).id;
                }
                mEventAdapter = new EventAdapter(mEventPosition, EventsManger.getInstance().mCollectionEventsDtoMap.get(collectionId).collections.collectionEvents.get(mEventPosition).events, mFragmentListener, From.EVENT);
            } else if (mSourcePage == From.CATEGORY) {
                mEventAdapter = new EventAdapter(mEventPosition, getEvents(), mFragmentListener, From.CATEGORY);
                mEventAdapter.setStringName(mCategoryName);
                downloadEventsWithTopics();
            } else if (mSourcePage == From.FAV) {
                downloadFavEvents();
            }
        } else {
            if(EventsManger.getInstance().mOrganizerEventsDto.allEvents.get(mEventPosition).events.size() > 0) {
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mProgressbar.setVisibility(View.GONE);
                mDefaultOrgEventsText.setVisibility(View.GONE);
                mEventAdapter = new EventAdapter(mEventPosition, EventsManger.getInstance().mOrganizerEventsDto.allEvents.get(mEventPosition).events, mFragmentListener, From.ORG);
            }else{
                mSwipeRefreshLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                mProgressbar.setVisibility(View.GONE);
                mDefaultOrgEventsText.setVisibility(View.VISIBLE);
            }
        }
        loadAdapter();

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private List<CollectionEventsDto.Events> getEvents() {
        return EventsManger.getInstance().categoriesResponseDtoMap.get(mCategoryName).getSubCategoryEvents().get(mEventPosition).getEvents();
    }

    private void loadAdapter() {
        if (mEventAdapter != null) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setAdapter(mEventAdapter);
            mProgressbar.setVisibility(View.GONE);
        }
    }

    private void downloadFavEvents() {
        EventsManger.getInstance().downloadFavEvents(getActivity().getApplicationContext(), new EventsManger.FavEventsDownloadListener() {
            @Override
            public void onFavEventsDownloadSuccess(FavouriteEventsDto favouriteEventsDto) {
                if (getActivity() != null) {
                    if (favouriteEventsDto.events == null || favouriteEventsDto.events.size() == 0) {
                        mRecyclerView.setVisibility(View.INVISIBLE);
                        mProgressbar.setVisibility(View.GONE);
                        mDefaultText.setVisibility(View.VISIBLE);
                    } else {
                        mEventAdapter = new EventAdapter(mEventPosition, EventsManger.getInstance().mFavouriteEventsDto.events, mFragmentListener, mFavButtonClickListener, From.FAV);
                        loadAdapter();
                    }

                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Error in fav events download", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFavEventsDownloadFailed(VolleyError volleyError) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity().getApplicationContext(), "Fav Events download failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    }

    private void downloadEventsWithTopics() {

        CategoriesResponseDto categoryEvents = EventsManger.getInstance().getCategory(mCategoryName);
        List<String> subCategories = categoryEvents.subCategoryId;

        EventsManger.getInstance().downloadEventsWithTopics(getActivity(), TextUtils.join(",", subCategories), new EventsManger.EventsWithTopicsResponseListener() {
            @Override
            public void onEventsWithTopicsSuccessListener(EventsWithTopicsDto eventsWithTopicsDto) {
                if (getActivity() != null) {
                    if (mEventAdapter != null)
                        mEventAdapter.addTopicItem();
                }
            }

            @Override
            public void onEventsWithTopicsFailureListener(VolleyError volleyError) {
                volleyError.printStackTrace();

            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentListener = (FragmentListener) activity;
        mFavButtonClickListener = this;
    }

    @Override
    public void onDetach() {
        mFragmentListener = null;
        mFavButtonClickListener = null;
        mRecyclerView = null;
        if (mEventAdapter != null) {
            Filter filter = mEventAdapter.getFilter();
            if (filter instanceof EventsFilter) {
                EventsFilter eventsFilter = (EventsFilter) mEventAdapter.getFilter();
                if (eventsFilter != null) {
                    eventsFilter.cleanUp();
                }
            }
        }
        mEventAdapter = null;

        super.onDetach();
    }

    public void filterBy(int filterOption1, int filterOption2, int filterOption3) {
        if (mEventAdapter != null) {
            EventsFilter eventsFilter = (EventsFilter) mEventAdapter.getFilter();
            if (eventsFilter != null) {
                List<CollectionEventsDto.Events> source = getEvents();

//                ArrayList<CollectionEventsDto.Events> source1 = new ArrayList<>(source);

                List<CollectionEventsDto.Events> destion = copyList(source);
                CopyOnWriteArrayList<CollectionEventsDto.Events> events = new CopyOnWriteArrayList<>();
//                Collections.copy(source, destion);
//                events.addAll(getEvents();
                events.addAll(destion);
                eventsFilter.setList(events);
                eventsFilter.setFilters(filterOption1, filterOption2, filterOption3);
                eventsFilter.setFilterUpdateListener(this);
                eventsFilter.filter("");
            }
        }
    }

    public static <T> List<T> copyList(List<T> source) {
        List<T> dest = new ArrayList<T>();
        for (T item : source) {
            dest.add(item);
        }
        return dest;
    }

    @Override
    public void onFavButtonClicked(final String eventId, final int position) {
        if (Utility.isNetworkAvailable(getActivity().getApplicationContext())) {
            EventsManger.getInstance().saveEventFavTopic(getActivity().getApplicationContext(), new EventsManger.SavEventFavListener() {
                @Override
                public void onSaveEventFavSuccess(SaveTopicsResponse saveTopicsResponse) {
                    //Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                    if (getActivity() != null && saveTopicsResponse != null) {
                        if (saveTopicsResponse.status.equals("Event unliked successfully")) {
                            //Toast.makeText(getActivity(), saveTopicsResponse.status.toString(), Toast.LENGTH_LONG).show();
                            reloadMyFavEvents(position);
                        }
                    }
                }

                @Override
                public void onSavEventFailed() {
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "Some Error occured in event unfavourite", Toast.LENGTH_LONG).show();
                    }

                }
            }, eventId, TAG);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.internet_check_msg), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updatedList(List<CollectionEventsDto.Events> events) {
        if (getActivity() != null) {
            mEventAdapter = new EventAdapter(mEventPosition, events, mFragmentListener, From.CATEGORY);
            mEventAdapter.setStringName(mCategoryName);
            mRecyclerView.setAdapter(mEventAdapter);
        }

        if (events.size() == 0) {
            mFilterResultEmptyText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void refresh() {
        if (Constants.EXPLARA_ONLY) {
            if (mSourcePage == From.EVENT) {
                //mEventAdapter = new EventAdapter(mEventPosition,EventsManger.newInstance().mCollectionEventsDtoMap.get(collectionId).collections.collectionEvents.get(mEventPosition).events, mFragmentListener, From.EVENT);
            } else if (mSourcePage == From.CATEGORY) {
                mEventAdapter = new EventAdapter(mEventPosition, getEvents(), mFragmentListener, From.CATEGORY);
            } else if (mSourcePage == From.FAV) {
                downloadFavEvents();
            }
        } else {
            mEventAdapter = new EventAdapter(mEventPosition, EventsManger.getInstance().mOrganizerEventsDto.allEvents.get(mEventPosition).events, mFragmentListener, From.ORG);
        }
        loadAdapter();
    }


}
