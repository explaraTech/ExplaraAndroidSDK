package com.explara_core.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.explara_core.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

/**
 * Created by riteshranjan on 24/07/15.
 */
public class AppUtility {

    private static final String TAG = AppUtility.class.getSimpleName();
    private static ProgressDialog progress;

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {

            // getting application package name, as defined in manifest
            String packageName = context.getApplicationContext()
                    .getPackageName();

            // Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext()
                    .getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);

            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    public static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static String getDeviceID(Context context) {
        return ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    public static String getDeviceName() {
        return android.os.Build.MANUFACTURER;
    }

    public static int getOSVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    public static void showAlert(Context context, String title, String message) {
        if (((Activity) context).isFinishing() == false) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title).setMessage(message).setCancelable(false);
            builder.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.show();
        }
    }

    public static void showErrorMsg(Context context, String message,
                                    final TextView textView) {
        if (TextUtils.isEmpty(message) || textView == null) {
            textView.setVisibility(View.INVISIBLE);
            return;
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(message);
            new Handler().postDelayed(new Runnable() {

                // Using handler with postDelayed called runnable run method

                @Override
                public void run() {
                    textView.setVisibility(View.INVISIBLE);
                    textView.setText("");
                }
            }, 4000); // wait for 3 seconds
        }
    }

    public static void showProgress(Context context, String message) {
        progress = new ProgressDialog(context);
        progress.setMessage(message);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }

    public static void hideProgress() {
        if (progress != null) {
            progress.hide();
        }

    }

    public static boolean isPackageInstall(String packagename, Context context) {
        boolean isInstall = false;
        final PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(packagename,
                    PackageManager.GET_ACTIVITIES);
            isInstall = true;
        } catch (PackageManager.NameNotFoundException e) {
            isInstall = false;
        }

        return isInstall;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager ConnectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (ConnectMgr == null)
            return false;
        NetworkInfo NetInfo = ConnectMgr.getActiveNetworkInfo();
        if (NetInfo == null)
            return false;

        return NetInfo.isConnected();
    }

    public static String md5(String in) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSharedPref(Context context, String key) {
        SharedPreferences preference = PreferenceManager
                .getDefaultSharedPreferences(context);
        return preference.getString(key, "");
    }

    public static void setSharedPref(Context context, String key, String value) {
        if (context != null) {
            SharedPreferences preference = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preference.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public static String formatNumber(String number) {
        if (number != null && !number.trim().equalsIgnoreCase("")) {
            NumberFormat formatter = new DecimalFormat("##,##,##,##,###.##");
            float floatNumber = Float.parseFloat(number);
            return formatter.format(floatNumber);
        }
        return "";
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    public static Date getDate(String date) {
        if (TextUtils.isEmpty(date)) {
            Log.d(TAG, "Date is empty");
            return null;
        }
        SimpleDateFormat givenFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm",
                Locale.ENGLISH);
        Date actualDate = new Date();
        try {
            actualDate = givenFormat.parse(date);
        } catch (Exception e) {
        }

        return actualDate;
    }

    public static String formateDate(String formetter, Date date) {
        if (TextUtils.isEmpty(formetter) || date == null) {
            Log.d(TAG, "Invalid frometter or date");
            return "";
        }
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(formetter,
                Locale.ENGLISH);
        return dateFormatter.format(date);
    }

    public static long convert24HoursTo12Hours(long hour) {
        long hourVal = hour % 12;
        if (hourVal == 0) {
            return 12; // the hour '0' should be '12'
        }
        return hourVal;
    }

    public static String getDay(TimeZone currentTz, TimeZone tz,
                                Context mContext) {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss Z");
        simpleDateFormat1.setTimeZone(currentTz);

        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss Z");
        simpleDateFormat2.setTimeZone(tz);

        Calendar cal1 = simpleDateFormat1.getCalendar();

        Calendar cal2 = simpleDateFormat2.getCalendar();

        if (cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR))
            return "yesterday";
        else if (cal1.get(Calendar.DAY_OF_YEAR) < cal2
                .get(Calendar.DAY_OF_YEAR))
            return "tommorow";
        else
            return "today";
    }

    public static String elipsize(String val, int length, int range) {
        if (TextUtils.isEmpty(val)) {
            return "";
        }
        if (val.length() > range) {
            return val.subSequence(0, length - 3) + "...";
        } else {
            return val;
        }
    }

    public static float pxToDp(float px, Context context) {
        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();
        float dp = Math.round(px
                / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static float dpToPx(float dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();
        float px = Math.round(dp
                * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static String getText(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return "";
        }
        return msg;
    }

    public static Bitmap loadBitmap(int resource, Context context) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeResource(context.getResources(), resource,
                options);
    }


    public static String toCamelCase(String s) {
        if (!TextUtils.isEmpty(s)) {
            String[] parts = s.split(" ");
            String camelCaseString = "";
            for (String part : parts) {
                if (part != null && part.trim().length() > 0)
                    camelCaseString = camelCaseString + toProperCase(part);
                else
                    camelCaseString = camelCaseString + part + " ";
            }
            return camelCaseString;
        }
        return s;
    }

    static String toProperCase(String s) {
        String temp = s.trim();
        String spaces = "";
        if (temp.length() != s.length()) {
            int startCharIndex = s.charAt(temp.indexOf(0));
            spaces = s.substring(0, startCharIndex);
        }
        temp = temp.substring(0, 1).toUpperCase() +
                spaces + temp.substring(1).toLowerCase() + " ";
        return temp;

    }


    public static void createSnackWithMessage(View parentLayout, String message) {

        if (parentLayout != null && message != null) {
            Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.WHITE).show(); // Don’t forget to show!
        }
    }

    public static String getCurrencyFormatedString(Context context, String currencyName, String price) {

        String formattedPrice = "";
        if (currencyName != "") {
            switch (currencyName) {
                case "INR":
                    formattedPrice = context.getResources().getString(
                            R.string.rupess, price);
                    break;
                case "USD":
                    formattedPrice = "$ " + price;
                    break;
                default:
                    formattedPrice = currencyName + " " + price;

            }
        }
        return formattedPrice;
    }

    public static String[] getQuantity(int min, int max) {
        String[] quan = new String[max + 1];
        for (int i = 0; i <= max; i++) {
            quan[i] = i + "";
        }
        return quan;
    }

    private String formatEnddate(String endDateStr) {
        String finalEndDateStr = "";


        String inputFormat = "yyyy-MM-dd";
        String outputFormat = "dd MMM, yyyy";

        SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);

        try {

            Date date = formatter.parse(endDateStr);
            SimpleDateFormat newFormat = new SimpleDateFormat(outputFormat);
            finalEndDateStr = newFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return finalEndDateStr;
    }

    private String formatTime(String endTimeStr) {

        String inputFormat = "kk:mm:ss";
        String outputFormat = "hh:mm a";
        String finalTimeString = "";
        String tag = "eventTime";

        SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);

        try {

            Date time = formatter.parse(endTimeStr);
            SimpleDateFormat newFormat = new SimpleDateFormat(outputFormat);

            // Remove 0 if first character is 0
            finalTimeString = newFormat.format(time);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return finalTimeString;

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
                        Log.d("", "SharedLink : " + sharedLink + "SharedChannel : " + sharedChannel);
                        Log.d("", error.getMessage());
                    }

                    @Override
                    public void onChannelSelected(String channelName) {
                    }
                });
    }
}
