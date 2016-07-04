package com.explara.explara_ticketing_sdk_ui.attendee.ui;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import com.explara_core.utils.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by anudeep on 08/01/16.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private static final String TAG = TimePickerFragment.class.getSimpleName();
    private TextView dateText;
    private String selectedDate;
    private String mTag;
    private TimePickerDialog timePickerDialog;
    private String mSourcePage;
    private boolean isDateSet = true;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));

       /* //Only for create event.Not for attendee form
        //if (ConstantKeys.CREATE_EVENT_KEYS.FROM_END_DATE_TAG.equals(mTag) && !mTag.equals(ConstantKeys.CREATE_EVENT_KEYS.EDIT_ACTION_TYPE)) {
        if (ConstantKeys.CREATE_EVENT_KEYS.FROM_END_DATE_TAG.equals(mTag)) {
            if (CreateEventManager.getInstance().mCreateEventDataDtoObj != null) {
                updateTime();
            }
        } else if (ConstantKeys.CREATE_EVENT_KEYS.FROM_START_DATE_TAG.equals(mTag)) {
            Log.d(TAG, "From ==== " + mTag + "");
            if (CreateEventManager.getInstance().mCreateEventDataDtoObj != null) {
                updateTime();
            }
        }*/

        // Create a new instance of TimePickerDialog and return it
        return timePickerDialog;
    }

    //Only for create event.Not for attendee form
   /* private void updateTime() {
        String startDate = CreateEventManager.getInstance().mCreateEventDataDtoObj.startDateTime;
        if (!TextUtils.isEmpty(startDate)) {
            String[] hourMinute = startDate.split(" ");
            Calendar mStartCalendar = getUpdatedHourAndMinute(hourMinute[1]);
            timePickerDialog.updateTime(mStartCalendar.get(Calendar.HOUR_OF_DAY), mStartCalendar.get(Calendar.MINUTE));
        }
    }*/

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        Log.d("Hour : ", hourOfDay + "");
        Log.d("minute : ", minute + "");

        //Only for create event.Not for attendee form
        /*Fragment targetFragment = getTargetFragment();
        if(targetFragment instanceof CreateEventFragment) {
            if (ConstantKeys.CREATE_EVENT_KEYS.FROM_START_DATE_TAG.equals(mTag)) {
                ((CreateEventFragment) targetFragment).getStartDate(selectedDate + " " + getSelectedTime(hourOfDay + ":" + minute));
                if (CreateEventManager.getInstance().mCreateEventDataDtoObj != null) {
                    CreateEventDataDto createEventDataDto = CreateEventManager.getInstance().mCreateEventDataDtoObj;
                    createEventDataDto.startDateTime = selectedDate + " " + getSelectedTime(hourOfDay + ":" + minute);
                }
            } else {
                ((CreateEventFragment) targetFragment).getEndDate(selectedDate + " " + getSelectedTime(hourOfDay + ":" + minute));
            }
        }*/

        dateText.setText(selectedDate + " " + getSelectedTime(hourOfDay + ":" + minute));
        dateText.setTextColor(Color.BLACK);
    }


    private String getSelectedTime(String inputDateTime) {
        String inputFormat = "kk:mm";
        String outputFormat = "kk:mm:ss";
        String finalDateTimeString = "";
        SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);
        try {
            Date startDate = formatter.parse(inputDateTime);
            SimpleDateFormat newFormat = new SimpleDateFormat(outputFormat);
            finalDateTimeString = newFormat.format(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalDateTimeString.toString();
    }

    private Calendar getUpdatedHourAndMinute(String inputDateTime) {
        String inputFormat = "kk:mm";
        String outputFormat = "kk:mm";
        Calendar mCalendar = null;
        SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);
        try {
            Date startDate = formatter.parse(inputDateTime);
            SimpleDateFormat newFormat = new SimpleDateFormat(outputFormat);
            newFormat.format(startDate);
            mCalendar = newFormat.getCalendar();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mCalendar;
    }

    public void setDateText(TextView dateText, String selectedDate) {
        this.dateText = dateText;
        this.selectedDate = selectedDate;
    }

    public void setTag(String tag) {
        this.mTag = tag;
    }

    public void sourcePage(String sourcePage) {
        this.mSourcePage = sourcePage;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dateText = null;
        selectedDate = null;
    }

}
