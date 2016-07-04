package com.explara.explara_ticketing_sdk.tickets;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.explara.explara_ticketing_sdk.common.BaseManager;
import com.explara.explara_ticketing_sdk.tickets.dto.AttendeeFields;
import com.explara.explara_ticketing_sdk.tickets.dto.BuyerDetailWithOutAttendeeFormDto;
import com.explara.explara_ticketing_sdk.tickets.dto.BuyerFormDataObj;
import com.explara.explara_ticketing_sdk.tickets.dto.CartCalculationObject;
import com.explara.explara_ticketing_sdk.tickets.dto.Category;
import com.explara.explara_ticketing_sdk.tickets.dto.ConfCartCalculationObject;
import com.explara.explara_ticketing_sdk.tickets.dto.ConfirmRsvpDto;
import com.explara.explara_ticketing_sdk.tickets.dto.DiscountResponse;
import com.explara.explara_ticketing_sdk.tickets.dto.MonthDetails;
import com.explara.explara_ticketing_sdk.tickets.dto.MultiSessionDto;
import com.explara.explara_ticketing_sdk.tickets.dto.MultidateDataObj;
import com.explara.explara_ticketing_sdk.tickets.dto.Order;
import com.explara.explara_ticketing_sdk.tickets.dto.PackageDetailsWithTimingsDto;
import com.explara.explara_ticketing_sdk.tickets.dto.PaymentOptions;
import com.explara.explara_ticketing_sdk.tickets.dto.SaveBuyerFormDataDto;
import com.explara.explara_ticketing_sdk.tickets.dto.SelectedSessionDetailsDto;
import com.explara.explara_ticketing_sdk.tickets.dto.Ticket;
import com.explara.explara_ticketing_sdk.tickets.dto.TicketDatesDto;
import com.explara.explara_ticketing_sdk.tickets.dto.TicketDetailResponse;
import com.explara.explara_ticketing_sdk.tickets.dto.TicketDetailsWithTimingsDto;
import com.explara.explara_ticketing_sdk.tickets.dto.UpdatePaymentStatusDataDto;
import com.explara.explara_ticketing_sdk.tickets.dto.UpdatePaymentStatusResponseDto;
import com.explara.explara_ticketing_sdk.tickets.io.TicketsConnectionManager;
import com.explara.explara_ticketing_sdk.utils.Callback;
import com.explara.explara_ticketing_sdk.utils.ExplaraError;
import com.explara.explara_ticketing_sdk.utils.TicketingConstantKeys;
import com.explara_core.communication.BaseCommunicationManager;
import com.explara_core.utils.Log;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ananthasooraj on 9/3/15.
 */
public class TicketsManager extends BaseManager {

    // For my tickets page
    //public GetMyTicketsResponse mGetMyTicketsResponse;
    public Order mOrder;
    public PaymentOptions mPaymentOptions;
    public Map<String, Order> mEventOrderMap = new HashMap<>();
    public TicketDetailResponse mTicketDetailResponse;
    public DiscountResponse mDiscountResponse;
    public Double mTotal;
    public CartCalculationObject cartObj = new CartCalculationObject();
    public List<Ticket> tickets;
    public ConfirmRsvpDto mConfirmRsvpDto;
    public BuyerDetailWithOutAttendeeFormDto mBuyerDetailWithOutAttendeeFormDto = new BuyerDetailWithOutAttendeeFormDto();
    public BuyerFormDataObj mBuyerFormDataObj = new BuyerFormDataObj();
    public SaveBuyerFormDataDto mSaveBuyerFormDataDto;
    public UpdatePaymentStatusResponseDto mUpdatePaymentStatusResponseDto;

    // For multidate package booking
    public MultidateDataObj mMultidateDataObj = new MultidateDataObj();
    public Map<String, PackageDetailsWithTimingsDto> mPackageDetailsWithTimingsMap = new HashMap<>();
    public Map<Integer, PackageDetailsWithTimingsDto> mPackageDetailsMapWithPosition = new HashMap<>();

    //For theater session details
    //public TicketDetailsWithTimingsDto mTicketDetailsWithTimingsDto;
    public TicketDatesDto mTicketDatesDto;
    public Map<String, TicketDetailsWithTimingsDto> mTicketDetailsWithTimingsMap = new HashMap<>();
    public Map<Integer, TicketDetailsWithTimingsDto> sessionDetailsMap = new HashMap<>();
    public SelectedSessionDetailsDto selectedSessionDetailsDto = new SelectedSessionDetailsDto();
    public Map<String, SelectedSessionDetailsDto> mSelectedSessionDetailsDtoMap = new HashMap<>();

    //For Conference session details
    public MultiSessionDto mMultiSessionDto;
    public ConfCartCalculationObject confCartObj = new ConfCartCalculationObject();
    public int selectedSessionQuantity = 0;

    //Communication Listeners
    public BaseCommunicationManager.TicketListingCallBackListnener mTicketListingCallBackListnener;
    public String mEventSessionType = "ticketing";
    public BaseCommunicationManager.AnalyticsListener mAnalyticsListener;
    public String mWalletbalance;
    public BaseCommunicationManager.AppCallBackListener mAppCallBackListener;


    public void initListener(BaseCommunicationManager.TicketListingCallBackListnener ticketListingCallBackListnener) {
        this.mTicketListingCallBackListnener = ticketListingCallBackListnener;
    }

