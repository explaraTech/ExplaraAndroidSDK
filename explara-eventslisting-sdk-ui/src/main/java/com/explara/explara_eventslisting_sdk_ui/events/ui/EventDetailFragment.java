package com.explara.explara_eventslisting_sdk_ui.events.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.explara.explara_eventslisting_sdk.events.EventsManger;
import com.explara.explara_eventslisting_sdk.events.dto.EventsDetailDto;
import com.explara.explara_eventslisting_sdk_ui.R;
import com.explara.explara_eventslisting_sdk_ui.common.BaseFragmentWithBottomSheet;
import com.explara_core.utils.AppUtility;
import com.explara_core.utils.Constants;
import com.explara_core.utils.Log;
import com.explara_core.utils.WidgetsColorUtil;

/**
 * Created by anudeep on 23/10/15.
 */
public class EventDetailFragment extends BaseFragmentWithBottomSheet {

    private static final String TAG = EventDetailFragment.class.getSimpleName();
    public static String EVENT_ID = "event_id";
    public String mEventId;
    private ExpandableListView expandableListView;
    private ProgressBar mProgressbar;
    private View mBaseLayout;
    private EventsDetailPagerFragment.EventsDetailFragmentListener mFragmentListener;
    private EventsDetailDto mEventsDetailDto;
    private Button mButtonGetTickets;
    private Button mButtonEnquiry;
    private int lastExpandedPosition = -1;
    private TextView mErrorText;
    private String mSoldOut;

    public EventDetailFragment() {
    }

    public static EventDetailFragment getInstance(String eventId) {
        EventDetailFragment detailFragment = new EventDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EVENT_ID, eventId);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.event_detail_expandable_fragment, container, false);
