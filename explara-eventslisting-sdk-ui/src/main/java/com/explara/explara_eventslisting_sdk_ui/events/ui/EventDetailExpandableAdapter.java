package com.explara.explara_eventslisting_sdk_ui.events.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.explara.explara_eventslisting_sdk.events.dto.Event;
import com.explara.explara_eventslisting_sdk.events.dto.EventsDetailDto;
import com.explara.explara_eventslisting_sdk_ui.R;
import com.explara_core.utils.AppUtility;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.ImageCacheManager;
import com.explara_core.utils.Log;
import com.explara_core.utils.SpaceItemDecoratorHorizontal;
import com.explara_core.utils.URLDrawable;

import org.sufficientlysecure.htmltextview.HtmlTextView;
import org.sufficientlysecure.htmltextview.LocalLinkMovementMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by anudeep on 20/10/15.
 */
public class EventDetailExpandableAdapter extends BaseExpandableListAdapter {
    private static final int HEADER_COUNT = 1;
    private static int HEADER_GROUP_POSITION = 0;
    private static int HEADER_TYPE_COUNT = 2;
    private static final int HEADER_TYPE = 0;
    private static final int TAB_TYPE = 1;

    private EventsDetailDto eventsDetailDto;
    private LayoutInflater mLayoutInflater;
    private EventsDetailPagerFragment.EventsDetailFragmentListener mFragmentListener;
    private URLDrawable drawable;
    private Html.ImageGetter imageGetter;
    private CardView mCardViewTopics;
    private TextView mEventFullAddress;
    private static final String TAG = EventDetailExpandableAdapter.class.getSimpleName();

    public EventDetailExpandableAdapter(EventsDetailDto eventsDetailDto, final Context context, EventsDetailPagerFragment.EventsDetailFragmentListener eventsDetailFragmentListener) {
        this.eventsDetailDto = eventsDetailDto;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFragmentListener = eventsDetailFragmentListener;
        imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Bitmap bitmap = ImageCacheManager.getInstance(context).getBitmap(source);
                if (bitmap == null) {
//                    Toast.makeText(context, source, Toast.LENGTH_LONG).show();
                    EventDetailExpandableAdapter.this.drawable = new URLDrawable(context.getResources());
                    downloadImageRequest(source, context, EventDetailExpandableAdapter.this.drawable);
                } else {
                    BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    return drawable;
                }
                return EventDetailExpandableAdapter.this.drawable;
            }
        };
    }