    public void initAnalyticsListener(BaseCommunicationManager.AnalyticsListener analyticsListener) {
        this.mAnalyticsListener = analyticsListener;
    }

    public void initAppListener(BaseCommunicationManager.AppCallBackListener appCallBackListener) {
        this.mAppCallBackListener = appCallBackListener;
    }

    public interface UpdatePaymentStatusListener {
        void onPaymentStatusUpdated(UpdatePaymentStatusResponseDto updatePaymentStatusResponseDto);

        void onPaymentStatusUpdateFailed(VolleyError volleyError);
    }

    public interface SaveBuyerFormDataListener {
        void onBuyerFormDataSaved(SaveBuyerFormDataDto saveBuyerFormDataDto);

        void onBuyerFormDataSaveFailed(VolleyError volleyError);
    }

    // For session cart details
    public interface SessionCartListener {
        void onSessionCartDownloaded(DiscountResponse discountResponse);

        void onSessionCartDownloadFailed(VolleyError volleyError);
    }

    // For theater tickets page
    public interface SessionDetailsListener {
        void onSessionDetailsDownloaded(TicketDetailsWithTimingsDto ticketDetailsWithTimingsDto);

        void onSessionDetailsDownloadFailed(VolleyError volleyError);
    }

    // For theater tickets page
    public interface TicketsDatesListener {
        void onTicketDatesCalculated(TicketDatesDto ticketDatesDto);

        void onTicketDatesCalculationFailed(VolleyError volleyError);
    }

    // For Cart calculation
    public interface CartCalculationListener {
        void onCartCalculated(DiscountResponse discountResponse);

        void onCartCalculationFailed(VolleyError volleyError);
    }

    // For Conf Cart calculation
    public interface ConfCartCalculationListener {
        void onConfCartCalculated(DiscountResponse discountResponse);

        void onConfCartCalculationFailed(VolleyError volleyError);
    }


    // For my tickets page
    /*public interface TicketDownloadListener {
        void onTicketsDownloaded(GetMyTicketsResponse ticketsDto);

        void onTicketsDownloadedFailed(VolleyError volleyError);
    }*/

    //For ticket checkout page
    public interface TicketsDetailDownloadListener {
        void onTicketsDetailDownloadListener(TicketDetailResponse ticketDetailResponse);

        void onTicketDetailDownloadFailed(VolleyError volleyError);
    }

    //For confirm rsvp
    public interface ConfirmRsvpListener {
        void onConfirmRsvpListener(ConfirmRsvpDto confirmRsvpDto);

        void onConfirmRsvpFailed(VolleyError volleyError);
    }

    //For multidate package events
    public interface PackageDetailDownloadListener {
        void onPackageDetailDownloadListener(TicketDetailResponse ticketDetailResponse);

        void onPackageDetailDownloadFailed(VolleyError volleyError);
    }

    public interface GenerateMultipleSessionOrderListener {
        void onMultipleSessionOrderGenerated(Order order);

        void onMultipleSessionOrderGenerateFailed();
    }

    public interface GenerateSessionOrderListener {
        void onSessionOrderGenerated(Order order);

        void onSessionOrderGenerateFailed();
    }

    public interface GenerateOrderListener {
        void onOrderGenerated(Order order);

        void onOrderGeneratedFailed();
    }

    private static TicketsManager sTicketsManager;

    private TicketsManager() {

    }

    public static synchronized TicketsManager getInstance() {
        if (sTicketsManager == null) {
            sTicketsManager = new TicketsManager();
        }
        return sTicketsManager;
    }

    /**
     * For Sdk only
     * accessToken	(mandatory)
     * orderNo:	Explara	Reference	Order	No(mandatory)
     * status:	confirm/pending/fail	(mandatory)
     * referenceNo	:	Merchant	Reference	No
     */
    public void updatePaymentStatus(Context context, String publisherAccessToken, String orderNo, String status, String referenceNo, UpdatePaymentStatusListener updatePaymentStatusListener) {
        UpdatePaymentStatusDataDto updatePaymentStatusDataDto = new UpdatePaymentStatusDataDto();
        updatePaymentStatusDataDto.accessToken = publisherAccessToken;
        updatePaymentStatusDataDto.orderNo = orderNo;
        updatePaymentStatusDataDto.status = status;
        updatePaymentStatusDataDto.referenceNo = referenceNo;

        Gson gson = new Gson();
        String jsonStr = gson.toJson(updatePaymentStatusDataDto);
        Log.d("JsonString", jsonStr);

        TicketsConnectionManager ticketsConnectionManager = new TicketsConnectionManager();
        ticketsConnectionManager.updatePaymentStatus(context, jsonStr, updatePaymentStatusSuccessListener(updatePaymentStatusListener), updatePaymentStatusErrorListener(updatePaymentStatusListener));
    }

    // Update Payment Status - success
    private Response.Listener<UpdatePaymentStatusResponseDto> updatePaymentStatusSuccessListener(final UpdatePaymentStatusListener updatePaymentStatusListener) {
        return new Response.Listener<UpdatePaymentStatusResponseDto>() {
            @Override
            public void onResponse(UpdatePaymentStatusResponseDto response) {
                mUpdatePaymentStatusResponseDto = response;
                if (updatePaymentStatusListener != null) {
                    updatePaymentStatusListener.onPaymentStatusUpdated(response);
                }
            }
        };
    }

