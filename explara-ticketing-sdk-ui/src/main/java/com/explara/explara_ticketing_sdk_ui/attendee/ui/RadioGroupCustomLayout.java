package com.explara.explara_ticketing_sdk_ui.attendee.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.explara.explara_ticketing_sdk.attendee.dto.AttendeeDetailsResponseDto;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara_core.utils.ConstantKeys;

import java.util.Collection;

/**
 * Created by anudeep on 14/01/16.
 */
public class RadioGroupCustomLayout extends LinearLayout {

    private static final String TAG = RadioGroupCustomLayout.class.getSimpleName();
    private AttendeeDetailsResponseDto.AttendeeDto attendeeDto;
    private RadioGroup mRadioGroup;
    private TextView mErrorTextView;

    public RadioGroupCustomLayout(Context context) {
        super(context);
    }

    public RadioGroupCustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadioGroupCustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RadioGroupCustomLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void validateData() {

    }

    public void addElementsToRadioGraup(AttendeeDetailsResponseDto.AttendeeDto attendeeDto) {
        this.attendeeDto = attendeeDto;
        if (attendeeDto.options != null) {

            TextView child = (TextView) getActivityContext().getLayoutInflater().inflate(R.layout.attendee_form_label_item, this, false);
            if (attendeeDto.mandatory) {
                child.setText(attendeeDto.label + " *");
            } else {
                child.setText(attendeeDto.label);
            }
            addView(child); // Add the multicheckbox label

            final RadioButton[] rb = new RadioButton[attendeeDto.options.size()];
            mRadioGroup = new RadioGroup(getContext()); //create the RadioGroup
            mRadioGroup.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL

            Collection<String> values = attendeeDto.options.values();
            for (int i = 0; i < values.size(); i++) {
                rb[i] = new RadioButton(getContext());
                int textColor = Color.parseColor("#DB4344");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rb[i].setButtonTintList(ColorStateList.valueOf(textColor));
                }
//            rg.addView(rb[i]);
                rb[i].setText((String) values.toArray()[i]);
                rb[i].setId(i + 100);
                mRadioGroup.addView(rb[i]);
            }
            //mRadioGroup.setTag(attendeeDto);
            addView(mRadioGroup);//you add the whole RadioGroup to the layout

            mErrorTextView = (TextView) getActivityContext().getLayoutInflater().inflate(R.layout.attendee_form_error_text_item, this, false);
            mErrorTextView.setVisibility(GONE);
            addView(mErrorTextView);
        }

    }

    public void validateRedioGroupData() {
        int selectedId = mRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedId);
        if (selectedRadioButton != null) {
            String selectRadioText = selectedRadioButton.getText().toString();
            attendeeDto.isValid = true;
            mErrorTextView.setText("");
            mErrorTextView.setVisibility(GONE);
        } else {
            attendeeDto.isValid = false;
            mErrorTextView.setText(ConstantKeys.ValidationMessages.RADIO_UNSELECT);
            mErrorTextView.setVisibility(VISIBLE);
        }
    }

    public String getSelectedRadioText() {
        int selectedId = mRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedId);
        if (selectedRadioButton != null) {
            String selectRadioText = selectedRadioButton.getText().toString();
            return selectRadioText;
        } else {
            return null;
        }
    }

    private FragmentActivity getActivityContext() {
        return ((FragmentActivity) getContext());
    }
}
