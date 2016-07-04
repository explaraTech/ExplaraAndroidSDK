package com.explara.explara_ticketing_sdk_ui.attendee.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;
import android.widget.TextView;

import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by anudeep on 08/01/16.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = DatePickerFragment.class.getSimpleName();
    private TextView dateText;
    private String calenderType;
    private String mTag;
    private String mSourcePage;
    Fragment mTargetFragment;
    DatePickerDialog datePickerDialog;
    String startDate = "";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        mTargetFragment = getTargetFragment();
        // Create a new instance of DatePickerDialog and return it
        datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);

       /* // Only for create event. Not for Attendee form
        if (mTargetFragment instanceof CreateEventFragment) {
            if (ConstantKeys.CREATE_EVENT_KEYS.FROM_START_DATE_TAG.equals(mTag)) {
                if (CreateEventManager.getInstance().mCreateEventDataDtoObj != null) {
                    CreateEventDataDto createEventDataDto = CreateEventManager.getInstance().mCreateEventDataDtoObj;
                    if (createEventDataDto.startDateTime != null) {
                        startDate = createEventDataDto.startDateTime;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Date date = null;
                        try {
                            date = dateFormat.parse(startDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "Date:" + date);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        String updatedYear = String.valueOf(calendar.get(Calendar.YEAR));
                        String updatedDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                        String updatedMonth = String.valueOf(calendar.get(Calendar.MONTH));

                        datePickerDialog.updateDate(Integer.valueOf(updatedYear), Integer.valueOf(updatedMonth), Integer.valueOf(updatedDay));
                        //datePickerDialog = new DatePickerDialog(getActivity(), this, Integer.valueOf(updatedYear), Integer.valueOf(updatedMonth), Integer.valueOf(updatedDay));
                        }
                }
            }

            if (ConstantKeys.CREATE_EVENT_KEYS.FROM_END_DATE_TAG.equals(mTag)) {
                if (CreateEventManager.getInstance().mCreateEventDataDtoObj != null) {
                    startDate = CreateEventManager.getInstance().mCreateEventDataDtoObj.startDateTime;
                    if (!TextUtils.isEmpty(startDate)) {
                        long startDateMillis = changeDateToMills(startDate);
                        datePickerDialog.getDatePicker().setMinDate(startDateMillis);
                    } else {
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    }
                }
            } else {
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            }
        } */
        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Log.d("Year :", year + "");
        Log.d("Year month:", month + "");
        Log.d("Year day:", day + "");
        month = month + 1;

        if (ConstantKeys.AttendeeFormValidationKeys.DATE_AND_TIME.equals(calenderType)) {
            this.dismiss();
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.setDateText(dateText, getSelectedDate(year + "-" + month + "-" + day));

           /* // Only for create event. Not for Attendee form
            if (mTargetFragment instanceof CreateEventFragment) {
                timePickerFragment.setTargetFragment(mTargetFragment, mTargetFragment.getTargetRequestCode());
                timePickerFragment.setTag(mTag);
                timePickerFragment.sourcePage(mSourcePage);

                //if (ConstantKeys.CREATE_EVENT_KEYS.FROM_END_DATE_TAG.equals(mTag)) {
                //    if (CreateEventManager.getInstance().mCreateEventDataDtoObj != null) {
                //        CreateEventDataDto createEventDataDto = CreateEventManager.getInstance().mCreateEventDataDtoObj;
                //        startDate = createEventDataDto.startDateTime;
                //    }
                //}
            }*/
            timePickerFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
        } else {
            dateText.setText(getSelectedDate(year + "-" + month + "-" + day));
            dateText.setTextColor(Color.BLACK);
        }
    }

    public void setDateText(TextView dateText, String calenderType) {
        this.dateText = dateText;
        this.calenderType = calenderType;
    }

    public void setTag(String tag) {
        this.mTag = tag;
    }

    public void sourcePage(String sourcePage) {
        this.mSourcePage = sourcePage;
    }

    private String getSelectedDate(String inputDate) {
        String inputFormat = "yyyy-M-dd";
        String outputFormat = "yyyy-MM-dd";
        String finalDateString = "";
        SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);
        try {
            Date startDate = formatter.parse(inputDate);
            SimpleDateFormat newFormat = new SimpleDateFormat(outputFormat);
            finalDateString = newFormat.format(startDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalDateString.toString();
    }

    private long changeDateToMills(String inputDate) {
        String inputFormat = "yyyy-M-dd";
        String outputFormat = "yyyy-MM-dd";
        long finalDateinMills = 0;
        SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);
        try {
            if (inputDate != null) {
                Date startDate = formatter.parse(inputDate);
                SimpleDateFormat newFormat = new SimpleDateFormat(outputFormat);
                newFormat.format(startDate);
                Calendar mCalendar = newFormat.getCalendar();
                finalDateinMills = mCalendar.getTimeInMillis();
            } else {
                Log.d(TAG, "Null");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalDateinMills;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dateText = null;
        calenderType = null;
    }

}