//        initViews(view);
//        extractArguments();
//        return view;
//    }

    @Override
    protected void addContainerLayout(FrameLayout containerLayout, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.event_detail_expandable_fragment, containerLayout, false);
        containerLayout.addView(view);
        initViews(view);
        extractArguments();
    }

    @Override
    protected void addBottomLayout(FrameLayout slidingLayout, LayoutInflater inflater) {
    }

    private void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEventId = args.getString(EVENT_ID);

        }
    }

    private void initViews(View view) {
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_list_view);
        mProgressbar = (ProgressBar) view.findViewById(R.id.progress_bar);
        WidgetsColorUtil.setProgressBarTintColor(mProgressbar, getResources().getColor(R.color.accentColor));
        //mProgressbar.getIndeterminateDrawable().setColorFilter(getActivity().getResources().getColor(R.color.accentColor), PorterDuff.Mode.SRC_IN);
        mBaseLayout = view.findViewById(R.id.list_base_layout);
        mErrorText = (TextView) view.findViewById(R.id.error_img);
        mButtonGetTickets = (Button) view.findViewById(R.id.event_detail_register);
        mButtonEnquiry = (Button) view.findViewById(R.id.enquiry);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventsManger.getInstance().downloadEventsDetail(getActivity(), mEventId, new EventsManger.EventsDetailDownloadListener() {
            @Override
            public void onEventsDetailDownloaded(EventsDetailDto eventsDetailDto) {
                //Log.d(TAG, eventsDetailDto.getStatus());
                if (getActivity() != null && eventsDetailDto != null) {
                    displayData(eventsDetailDto);
                }
            }

            @Override
            public void onEventsDetailDownloadFailed(VolleyError volleyError) {
                if (getActivity() != null) {
//                    mProgressBar.setVisibility(View.GONE);
                    mButtonGetTickets.setVisibility(View.GONE);
                    expandableListView.setVisibility(View.GONE);
                    mButtonEnquiry.setVisibility(View.GONE);

                    if (volleyError.getMessage().contains(getResources().getString(R.string.volley_unknownhost_exception))) {
                        handleNetworkConnection();
                    } else {
                        handleErrors();
                    }
                }
            }
        }, TAG);
    }

    private void handleNetworkConnection() {
        mErrorText.setVisibility(View.VISIBLE);
        mErrorText.setText(getResources().getString(R.string.internet_check_msg));
        Constants.createToastWithMessage(getActivity().getApplicationContext(), getActivity().getResources().getString(R.string.internet_check_msg));
        mProgressbar.setVisibility(View.GONE);
    }

    private void displayData(final EventsDetailDto eventsDetailDto) {
        if (getActivity() != null && eventsDetailDto != null) {
            mBaseLayout.setVisibility(View.VISIBLE);
            mProgressbar.setVisibility(View.GONE);
            mEventsDetailDto = eventsDetailDto;

            //if (getActivity() instanceof EventDetailActivity || getActivity() instanceof FeaturedEventsActivity) {
            mFragmentListener.enableDialer(eventsDetailDto.events.contactDetails);
            //}

            //if (getActivity() instanceof FeaturedEventsActivity) {
            mFragmentListener.isEventFavorited(eventsDetailDto.favourite.equals("yes"));
            //}

            if (getUserVisibleHint()) {
                mFragmentListener.isEventFavorited(eventsDetailDto.favourite.equals("yes"));
                mFragmentListener.enableDialer(eventsDetailDto.events.contactDetails);
                Log.d(TAG, "GetVisibleHint");
                if (mEventsDetailDto != null && mEventsDetailDto.events != null) {
                    mFragmentListener.onSetEventName(Html.fromHtml(AppUtility.toCamelCase(mEventsDetailDto.events.getTitle())));
                    if (Constants.EXPLARA_ONLY) {
                        sendEventTitleToCleverTap();
                    }
                }
            }

            expandableListView.setAdapter(new EventDetailExpandableAdapter(eventsDetailDto, getActivity(), mFragmentListener));

            if (eventsDetailDto.events.getShowButton() != null) {
                if ("yes".equals(eventsDetailDto.events.getShowButton().toLowerCase())) {
                    if(Constants.EXPLARA_ONLY){
                        if ("rsvp".equals(eventsDetailDto.events.getType())) {
                            mButtonGetTickets.setText("RSVP");
                        }
                        mButtonGetTickets.setVisibility(View.VISIBLE);
                    }else{
                        if("INR".equals(eventsDetailDto.events.getCurrency())){
                            if ("rsvp".equals(eventsDetailDto.events.getType())) {
                                mButtonGetTickets.setText("RSVP");
                            }
                            mButtonGetTickets.setVisibility(View.VISIBLE);
                        }else{
                            mButtonGetTickets.setVisibility(View.GONE);
                        }
                    }
                } else {
                    mButtonGetTickets.setVisibility(View.GONE);
                }
            }



            mButtonGetTickets.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (eventsDetailDto.events.getType().equals("rsvp")) {
                        //mFragmentListener.onGetRespForm(eventsDetailDto.events.getId());
                        mSlidingLayout.removeAllViews();
                        showBottomSheetFrgament(EventDetailFragment.this, EventsManger.getInstance().mEventListingCallBackListener.getRsvpFragmentFromEventId(mEventId), getResources().getDimension(R.dimen.rsvp_form_height));
                    } else {
                        mFragmentListener.onGetTicketsButton(eventsDetailDto.events.getId(),
                                eventsDetailDto.events.getTitle(),
                                eventsDetailDto.events.getStartDate(),
                                eventsDetailDto.events.getStartTime(),
                                eventsDetailDto.events.getCurrency().equals("$") ? "USD" :
                                        (eventsDetailDto.events.getCurrency().equals("&#8377;") ? "INR" :
                                                eventsDetailDto.events.getCurrency()));
                    }
                }
            });


        }

        if (EventsManger.getInstance().checkEnquiryFormEnabled(mEventId)) {
            mButtonEnquiry.setVisibility(View.VISIBLE);
            //if ("no".equals(eventsDetailDto.events.getShowButton())) {
            if (mButtonGetTickets.getVisibility() == View.GONE) {
                PercentRelativeLayout.LayoutParams layoutParams = (PercentRelativeLayout.LayoutParams) mButtonEnquiry.getLayoutParams();
                PercentLayoutHelper.PercentLayoutInfo info = layoutParams.getPercentLayoutInfo();
                info.widthPercent = 1.0f;
            }
        } else {
            mButtonEnquiry.setVisibility(View.GONE);
            if (mButtonGetTickets.getVisibility() == View.VISIBLE) {
                mButtonGetTickets.setVisibility(View.VISIBLE);
                PercentRelativeLayout.LayoutParams layoutParams = (PercentRelativeLayout.LayoutParams) mButtonGetTickets.getLayoutParams();
                PercentLayoutHelper.PercentLayoutInfo info = layoutParams.getPercentLayoutInfo();
                info.widthPercent = 1.0f;

                //LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) mButtonGetTickets.getLayoutParams();
                //param.weight = 1;
                //mButtonGetTickets.setLayoutParams(param);
            }
        }
        mButtonEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlidingLayout.removeAllViews();
                showBottomSheetFrgament(EventDetailFragment.this, EnquiryFragment.getInstance(mEventId), getResources().getDimension(R.dimen.enquiry_form_height));
            }
        });

        mSoldOut = eventsDetailDto.soldout;
        if (mSoldOut.equalsIgnoreCase("yes")) {

            mButtonGetTickets.setText("Sold Out");
            mButtonGetTickets.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Sold Out", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void sendEventTitleToCleverTap() {
        Intent intent = new Intent();
        intent.setAction("com.explara.android.utils");
        intent.putExtra(Constants.CLEVER_TAP_TYPE, Constants.CLEVER_TAP_EVENT_TITLE_TYPE);
        intent.putExtra(Constants.CLEVER_TAP_EVENT_KEY, mEventsDetailDto.events.getTitle());
        getContext().sendBroadcast(intent);

        /*try {
            CleverTapAPI cleverTapAPI = CleverTapAPI.newInstance(getActivity().getApplicationContext());
            Map<String, Object> topicUpdate = new HashMap<>();

            topicUpdate.put(CleverTapHelper.EventPropertyNames.SCREEN_TYPE, "Event");
            if (mEventsDetailDto.events.getTitle() != null) {
                topicUpdate.put(CleverTapHelper.EventPropertyNames.SCREEN_TITLE, mEventsDetailDto.events.getTitle());
            }

            cleverTapAPI.event.push(CleverTapHelper.EventNames.EVENT_VIEW_NAME, topicUpdate);
        } catch (CleverTapMetaDataNotFoundException | CleverTapPermissionsNotSatisfied e) {
            e.printStackTrace();
        }*/
    }

    public void setTitle() {
        if (mEventsDetailDto != null && mEventsDetailDto.events != null) {
            mFragmentListener.onSetEventName(Html.fromHtml(AppUtility.toCamelCase(mEventsDetailDto.events.getTitle())));
            mFragmentListener.isEventFavorited(mEventsDetailDto.favourite.equals("yes"));
            mFragmentListener.enableDialer(mEventsDetailDto.events.contactDetails);
            Log.d(TAG, "setTitle");
        }
    }

    private void handleErrors() {
        expandableListView.setVisibility(View.GONE);
        mProgressbar.setVisibility(View.GONE);
        mErrorText.setVisibility(View.VISIBLE);
    }


    @Override
    public void refresh() {

    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mFragmentListener = (EventsDetailPagerFragment.EventsDetailFragmentListener) getActivity();
        //mEventsDetailFragmentListener = (EventsDetailFragmentListener) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mEventsDetailDto = null;
        mFragmentListener = null;
    }


}
