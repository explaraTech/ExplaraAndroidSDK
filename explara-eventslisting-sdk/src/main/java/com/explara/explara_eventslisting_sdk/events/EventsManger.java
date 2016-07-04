package com.explara.explara_eventslisting_sdk.events;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.explara.explara_eventslisting_sdk.common.BaseManager;
import com.explara.explara_eventslisting_sdk.events.dto.CategoriesResponseDto;
import com.explara.explara_eventslisting_sdk.events.dto.CityNamesResponseDto;
import com.explara.explara_eventslisting_sdk.events.dto.CollectionEventsDto;
import com.explara.explara_eventslisting_sdk.events.dto.EnquiryResponseDto;
import com.explara.explara_eventslisting_sdk.events.dto.EventListingCustomizationDto;
import com.explara.explara_eventslisting_sdk.events.dto.EventsDetailDto;
import com.explara.explara_eventslisting_sdk.events.dto.EventsWithTopicsDto;
import com.explara.explara_eventslisting_sdk.events.dto.FavouriteEventsDto;
import com.explara.explara_eventslisting_sdk.events.dto.OrganizerEventsDto;
import com.explara.explara_eventslisting_sdk.events.dto.SaveTopicsResponse;
import com.explara.explara_eventslisting_sdk.events.io.EventsConnectionManger;
import com.explara.explara_eventslisting_sdk.utils.EventsListingConstantKeys;
import com.explara_core.communication.BaseCommunicationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anudeep on 04/09/15.
 */
public class EventsManger extends BaseManager {


    public Map<String, CollectionEventsDto> mCollectionEventsDtoMap = new HashMap<>();
    public Map<String, CategoriesResponseDto> categoriesResponseDtoMap = new HashMap<>();
    public Map<String, CategoriesResponseDto> categoriesResponseFilterDtoMap = new HashMap<>();
    public FavouriteEventsDto mFavouriteEventsDto;
    public Map<String, EventsWithTopicsDto> mTopicsDtoMap = new HashMap<>();
    public EventsWithTopicsDto mTopicItemDtoResponse;
    private Map<String, CityNamesResponseDto> mCityNamesResponseDto = new HashMap<>();
    public Map<String, EventsDetailDto> eventsDetailDtoMap = new HashMap<>();
    private EventsDetailDto mEventsDetailDtosList;
    public EnquiryResponseDto mEnquiryResponseDto;
    public EventListingCustomizationDto mEventListingCustomizationDto = new EventListingCustomizationDto();
    public BaseCommunicationManager.EventListingCallBackListener mEventListingCallBackListener;
    public BaseCommunicationManager.AppCallBackListener mAppCallBackListener;
    public String mSelectedSessionId;
    public OrganizerEventsDto mOrganizerEventsDto;
    public BaseCommunicationManager.AnalyticsListener mAnalyticsListener;

    public void initAnalyticsListener(BaseCommunicationManager.AnalyticsListener analyticsListener) {
        this.mAnalyticsListener = analyticsListener;
    }

    private static EventsManger sEventsManger;

    private EventsManger() {

    }

    public void initListeners(BaseCommunicationManager.EventListingCallBackListener eventListingCallBackListener) {
        this.mEventListingCallBackListener = eventListingCallBackListener;
    }

    public void initAppListeners(BaseCommunicationManager.AppCallBackListener appCallBackListener) {
        this.mAppCallBackListener = appCallBackListener;
    }

    public static synchronized EventsManger getInstance() {
        if (sEventsManger == null) {
            sEventsManger = new EventsManger();
        }
        return sEventsManger;
    }

    public interface DownloadOrganizerEventsListener {
        void onOrganizerEventsDownloaded(OrganizerEventsDto organizerEventsDto);

        void onOrganizerEventsDownloadFailed(VolleyError volleyError);
    }

    public interface CollectionEventDownloadListener {
        void onCollectionEventDownload(CollectionEventsDto collectionEventsDto);

        void onCollectionEventDownloadFailed(VolleyError volleyError);
    }