    // Update Payment Status - Failure
    private Response.ErrorListener updatePaymentStatusErrorListener(final UpdatePaymentStatusListener updatePaymentStatusListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (updatePaymentStatusListener != null) {
                    updatePaymentStatusListener.onPaymentStatusUpdateFailed(error);
                }
            }
        };
    }


    public void generateOrderNo(Context context, String eventId, GenerateOrderListener generateOrderListener, String tag) {
        TicketsConnectionManager ticketsConnectionManager = new TicketsConnectionManager();
        ticketsConnectionManager.generateOrderNo(context, generateOrderSuccessListener(eventId, generateOrderListener), generateOrderErrorListener(generateOrderListener), tag);
    }

    // Generate orderno - success
    private Response.Listener<Order> generateOrderSuccessListener(final String eventId, final GenerateOrderListener generateOrderListener) {
        return new Response.Listener<Order>() {
            @Override
            public void onResponse(Order response) {
                mOrder = response;
                mPaymentOptions = response.getPaymentOption();
                mEventOrderMap.put(eventId, response);
                mTicketListingCallBackListnener.storeOrderNoInTransactionDto(response.getOrderNo());

                if (generateOrderListener != null) {
                    generateOrderListener.onOrderGenerated(response);
                }
            }
        };
    }

    // Generate orderno - Failure
    private Response.ErrorListener generateOrderErrorListener(final GenerateOrderListener generateOrderListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (generateOrderListener != null) {
                    generateOrderListener.onOrderGeneratedFailed();
                }
            }
        };
    }

    public void generateOrderNoForMultiSession(Context context, String eventId, GenerateMultipleSessionOrderListener generateMultipleSessionOrderListener, String tag) {
        TicketsConnectionManager ticketsConnectionManager = new TicketsConnectionManager();
        ticketsConnectionManager.generateOrderNoForMultiSession(context, generateMultipleSessionOrderSuccessListener(eventId, generateMultipleSessionOrderListener), generateMultipleSessionOrderErrorListener(generateMultipleSessionOrderListener), tag);
    }

    // Generate multiple session orderno - success
    private Response.Listener<Order> generateMultipleSessionOrderSuccessListener(final String eventId, final GenerateMultipleSessionOrderListener generateMultipleSessionOrderListener) {
        return new Response.Listener<Order>() {
            @Override
            public void onResponse(Order response) {
                mOrder = response;
                mPaymentOptions = response.getPaymentOption();
                mEventOrderMap.put(eventId, response);
                mTicketListingCallBackListnener.storeOrderNoInTransactionDto(response.getOrderNo());

                if (generateMultipleSessionOrderListener != null) {
                    generateMultipleSessionOrderListener.onMultipleSessionOrderGenerated(response);
                }
            }
        };
    }

    // Generate multiple session orderno - Failure
    private Response.ErrorListener generateMultipleSessionOrderErrorListener(final GenerateMultipleSessionOrderListener generateMultipleSessionOrderListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (generateMultipleSessionOrderListener != null) {
                    generateMultipleSessionOrderListener.onMultipleSessionOrderGenerateFailed();
                }
            }
        };
    }


    public void generateOrderNoForSession(Context context, String eventId, GenerateSessionOrderListener generateSessionOrderListener) {
        TicketsConnectionManager ticketsConnectionManager = new TicketsConnectionManager();
        ticketsConnectionManager.generateOrderNoForSession(context, generateSessionOrderSuccessListener(eventId, generateSessionOrderListener), generateSessionOrderErrorListener(generateSessionOrderListener));
    }

    // Generate session orderno - success
    private Response.Listener<Order> generateSessionOrderSuccessListener(final String eventId, final GenerateSessionOrderListener generateSessionOrderListener) {
        return new Response.Listener<Order>() {
            @Override
            public void onResponse(Order response) {
                mOrder = response;
                mPaymentOptions = response.getPaymentOption();
                mEventOrderMap.put(eventId, response);
                mTicketListingCallBackListnener.storeOrderNoInTransactionDto(response.getOrderNo());

                if (generateSessionOrderListener != null) {
                    generateSessionOrderListener.onSessionOrderGenerated(response);
                }
            }
        };
    }

    // Generate session orderno - Failure
    private Response.ErrorListener generateSessionOrderErrorListener(final GenerateSessionOrderListener generateSessionOrderListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (generateSessionOrderListener != null) {
                    generateSessionOrderListener.onSessionOrderGenerateFailed();
                }
            }
        };
    }


    // For ticketing cart caculation
    public void getAllCartCalculation(Context context, final CartCalculationListener cartCalculationListener) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(cartObj);

        // Storing in transaction dto
        // Implemented later
        //PaymentManager.getInstance().mTransaction.eventId = cartObj.eventId;
        //PaymentManager.getInstance().mTransaction.cartCalculationObject = cartObj;
        //PaymentManager.getInstance().mTransaction.selectedSessionDetailsDto = null;
        //PaymentManager.getInstance().mTransaction.confCartCalculationObject = null;

        Log.i("CartJsonStr", jsonStr);

        TicketsConnectionManager ticketsConnectionManager = new TicketsConnectionManager();
        ticketsConnectionManager.getAllCartCalculation(context, jsonStr,
                cartCalculationSuccessListener(cartCalculationListener),
                cartCalculationFailureListener(cartCalculationListener));
    }

    private Response.Listener<DiscountResponse> cartCalculationSuccessListener(final CartCalculationListener cartCalculationListener) {
        return new Response.Listener<DiscountResponse>() {
            @Override
            public void onResponse(DiscountResponse response) {
                mDiscountResponse = response;
                mTotal = Double.parseDouble(response.getCart().getGrandTotal());
                mTicketListingCallBackListnener.storeCartTotalAmountInTransactionDto(response.getCart().getGrandTotal());
                // Implemented later
                //PaymentManager.getInstance().mTransaction.cart = mDiscountResponse.getCart();
                if (cartCalculationListener != null) {
                    cartCalculationListener.onCartCalculated(response);
                }
            }
        };
    }

    private Response.ErrorListener cartCalculationFailureListener(final CartCalculationListener cartCalculationListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (cartCalculationListener != null) {
                    cartCalculationListener.onCartCalculationFailed(error);
                }
            }
        };
    }


    // For my tickets page
    /*public void downloadTickets(Context context, final TicketDownloadListener ticketDownloadListener, String tag) {
        TicketsConnectionManager ticketsConnectionManager = new TicketsConnectionManager();
        ticketsConnectionManager.downloadTickets(context,
                getTicketsDownloadSuccessListener(ticketDownloadListener),
                getTicketsDownloadFailureListener(ticketDownloadListener), tag);
    }

    private Response.Listener<GetMyTicketsResponse> getTicketsDownloadSuccessListener(final TicketDownloadListener ticketDownloadListener) {
        return new Response.Listener<GetMyTicketsResponse>() {
            @Override
            public void onResponse(GetMyTicketsResponse response) {
                mGetMyTicketsResponse = response;
                Log.d("ticketsmanager", "ticketdetailresponse" + mTicketDetailResponse);
                if (ticketDownloadListener != null) {
                    ticketDownloadListener.onTicketsDownloaded(response);
                }
            }
        };
    }

    private Response.ErrorListener getTicketsDownloadFailureListener(final TicketDownloadListener ticketDownloadListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (ticketDownloadListener != null) {
                    ticketDownloadListener.onTicketsDownloadedFailed(error);
                }
            }
        };
    } */

    //Fetch package details for multidate events
    public void downloadPackageDetails(Context context, String packageId, final PackageDetailDownloadListener packageDetailDownloadListener) {
        //MultidateDataObj multidateDataObj = com.explara.ticketing.tickets.TicketsManager.getInstance().mMultidateDataObj;
        if (mMultidateDataObj != null) {
            mMultidateDataObj.packageId = packageId;
            mMultidateDataObj.eventId = null;
            mMultidateDataObj.date = null;
        }

        Gson gson = new Gson();
        String jsonStr = gson.toJson(mMultidateDataObj);
        Log.d("JsonString", jsonStr);

        TicketsConnectionManager ticketsConnectionManager = new TicketsConnectionManager();
        ticketsConnectionManager.downloadPackageDetails(context, jsonStr,
                getPackageDetailDownloadSuccessListener(packageDetailDownloadListener),
                getPackageDetailDownloadFailureListener(packageDetailDownloadListener));
    }

    private Response.Listener<TicketDetailResponse> getPackageDetailDownloadSuccessListener(final PackageDetailDownloadListener packageDetailDownloadListener) {
        return new Response.Listener<TicketDetailResponse>() {
            @Override
            public void onResponse(TicketDetailResponse response) {
                mTicketDetailResponse = response;
                if (packageDetailDownloadListener != null) {
                    packageDetailDownloadListener.onPackageDetailDownloadListener(response);
                }
            }
        };
    }

    private Response.ErrorListener getPackageDetailDownloadFailureListener(final PackageDetailDownloadListener packageDetailDownloadListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (packageDetailDownloadListener != null) {
                    packageDetailDownloadListener.onPackageDetailDownloadFailed(error);
                }
            }
        };
    }


    //Fetch ticket details for ticketing events
    public void downloadDetailTickets(Context context, String eventId, final TicketsDetailDownloadListener ticketDownloadListener) {
        TicketsConnectionManager ticketsConnectionManager = new TicketsConnectionManager();
        ticketsConnectionManager.downloadTicketsDetail(context, eventId,
                getTicketsDetailDownloadSuccessListener(ticketDownloadListener),
                getTicketsDetailDownloadFailureListener(ticketDownloadListener));
    }

    private Response.Listener<TicketDetailResponse> getTicketsDetailDownloadSuccessListener(final TicketsDetailDownloadListener ticketDownloadListener) {
        return new Response.Listener<TicketDetailResponse>() {
            @Override
            public void onResponse(TicketDetailResponse response) {
                mTicketDetailResponse = response;
                if (ticketDownloadListener != null) {
                    ticketDownloadListener.onTicketsDetailDownloadListener(response);
                }
            }
        };
    }

    private Response.ErrorListener getTicketsDetailDownloadFailureListener(final TicketsDetailDownloadListener ticketDownloadListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (ticketDownloadListener != null) {
                    ticketDownloadListener.onTicketDetailDownloadFailed(error);
                }
            }
        };
    }

    //For confirm RSVP form

    public void confirmRequestRsvp(Context context, String eventId, String buyerName, String buyerEmail, String buyerMobileNo, final ConfirmRsvpListener confirmRsvpListener) {
        TicketsConnectionManager ticketsConnectionManager = new TicketsConnectionManager();
        ticketsConnectionManager.confirmRequestRsvp(context, eventId, buyerName, buyerEmail, buyerMobileNo,
                confirmRsvpSuccessListener(confirmRsvpListener),
                confirmRsvpFailureListener(confirmRsvpListener));
    }

    private Response.Listener<ConfirmRsvpDto> confirmRsvpSuccessListener(final ConfirmRsvpListener confirmRsvpListener) {
        return new Response.Listener<ConfirmRsvpDto>() {
            @Override
            public void onResponse(ConfirmRsvpDto response) {
                mConfirmRsvpDto = response;
                if (confirmRsvpListener != null) {
                    confirmRsvpListener.onConfirmRsvpListener(response);
                }
            }
        };
    }

    private Response.ErrorListener confirmRsvpFailureListener(final ConfirmRsvpListener confirmRsvpListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (confirmRsvpListener != null) {
                    confirmRsvpListener.onConfirmRsvpFailed(error);
                }
            }
        };
    }


    /*public List<BookedTicket> getBookedTickets(int position) {

        if(mGetMyTicketsResponse != null) {
            mGetMyTicketsResponse.getUpcoming();
        }
        Log.d("mGetMyTicketsResponse",""+mGetMyTicketsResponse.getUpcoming());
        return java.util.Collections.EMPTY_LIST;
    }*/

    /*public List<BookedTicket> getTickets() {
        if (mGetMyTicketsResponse != null) {
            return mGetMyTicketsResponse.getUpcoming();
        }
        return java.util.Collections.EMPTY_LIST;
    }

    public BookedTicket getBookedTickets(int position) {
        return getTickets().get(position);
    }*/

    public List<Ticket> getTicketsList() {
        tickets = new ArrayList<>();

        if (mTicketDetailResponse.getCategoryList() == null || mTicketDetailResponse.getCategoryList().isEmpty()) {
            if (mTicketDetailResponse.getTickets() != null && mTicketDetailResponse.getTickets().size() > 0) {
                tickets = mTicketDetailResponse.getTickets();
                for (Iterator<Ticket> flavoursIter = tickets.iterator(); flavoursIter.hasNext(); ) {
                    if (flavoursIter.next().getTicketStatus().equalsIgnoreCase("Expired")) {
                        flavoursIter.remove();
                    }
                }
            }
            // return mTicketDetailResponse.getTickets();
            return tickets;
        }

        if (mTicketDetailResponse.getCategoryList() != null && !mTicketDetailResponse.getCategoryList().isEmpty()) {
            for (Category category : mTicketDetailResponse.getCategoryList()) {
                Ticket ticket = new Ticket();
                ticket.categoryName = category.getCategoryName();
                ticket.itemType = TicketingConstantKeys.TicketListkeys.HEADER_TYPE;
                tickets.add(ticket);
                tickets.addAll(category.getTickets());
            }
            return tickets;
        }
        return java.util.Collections.EMPTY_LIST;
    }

    public void onQuantityChanged(String eventId, String packageId, Context context, String discountCode) {
        cartObj.eventId = eventId;
        if (packageId != null) {
            cartObj.multiDateId = packageId;
        } else {
            cartObj.multiDateId = null;
        }
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String versionName = pInfo.versionName;
            //params.add(new BasicNameValuePair(ConstantKeys.BundleKeys.VERSION_NUMBER, versionName));
            cartObj.version = versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        cartObj.tickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            // Only adding that tickets which have quantity is greather than 0
            if (ticket.userSelectedTicketQuantity > 0) {
                CartCalculationObject.CartTicket cartTicket = cartObj.new CartTicket();
                // Pass discount code here for calculation
                cartTicket.discountCode = discountCode;
                cartTicket.quantity = ticket.userSelectedTicketQuantity;
                cartTicket.ticketId = ticket.getId();
                cartTicket.ticketName = ticket.getName();
                cartTicket.ticketPrice = ticket.getPrice();
                if (!TextUtils.isEmpty(ticket.getCategoryId())) {
                    cartTicket.categoryId = ticket.getCategoryId();
                } else {
                    cartTicket.categoryId = null;
                }
                cartObj.tickets.add(cartTicket);
            }
        }
        Log.d("Ticket", "" + cartObj.tickets.size());
    }


    public void onSessionSelected(String eventId, Context context) {
        confCartObj.eventId = eventId;
        confCartObj.discountCode = mMultiSessionDto.discountCodeUsed;
        confCartObj.quantity = mMultiSessionDto.attendeeQuantity;
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String versionName = pInfo.versionName;
            //params.add(new BasicNameValuePair(ConstantKeys.BundleKeys.VERSION_NUMBER, versionName));
            confCartObj.version = versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        confCartObj.sessions = new ArrayList<>();
        Collection<SelectedSessionDetailsDto> values = mSelectedSessionDetailsDtoMap.values();
        for (SelectedSessionDetailsDto selectedSessionDetailsDto : values) {
            ConfCartCalculationObject.CartSession cartSession = confCartObj.new CartSession();
            cartSession.sessionId = selectedSessionDetailsDto.sessionsDesc.id;
            cartSession.sessionPrice = selectedSessionDetailsDto.sessionsDesc.price;
            confCartObj.sessions.add(cartSession);
        }
        Log.d("Session", "" + confCartObj.sessions.size());

    }

    public HashMap<Integer, Integer> getTicketInfo() {
        HashMap<Integer, Integer> ticketsInfo = new HashMap<>();
        for (CartCalculationObject.CartTicket cartTicket : cartObj.getTickets()) {
            ticketsInfo.put(cartTicket.ticketId, cartTicket.quantity);
        }
        return ticketsInfo;
    }
