package com.explara.explara_payment_sdk.payment.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.explara.explara_payment_sdk.R;
import com.explara.explara_payment_sdk.payment.PaymentManager;
import com.explara.explara_payment_sdk.payment.dto.SendMutipleEmailDto;
import com.explara_core.utils.Constants;
import com.explara_core.utils.Log;

/**
 * Created by debasishpanda on 12/09/15.
 */
public class EmailTicketDialogFragment extends DialogFragment {

    private static final String TAG = EmailTicketDialogFragment.class.getSimpleName();
    private String mEventId;
    private Button mDismissBtn;
    private Button mSendBtn;
    private String mMultipleEmailIds;
    private TextInputLayout mMultipleEmailIdLayout;
    private EditText mEmailIdText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_email_ticket_dialog, container, false);
        setDialogTitle();
        extractArguments();
        initView(rootView);
        return rootView;
    }

    public static EmailTicketDialogFragment getInstance(String eventId) {
        EmailTicketDialogFragment emailTicketFragment = new EmailTicketDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EVENT_ID, eventId);
        emailTicketFragment.setArguments(bundle);
        return emailTicketFragment;
    }

    public void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mEventId = args.getString(Constants.EVENT_ID);
        }
    }

    public void setDialogTitle() {
        getDialog().setTitle("Email Ticket");
    }

    public void initView(View view) {
        mMultipleEmailIdLayout = (TextInputLayout) view.findViewById(R.id.email_text_input);
        mEmailIdText = (EditText) view.findViewById(R.id.emailid_edit_text);
        mDismissBtn = (Button) view.findViewById(R.id.dismiss_btn);
        mDismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mSendBtn = (Button) view.findViewById(R.id.email_tickets);
        googleAnalyticsSendScreenName();

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMultipleEmailIds = String.valueOf(mEmailIdText.getText()).trim();
                if (!mMultipleEmailIds.isEmpty() && !mMultipleEmailIds.equals("")) {
                    if (checkForAllVaildEmailIds(mMultipleEmailIds)) {
                        mMultipleEmailIdLayout.setError(null);
                        onSendBtnClick(mMultipleEmailIds);
                    } else {
                        mMultipleEmailIdLayout.setError("Enter valid emailids");
                    }
                } else {
                    mMultipleEmailIdLayout.setError("Enter valid emailids");
                }
            }
        });
    }

    private void googleAnalyticsSendScreenName() {
        if (PaymentManager.getInstance().mAnalyticsListener != null) {
            PaymentManager.getInstance().mAnalyticsListener.sendScreenName(getResources().getString(R.string.email_ticket_dailog), getActivity().getApplication(), getContext());
        }
        //AnalyticsHelper.sendScreenName(getResources().getString(R.string.email_ticket_dailog), getActivity().getApplication(), getContext());
    }

    public boolean checkForAllVaildEmailIds(String mMultipleEmailIds) {
        boolean valid = true;
        String[] emailIds = mMultipleEmailIds.split(",");
        for (String email : emailIds) {
            Log.i("eamilid", email);
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                valid = false;
                return valid;
            }
        }
        return valid;
    }

    public void onSendBtnClick(String mMultipleEmailIds) {
        PaymentManager.getInstance().onSendBtnClick(getActivity().getApplicationContext(), mMultipleEmailIds, new PaymentManager.SendMutipleEmailListener() {
            @Override
            public void onMultipleEmailSent(SendMutipleEmailDto sendMutipleEmailDto) {
                if (getActivity() != null && sendMutipleEmailDto != null) {
                    if (sendMutipleEmailDto.res.equals("1")) {
                        Toast.makeText(getActivity().getApplicationContext(), sendMutipleEmailDto.msg, Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), sendMutipleEmailDto.msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onMultipleEmailSendFailed(VolleyError volleyError) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity().getApplicationContext(), "Email sending failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    }

}