    public interface EventsDownloadListener {
        void onEventsDownloaded(CategoriesResponseDto collectionsDto);

        void onEventsDownloadFailed(VolleyError volleyError);
    }

    public interface FavEventsDownloadListener {
        void onFavEventsDownloadSuccess(FavouriteEventsDto favouriteEventsDto);

        void onFavEventsDownloadFailed(VolleyError volleyError);
    }

    public interface EventsWithTopicsResponseListener {
        void onEventsWithTopicsSuccessListener(EventsWithTopicsDto eventsWithTopicsDto);

        void onEventsWithTopicsFailureListener(VolleyError volleyError);
    }

    public interface SavEventFavListener {
        void onSaveEventFavSuccess(SaveTopicsResponse saveTopicsResponse);

        void onSavEventFailed();
    }

    public interface CityNamesResponseListener {
        void onCityResponseSuccessListener(CityNamesResponseDto cityNamesResponseDto);

        void onCityResponseFailureListener(VolleyError volleyError);
    }

    public interface EventsDetailDownloadListener {
        void onEventsDetailDownloaded(EventsDetailDto eventsDetailDto);

        void onEventsDetailDownloadFailed(VolleyError volleyError);
    }

    public interface SendEnquiryListener {
        void onSendEnquirySuccess(EnquiryResponseDto enquiryResponseDto);

        void onSendEnquiryFailed(VolleyError volleyError);

    }

    //Download Organizer events
    public void downloadOrganizerEvents(Context context, String accountId, DownloadOrganizerEventsListener downloadOrganizerEventsListener, String tag) {
        EventsConnectionManger eventsConnectionManger = new EventsConnectionManger();
        eventsConnectionManger.downloadOrganizerEvents(context, accountId, downloadOrganizerEventsSuccessListener(downloadOrganizerEventsListener), downloadOrganizerEventsErrorListener(downloadOrganizerEventsListener), tag);
    }

    private Response.Listener<OrganizerEventsDto> downloadOrganizerEventsSuccessListener(final DownloadOrganizerEventsListener downloadOrganizerEventsListener) {
        return new Response.Listener<OrganizerEventsDto>() {

            @Override
            public void onResponse(OrganizerEventsDto response) {
                mOrganizerEventsDto = response;
                if (downloadOrganizerEventsListener != null) {
                    downloadOrganizerEventsListener.onOrganizerEventsDownloaded(response);
                }
            }
        };
    }