//    ticketInfo;// = new HashMap<ticket_id, quantity>();


    //For theater tickets page
    public void getAllDatesAndTimes(Context context, String eventId, boolean isConfType, final TicketsDatesListener ticketsDatesListener) {
        TicketsConnectionManager ticketsConnectionManager = new TicketsConnectionManager();
        ticketsConnectionManager.getAllDatesAndTimes(context, eventId, isConfType,
                getTicketDatesSuccessListener(ticketsDatesListener),
                getTicketDatesFailureListener(ticketsDatesListener));
    }

    private Response.Listener<TicketDatesDto> getTicketDatesSuccessListener(final TicketsDatesListener ticketsDatesListener) {
        return new Response.Listener<TicketDatesDto>() {
            @Override
            public void onResponse(TicketDatesDto response) {
                mTicketDatesDto = response;
                if (ticketsDatesListener != null) {
                    ticketsDatesListener.onTicketDatesCalculated(response);
                }
            }
        };
    }

    private Response.ErrorListener getTicketDatesFailureListener(final TicketsDatesListener ticketsDatesListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (ticketsDatesListener != null) {
                    ticketsDatesListener.onTicketDatesCalculationFailed(error);
                }
            }
        };
    }

    //For ticket details with timings
    public void getTicketDetailsByDate(Context context, String eventId, String sessionDate, int position, boolean confType, final SessionDetailsListener sessionDetailsListener) {
        TicketsConnectionManager ticketsConnectionManager = new TicketsConnectionManager();
        ticketsConnectionManager.getTicketDetailsByDate(context, eventId, sessionDate, confType,
                getSessionDetailsSuccessListener(sessionDetailsListener, position, sessionDate),
                getSessionDetailsFailureListener(sessionDetailsListener));
    }

    private Response.Listener<TicketDetailsWithTimingsDto> getSessionDetailsSuccessListener(final SessionDetailsListener sessionDetailsListener, final int position, final String sessionDate) {
        return new Response.Listener<TicketDetailsWithTimingsDto>() {
            @Override
            public void onResponse(TicketDetailsWithTimingsDto response) {
                //mTicketDetailsWithTimingsDto = response;
                mTicketDetailsWithTimingsMap.put(sessionDate, response);
                sessionDetailsMap.put(position, response);
                if (sessionDetailsListener != null) {
                    sessionDetailsListener.onSessionDetailsDownloaded(response);
                }
            }
        };
    }

    private Response.ErrorListener getSessionDetailsFailureListener(final SessionDetailsListener sessionDetailsListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (sessionDetailsListener != null) {
                    sessionDetailsListener.onSessionDetailsDownloadFailed(error);
                }
            }
        };
    }

    //For Theater cart - not in use
    public void getSessionCartDetails(Context context, String sessionId, int selectedQuantity, String couponCode, final SessionCartListener sessionCartListener) {
        // Stroing data in transactionDto
        selectedSessionDetailsDto.sessionId = sessionId;
        selectedSessionDetailsDto.selectedQuantity = selectedQuantity;
        selectedSessionDetailsDto.discountUsed = couponCode;
        // Implemented later
        //PaymentManager.getInstance().mTransaction.selectedSessionDetailsDto = selectedSessionDetailsDto;

        TicketsConnectionManager ticketsConnectionManager = new TicketsConnectionManager();
        ticketsConnectionManager.getSessionCartDetails(context, sessionId, selectedQuantity, couponCode,
                getSessionCartSuccessListener(sessionCartListener),
                getSessionCartFailureListener(sessionCartListener));
    }

    private Response.Listener<DiscountResponse> getSessionCartSuccessListener(final SessionCartListener sessionCartListener) {
        return new Response.Listener<DiscountResponse>() {
            @Override
            public void onResponse(DiscountResponse response) {
                mDiscountResponse = response;
                mTicketListingCallBackListnener.storeCartTotalAmountInTransactionDto(response.getCart().getGrandTotal());
                mTotal = Double.parseDouble(response.getCart().getGrandTotal());
                // Implemented later
                //PaymentManager.getInstance().mTransaction.cart = mDiscountResponse.getCart();
                if (sessionCartListener != null) {
                    sessionCartListener.onSessionCartDownloaded(response);
                }
            }
        };
    }

    private Response.ErrorListener getSessionCartFailureListener(final SessionCartListener sessionCartListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (sessionCartListener != null) {
                    sessionCartListener.onSessionCartDownloadFailed(error);
                }
            }
        };
    }

    //For Conference cart - not in use
    public void getConfSessionCartDetails(Context context, final ConfCartCalculationListener confCartCalculationListener) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(confCartObj);
        Log.d("JsonString", jsonStr);

        //Storing Data in Transaction Dto
        // Implemented later
        //PaymentManager.getInstance().mTransaction.eventId = confCartObj.eventId;
        //PaymentManager.getInstance().mTransaction.confCartCalculationObject = confCartObj;
        //PaymentManager.getInstance().mTransaction.cartCalculationObject = null;
        //PaymentManager.getInstance().mTransaction.selectedSessionDetailsDto = null;

        TicketsConnectionManager ticketsConnectionManager = new TicketsConnectionManager();
        ticketsConnectionManager.getConfSessionCartDetails(context, jsonStr,
                confCartCalculationSuccessListener(confCartCalculationListener),
                confCartCalculationFailureListener(confCartCalculationListener));
    }

    private Response.Listener<DiscountResponse> confCartCalculationSuccessListener(final ConfCartCalculationListener confCartCalculationListener) {
        return new Response.Listener<DiscountResponse>() {
            @Override
            public void onResponse(DiscountResponse response) {
                mDiscountResponse = response;
                // Implemented later
                //PaymentManager.getInstance().mTransaction.cart = mDiscountResponse.getCart();
                if (response.getCart() != null) {
                    mTotal = Double.parseDouble(response.getCart().getGrandTotal());
                    mTicketListingCallBackListnener.storeCartTotalAmountInTransactionDto(response.getCart().getGrandTotal());
                }
                if (confCartCalculationListener != null) {
                    confCartCalculationListener.onConfCartCalculated(response);
                }
            }
        };
    }

    private Response.ErrorListener confCartCalculationFailureListener(final ConfCartCalculationListener confCartCalculationListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (confCartCalculationListener != null) {
                    confCartCalculationListener.onConfCartCalculationFailed(error);
                }
            }
        };
    }

    //For theater tickets page
    public void getAllPackagesDatesAndTimes(Context context, String eventId, final Callback<TicketDatesDto> ticketsDatesListener) {

        if (mMultidateDataObj != null) {
            mMultidateDataObj.eventId = eventId;
            mMultidateDataObj.date = null;
        }
        Gson gson = new Gson();
        String jsonStr = gson.toJson(mMultidateDataObj);
        Log.d("JsonString", jsonStr);

        TicketsConnectionManager ticketsConnectionManager = new TicketsConnectionManager();
        ticketsConnectionManager.getAllPackagesDatesAndTimes(context, jsonStr,
                getTicketDatesSuccessListener(ticketsDatesListener),
                getTicketDatesFailureListener(ticketsDatesListener));
    }

    private Response.Listener<TicketDatesDto> getTicketDatesSuccessListener(final Callback<TicketDatesDto> ticketsDatesListener) {
        return new Response.Listener<TicketDatesDto>() {
            @Override
            public void onResponse(TicketDatesDto response) {
                mTicketDatesDto = response;
                if (ticketsDatesListener != null) {
                    ticketsDatesListener.success(response);
                }
            }
        };
    }

    private Response.ErrorListener getTicketDatesFailureListener(final Callback<TicketDatesDto> ticketsDatesListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (ticketsDatesListener != null) {
                    ticketsDatesListener.error(new ExplaraError(error));
                }
            }
        };
    }

    public int getTicketDatesCount(int mSelectedMonthPos) {
        MonthDetails selectedMonth = mTicketDatesDto.sessionDates.months.get(mSelectedMonthPos);
        return selectedMonth.endPosition - selectedMonth.startPosition + 1;
    }

    //For ticket details with timings
    public void getPackageDetailsByDate(Context context, String eventId, String sessionDate, int position, final Callback<PackageDetailsWithTimingsDto> sessionDetailsListener) {
        if (mMultidateDataObj != null) {
            mMultidateDataObj.eventId = eventId;
            mMultidateDataObj.date = sessionDate;
        }
        Gson gson = new Gson();
        String jsonStr = gson.toJson(mMultidateDataObj);
        Log.d("JsonString", jsonStr);

        TicketsConnectionManager ticketsConnectionManager = new TicketsConnectionManager();
        ticketsConnectionManager.getPackageDetailsByDate(context, jsonStr,
                getSessionDetailsSuccessListener(sessionDetailsListener, position, sessionDate),
                getSessionDetailsFailureListener(sessionDetailsListener));
    }

    private Response.Listener<PackageDetailsWithTimingsDto> getSessionDetailsSuccessListener(final Callback<PackageDetailsWithTimingsDto> sessionDetailsListener, final int position, final String sessionDate) {
        return new Response.Listener<PackageDetailsWithTimingsDto>() {
            @Override
            public void onResponse(PackageDetailsWithTimingsDto response) {
                mPackageDetailsWithTimingsMap.put(sessionDate, response);
                mPackageDetailsMapWithPosition.put(position, response);
                if (sessionDetailsListener != null) {
                    sessionDetailsListener.success(response);
                }
            }
        };
    }

    private Response.ErrorListener getSessionDetailsFailureListener(final Callback<PackageDetailsWithTimingsDto> sessionDetailsListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (sessionDetailsListener != null) {
                    sessionDetailsListener.error(new ExplaraError(error));
                }
            }
        };
    }

    public void cleanSelectedSessionData() {
        mSelectedSessionDetailsDtoMap.clear();
        mDiscountResponse = null;
    }

    public String getGrandTotal() {
        return mDiscountResponse.getCart().getGrandTotal();
    }

    public void clearCartObj() {
        cartObj = new CartCalculationObject();
    }

    public void clearConfCartobj() {
        confCartObj = new ConfCartCalculationObject();
    }


    public void prepareBuyerFormData() {
        mBuyerFormDataObj.orderNo = TicketsManager.getInstance().mOrder.getOrderNo();
        AttendeeFields attendeeFields1 = new AttendeeFields();
        attendeeFields1.id = "default_1";
        attendeeFields1.label = "Name";
        // not taking for transacrtion dto
        //attendeeFields1.value = mTransaction.buyerDetailWithOutAttendeeFormDto.buyerName;
        attendeeFields1.value = mBuyerDetailWithOutAttendeeFormDto.buyerName;

        AttendeeFields attendeeFields2 = new AttendeeFields();
        attendeeFields2.id = "default_2";
        attendeeFields2.label = "Email";
        // not taking for transacrtion dto
        //attendeeFields2.value = mTransaction.buyerDetailWithOutAttendeeFormDto.buyerEmail;
        attendeeFields2.value = mBuyerDetailWithOutAttendeeFormDto.buyerEmail;

        AttendeeFields attendeeFields3 = new AttendeeFields();
        attendeeFields3.id = "default_3";
        attendeeFields3.label = "Mobile No.";
        // not taking for transacrtion dto
        //attendeeFields3.value = mTransaction.buyerDetailWithOutAttendeeFormDto.buyerPhone;
        attendeeFields3.value = mBuyerDetailWithOutAttendeeFormDto.buyerPhone;

        List<AttendeeFields> attendeeFields = new ArrayList<>();
        attendeeFields.add(attendeeFields1);
        attendeeFields.add(attendeeFields2);
        attendeeFields.add(attendeeFields3);

        mBuyerFormDataObj.attendees.put("attendee_1", attendeeFields);
    }

    public boolean isBuyerDetailsFilled() {

        if (TextUtils.isEmpty(mBuyerDetailWithOutAttendeeFormDto.buyerName)) {
            return false;
        } else if (TextUtils.isEmpty(mBuyerDetailWithOutAttendeeFormDto.buyerEmail)) {
            return false;
        } else if (TextUtils.isEmpty(mBuyerDetailWithOutAttendeeFormDto.buyerPhone)) {
            return false;
        }
        return true;
    }

    // For save buyer form - when attendee form disabled
    public void saveBuyerFormData(Context context, final SaveBuyerFormDataListener saveBuyerFormDataListener, String tag) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(mBuyerFormDataObj);
        Log.i("FormJsonStr", jsonStr);
        //Map<String,String> buyerFormDataMap = new HashMap<>();
        //buyerFormDataMap.put("data", jsonStr);

        TicketsConnectionManager ticketsConnectionManager = new TicketsConnectionManager();
        ticketsConnectionManager.saveBuyerFormData(context, jsonStr,
                saveBuyerFormDataSuccessListener(saveBuyerFormDataListener),
                saveBuyerFormDataFailureListener(saveBuyerFormDataListener), tag);
    }

    private Response.Listener<SaveBuyerFormDataDto> saveBuyerFormDataSuccessListener(final SaveBuyerFormDataListener saveBuyerFormDataListener) {
        return new Response.Listener<SaveBuyerFormDataDto>() {
            @Override
            public void onResponse(SaveBuyerFormDataDto response) {
                mSaveBuyerFormDataDto = response;
                if (saveBuyerFormDataListener != null) {
                    saveBuyerFormDataListener.onBuyerFormDataSaved(response);
                }
            }
        };
    }

    private Response.ErrorListener saveBuyerFormDataFailureListener(final SaveBuyerFormDataListener saveBuyerFormDataListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (saveBuyerFormDataListener != null) {
                    saveBuyerFormDataListener.onBuyerFormDataSaveFailed(error);
                }
            }
        };
    }

    @Override
    public void cleanUp() {
        //mGetMyTicketsResponse = null;
        sTicketsManager = null;
        mOrder = null;
        mPaymentOptions = null;
        if (mEventOrderMap != null) {
            mEventOrderMap.clear();
            mEventOrderMap = null;
        }
        mTicketDetailResponse = null;
        mDiscountResponse = null;
        mTotal = null;
        cartObj = null;
        if (tickets != null) {
            tickets.clear();
            tickets = null;
        }
        mTicketDatesDto = null;
        sessionDetailsMap = null;
        mConfirmRsvpDto = null;
        mBuyerDetailWithOutAttendeeFormDto = null;
        mBuyerFormDataObj = null;
        mSaveBuyerFormDataDto = null;
        mMultidateDataObj = null;
        if (mPackageDetailsWithTimingsMap != null) {
            mPackageDetailsWithTimingsMap.clear();
            mPackageDetailsWithTimingsMap = null;
        }
        if (mTicketDetailsWithTimingsMap != null) {
            mTicketDetailsWithTimingsMap.clear();
            mTicketDetailsWithTimingsMap = null;
        }

        if (mPackageDetailsMapWithPosition != null) {
            mPackageDetailsMapWithPosition.clear();
            mPackageDetailsMapWithPosition = null;
        }

        if (sessionDetailsMap != null) {
            sessionDetailsMap.clear();
            sessionDetailsMap = null;
        }

        if (mSelectedSessionDetailsDtoMap != null) {
            mSelectedSessionDetailsDtoMap.clear();
            mSelectedSessionDetailsDtoMap = null;
        }
        selectedSessionDetailsDto = null;
        mMultiSessionDto = null;
        confCartObj = null;
        mTicketListingCallBackListnener = null;
        mUpdatePaymentStatusResponseDto = null;

    }


}