//    @Override
//    public int getGroupCount() {
//        return HEADER_COUNT + eventsDetailDto.tabs.size();
//    }

    @Override
    public int getGroupCount() {
        if (eventsDetailDto.tabs != null) {
            return HEADER_COUNT + eventsDetailDto.tabs.size();
        } else {
            return HEADER_COUNT;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (HEADER_GROUP_POSITION == groupPosition) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view;

        if (getGroupType(groupPosition) == HEADER_TYPE) {
            view = mLayoutInflater.inflate(R.layout.event_detail_pager_fragment, parent, false);

            mCardViewTopics = (CardView) view.findViewById(R.id.cardView_Topics);
            mEventFullAddress = (TextView) view.findViewById(R.id.events_list_item_location);

            LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

//            makeImageRequest((ImageView) view.findViewById(R.id.event_big_image), eventsDetailDto.events.getLargeImage());

            ImageLoader imageLoader = ImageCacheManager.getInstance(view.getContext().getApplicationContext()).getImageLoader();
            ((NetworkImageView) view.findViewById(R.id.event_big_image)).setImageUrl(eventsDetailDto.events.getLargeImage(), imageLoader);


            ((TextView) view.findViewById(R.id.event_title)).setText(Html.fromHtml(eventsDetailDto.events.getTitle()));
            ((TextView) view.findViewById(R.id.event_price)).setText(getPriceAndCurrency(view.getContext()));
            setUpClickShowMore(view);
            if (eventsDetailDto.events.getLocation() != null && !eventsDetailDto.events.getLocation().isEmpty()) {
                ((TextView) view.findViewById(R.id.events_list_view_item_venue)).setText(eventsDetailDto.events.getLocation().get(0).getVenueName());
            } else {
                ((TextView) view.findViewById(R.id.events_list_view_item_venue)).setVisibility(View.GONE);
            }
            if (ConstantKeys.EVENT_SESSION_TYPE.THEATER.equals(eventsDetailDto.events.getEventSessionType()) ||
                    ConstantKeys.EVENT_SESSION_TYPE.CONFERENCE.equals(eventsDetailDto.events.getEventSessionType()) ||
                    ConstantKeys.EVENT_SESSION_TYPE.MULTIDATE.equals(eventsDetailDto.events.getEventSessionType())) {

                if ("yes".equals(eventsDetailDto.events.getShowButton().toLowerCase())) {
                    ((TextView) view.findViewById(R.id.events_list_view_item_date)).setText("Check Availability");
                    ((TextView) view.findViewById(R.id.events_list_view_item_date)).setTextColor(view.getContext().getResources().getColor(R.color.colorPrimary));
                    ((TextView) view.findViewById(R.id.events_list_view_item_date)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    ((TextView) view.findViewById(R.id.events_list_view_item_date)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!eventsDetailDto.events.getType().equals("rsvp")) {
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
                } else {
                    ((TextView) view.findViewById(R.id.events_list_view_item_date)).setVisibility(View.GONE);

                }
                ((TextView) view.findViewById(R.id.events_list_view_item_time)).setVisibility(View.GONE);
            } else {
                ((TextView) view.findViewById(R.id.events_list_view_item_date)).setText(getEventDate() /*+ " @ " + getEventTime()*/);
                ((TextView) view.findViewById(R.id.events_list_view_item_time)).setText(getEventTime());
            }

            if (eventsDetailDto.events.getLocation() != null && !eventsDetailDto.events.getLocation().isEmpty() &&
                    eventsDetailDto.events.getLocation().get(0).getAddress() != null && !eventsDetailDto.events.getLocation().get(0).getAddress().isEmpty()) {
                mEventFullAddress.setText(eventsDetailDto.events.getLocation().get(groupPosition).getVenueName() + ", " + eventsDetailDto.events.getLocation().get(groupPosition).getAddress());
                Log.d(TAG, "Location/Address is NotEmpty");
            } else {
                ((LinearLayout) view.findViewById(R.id.events_list_location_layout)).setVisibility(View.GONE);
                Log.d(TAG, "Location/Address isEmpty");
            }


            RecyclerView categoryView = (RecyclerView) view.findViewById(R.id.categories_event);
            categoryView.setLayoutManager(layoutManager);

            setTopics(categoryView);
            //setAttendeesCount(view);


            RecyclerView attendes = (RecyclerView) view.findViewById(R.id.attendes);

            if (!eventsDetailDto.attendee.isEmpty()) {
                LinearLayoutManager layoutManagerAttendes = new LinearLayoutManager(view.getContext());
                layoutManagerAttendes.setOrientation(LinearLayoutManager.HORIZONTAL);
                attendes.setLayoutManager(layoutManagerAttendes);
                attendes.setAdapter(new EventAttendeAdapter(eventsDetailDto.attendee));
            } else {
                attendes.setVisibility(View.GONE);
            }

            ((HtmlTextView) view.findViewById(R.id.text_description_short)).setHtmlFromString(eventsDetailDto.events.getTextDescription(), new HtmlTextView.LocalImageGetter());
            setupGoogleMapView(view);

        } else {
            view = mLayoutInflater.inflate(R.layout.event_detail_tab_header, parent, false);
            TextView tabHeader = (TextView) view.findViewById(R.id.tab_header);
            tabHeader.setText(eventsDetailDto.tabs.get(groupPosition - 1).name);
        }


        return view;
    }

    private void setUpClickShowMore(View view) {
        view.findViewById(R.id.show_more_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentListener.onShowMoreTextClicked(eventsDetailDto.events.getTextDescription(),
                        eventsDetailDto.events.getTitle());
            }
        });

    }



    private void setTopics(RecyclerView categoryView) {

        if (eventsDetailDto != null && eventsDetailDto.events != null && !eventsDetailDto.toString().trim().isEmpty() && !eventsDetailDto.events.toString().trim().isEmpty() &&
                eventsDetailDto.events.getTopics() != null && !eventsDetailDto.events.getTopics().toString().trim().isEmpty()) {
            Log.d(TAG, "not null" + " not empty");
            List<String> mTopics = eventsDetailDto.events.getTopics();
            if (Constants.EXPLARA_ONLY) {
                sendRelatedTopicsToCleverTap(categoryView, mTopics);
            }
        } else {
            Log.d(TAG, " topics null");
        }
        if (Constants.EXPLARA_ONLY) {
            if (eventsDetailDto != null && eventsDetailDto.events != null && eventsDetailDto.events.eventTopics != null && !eventsDetailDto.toString().trim().isEmpty() && !eventsDetailDto.events.toString().trim().isEmpty() && !eventsDetailDto.events.eventTopics.toString().trim().isEmpty()
                    && eventsDetailDto.events.eventTopics.size() > 0) {

                Log.d(TAG, "Event Topics SIZE" + "" + eventsDetailDto.events.eventTopics.size());
                EventDetailTopicsAdapter eventDetailTopicsAdapter = new EventDetailTopicsAdapter(eventsDetailDto.events.eventTopics, mFragmentListener);
                categoryView.addItemDecoration(new SpaceItemDecoratorHorizontal(5));
                categoryView.setAdapter(eventDetailTopicsAdapter);
            } else {
                //categoryView.setVisibility(View.GONE);
                mCardViewTopics.setVisibility(View.GONE);
            }
        } else {
            mCardViewTopics.setVisibility(View.GONE);
        }
    }


    public void sendRelatedTopicsToCleverTap(RecyclerView categoryView, List<String> mTopics) {

        Intent intent = new Intent();
        intent.setAction("com.explara.android.utils");
        intent.putExtra(Constants.CLEVER_TAP_TYPE, Constants.CLEVER_TAP_TOPICS_TYPE);
        intent.putStringArrayListExtra(Constants.CLEVER_TAP_LIST_KEY, new ArrayList(mTopics));
        categoryView.getContext().sendBroadcast(intent);

       /* try {
            CleverTapAPI cleverTapAPI = CleverTapAPI.newInstance(categoryView.getContext());
            Map<String, Object> topicUpdate = new HashMap<>();
            if (mTopics != null) {
                topicUpdate.put(CleverTapHelper.EventNames.RELATED_TOPICS_EVENT, "" + mTopics);
            }

            cleverTapAPI.event.push(CleverTapHelper.EventNames.RELATED_TOPICS_EVENT, topicUpdate);


            //  cleverTapAPI.event.push(CleverTapHelper.EventNames.RELATEDP_TOPICS_EVENT,mTopics);
        } catch (CleverTapMetaDataNotFoundException e) {
            e.printStackTrace();
        } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
            cleverTapPermissionsNotSatisfied.printStackTrace();
        }*/
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final View view = mLayoutInflater.inflate(R.layout.event_detail_child_tab, parent, false);
        TextView tabHeader = (TextView) view.findViewById(R.id.tab_child);
//        tabHeader.setHtmlFromString(eventsDetailDto.tabs.get(groupPosition - 1).details, new HtmlTextView.RemoteImageGetter());

        tabHeader.setText(Html.fromHtml(eventsDetailDto.tabs.get(groupPosition - 1).details, imageGetter, null));
        tabHeader.setMovementMethod(LocalLinkMovementMethod.getInstance());


        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public int getGroupTypeCount() {
        return HEADER_TYPE_COUNT;
    }

    @Override
    public int getGroupType(int groupPosition) {
        if (groupPosition == HEADER_GROUP_POSITION) {
            return HEADER_TYPE;
        } else {
            return TAB_TYPE;
        }
    }

    private void makeImageRequest(final ImageView img, final String url) {

        if (url != null) {
            ImageCacheManager.getInstance(img.getContext()).getImageLoader().get(url, ImageLoader.getImageListener(img, 0, 0), img.getWidth(), img.getHeight());
        }
    }

    private int getdeviceWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    private void downloadImageRequest(final String url, final Context context, BitmapDrawable drawable) {

        if (url != null) {
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, context.getResources().getDisplayMetrics());
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = getdeviceWidth(context);
            ImageCacheManager.getInstance(context).getImageLoader().get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response.getBitmap() != null) {
                        EventDetailExpandableAdapter.this.drawable = new URLDrawable(context.getResources(), response.getBitmap());
                        ImageCacheManager.getInstance(context).putBitmap(response.getBitmap(), url);
                        EventDetailExpandableAdapter.this.notifyDataSetChanged();

                    }
//                    drawable.setb
//                    drawable
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }, width, height);
        }
    }


    private String getPriceAndCurrency(Context context) {
        Event event = eventsDetailDto.events;
        String price = "";
        String priceStr = AppUtility.getText(event.getPrice() == null ? "0" : event.getPrice());

        switch (event.getCurrency()) {
            case "INR":
                price = context.getResources().getString(R.string.rupess, priceStr);
                break;
            case "USD":
                price = "$ " + priceStr;
                break;
            default:
                price = event.getCurrency().toString() + " " + priceStr;
        }

        return price;
    }

    private String getEventDate() {

        String inputFormat = "yyyy-MM-dd";
        //String outputFormat = "EEE, dd MMM, yyyy";
        String outputFormat = "dd MMM yyyy";

        String finalDateString = "";

        String tag = "eventTime";

        SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);
        Event event = eventsDetailDto.events;
        String startDateString = event.getStartDate().toString();
        String endDateString = event.getEndDate().toString();

        try {

            Date startDate = formatter.parse(startDateString);

            Date endDate = formatter.parse(endDateString);

            SimpleDateFormat newFormat = new SimpleDateFormat(outputFormat);

            finalDateString = newFormat.format(startDate) + " - " + newFormat.format(endDate);

        } catch (ParseException e) {
            e.printStackTrace();

        }

        return finalDateString.toString();
    }

    // Get Event Time and format it based on App requirement
    private String getEventTime() {
        String inputFormat = "kk:mm";
        String outputFormat = "hh:mm a";

        String finalTimeString = "";

        String tag = "eventTime";

        SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);
        Event event = eventsDetailDto.events;
        String startTimeString = AppUtility.getText(event.getStartTime());
        String endTimeString = AppUtility.getText(event.getEndTime());

        try {
            Date startTime = formatter.parse(startTimeString);
            Date endTime = formatter.parse(endTimeString);
            SimpleDateFormat newFormat = new SimpleDateFormat(outputFormat);

            if (startTime == null || endTime == null) {

                finalTimeString = removeFirstZeroFromString(newFormat.format(startTime));

            } else {
                // Remove 0 if first character is 0
                finalTimeString = removeFirstZeroFromString(newFormat.format(startTime));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return finalTimeString.toString();
    }

    // Remove 0 if first character is 0 used in formatting time
    private String removeFirstZeroFromString(String str) {
        String newString = str;


        if (str.charAt(0) == "0".charAt(0)) {

            StringBuilder sb = new StringBuilder(str);
            sb.deleteCharAt(0);
            newString = sb.toString();
        }

        return newString;
    }

    public void setupGoogleMapView(final View view) {
        NetworkImageView mapImageView = (NetworkImageView) view.findViewById(R.id.map_image_view);
        if (eventsDetailDto.events.getLocation() != null && !eventsDetailDto.events.getLocation().isEmpty()) {
            final String lat = eventsDetailDto.events.getLocation().get(0).getLatitude();
            final String longitude = eventsDetailDto.events.getLocation().get(0).getLongitude();
            if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(longitude)) {
                mapImageView.setVisibility(View.VISIBLE);
                mapImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mapIntent = new Intent(view.getContext(), MapActivity.class);
                        mapIntent.putExtra(MapActivity.LATITUDE, Double.valueOf(lat));
                        mapIntent.putExtra(MapActivity.LONGITUDE, Double.valueOf(longitude));
                        mapIntent.putExtra(MapActivity.VENUE_NAME, eventsDetailDto.events.getLocation().get(0).getVenueName());
                        view.getContext().startActivity(mapIntent);
                    }
                });
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, view.getContext().getResources().getDisplayMetrics());
                int width = getdeviceWidth(view.getContext());

                String url = Uri.parse("https://maps.googleapis.com/maps/api/staticmap").buildUpon().
                        appendQueryParameter("center", eventsDetailDto.events.getLocation().get(0).getVenueName() + "," + eventsDetailDto.events.getLocation().get(0).getAddress() + "," + eventsDetailDto.events.getLocation().get(0).city +
                                "," + eventsDetailDto.events.getLocation().get(0).getState() + "," + eventsDetailDto.events.getLocation().get(0).getCountry()).
                        appendQueryParameter("zoom", "13").appendQueryParameter("size", width + "x" + height).appendQueryParameter("maptype", "roadmap").
                        appendQueryParameter("markers", "color:red|" + lat + "," + longitude).appendQueryParameter("key", "AIzaSyBpR4CmxXkG8RIUF6QifBt8vZf1TX4nezM").build().toString();
                mapImageView.setImageUrl(url, ImageCacheManager.getInstance(view.getContext()).getImageLoader());
            } else {
                mapImageView.setVisibility(View.GONE);
            }
        } else {
            mapImageView.setVisibility(View.GONE);
        }
    }
}
