package com.explara.explara_ticketing_sdk_ui.common;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.explara.explara_ticketing_sdk.tickets.TicketsManager;
import com.explara.explara_ticketing_sdk.tickets.dto.Order;
import com.explara.explara_ticketing_sdk.tickets.dto.SaveBuyerFormDataDto;
import com.explara.explara_ticketing_sdk_ui.R;
import com.explara.explara_ticketing_sdk_ui.attendee.ui.AttendeeFormDialogFragment;
import com.explara_core.common.BaseFragment;
import com.explara_core.communication.dto.CheckoutOfflineCallBackDataDto;
import com.explara_core.communication.dto.LunchPaymentActivityDataDto;
import com.explara_core.utils.Constants;
import com.explara_core.utils.PreferenceManager;

/**
 * Created by Debasish on 31/08/15.
 */
public abstract class TicketingBaseFragment extends BaseFragment {


    public void generateOrderNo(final boolean launchDefaultPayment, final String eventId, final String TAG) {
        TicketsManager.getInstance().generateOrderNo(getActivity().getApplicationContext(), eventId, new TicketsManager.GenerateOrderListener() {
            @Override
            public void onOrderGenerated(Order order) {
                if (getActivity() != null && order != null) {
                    if (Constants.STATUS_SUCCESS.equals(order.getStatus())) {
                        // save buyerdetails
                        TicketsManager.getInstance().prepareBuyerFormData();
                        saveBuyerFormData(launchDefaultPayment, eventId, TAG);
                    }
                }
            }

            @Override
            public void onOrderGeneratedFailed() {
                if (getActivity() != null) {
                    dismissMaterialDialog();
                    Toast.makeText(getActivity().getApplicationContext(), "Order generation failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    }

    public void generateOrderNoForMultiSession(final boolean launchDefaultPayment, final String eventId, final String TAG) {
        TicketsManager.getInstance().generateOrderNoForMultiSession(getActivity().getApplicationContext(), eventId, new TicketsManager.GenerateMultipleSessionOrderListener() {
            @Override
            public void onMultipleSessionOrderGenerated(Order order) {
                if (getActivity() != null && order != null) {
                    // save buyerdetails
                    TicketsManager.getInstance().prepareBuyerFormData();
                    saveBuyerFormData(launchDefaultPayment, eventId, TAG);
                }
            }

            @Override
            public void onMultipleSessionOrderGenerateFailed() {
                //mProgressbar.setVisibility(View.GONE);
                dismissMaterialDialog();
                Toast.makeText(getActivity().getApplicationContext(), "Order generation failed", Toast.LENGTH_SHORT).show();
            }
        }, TAG);

    }

    public void generateOrderNoForSession(final boolean launchDefaultPayment, final String mEventId, final String TAG) {
        TicketsManager.getInstance().generateOrderNoForSession(getActivity().getApplicationContext(), mEventId, new TicketsManager.GenerateSessionOrderListener() {
            @Override
            public void onSessionOrderGenerated(Order order) {
                if (getActivity() != null && order != null) {
                    // save buyerdetails
                    TicketsManager.getInstance().prepareBuyerFormData();
                    saveBuyerFormData(launchDefaultPayment, mEventId, TAG);
                }
            }

            @Override
            public void onSessionOrderGenerateFailed() {
                if (getActivity() != null) {
                    //mProgressbar.setVisibility(View.GONE);
                    dismissMaterialDialog();
                    Toast.makeText(getActivity().getApplicationContext(), "Order generation failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // Save buyer form data - Attendee form disabled
    public void saveBuyerFormData(final boolean launchdefaultpaymentOption, final String eventId, final String TAG) {

        TicketsManager.getInstance().saveBuyerFormData(getContext(), new TicketsManager.SaveBuyerFormDataListener() {
            @Override
            public void onBuyerFormDataSaved(SaveBuyerFormDataDto saveBuyerFormDataDto) {
                if (getActivity() != null && saveBuyerFormDataDto != null) {
                    if (saveBuyerFormDataDto.status.equals(Constants.STATUS_SUCCESS)) {
                        dismissMaterialDialog();
                        navigateToPaymentSelectionActivity(launchdefaultpaymentOption, eventId, TAG);
                    } else {
                        dismissMaterialDialog();
                        Toast.makeText(getActivity(), saveBuyerFormDataDto.message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onBuyerFormDataSaveFailed(VolleyError error) {
                if (getActivity() != null) {
                    dismissMaterialDialog();
                    error.printStackTrace();
                    Toast.makeText(getActivity(), "Save buyer form data failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    }

    public void navigateToPaymentSelectionActivity(boolean launchdefaultpaymentOption, String eventId, String TAG) {

        if (TicketsManager.getInstance().mTotal == 0) {
            //checkoutOffline("free", eventId, TAG);
            // Call checkout offline listener
            CheckoutOfflineCallBackDataDto checkoutOfflineCallBackDataDto = new CheckoutOfflineCallBackDataDto();
            checkoutOfflineCallBackDataDto.eventId = eventId;
            checkoutOfflineCallBackDataDto.paymentMode = "free";
            checkoutOfflineCallBackDataDto.tag = TAG;
            TicketsManager.getInstance().mTicketListingCallBackListnener.onCheckoutOffline(getContext(), checkoutOfflineCallBackDataDto);
        } else {
            dismissMaterialDialog();
            LunchPaymentActivityDataDto lunchPaymentActivityDataDto = new LunchPaymentActivityDataDto();
            lunchPaymentActivityDataDto.eventId = eventId;
            lunchPaymentActivityDataDto.launchDefaultPaymentOption = launchdefaultpaymentOption;
            TicketsManager.getInstance().mTicketListingCallBackListnener.onPaymentActivity(getContext(), lunchPaymentActivityDataDto);

             /*//Intent intent = new Intent(getActivity(), PaymentActivity.class);
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(Constants.PAYMENT_PACKAGE_NAME, getActivity().getString(R.string.payment_activity)));
            intent.putExtra(Constants.EVENT_ID, eventId);
            if (launchdefaultpaymentOption) {
                intent.putExtra(TicketsDetailsFragment.SHOULD_LAUNCH_PREFFERED_WALLET, launchdefaultpaymentOption);
            }
            startActivity(intent);*/

        }
    }

    public void storeBuyerDetailsInPreference(String buyerName, String buyerEmail, String buyerMobile) {
        if (TextUtils.isEmpty(PreferenceManager.getInstance(getContext()).getUserName())) {
            PreferenceManager.getInstance(getContext()).setUserName(buyerName);
        }

        if (TextUtils.isEmpty(PreferenceManager.getInstance(getContext()).getEmail())) {
            PreferenceManager.getInstance(getContext()).setEmail(buyerEmail);
        }

        if (TextUtils.isEmpty(PreferenceManager.getInstance(getContext()).getPhoneNo())) {
            PreferenceManager.getInstance(getContext()).setPhoneNo(buyerMobile);
        }
    }

    public void saveAttendeeFormData(final String eventId, final String TAG) {

        TicketsManager.getInstance().saveBuyerFormData(getContext(), new TicketsManager.SaveBuyerFormDataListener() {
            @Override
            public void onBuyerFormDataSaved(SaveBuyerFormDataDto saveBuyerFormDataDto) {
                if (getActivity() != null && saveBuyerFormDataDto != null) {
                    dismissMaterialDialog();
                    if (Constants.STATUS_SUCCESS.equals(saveBuyerFormDataDto.status)) {
                        if (TicketsManager.getInstance().mTotal == 0) {
                            navigateToPaymentSelectionActivity(false, eventId, TAG);
                        } else {
                            if (PreferenceManager.getInstance(getContext()).isPreferredWalletOptionSelected()) {
                                showConfirmAttendeeDialog(eventId);
                            } else {
                                navigateToPaymentSelectionActivity(false, eventId, TAG);
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), saveBuyerFormDataDto.message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onBuyerFormDataSaveFailed(VolleyError error) {
                if (getActivity() != null) {
                    dismissMaterialDialog();
                    error.printStackTrace();
                    Toast.makeText(getActivity(), "Save buyer form data failed.Please Try again", Toast.LENGTH_SHORT).show();
                }
            }
        }, TAG);
    }

    public void showConfirmAttendeeDialog(String eventId) {
        AttendeeFormDialogFragment attendeeFormDialogFragment = AttendeeFormDialogFragment.newInstance(eventId);
        attendeeFormDialogFragment.show(getChildFragmentManager(), "attendee dialog");
    }

    // Load prefered wallet in inline wallet part
    public void handlePaymentOptionSection(View view) {
        if (view != null) {
            boolean isPreferredWalletSelected = PreferenceManager.getInstance(getContext()).isPreferredWalletOptionSelected();
            view.findViewById(R.id.payment_details_tab).setVisibility(isPreferredWalletSelected ? View.VISIBLE : View.GONE);
            view.findViewById(R.id.separator_2).setVisibility(isPreferredWalletSelected ? View.VISIBLE : View.GONE);
            if (isPreferredWalletSelected) {
                ImageView paymentOptionImage = (ImageView) view.findViewById(R.id.payment_mode_name_images);
                if (PreferenceManager.getInstance(getContext()).getPreferredWalletOption() == PreferenceManager.PAY_TM_PREFERRED_WALLET)
                    paymentOptionImage.setImageResource(R.drawable.paytm);
                else {
                    paymentOptionImage.setImageResource(R.drawable.citrus);
                }
            }
        }
    }

    public void storePreferenceDataInBuyerDetailsDto() {
        if (TicketsManager.getInstance().mBuyerDetailWithOutAttendeeFormDto != null) {
            if (!TextUtils.isEmpty(PreferenceManager.getInstance(getContext()).getUserName())) {
                TicketsManager.getInstance().mBuyerDetailWithOutAttendeeFormDto.buyerName = PreferenceManager.getInstance(getContext()).getUserName();
            }

            if (!TextUtils.isEmpty(PreferenceManager.getInstance(getContext()).getEmail())) {
                TicketsManager.getInstance().mBuyerDetailWithOutAttendeeFormDto.buyerEmail = PreferenceManager.getInstance(getContext()).getEmail();
            }

            if (!TextUtils.isEmpty(PreferenceManager.getInstance(getContext()).getPhoneNo())) {
                TicketsManager.getInstance().mBuyerDetailWithOutAttendeeFormDto.buyerPhone = PreferenceManager.getInstance(getContext()).getPhoneNo();
            }
        }
    }

    /*public void loadBalanceForPrefferedWallet(final String TAG) {
        PreferenceManager preferenceManager = PreferenceManager.getInstance(getContext());
        if (preferenceManager.isPreferredWalletOptionSelected()) {
            int preferredWalletOption = preferenceManager.getPreferredWalletOption();
            if (preferredWalletOption == PreferenceManager.PAY_TM_PREFERRED_WALLET) {

                PaymentManager.getInstance().getPayTmUserDetails(getContext(), preferenceManager.getPayTmAccessToken(), new PaymentManager.FetchPaytmProfileListener() {
                    @Override
                    public void onFetchPaytmUserProfile(PayTmUserProfile payTmUserProfile, TextView textView) {
                        if (getActivity() != null)
                            textView.setText(String.format("Balance -  %s %s", getContext().getString(R.string.rupee_symbol), payTmUserProfile.WALLETBALANCE));
                    }

                    @Override
                    public void onFetchpaytmUserProfileFailed(VolleyError volleyError) {

                    }
                }, TAG, (TextView) getView().findViewById(R.id.payment_mode_name));
            } else if (preferredWalletOption == PreferenceManager.CITRUS_PREFFRED_WALLET) {
                CitrusClient.getInstance(getContext()).getBalance(new Callback<Amount>() {
                    @Override
                    public void success(Amount amount) {
                        if (getActivity() != null)
                            setBalance(getView(), String.format("Balance -  %s %s", getContext().getString(R.string.rupee_symbol), amount.getValueAsFormattedDouble("#.00")));

                    }

                    @Override
                    public void error(CitrusError citrusError) {
                        Logger.d(TAG + " get Balance failure " + citrusError.getMessage());
                    }
                });
            }
        }

    }*/


}
