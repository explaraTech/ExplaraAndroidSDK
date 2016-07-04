package com.explara.explara_ticketing_sdk_ui.attendee.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.explara.explara_ticketing_sdk.attendee.dto.AttendeeDetailsResponseDto;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara_core.utils.ConstantKeys;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by anudeep on 14/01/16.
 */
public class MultiCheckboxCustomLayout extends LinearLayout {

    private static final String TAG = MultiCheckboxCustomLayout.class.getSimpleName();
    private AttendeeDetailsResponseDto.AttendeeDto attendeeDto;
    private List<CheckBox> mMultiCheckBoxList = new ArrayList<>();
    private TextView mErrorTextView;

    public MultiCheckboxCustomLayout(Context context) {
        super(context);
    }

    public MultiCheckboxCustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiCheckboxCustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MultiCheckboxCustomLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void validateData() {

    }

    public void addTermsAndConditons(AttendeeDetailsResponseDto.AttendeeDto attendeeDto) {
        this.attendeeDto = attendeeDto;
        if (attendeeDto.options != null) {
            TextView child = (TextView) getActivityContext().getLayoutInflater().inflate(R.layout.attendee_form_label_item, this, false);
            //if(attendeeDto.mandatory) {
            child.setText(attendeeDto.label + " *");
            /*}else{
                child.setText(attendeeDto.label);
            }*/
            addView(child);

            Collection<String> values = attendeeDto.options.values();
            int checkBoxId = 0;
            for (String item : values) {
                //CheckBox cb = new CheckBox(getContext());
                //getTermsAndConditionsText(cb,item);
                //cb.setText(item);
                View view = getActivityContext().getLayoutInflater().inflate(R.layout.attendee_terms_conditions_item, this, false);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.chkIos);
                TextView textView = (TextView) view.findViewById(R.id.terms_n_conditions_link);
                checkBox.setText("");
                textView.setText(Html.fromHtml("<a href='" + item + "'>TERMS AND CONDITIONS</a>"));
                textView.setClickable(true);
                textView.setMovementMethod(LinkMovementMethod.getInstance());

                checkBox.setId(checkBoxId + 20);
                checkBoxId++;
                // mMultiCheckBoxList.add(cb);
                addView(view);
            }

           /* mErrorTextView = (TextView) getActivityContext().getLayoutInflater().inflate(R.layout.attendee_form_error_text_item, this, false);
            mErrorTextView.setVisibility(GONE);
            addView(mErrorTextView); */

        }
    }

  /*  private void getTermsAndConditionsText(final CheckBox checkBox, final String url){
        SpannableString termsString = new SpannableString("Terms & Conditions");
        ClickableSpan clickableSpanTerms = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                getContext().startActivity(i);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        termsString.setSpan(clickableSpanTerms, 0, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append("Agree to");
        sb.append(" ");
        sb.append(termsString);

        // TextView textView = (TextView) findViewById(R.id.hello);
        checkBox.setText(sb);
        checkBox.setTextColor(Color.BLACK);
        checkBox.setMovementMethod(LinkMovementMethod.getInstance());
        //mUserName.setHighlightColor(Color.TRANSPARENT);
    } */

    public void addElementsToMultiCheckbox(AttendeeDetailsResponseDto.AttendeeDto attendeeDto) {
        this.attendeeDto = attendeeDto;
        if (attendeeDto.options != null) {
            TextView child = (TextView) getActivityContext().getLayoutInflater().inflate(R.layout.attendee_form_label_item, this, false);
            if (attendeeDto.mandatory) {
                child.setText(attendeeDto.label + " *");
            } else {
                child.setText(attendeeDto.label);
            }
            addView(child);

            Collection<String> values = attendeeDto.options.values();
            int checkBoxId = 0;
            for (String item : values) {
                CheckBox cb = new CheckBox(getContext());
                cb.setText(item);
                cb.setId(checkBoxId + 10);
                checkBoxId++;
                mMultiCheckBoxList.add(cb);
                addView(cb);
            }

            mErrorTextView = (TextView) getActivityContext().getLayoutInflater().inflate(R.layout.attendee_form_error_text_item, this, false);
            mErrorTextView.setVisibility(GONE);
            addView(mErrorTextView);

        }
    }

    public void validateMultiCheckBoxData() {
        if (isCheckBoxesChecked()) {
            attendeeDto.isValid = true;
            mErrorTextView.setText("");
            mErrorTextView.setVisibility(GONE);
        } else {
            attendeeDto.isValid = false;
            mErrorTextView.setText(ConstantKeys.ValidationMessages.CHECK_UNCHECKED);
            mErrorTextView.setVisibility(VISIBLE);
        }
    }

    public boolean isCheckBoxesChecked() {
        if (!mMultiCheckBoxList.isEmpty() && mMultiCheckBoxList.size() > 0) {
            for (CheckBox cb : mMultiCheckBoxList) {
                if (cb.isChecked()) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> getAllSelectedCheckBoxData() {
        List<String> selectedCheckBoxes = new ArrayList<>();
        if (!mMultiCheckBoxList.isEmpty() && mMultiCheckBoxList.size() > 0) {
            for (CheckBox cb : mMultiCheckBoxList) {
                if (cb.isChecked()) {
                    selectedCheckBoxes.add(cb.getText().toString().trim());
                } else {
                    selectedCheckBoxes.remove(cb.getText().toString().trim());
                }
            }
        } else {
            selectedCheckBoxes = null;
        }
        return selectedCheckBoxes;
    }

    private FragmentActivity getActivityContext() {
        return ((FragmentActivity) getContext());
    }
}
