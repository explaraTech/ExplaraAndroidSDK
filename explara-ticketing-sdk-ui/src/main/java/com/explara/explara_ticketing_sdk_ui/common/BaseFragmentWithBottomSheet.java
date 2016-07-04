package com.explara.explara_ticketing_sdk_ui.common;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.explara.explara_ticketing_sdk_ui.R;
import com.explara_core.utils.FragmentHelper;

/**
 * Created by Debasish on 11/01/16.
 */
public abstract class BaseFragmentWithBottomSheet extends TicketingBaseFragment implements Animator.AnimatorListener {

    private FrameLayout mContainerLayout;
    public FrameLayout mSlidingLayout;
    private boolean isReverseAnimation;
    int animationHeightVal;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_layout_with_bottom_sheet, container, false);
        initViews(view);
        addContainerLayout(mContainerLayout, inflater);
        addBottomLayout(mSlidingLayout, inflater);
        setListener();
        return view;
    }

    private void initViews(View view) {
        mSlidingLayout = (FrameLayout) view.findViewById(R.id.bottom_sheet);
        mContainerLayout = (FrameLayout) view.findViewById(R.id.container_layout);
    }

    private void setListener() {
        mSlidingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "lsandfklasndflkns", Toast.LENGTH_LONG).show();
                dismissKeyboard();
                hideShowBottomSlideContainer();
            }
        });
    }

    protected abstract void addBottomLayout(FrameLayout slidingLayout, LayoutInflater inflater);

    protected abstract void addContainerLayout(FrameLayout view, LayoutInflater inflater);


    public void hideShowBottomSlideContainer() {

        mContainerLayout.post(new Runnable() {
            @Override
            public void run() {

                if (isReverseAnimation) {
                    animationHeightVal = 0;
                    isReverseAnimation = false;
                } else {
                    isReverseAnimation = true;
                }

                final ValueAnimator homeSectionAnimator = ValueAnimator.ofInt(mSlidingLayout.getMeasuredHeight(), animationHeightVal);//mMainContainer.getMeasuredHeight());

                homeSectionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = mSlidingLayout.getLayoutParams();
                        layoutParams.height = val;
                        mSlidingLayout.setLayoutParams(layoutParams);
                    }
                });

                homeSectionAnimator.setDuration(500);
                homeSectionAnimator.addListener(BaseFragmentWithBottomSheet.this);
                homeSectionAnimator.start();
            }

        });
    }


    @Override
    public void refresh() {

    }


    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    public void showBottomSheetFrgament(Fragment parentFragment, Fragment fragmentToShow, float dimension) {
        animationHeightVal = (int) dimension;
        FragmentHelper.replaceChildFragment(parentFragment, mSlidingLayout.getId(), fragmentToShow);
        hideShowBottomSlideContainer();
    }

    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    public boolean shouldHideOnBackpress() {
        return isReverseAnimation;
    }

}