    private Response.ErrorListener downloadOrganizerEventsErrorListener(final DownloadOrganizerEventsListener downloadOrganizerEventsListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (downloadOrganizerEventsListener != null) {
                    downloadOrganizerEventsListener.onOrganizerEventsDownloadFailed(error);
                }
            }
        };
    }


    public void downloadEventsDetail(Context context, String eventId, EventsDetailDownloadListener eventsDownloadListener, String tag) {
        EventsConnectionManger eventsConnectionManger = new EventsConnectionManger();
        eventsConnectionManger.downloadDetailEvents(context, eventId, getEventsDownloadSuccessListener(eventId, eventsDownloadListener), getEventsDownloadErrorListener(eventsDownloadListener), tag);

    }

    private Response.Listener<EventsDetailDto> getEventsDownloadSuccessListener(final String eventId, final EventsDetailDownloadListener eventsDownloadListener) {
        return new Response.Listener<EventsDetailDto>() {

            @Override
            public void onResponse(EventsDetailDto response) {
                eventsDetailDtoMap.put(eventId, response);
                mEventsDetailDtosList = response;

                if (eventsDownloadListener != null) {
                    eventsDownloadListener.onEventsDetailDownloaded(response);
                }
            }
        };
    }

    private Response.ErrorListener getEventsDownloadErrorListener(final EventsDetailDownloadListener eventsDownloadListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (eventsDownloadListener != null) {
                    eventsDownloadListener.onEventsDetailDownloadFailed(error);
                }

            }
        };
    }

    public void enquiry(Context context, String name, String emailId, String phoneNo, String enquiry, String eventId, SendEnquiryListener sendEnquiryListener, String tag) {
        EventsConnectionManger eventsConnectionManger = new EventsConnectionManger();
        eventsConnectionManger.enquiry(context, name, emailId, phoneNo, enquiry, eventId, sendEnquirySuccessListener(sendEnquiryListener), sendEnquiryErrorListener(sendEnquiryListener), tag);
    }


    private Response.Listener<EnquiryResponseDto> sendEnquirySuccessListener(final SendEnquiryListener sendEnquiryListener) {
        return new Response.Listener<EnquiryResponseDto>() {
            @Override
            public void onResponse(EnquiryResponseDto response) {
                mEnquiryResponseDto = response;
                if (sendEnquiryListener != null) {
                    sendEnquiryListener.onSendEnquirySuccess(response);
                }
            }
        };
    }

    private Response.ErrorListener sendEnquiryErrorListener(final SendEnquiryListener sendEnquiryListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (sendEnquiryListener != null) {
                    sendEnquiryListener.onSendEnquiryFailed(error);
                }
            }
        };
    }


    public void downloadCollectionEvents(Context context, String collectionId, CollectionEventDownloadListener collectionEventDownloadListener, String tag) {
        EventsConnectionManger eventsConnectionManger = new EventsConnectionManger();
        eventsConnectionManger.downloadCollectionEvents(context, collectionId, getCollectionsEventsDownloadSuccessListener(collectionId, collectionEventDownloadListener), getCollectionsEventsDownloadErrorListener(collectionEventDownloadListener), tag);
    }

    private Response.Listener<CollectionEventsDto> getCollectionsEventsDownloadSuccessListener(final String collectionId, final CollectionEventDownloadListener collectionEventDownloadListener) {
        return new Response.Listener<CollectionEventsDto>() {

            @Override
            public void onResponse(CollectionEventsDto response) {
                List<CollectionEventsDto.CollectionEvents> collectionEvents = response.collections.collectionEvents;
                for (CollectionEventsDto.CollectionEvents events : collectionEvents) {
                    List<CollectionEventsDto.Events> events1 = events.events;
                    for (CollectionEventsDto.Events events2 : events1) {
                        if (events2.getFilter() != null) {
                            if (TextUtils.isEmpty(events2.getFilter().pageView)) {
                                events2.getFilter().pageView = "0";
                            }
                        }
                    }
                }

                mCollectionEventsDtoMap.put(collectionId, response);
                if (collectionEventDownloadListener != null) {
                    collectionEventDownloadListener.onCollectionEventDownload(response);

                }
            }
        };
    }


    private Response.ErrorListener getCollectionsEventsDownloadErrorListener(final CollectionEventDownloadListener eventsDownloadListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (eventsDownloadListener != null) {
                    eventsDownloadListener.onCollectionEventDownloadFailed(error);
                }

            }
        };
    }

    public int getEventsCount(String collectionid) {
        return mCollectionEventsDtoMap.get(collectionid).collections.collectionEvents.size();
    }

    public void downloadEventsBasedOnCategory(Context context, String categoryName, String cityName, EventsDownloadListener eventsDownloadListener, String tag) {
        EventsConnectionManger eventsConnectionManger = new EventsConnectionManger();
        eventsConnectionManger.downloadEvents(context, categoryName, cityName, getEventsDownloadSuccessListener(categoryName, eventsDownloadListener),
                getEventsDownloadErrorListener(eventsDownloadListener), tag);

    }


    private Response.Listener<CategoriesResponseDto> getEventsDownloadSuccessListener(final String categoryName, final EventsDownloadListener eventsDownloadListener) {
        return new Response.Listener<CategoriesResponseDto>() {

            @Override
            public void onResponse(CategoriesResponseDto response) {
                categoriesResponseDtoMap.put(categoryName, response);
                categoriesResponseFilterDtoMap.put(categoryName, response);
                if (eventsDownloadListener != null) {
                    eventsDownloadListener.onEventsDownloaded(response);

                }
            }
        };
    }


    private Response.ErrorListener getEventsDownloadErrorListener(final EventsDownloadListener eventsDownloadListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (eventsDownloadListener != null) {
                    eventsDownloadListener.onEventsDownloadFailed(error);
                }

            }
        };
    }

    // Download favourite events
    public void downloadFavEvents(Context context, FavEventsDownloadListener favEventsDownloadListener, String TAG) {
        EventsConnectionManger eventsConnectionManger = new EventsConnectionManger();
        eventsConnectionManger.downloadFavEvents(context, getFavEventsSuccessListener(favEventsDownloadListener), getFavEventsErrorListener(favEventsDownloadListener), TAG);
    }

    // For Fav events download - Success
    private Response.Listener<FavouriteEventsDto> getFavEventsSuccessListener(final FavEventsDownloadListener favEventsDownloadListener) {
        return new Response.Listener<FavouriteEventsDto>() {
            @Override
            public void onResponse(FavouriteEventsDto response) {
                mFavouriteEventsDto = response;
                if (favEventsDownloadListener != null) {
                    favEventsDownloadListener.onFavEventsDownloadSuccess(response);
                }
            }
        };
    }

    // For Fav events download - Failure
    private Response.ErrorListener getFavEventsErrorListener(final FavEventsDownloadListener favEventsDownloadListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (favEventsDownloadListener != null) {
                    favEventsDownloadListener.onFavEventsDownloadFailed(error);
                }
            }
        };
    }

    //Download Events with topics
    public void downloadEventsWithTopics(Context context, String categoryName, EventsWithTopicsResponseListener responseListener) {

        if (mTopicsDtoMap != null) {
            EventsWithTopicsDto eventsWithTopicsDto = mTopicsDtoMap.get(categoryName);
            if (eventsWithTopicsDto != null) {
                responseListener.onEventsWithTopicsSuccessListener(eventsWithTopicsDto);
                return;
            }
        }
        EventsConnectionManger connectionManger = new EventsConnectionManger();
        connectionManger.downloadEventsWithTopics(context, categoryName, getEventsWithTopicsDownloadSuccessListener(responseListener, categoryName), getEventsWithTopicsDownloadFailureListener(responseListener));
    }

    private Response.Listener<EventsWithTopicsDto> getEventsWithTopicsDownloadSuccessListener(final EventsWithTopicsResponseListener responseListener, final String categoryName) {
        return new Response.Listener<EventsWithTopicsDto>() {
            @Override
            public void onResponse(EventsWithTopicsDto response) {
                mTopicItemDtoResponse = response;
                mTopicsDtoMap.put(categoryName, response);
                responseListener.onEventsWithTopicsSuccessListener(response);
            }
        };
    }

    private Response.ErrorListener getEventsWithTopicsDownloadFailureListener(final EventsWithTopicsResponseListener responseListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responseListener.onEventsWithTopicsFailureListener(error);
            }
        };
    }

    public void saveEventFavTopic(Context context, SavEventFavListener saveEventFavListener, String eventId, String tag) {
        EventsConnectionManger topicConnectionManager = new EventsConnectionManger();
        topicConnectionManager.saveEventFav(context, getFavEventSuccessListener(saveEventFavListener), getFavErrorListener(saveEventFavListener), eventId, tag);
    }

    private Response.Listener<SaveTopicsResponse> getFavEventSuccessListener(final SavEventFavListener savEventFavListener) {
        return new Response.Listener<SaveTopicsResponse>() {
            @Override
            public void onResponse(SaveTopicsResponse response) {
                if (savEventFavListener != null) {
                    savEventFavListener.onSaveEventFavSuccess(response);
                }
            }
        };
    }

    // For topics list in the My topics page - failure
    private Response.ErrorListener getFavErrorListener(final SavEventFavListener savEventFavListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (savEventFavListener != null) {
                    savEventFavListener.onSavEventFailed();
                }
            }
        };
    }

    public CategoriesResponseDto getCategory(String categoryName) {
        return EventsManger.getInstance().categoriesResponseDtoMap.get(categoryName);
    }

    public void downloadCityNames(Context context, CityNamesResponseListener cityNamesResponseListener, String segmentName) {

        if (mCityNamesResponseDto != null && mCityNamesResponseDto.get(segmentName) != null) {
            cityNamesResponseListener.onCityResponseSuccessListener(mCityNamesResponseDto.get(segmentName));
            return;
        }

        EventsConnectionManger eventsConnectionManger = new EventsConnectionManger();
        eventsConnectionManger.downloadCityResponseFromurl(context, getCityNamesResponseSuccessListener(cityNamesResponseListener, segmentName), getCityNamesResponseFailureListener(cityNamesResponseListener), segmentName);
    }

    private Response.Listener<CityNamesResponseDto> getCityNamesResponseSuccessListener(final CityNamesResponseListener responseListener, final String segmentName) {
        return new Response.Listener<CityNamesResponseDto>() {
            @Override
            public void onResponse(CityNamesResponseDto response) {
                mCityNamesResponseDto.put(segmentName, response);
                responseListener.onCityResponseSuccessListener(response);
            }
        };
    }

    private Response.ErrorListener getCityNamesResponseFailureListener(final CityNamesResponseListener responseListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responseListener.onCityResponseFailureListener(error);
            }
        };
    }


    public List<CollectionEventsDto.Events> getCategoryEvents(String categoryName, int eventPos) {
        CategoriesResponseDto categoriesResponseDto = getCategory(categoryName);
        return categoriesResponseDto.getSubCategoryEvents().get(eventPos).getEvents();
    }

    public List<CollectionEventsDto.Events> getCategoryEventsWithTopics(String categoryName, int eventPos) {
        List<CollectionEventsDto.Events> categoryEvents = getCategoryEvents(categoryName, eventPos);

        List<CollectionEventsDto.Events> categoryEventsList = new ArrayList<>(categoryEvents.size() + 1);
        categoryEventsList.addAll(categoryEvents);

        CollectionEventsDto.Events events = categoryEvents.get(0);
        if (events.itemType == EventsListingConstantKeys.EventKeys.EVENT_TOPIC_TYPE) {
            categoryEventsList.remove(0);
        }
        return categoryEventsList;
    }

    public boolean checkEnquiryFormEnabled(String eventId) {
        return "yes".equals(eventsDetailDtoMap.get(eventId).events.getIsEnquiryFormEnabled());

    }

    public void prepareEventListingCustomizationDto() {
        if (mEventListingCustomizationDto != null) {
            mEventListingCustomizationDto.showFilterLayout = false;
            mEventListingCustomizationDto.showCityLayout = false;

        }
    }

    @Override
    public void cleanUp() {
        sEventsManger = null;
        if (mCollectionEventsDtoMap != null) {
            mCollectionEventsDtoMap.clear();
            mCollectionEventsDtoMap = null;
        }
        if (categoriesResponseDtoMap != null) {
            categoriesResponseDtoMap.clear();
            categoriesResponseDtoMap = null;
        }
        if (categoriesResponseFilterDtoMap != null) {
            categoriesResponseFilterDtoMap.clear();
            categoriesResponseFilterDtoMap = null;
        }
        if (eventsDetailDtoMap != null) {
            eventsDetailDtoMap.clear();
            eventsDetailDtoMap = null;
        }
        if (mTopicsDtoMap != null) {
            mTopicsDtoMap.clear();
            mTopicsDtoMap = null;
        }

        if (mCityNamesResponseDto != null) {
            mCityNamesResponseDto.clear();
            mCityNamesResponseDto = null;
        }

        mFavouriteEventsDto = null;
        mTopicItemDtoResponse = null;
        mEnquiryResponseDto = null;
        mEventListingCustomizationDto = null;
        mEventListingCallBackListener = null;
        mAppCallBackListener = null;

    }


}
