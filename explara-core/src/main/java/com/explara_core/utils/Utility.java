package com.explara_core.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.explara_core.common_dto.CurrencyDetailsDto;
import com.explara_core.database.DataBaseManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

/**
 * Created by anudeep on 16/09/15.
 */
public class Utility {

    // Get String from Assets (/api)
    public static String getStringFromAssets(final Context context, final String fileName) {
        try {

            StringBuilder buf = new StringBuilder();
            InputStream json = context.getResources().getAssets().open(fileName);
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;

            while ((str = in.readLine()) != null) {
                buf.append(str);
            }

            in.close();
            String cityResponse = buf.toString();
            return cityResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    

    public static String getTopicNameFromTopicId(String topicId) {
        String topicName = topicId.replace("-", " ");
        String[] topicArray = topicName.split(" ");
        String finalTopicName = "";
        int count = 0;
        for (String str : topicArray) {
            if (count == topicArray.length - 1) {
                finalTopicName += str.substring(0, 1).toUpperCase() + str.substring(1);
            } else {
                finalTopicName += str.substring(0, 1).toUpperCase() + str.substring(1) + " ";
            }
            count++;
        }
        return finalTopicName;
    }

    public static String getTopicIdFromTopicName(String topicName) {
        String topicId = topicName.trim().replace(" ", "-");
        return topicId.toLowerCase();
    }

    public static String getPriceAndCurrency(String currency, String priceValue, Context context) {
        String price = "";
        if (priceValue != null) {
            if (TextUtils.isEmpty(priceValue) || priceValue.equals("0")) {
                return "Free";
            }
            String priceStr = AppUtility.getText(priceValue);
            String currencySymbol = "";
            List<CurrencyDetailsDto> currencyDetailsDtoList = DataBaseManager.getInstance(context).getCurrencyDetailsDtoList();
            for (CurrencyDetailsDto currencyDetailsDto : currencyDetailsDtoList) {
                if (currency.equals(currencyDetailsDto.currency)) {
                    currencySymbol = currencyDetailsDto.symbol;
                    break;
                }
            }
            Spanned spannedINR = Html.fromHtml(currencySymbol + " " + priceStr);
            price = spannedINR.toString();
        }
        return price;
    }

    public static LinkedHashMap<String, String> jsonToMap(String t) throws JSONException {

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        JSONObject jObject = new JSONObject(t);
        Iterator<?> keys = jObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = jObject.getString(key);
            map.put(key, value);
        }

        return map;
        //System.out.println("json : "+jObject);
        //System.out.println("map : "+map);
    }

    public static void sendRevenuetoAdjust(Double amount, String currency) {
       /* AdjustEvent event = new AdjustEvent("h4ncfj");
        event.setRevenue(amount, currency);
        Adjust.trackEvent(event);*/
    }

    public static void branchShare(ShareSheetStyle shareSheetStyle, Context context, String eventTitle, String eventDesc, String url, String eventId, String desktopUrl) {
        shareSheetStyle.setCopyUrlStyle(context.getResources().getDrawable(android.R.drawable.ic_menu_send), "Copy", "Added to clipboard")
                .setMoreOptionStyle(context.getResources().getDrawable(android.R.drawable.ic_menu_search), "Show more")
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.WHATS_APP)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.MESSAGE)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.TWITTER);

        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier(eventId)
                .setTitle(eventTitle)
                .setContentDescription(eventDesc)
                .setContentImageUrl(url)
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .addContentMetadata("property1", "EVENT_DETAIL");

        /*String desktopUrl = EventsManger.getInstance().eventsDetailDtoMap.get(eventId.events.getUrl();*/

        LinkProperties linkProperties = new LinkProperties()
                .setFeature("sharing")
                .addControlParameter("$desktop_url", desktopUrl);

        branchUniversalObject.showShareSheet((Activity) context,
                linkProperties,
                shareSheetStyle,
                new Branch.BranchLinkShareListener() {
                    @Override
                    public void onShareLinkDialogLaunched() {

                    }

                    @Override
                    public void onShareLinkDialogDismissed() {
                    }

                    @Override
                    public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {
                        android.util.Log.d("", "SharedLink : " + sharedLink + "SharedChannel : " + sharedChannel);
                        android.util.Log.d("", error.getMessage());
                    }

                    @Override
                    public void onChannelSelected(String channelName) {
                    }
                });
    }


}
