package com.explara.explara_ticketing_sdk_ui.attendee.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.explara.explara_ticketing_sdk.attendee.dto.AttendeeDetailsResponseDto;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk.tickets.dto.AttendeeFields;
import com.explara.explara_ticketing_sdk.tickets.dto.BuyerFormDataObj;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Log;
import com.explara_core.utils.Utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anudeep on 14/01/16.
 */
public class AttendeeCustomLayout extends LinearLayout {

    private static final String TAG = AttendeeCustomLayout.class.getSimpleName();
    private List<AttendeeDetailsResponseDto.AttendeeDto> attendeeDtoList;


    public AttendeeCustomLayout(Context context) {
        super(context);
    }

    public AttendeeCustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AttendeeCustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AttendeeCustomLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void validateData() {

    }

    public void addElements(List<AttendeeDetailsResponseDto.AttendeeDto> attendeeDtoList) {
        this.attendeeDtoList = attendeeDtoList;
        for (final AttendeeDetailsResponseDto.AttendeeDto attendeeDto : attendeeDtoList) {
            Log.d(TAG, attendeeDto.type);
            if (attendeeDto.options != null) {
                Log.d(TAG, attendeeDto.options.size() + "");
            }
            switch (attendeeDto.type) {
                case ConstantKeys.AttendeeFormTypes.LABEL:
                    TextView child = (TextView) ((FragmentActivity) getContext()).getLayoutInflater().inflate(R.layout.attendee_form_label_item, this, false);
                    if (attendeeDto.mandatory) {
                        child.setText(attendeeDto.label + " *");
                    } else {
                        child.setText(attendeeDto.label);
                    }
                    child.setTag(attendeeDto);
                    addView(child);
                    break;
                case ConstantKeys.AttendeeFormTypes.EDIT_TEXT:
                    TextView editTextLabel = (TextView) ((FragmentActivity) getContext()).getLayoutInflater().inflate(R.layout.attendee_form_label_item, this, false);
                    if (attendeeDto.mandatory) {
                        editTextLabel.setText(attendeeDto.label + " *");
                    } else {
                        editTextLabel.setText(attendeeDto.label);
                    }
                    addView(editTextLabel);

                    if (ConstantKeys.AttendeeFormValidationKeys.DATE.equals(attendeeDto.validation)) {
                        View view = getActivityContext().getLayoutInflater().inflate(R.layout.attendee_form_date_item, this, false);
                        final TextView textView = (TextView) view.findViewById(R.id.selected_date_text);

                        Button selectDateBtn = (Button) view.findViewById(R.id.select_date_btn);
                        selectDateBtn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                getActivityContext().startActivityForResult(new Intent(),attendeeDto.id);
                                dismissKeyboard();
                                DatePickerFragment newFragment = new DatePickerFragment();
                                newFragment.setDateText(textView, ConstantKeys.AttendeeFormValidationKeys.DATE);
                                newFragment.show(getActivityContext().getSupportFragmentManager(), "datePicker");
                            }
                        });
                        //textView.setId(attendeeDto.id);
                        view.setTag(attendeeDto);
                        addView(view);
                    } else if (ConstantKeys.AttendeeFormValidationKeys.DATE_AND_TIME.equals(attendeeDto.validation)) {
                        View view = getActivityContext().getLayoutInflater().inflate(R.layout.attendee_form_date_time_item, this, false);
                        final TextView textView = (TextView) view.findViewById(R.id.selected_date_time_text);

                        Button selectDateBtn = (Button) view.findViewById(R.id.select_date_time_btn);
                        selectDateBtn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                getActivityContext().startActivityForResult(new Intent(),attendeeDto.id);
                                dismissKeyboard();
                                DatePickerFragment newFragment = new DatePickerFragment();
                                newFragment.setDateText(textView, ConstantKeys.AttendeeFormValidationKeys.DATE_AND_TIME);
                                newFragment.show(getActivityContext().getSupportFragmentManager(), "datePicker");
                            }
                        });
                        //textView.setId(attendeeDto.id);
                        view.setTag(attendeeDto);
                        addView(view);
                    } else {
                        TextInputLayout textInputLayout = (TextInputLayout) getActivityContext().getLayoutInflater().inflate(R.layout.attendee_form_edit_text_item, this, false);
                        final EditText editTxt = textInputLayout.getEditText();
                        editTxt.setTag(attendeeDto.id);
                        editTxt.setHint(TextUtils.isEmpty(attendeeDto.description) ? "" : attendeeDto.description);
                        //editTxt.setId(attendeeDto.id);
                        setInputType(editTxt, attendeeDto);
                        textInputLayout.setTag(attendeeDto);
                        addView(textInputLayout);
                    }
                    break;
                case ConstantKeys.AttendeeFormTypes.TEXT_AREA:
                    TextView textAreaLabel = (TextView) ((FragmentActivity) getContext()).getLayoutInflater().inflate(R.layout.attendee_form_label_item, this, false);
                    if (attendeeDto.mandatory) {
                        textAreaLabel.setText(attendeeDto.label + " *");
                    } else {
                        textAreaLabel.setText(attendeeDto.label);
                    }
                    addView(textAreaLabel);

                    TextInputLayout textAreaInputLayout = (TextInputLayout) getActivityContext().getLayoutInflater().inflate(R.layout.attendee_form_edit_text_item, this, false);
                    EditText textArea = textAreaInputLayout.getEditText();
                    textArea.setSingleLine(false);
                    textArea.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    textArea.setHint(TextUtils.isEmpty(attendeeDto.description) ? "" : attendeeDto.description);
                    textArea.setLines(3);
                    textAreaInputLayout.setTag(attendeeDto);
                    addView(textAreaInputLayout);
                case ConstantKeys.AttendeeFormTypes.RADIO_GROUP:
                    //createRadioButton(attendeeDto);
                    createRadioGroup(attendeeDto);
                    break;
                case ConstantKeys.AttendeeFormTypes.SPINNER:
                    createSpinner(attendeeDto);
                    break;
                case ConstantKeys.AttendeeFormTypes.CHECK_BOX:
                    // createCheckBoxes(attendeeDto);
                    createMultiCheckbox(attendeeDto);
                    break;
                case ConstantKeys.AttendeeFormTypes.FILE:
                    TextView fileUploadLabel = (TextView) ((FragmentActivity) getContext()).getLayoutInflater().inflate(R.layout.attendee_form_label_item, this, false);
                    if (attendeeDto.mandatory) {
                        fileUploadLabel.setText(attendeeDto.label + " *");
                    } else {
                        fileUploadLabel.setText(attendeeDto.label);
                    }
                    addView(fileUploadLabel);

                    View view = getActivityContext().getLayoutInflater().inflate(R.layout.attendee_form_file_upload_item, this, false);
                    final TextView textView = (TextView) view.findViewById(R.id.uploaded_url_text);
                    final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBarUpload);
                    Button uploadBtn = (Button) view.findViewById(R.id.upload_file_btn);
                    uploadBtn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                                getActivityContext().startActivityForResult(new Intent(),attendeeDto.id);
                            if (Utility.isNetworkAvailable(getContext())) {
                                dismissKeyboard();
                                progressBar.setVisibility(VISIBLE);
                                textView.setText("");
                                Intent intent = new Intent();
                                if (Build.VERSION.SDK_INT >= 19) {
                                    // For Android KitKat, we use a different intent to ensure
                                    // we can
                                    // get the file path from the returned intent URI
                                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                                    intent.setType("*/*");
                                } else {
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    intent.setType("file/*");
                                }
                                getActivityContext().startActivityForResult(intent, attendeeDto.requestCodeFileUpload);
                            } else {
                                Toast.makeText(getContext(), getContext().getString(R.string.internet_check_msg), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //textView.setId(attendeeDto.id);
                    view.setTag(attendeeDto);
                    addView(view);
                    break;
            }
        }
    }

    private void createSpinner(AttendeeDetailsResponseDto.AttendeeDto attendeeDto) {
        if (attendeeDto.options != null) {
            TextView child = (TextView) getActivityContext().getLayoutInflater().inflate(R.layout.attendee_form_label_item, this, false);
            if (attendeeDto.mandatory) {
                child.setText(attendeeDto.label + " *");
            } else {
                child.setText(attendeeDto.label);
            }
            addView(child);

            List<String> list = new ArrayList<>(attendeeDto.options.values());
            Spinner spinner = new Spinner(getContext());
            spinner.setPrompt(attendeeDto.label);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, list);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setTag(attendeeDto);
            addView(spinner);
        }
    }

   /* private void createCheckBoxes(AttendeeDetailsResponseDto.AttendeeDto attendeeDto) {
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
                addView(cb);
            }

        }
    }*/

    private void createRadioGroup(AttendeeDetailsResponseDto.AttendeeDto attendeeDto) {
        if (attendeeDto.options != null) {
            RadioGroupCustomLayout radioGroupCustomLayout = (RadioGroupCustomLayout) getActivityContext().getLayoutInflater().inflate(R.layout.attendee_radio_group_fragment, this, false);
            radioGroupCustomLayout.addElementsToRadioGraup(attendeeDto);
            radioGroupCustomLayout.setTag(attendeeDto);
            addView(radioGroupCustomLayout);
        }
    }

    private void createMultiCheckbox(AttendeeDetailsResponseDto.AttendeeDto attendeeDto) {
        if (attendeeDto.options != null) {
            MultiCheckboxCustomLayout multiCheckboxCustomLayout = (MultiCheckboxCustomLayout) getActivityContext().getLayoutInflater().inflate(R.layout.attendee_multi_checkbox_fragment, this, false);
            if (ConstantKeys.AttendeeFormTypes.TERMS_AND_CONDITIONS.equals(attendeeDto.label)) {
                multiCheckboxCustomLayout.addTermsAndConditons(attendeeDto);
            } else {
                multiCheckboxCustomLayout.addElementsToMultiCheckbox(attendeeDto);
            }
            multiCheckboxCustomLayout.setTag(attendeeDto);
            addView(multiCheckboxCustomLayout);
        }
    }

   /* private void createRadioButton(AttendeeDetailsResponseDto.AttendeeDto attendeeDto) {
        if (attendeeDto.options != null) {

            TextView child = (TextView) getActivityContext().getLayoutInflater().inflate(R.layout.attendee_form_label_item, this, false);
            if (attendeeDto.mandatory) {
                child.setText(attendeeDto.label + " *");
            } else {
                child.setText(attendeeDto.label);
            }
            addView(child); // Add the multicheckbox label

            final RadioButton[] rb = new RadioButton[5];
            RadioGroup rg = new RadioGroup(getContext()); //create the RadioGroup
            rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL

            Collection<String> values = attendeeDto.options.values();
            for (int i = 0; i < values.size(); i++) {
                rb[i] = new RadioButton(getContext());
//            rg.addView(rb[i]);
                rb[i].setText((String) values.toArray()[i]);
                rb[i].setId(i + 100);
                rg.addView(rb[i]);
            }
            rg.setTag(attendeeDto);
            addView(rg);//you add the whole RadioGroup to the layout

            TextView errorChild = (TextView) getActivityContext().getLayoutInflater().inflate(R.layout.attendee_form_error_text_item, this, false);
            errorChild.setVisibility(GONE);
            addView(child);
        }

    }*/

    private void setInputType(TextView view, AttendeeDetailsResponseDto.AttendeeDto attendeeDto) {
        if (!TextUtils.isEmpty(attendeeDto.validation)) {
            switch (attendeeDto.validation) {
                case ConstantKeys.AttendeeFormValidationKeys.EMAIL_ADDRESS:
                    view.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    break;
                case ConstantKeys.AttendeeFormValidationKeys.LINKS:
                    view.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
                    break;
                case ConstantKeys.AttendeeFormValidationKeys.DECIMAL:
                    view.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    break;
                case ConstantKeys.AttendeeFormValidationKeys.DIGITS:
                    view.setInputType(InputType.TYPE_CLASS_PHONE);
                    break;
                case ConstantKeys.AttendeeFormValidationKeys.FLOAT:
                    view.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    break;

            }
        }

        if ("Mobile No.".equals(attendeeDto.label)) {
            view.setInputType(InputType.TYPE_CLASS_PHONE);
        }
    }

    public AttendeeFields prepareAttndeeDtoData(String id, String label, String value, List<String> valueArr) {
        AttendeeFields attendeeFields = new AttendeeFields();
        attendeeFields.id = id;
        attendeeFields.label = label;
        if (!TextUtils.isEmpty(value)) {
            attendeeFields.value = value;
        } else {
            attendeeFields.value = null;
        }
        if (valueArr != null) {
            if (!valueArr.isEmpty() && valueArr.size() > 0) {
                attendeeFields.valueArr = valueArr;
            } else {
                attendeeFields.valueArr = null;
            }
        }
        return attendeeFields;
    }

    public void prepareAttendeeFormData(int attendeeNo) {
        BuyerFormDataObj mBuyerFormDataObj = TicketsManager.getInstance().mBuyerFormDataObj;
        mBuyerFormDataObj.orderNo = TicketsManager.getInstance().mOrder.getOrderNo();
        List<AttendeeFields> attendeeFields = new ArrayList<>();
        int attendeeFieldCount = getChildCount();
        for (int i = 0; i < attendeeFieldCount; i++) {
            View view = getChildAt(i);
            AttendeeDetailsResponseDto.AttendeeDto attendeeDto = (AttendeeDetailsResponseDto.AttendeeDto) view.getTag();
            if (attendeeDto != null) {
                switch (attendeeDto.type) {
                    case ConstantKeys.AttendeeFormTypes.EDIT_TEXT:
                        if (ConstantKeys.AttendeeFormValidationKeys.DATE.equals(attendeeDto.validation)) {
                            LinearLayout linearLayout = (LinearLayout) view;
                            TextView textView = (TextView) linearLayout.getChildAt(1);
                            attendeeFields.add(prepareAttndeeDtoData(attendeeDto.id, attendeeDto.label, textView.getText().toString().trim(), null));
                        } else if (ConstantKeys.AttendeeFormValidationKeys.DATE_AND_TIME.equals(attendeeDto.validation)) {
                            LinearLayout linearLayout = (LinearLayout) view;
                            TextView textView = (TextView) linearLayout.getChildAt(1);
                            attendeeFields.add(prepareAttndeeDtoData(attendeeDto.id, attendeeDto.label, textView.getText().toString().trim(), null));
                        } else {
                            TextInputLayout textInputLayout = (TextInputLayout) view;
                            attendeeFields.add(prepareAttndeeDtoData(attendeeDto.id, attendeeDto.label, textInputLayout.getEditText().getText().toString().trim(), null));
                        }
                        break;
                    case ConstantKeys.AttendeeFormTypes.RADIO_GROUP:
                        RadioGroupCustomLayout radioGroupCustomLayout = (RadioGroupCustomLayout) view;
                        attendeeFields.add(prepareAttndeeDtoData(attendeeDto.id, attendeeDto.label, radioGroupCustomLayout.getSelectedRadioText(), null));
                        break;
                    case ConstantKeys.AttendeeFormTypes.CHECK_BOX:
                        MultiCheckboxCustomLayout multiCheckboxCustomLayout = (MultiCheckboxCustomLayout) view;
                        if (ConstantKeys.AttendeeFormTypes.TERMS_AND_CONDITIONS.equals(attendeeDto.label)) {
                            Collection<String> values = attendeeDto.options.values();
                            String termsVal = "";
                            for (String item : values) {
                                termsVal = item;
                            }
                            attendeeFields.add(prepareAttndeeDtoData(attendeeDto.id, attendeeDto.label, termsVal, null));
                        } else {
                            attendeeFields.add(prepareAttndeeDtoData(attendeeDto.id, attendeeDto.label, null, multiCheckboxCustomLayout.getAllSelectedCheckBoxData()));
                        }
                        break;
                    case ConstantKeys.AttendeeFormTypes.SPINNER:
                        Spinner spinner = (Spinner) view;
                        attendeeFields.add(prepareAttndeeDtoData(attendeeDto.id, attendeeDto.label, spinner.getSelectedItem().toString().trim(), null));
                        break;
                    case ConstantKeys.AttendeeFormTypes.TEXT_AREA:
                        TextInputLayout textInputLayout = (TextInputLayout) view;
                        attendeeFields.add(prepareAttndeeDtoData(attendeeDto.id, attendeeDto.label, textInputLayout.getEditText().getText().toString().trim(), null));
                        break;
                    case ConstantKeys.AttendeeFormTypes.LABEL:
                        attendeeFields.add(prepareAttndeeDtoData(attendeeDto.id, attendeeDto.label, attendeeDto.label, null));
                        break;
                    case ConstantKeys.AttendeeFormTypes.FILE:
                        attendeeFields.add(prepareAttndeeDtoData(attendeeDto.id, attendeeDto.label, attendeeDto.fileName, null));
                        break;
                    default:
                        break;
                }
            }
        }
        mBuyerFormDataObj.attendees.put("attendee_" + attendeeNo, attendeeFields);
    }

    public HashMap<String, String> getBuyerDataFromFirstAttendeeForm() {
        HashMap<String, String> buyerDataMap = new HashMap<>();
        int attendeeFieldCount = getChildCount();
        for (int i = 0; i < attendeeFieldCount; i++) {
            View view = getChildAt(i);
            AttendeeDetailsResponseDto.AttendeeDto attendeeDto = (AttendeeDetailsResponseDto.AttendeeDto) view.getTag();

            if (attendeeDto != null) {
                switch (attendeeDto.label) {
                    case "Name":
                        TextInputLayout textInputLayoutName = (TextInputLayout) view;
                        EditText editTextName = textInputLayoutName.getEditText();
                        String enteredDataName = editTextName.getText().toString();
                        buyerDataMap.put("Name", enteredDataName);
                        break;
                    case "Email":
                        TextInputLayout textInputLayoutEmail = (TextInputLayout) view;
                        EditText editTextEmail = textInputLayoutEmail.getEditText();
                        String enteredDataEmail = editTextEmail.getText().toString();
                        buyerDataMap.put("Email", enteredDataEmail);
                        break;
                    case "Mobile No.":
                        TextInputLayout textInputLayoutMobile = (TextInputLayout) view;
                        EditText editTextMobile = textInputLayoutMobile.getEditText();
                        String enteredDataMobile = editTextMobile.getText().toString();
                        buyerDataMap.put("Mobile", enteredDataMobile);
                        break;

                }
            }
        }
        return buyerDataMap;
    }

    public boolean checkFormDataFilled() {
        int attendeeFieldCount = getChildCount();
        for (int i = 0; i < attendeeFieldCount; i++) {
            View view = getChildAt(i);
            AttendeeDetailsResponseDto.AttendeeDto attendeeDto = (AttendeeDetailsResponseDto.AttendeeDto) view.getTag();

            if (attendeeDto != null) {
                switch (attendeeDto.type) {
                    case ConstantKeys.AttendeeFormTypes.EDIT_TEXT:
                        if (attendeeDto.mandatory) {
                            if (ConstantKeys.AttendeeFormValidationKeys.DATE.equals(attendeeDto.validation)) {
                                LinearLayout linearLayout = (LinearLayout) view;
                                // TextView textView = (TextView)view.findViewById(R.id.selected_date_text);
                                TextView textView = (TextView) linearLayout.getChildAt(1);
                                validateDate(textView, attendeeDto);
                            } else if (ConstantKeys.AttendeeFormValidationKeys.DATE_AND_TIME.equals(attendeeDto.validation)) {
                                LinearLayout linearLayout = (LinearLayout) view;
                                TextView textView = (TextView) linearLayout.getChildAt(1);
                                validateDateTime(textView, attendeeDto);
                            } else {
                                TextInputLayout textInputLayout = (TextInputLayout) view;
                                validateEditTextData(attendeeDto, textInputLayout);
                            }
                        } else {
                            attendeeDto.isValid = true;
                        }
                        break;
                    case ConstantKeys.AttendeeFormTypes.RADIO_GROUP:
                        if (attendeeDto.mandatory) {
                            RadioGroupCustomLayout radioGroupCustomLayout = (RadioGroupCustomLayout) view;
                            radioGroupCustomLayout.validateRedioGroupData();
                        } else {
                            attendeeDto.isValid = true;
                        }
                        break;
                    case ConstantKeys.AttendeeFormTypes.CHECK_BOX:
                        if (ConstantKeys.AttendeeFormTypes.TERMS_AND_CONDITIONS.equals(attendeeDto.label)) {
                            attendeeDto.isValid = true;
                        } else {
                            if (attendeeDto.mandatory) {
                                MultiCheckboxCustomLayout multiCheckboxCustomLayout = (MultiCheckboxCustomLayout) view;
                                multiCheckboxCustomLayout.validateMultiCheckBoxData();
                            } else {
                                attendeeDto.isValid = true;
                            }
                        }
                        break;
                    case ConstantKeys.AttendeeFormTypes.SPINNER:
                        attendeeDto.isValid = true;
                        break;
                    case ConstantKeys.AttendeeFormTypes.TEXT_AREA:
                        if (attendeeDto.mandatory) {
                            TextInputLayout textInputLayout = (TextInputLayout) view;
                            validateTextAreaData(attendeeDto, textInputLayout);
                        } else {
                            attendeeDto.isValid = true;
                        }
                        break;
                    case ConstantKeys.AttendeeFormTypes.LABEL:
                        attendeeDto.isValid = true;
                        break;
                    case ConstantKeys.AttendeeFormTypes.FILE:
                        if (attendeeDto.mandatory) {
                            LinearLayout linearLayout = (LinearLayout) view;
                            // TextView textView = (TextView)view.findViewById(R.id.selected_date_text);
                            ProgressBar progressBar = (ProgressBar) linearLayout.getChildAt(1);
                            progressBar.setVisibility(GONE);
                            TextView textView = (TextView) linearLayout.getChildAt(2);
                            validateFile(textView, attendeeDto);
                        } else {
                            attendeeDto.isValid = true;
                        }
                        break;
                    default:
                        break;
                }
            }

        }

        for (AttendeeDetailsResponseDto.AttendeeDto attendeeDto : attendeeDtoList) {
            if (!attendeeDto.isValid) {
                return false;
            }
        }
        return true;
    }

    private void validateTextAreaData(AttendeeDetailsResponseDto.AttendeeDto attendeeDto, TextInputLayout textInputLayout) {
        EditText editText = textInputLayout.getEditText();
        String enteredData = editText.getText().toString();
        if (TextUtils.isEmpty(enteredData)) {
            textInputLayout.setError(ConstantKeys.ValidationMessages.EMPTY_FIELD);
            attendeeDto.isValid = false;
        } else {
            textInputLayout.setError(null);
            attendeeDto.isValid = true;
        }
    }

    private void validateEditTextData(AttendeeDetailsResponseDto.AttendeeDto attendeeDto, TextInputLayout textInputLayout) {

        if (!TextUtils.isEmpty(attendeeDto.validation) && attendeeDto.validation != null) {
            switch (attendeeDto.validation) {
                case ConstantKeys.AttendeeFormValidationKeys.EMAIL_ADDRESS:
                    attendeeDto.isValid = validateEmailAddress(textInputLayout);
                    break;
                case ConstantKeys.AttendeeFormValidationKeys.LINKS:
                    attendeeDto.isValid = validateUrl(textInputLayout);
                    break;
                case ConstantKeys.AttendeeFormValidationKeys.DIGITS:
                    attendeeDto.isValid = validateDigits(textInputLayout);
                    break;
                case ConstantKeys.AttendeeFormValidationKeys.DECIMAL:
                    attendeeDto.isValid = validateDecimal(textInputLayout);
                    break;
                case ConstantKeys.AttendeeFormValidationKeys.FLOAT:
                    attendeeDto.isValid = validateDecimal(textInputLayout);
                    break;
                default:
                    attendeeDto.isValid = validateDefaultField(textInputLayout);
                    break;

            }
        } else {
            attendeeDto.isValid = validateDefaultField(textInputLayout);
        }
    }

    public boolean validateDefaultField(TextInputLayout textInputLayout) {
        boolean isValid = true;
        EditText editText = textInputLayout.getEditText();
        String enteredData = editText.getText().toString();
        if (TextUtils.isEmpty(enteredData)) {
            textInputLayout.setError(ConstantKeys.ValidationMessages.EMPTY_FIELD);
            isValid = false;
        } else {
            textInputLayout.setError(null);
            isValid = true;
        }
        return isValid;
    }

    public boolean validateEmailAddress(TextInputLayout textInputLayout) {
        boolean isValid = true;
        EditText editText = textInputLayout.getEditText();
        String enteredData = editText.getText().toString();
        if (TextUtils.isEmpty(enteredData)) {
            textInputLayout.setError(ConstantKeys.ValidationMessages.EMPTY_EMAIL);
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(enteredData).matches()) {
            textInputLayout.setError(ConstantKeys.ValidationMessages.INVALID_EMAIL);
            isValid = false;
        } else {
            textInputLayout.setError(null);
            isValid = true;
        }
        return isValid;
    }

    public boolean validateUrl(TextInputLayout textInputLayout) {
        boolean isValid = true;
        EditText editText = textInputLayout.getEditText();
        String enteredData = editText.getText().toString();
        if (TextUtils.isEmpty(enteredData)) {
            textInputLayout.setError(ConstantKeys.ValidationMessages.EMPTY_URL);
            isValid = false;
        } else if (!Patterns.WEB_URL.matcher(enteredData).matches()) {
            textInputLayout.setError(ConstantKeys.ValidationMessages.INVALID_URL);
            isValid = false;
        } else {
            textInputLayout.setError(null);
            isValid = true;
        }
        return isValid;
    }

    public boolean validateDigits(TextInputLayout textInputLayout) {
        boolean isValid = true;
        EditText editText = textInputLayout.getEditText();
        String enteredData = editText.getText().toString();
        if (TextUtils.isEmpty(enteredData)) {
            textInputLayout.setError(ConstantKeys.ValidationMessages.EMPTY_FIELD);
            isValid = false;
        } else {
            textInputLayout.setError(null);
            isValid = true;
        }
        return isValid;
    }

    public boolean validateDecimal(TextInputLayout textInputLayout) {
        boolean isValid = true;
        EditText editText = textInputLayout.getEditText();
        String enteredData = editText.getText().toString();
        if (TextUtils.isEmpty(enteredData)) {
            textInputLayout.setError(ConstantKeys.ValidationMessages.EMPTY_FIELD);
            isValid = false;
        } else {
            textInputLayout.setError(null);
            isValid = true;
        }
        return isValid;
    }

    public void validateDateTime(TextView textView, AttendeeDetailsResponseDto.AttendeeDto attendeeDto) {
        String enteredData = textView.getText().toString();
        if (TextUtils.isEmpty(enteredData)) {
            textView.setText(ConstantKeys.ValidationMessages.DATE_TIME_FORMAT);
            textView.setTextColor(getResources().getColor(R.color.accentColor));
            attendeeDto.isValid = false;
        } else {
            attendeeDto.isValid = true;
            textView.setTextColor(Color.BLACK);
        }
    }

    public void validateDate(TextView textView, AttendeeDetailsResponseDto.AttendeeDto attendeeDto) {
        //EditText editText = textInputLayout.getEditText();
        String enteredData = textView.getText().toString();
        if (TextUtils.isEmpty(enteredData)) {
            textView.setText(ConstantKeys.ValidationMessages.DATE_FORMAT);
            textView.setTextColor(getResources().getColor(R.color.accentColor));
            attendeeDto.isValid = false;
        } else {
            attendeeDto.isValid = true;
            textView.setTextColor(Color.BLACK);
        }

    }

    public void validateFile(TextView textView, AttendeeDetailsResponseDto.AttendeeDto attendeeDto) {
        //EditText editText = textInputLayout.getEditText();
        //String enteredData = textView.getText().toString();
        if (TextUtils.isEmpty(attendeeDto.fileName)) {
            textView.setText(ConstantKeys.ValidationMessages.UPLOAD_FILE_EMPTY);
            textView.setTextColor(getResources().getColor(R.color.accentColor));
            attendeeDto.isValid = false;
        } else {
            if (!TransferState.COMPLETED.equals(attendeeDto.fileUploadStatus)) {
                textView.setText(ConstantKeys.ValidationMessages.RE_UPLOAD);
                textView.setTextColor(getResources().getColor(R.color.accentColor));
                attendeeDto.isValid = false;
            } else {
                attendeeDto.isValid = true;
                textView.setTextColor(Color.BLACK);
            }
        }

    }

    public void updateFileUploadStatus(int requestCode, TransferState newState) {
        int attendeeFieldCount = getChildCount();
        for (int i = 0; i < attendeeFieldCount; i++) {
            View view = getChildAt(i);
            AttendeeDetailsResponseDto.AttendeeDto attendeeDto = (AttendeeDetailsResponseDto.AttendeeDto) view.getTag();

            if (attendeeDto != null && ConstantKeys.AttendeeFormTypes.FILE.equals(attendeeDto.type)) {
                if (attendeeDto.requestCodeFileUpload == requestCode) {
                    LinearLayout linearLayout = (LinearLayout) view;
                    ProgressBar progressBar = (ProgressBar) linearLayout.getChildAt(1);
                    progressBar.setVisibility(GONE);
                    TextView textView = (TextView) linearLayout.getChildAt(2);
                    if (TransferState.COMPLETED.equals(newState)) {
                        textView.setText(ConstantKeys.ValidationMessages.UPLOAD_SUCCESS);
                        textView.setTextColor(Color.BLACK);
                    } else if (TransferState.PAUSED.equals(newState)) {
                        textView.setText(ConstantKeys.ValidationMessages.UPLOAD_FILE);
                        textView.setTextColor(getResources().getColor(R.color.accentColor));
                    } else {
                        textView.setText(ConstantKeys.ValidationMessages.UPLOAD_FAILED);
                        textView.setTextColor(getResources().getColor(R.color.accentColor));
                    }
                }
            }
        }
    }

    private FragmentActivity getActivityContext() {
        return ((FragmentActivity) getContext());
    }

    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivityContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getActivityContext().getCurrentFocus().getWindowToken(), 0);
        }
    }
}
