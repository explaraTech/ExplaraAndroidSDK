package com.explara_core.login.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.explara_core.R;
import com.explara_core.common.LoginBaseFragment;
import com.explara_core.login.LoginScreenManager;
import com.explara_core.login.util.CleverTapHelper;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;
import com.explara_core.utils.PreferenceManager;


/**
 * Created by debasishpanda on 22/09/15.
 */
public class LoginNewFragment extends LoginBaseFragment implements TabLayout.OnTabSelectedListener {
    private static final String TAG = LoginNewFragment.class.getSimpleName();
    //private static final String SOURCE = "source";
    public ViewPager mViewPager;
    private TabLayout mTabLayout;
    private LoginPagerAdapter mLoginPagerAdapter;
    private Button mSkipText;
    public static String mSource = Constants.SIGNUP;
    public static String mRedirect;
    public static int mViewPagerPosition;
    private String mSkipBtnSource;
    private Button mSkipButton;

    public static LoginNewFragment getInstance(Intent intent) {
        LoginNewFragment loginNewFragment = new LoginNewFragment();
        String source = "";
        String redirect = "";
        String tutorialSkipBtn = "";
        int viewPagerPosition = 0;
        if (intent.getStringExtra(Constants.SOURCE) != null) {
            source = intent.getStringExtra(Constants.SOURCE);
            redirect = intent.getStringExtra(Constants.REDIRECT);
            tutorialSkipBtn = intent.getStringExtra(Constants.FROM_TUTORIAL_SCREEN);
            viewPagerPosition = intent.getIntExtra(ConstantKeys.IntentKeys.VIEW_PAGER_KEYS, 0);
        } else {
            source = Constants.SIGNUP;
        }
        Bundle bundle = new Bundle();
        bundle.putString(Constants.SOURCE, source);
        bundle.putString(Constants.REDIRECT, redirect);
        bundle.putString(Constants.SKIP_BUTTON, tutorialSkipBtn);
        bundle.putInt(ConstantKeys.IntentKeys.VIEW_PAGER_KEYS, viewPagerPosition);
        loginNewFragment.setArguments(bundle);
        return loginNewFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_new_login, container, false);
        extractArguments();
        initViews(view);
        //setSelectedTab();
        return view;
    }

    public void setSelectedTab() {

    }

    public void extractArguments() {
        Bundle args = getArguments();
        if (args != null) {
            mSource = args.getString(Constants.SOURCE);
            mRedirect = args.getString(Constants.REDIRECT);
            mViewPagerPosition = args.getInt(ConstantKeys.IntentKeys.VIEW_PAGER_KEYS);
            mSkipBtnSource = args.getString(Constants.SKIP_BUTTON);
        }
    }

    public void initViews(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.login_view_pager);
        mTabLayout = (TabLayout) view.findViewById(R.id.login_tabs);
        mSkipButton = (Button) view.findViewById(R.id.skip_btn);
        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendSkipClickedToCleverTap();
                if (TextUtils.isEmpty(PreferenceManager.getInstance(getActivity().getApplicationContext()).getSelectedCity())) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME,"com.explara.android.selectCity.ui.SelectLocationActivity"));
                    startActivitySafely(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(Constants.BASE_PACKAGE_NAME,"com.explara.android.personalizedHome.ui.PersonalizeScreenActivity"));
                    startActivitySafely(intent);
                }
                getActivity().finish();

                googleAnalyticsSendScreenName();
            }
        });
    }

    private void sendSkipClickedToCleverTap() {
        Intent intent = new Intent();
        intent.setAction("com.explara.android.utils");
        intent.putExtra(Constants.CLEVER_TAP_TYPE, Constants.CLEVER_TAP_EVENT_TYPE);
        intent.putExtra(Constants.CLEVER_TAP_EVENT_KEY, CleverTapHelper.EventNames.SKIP_EVENT);
        getActivity().sendBroadcast(intent);


        /*CleverTapIntentDataDto cleverTapIntentDataDto = new CleverTapIntentDataDto();
        cleverTapIntentDataDto.screenName = Constants.LOGIN_PAGE_SKIP_BUTTON;
        cleverTapIntentDataDto.eventName =  null;

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.CLEVER_TAP_KEY, cleverTapIntentDataDto);
        Intent intent = new Intent();
        intent.setAction("com.explara.android.utils");
        intent.putExtras(bundle);
        getActivity().sendBroadcast(intent);*/
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLoginPagerAdapter = new LoginPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mLoginPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(mTabLayout) {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                    }
                }
        );
        if (mSource.equals(Constants.LOGIN)) {
            mViewPager.setCurrentItem(1);
        } else {
            mViewPager.setCurrentItem(0);
        }
        mTabLayout.setOnTabSelectedListener(this);
        if (!PreferenceManager.getInstance(getContext()).isAccountVerified()) {
            showVerifyAccountDialog(PreferenceManager.getInstance(getContext()).getEmail());
        }
    }

    public void showVerifyAccountDialog(String emailId) {
        VerifyAccountDialogFragment verifyAccountDialogFragment = VerifyAccountDialogFragment.newInstance(emailId);
        verifyAccountDialogFragment.show(getActivity().getSupportFragmentManager(), "Verify Account");
    }

    @Override
    public void refresh() {

    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //Toast.makeText(getActivity(), "Tab Selected : "+tab.getPosition(), Toast.LENGTH_LONG).show();
        mViewPager.setCurrentItem(tab.getPosition());
        if (tab.getPosition() == 0) {
            mSource = Constants.SIGNUP;
        } else {
            mSource = Constants.LOGIN;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (LoginBaseFragment baseFragment : mLoginPagerAdapter.mLoginFragmentMap.values()) {
            baseFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mLoginPagerAdapter.mLoginFragmentMap != null) {
            mLoginPagerAdapter.mLoginFragmentMap = null;
        }

        if (mLoginPagerAdapter != null) {
            mLoginPagerAdapter = null;
        }
    }

    private void googleAnalyticsSendScreenName() {
        if (LoginScreenManager.getInstance().mAnalyticsListener != null) {
            LoginScreenManager.getInstance().mAnalyticsListener.sendAnEvent(getString(R.string.event_category_skip),
                    getString(R.string.event_action_skip), getActivity().getApplication(), getContext());
        }
        //AnalyticsHelper.sendAnEvent(getString(R.string.event_category_skip), getString(R.string.event_action_skip), getActivity().getApplication(), getContext());
    }
}

