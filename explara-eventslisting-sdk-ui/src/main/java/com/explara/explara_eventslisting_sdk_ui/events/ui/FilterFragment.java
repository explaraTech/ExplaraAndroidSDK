package com.explara.explara_eventslisting_sdk_ui.events.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.explara.explara_eventslisting_sdk_ui.R;
import com.explara.explara_eventslisting_sdk_ui.common.EventBaseFragment;
import com.explara.explara_eventslisting_sdk_ui.utils.EventsFilter;
import com.explara_core.utils.PreferenceManager;

/**
 * Created by akshaya on 14/01/16.
 */
public class FilterFragment extends EventBaseFragment {

    private static final String TAG = FilterFragment.class.getSimpleName();
    public Button mBtnAppyFilter;
    public ImageView mPopularImg;
    public ImageView mTrendingImg;
    public ImageView mMostViewedImg;

    public ImageView mThisWeekImg;
    public ImageView mTodayImg;
    public ImageView mThisMonthImg;

    public TextView mUnderRs499TextView;
    public TextView mRs500to999TextView;
    public TextView mRs1000to4999TextView;
    public TextView mRs5000nAboveTextView;


    public int mCheckedIdPrice = EventsFilter.INVALID_FILTER_OPTION;
    public int mCheckedIdTime = EventsFilter.INVALID_FILTER_OPTION;
    private int mCheckedIdSort = EventsFilter.INVALID_FILTER_OPTION;


    public boolean mPriceIsChecked = false;
    public boolean mTimeIsChecked = false;
    public boolean mSortIsChecked = false;


    public TextView mPopularTextView;
    public TextView mTrendingTextView;
    public TextView mMostViewedTextView;

    public TextView mTodayTextView;
    public TextView mThisWeekTextView;
    public TextView mThisMonthTextView;


    public boolean PopularClicked = false;
    public boolean TrendingClicked = false;
    public boolean MostViewedClicked = false;

    public boolean thisMonthImageClicked;
    public boolean thisWeekImageClicked;
    public boolean todayImageClicked;

    public boolean TextUnder499Clicked;
    public boolean Text500to999Clicked = false;
    public boolean Text1000to4999Clicked = false;
    public boolean Text5000AboveClicked = false;

    public String timeSortClicked;


    public String priceSortClicked;
    public String sortByClicked;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_new_filter, container, false);

        intiViews(view);
        return view;
    }


    private void intiViews(View view) {
        mPopularTextView = (TextView) view.findViewById(R.id.popular_text);
        mTrendingTextView = (TextView) view.findViewById(R.id.trending_text);
        mMostViewedTextView = (TextView) view.findViewById(R.id.most_viewed_text);

        mTodayTextView = (TextView) view.findViewById(R.id.today_text);
        mThisMonthTextView = (TextView) view.findViewById(R.id.this_month_text);
        mThisWeekTextView = (TextView) view.findViewById(R.id.this_week_text);

        mBtnAppyFilter = (Button) view.findViewById(R.id.apply_filter);
        mPopularImg = (ImageView) view.findViewById(R.id.radio_btn_popular);
        mPopularImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popularImageClicked();
            }
        });
        mTrendingImg = (ImageView) view.findViewById(R.id.radio_btn_trending);
        mTrendingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trendingImageClicked();


            }
        });
        mMostViewedImg = (ImageView) view.findViewById(R.id.radio_btn_most_viewed);
        mMostViewedImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mostViewedImageClicked();
            }
        });

        mThisWeekImg = (ImageView) view.findViewById(R.id.radio_btn_this_week);
        mThisWeekImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                thisWeekImageClicked();

            }
        });
        mThisMonthImg = (ImageView) view.findViewById(R.id.radio_btn_this_month);
        mThisMonthImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisMonthImageClicked();


            }
        });
        mTodayImg = (ImageView) view.findViewById(R.id.radio_btn_today);
        mTodayImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int id = EventsFilter.FilterOptions.TODAY;
                todayImageClicked();


            }
        });

        mUnderRs499TextView = (TextView) view.findViewById(R.id.radio_btn_0_499rs);
        mUnderRs499TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text499Clicked();
//                PreferenceManager.newInstance()

            }

        });

        mRs500to999TextView = (TextView) view.findViewById(R.id.radio_btn_500_999rs);
        mRs500to999TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hundredFiveAboveClicked();
            }
        });


        mRs1000to4999TextView = (TextView) view.findViewById(R.id.radio_btn_1000_4999rs);
        mRs1000to4999TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thousandOneAboveClicked();
            }
        });


        mRs5000nAboveTextView = (TextView) view.findViewById(R.id.radio_btn_5000_above);
        mRs5000nAboveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thousandFiveAboveClicked();

            }
        });


        mBtnAppyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPriceIsChecked || mTimeIsChecked || mSortIsChecked) {
                    if (getParentFragment() instanceof CollectionEventFragment) {


//                        Log.d(TAG, "" + v.getTag());
                        if (todayImageClicked) {

                            timeSortClicked = "today";
                            PreferenceManager.getInstance(getActivity().getApplicationContext()).setFilterTimeSelected(timeSortClicked);
                            thisWeekImageClicked = false;
                            thisMonthImageClicked = false;


//                        mCheckedIdTime = EventsFilter.FilterOptions.PRICE_RANGE_UPTO_499;
                            mCheckedIdTime = EventsFilter.FilterOptions.TODAY;

                        }
                        if (thisWeekImageClicked) {

                            timeSortClicked = "thisweek";
                            PreferenceManager.getInstance(getActivity().getApplicationContext()).setFilterTimeSelected(timeSortClicked);
                            thisMonthImageClicked = false;
                            todayImageClicked = false;


                            mCheckedIdTime = EventsFilter.FilterOptions.THISWEEK;

                        }
                        if (thisMonthImageClicked) {


                            timeSortClicked = "thismonth";
                            PreferenceManager.getInstance(getActivity().getApplicationContext()).setFilterTimeSelected(timeSortClicked);
                            thisWeekImageClicked = false;
                            todayImageClicked = false;
                            mCheckedIdTime = EventsFilter.FilterOptions.THISMONTH;

                        }
                        if (TrendingClicked) {
                            sortByClicked = "trending";
                            PreferenceManager.getInstance(getActivity().getApplicationContext()).setFilterOptionSortBy(sortByClicked);
                            PopularClicked = false;
                            MostViewedClicked = false;


                            mCheckedIdSort = EventsFilter.FilterOptions.TRENDING;
                        }
                        if (PopularClicked) {
                            sortByClicked = "popularity";
                            PreferenceManager.getInstance(getActivity().getApplicationContext()).setFilterOptionSortBy(sortByClicked);
                            MostViewedClicked = false;
                            TrendingClicked = false;

                            mCheckedIdSort = EventsFilter.FilterOptions.POPULAR;


                        }
                        if (MostViewedClicked) {
                            sortByClicked = "mostviewed";
                            PreferenceManager.getInstance(getActivity().getApplicationContext()).setFilterOptionSortBy(sortByClicked);
                            TrendingClicked = false;
                            PopularClicked = false;


                            mCheckedIdSort = EventsFilter.FilterOptions.PAGEVIEW;
                        }
                        if (TextUnder499Clicked) {

                            priceSortClicked = "four";
                            PreferenceManager.getInstance(getActivity().getApplicationContext()).setFilterPrice(priceSortClicked);
                            Text5000AboveClicked = false;
                            Text500to999Clicked = false;
                            Text1000to4999Clicked = false;

                            mCheckedIdPrice = EventsFilter.FilterOptions.PRICE_RANGE_UPTO_499;

                        }
                        if (Text500to999Clicked) {
                            priceSortClicked = "five";
                            PreferenceManager.getInstance(getActivity().getApplicationContext()).setFilterPrice(priceSortClicked);
                            Text5000AboveClicked = false;
                            TextUnder499Clicked = false;

                            Text1000to4999Clicked = false;


                            mCheckedIdPrice = EventsFilter.FilterOptions.PRICE_RANGE_UPTO_999;
                        }
                        if (Text1000to4999Clicked) {
                            priceSortClicked = "thousand";
                            PreferenceManager.getInstance(getActivity().getApplicationContext()).setFilterPrice(priceSortClicked);
                            Text5000AboveClicked = false;
                            TextUnder499Clicked = false;

                            Text500to999Clicked = false;

                            mCheckedIdPrice = EventsFilter.FilterOptions.PRICE_RANGE_UPTO_4999;
                        }
                        if (Text5000AboveClicked) {

                            priceSortClicked = "fivethousand";
                            PreferenceManager.getInstance(getActivity().getApplicationContext()).setFilterPrice(priceSortClicked);
                            Text1000to4999Clicked = false;
                            TextUnder499Clicked = false;
                            Text500to999Clicked = false;


                            mCheckedIdPrice = EventsFilter.FilterOptions.PRICE_RANGE_5000_AND_ABOVE;
                        }

                        ((CollectionEventFragment) getParentFragment()).filterBy(mCheckedIdTime, mCheckedIdSort, mCheckedIdPrice);
                        ((CollectionEventFragment) getParentFragment()).hideShowBottomSlideContainer();
                    }
                } else {
                    ((CollectionEventFragment) getParentFragment()).hideShowBottomSlideContainer();

                }
            }
        });


        view.findViewById(R.id.header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CollectionEventFragment) getParentFragment()).hideShowBottomSlideContainer();
            }
        });

    }


    void thousandFiveAboveClicked() {
        if (!Text5000AboveClicked) {
            mRs5000nAboveTextView.setTextColor(getResources().getColor(R.color.filter_select_text));
            mRs5000nAboveTextView.setTypeface(null, Typeface.BOLD);
            mUnderRs499TextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mRs1000to4999TextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mRs500to999TextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mPriceIsChecked = true;
            Text5000AboveClicked = true;

            TextUnder499Clicked = false;
            Text500to999Clicked = false;
            Text1000to4999Clicked = false;


        } else {
            mRs5000nAboveTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            String unclickedPrice = "clear";
            PreferenceManager.getInstance(getContext()).setFilterPrice(unclickedPrice);

            Text5000AboveClicked = false;
            mPriceIsChecked = false;
        }
    }

    void thousandOneAboveClicked() {

        if (!Text1000to4999Clicked) {
            mRs1000to4999TextView.setTextColor(getResources().getColor(R.color.filter_select_text));
            mRs1000to4999TextView.setTypeface(null, Typeface.BOLD);
            mRs500to999TextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mRs5000nAboveTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mUnderRs499TextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mPriceIsChecked = true;
            Text1000to4999Clicked = true;


            Text5000AboveClicked = false;
            TextUnder499Clicked = false;
            Text500to999Clicked = false;


        } else {
            mRs1000to4999TextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            String unclickedPrice = "clear";
            PreferenceManager.getInstance(getContext()).setFilterPrice(unclickedPrice);
            Text1000to4999Clicked = false;
            mPriceIsChecked = false;

        }


    }


    void hundredFiveAboveClicked() {


        if (!Text500to999Clicked) {
            mRs500to999TextView.setTextColor(getResources().getColor(R.color.filter_select_text));
            mRs500to999TextView.setTypeface(null, Typeface.BOLD);
            mRs5000nAboveTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mUnderRs499TextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mRs1000to4999TextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mPriceIsChecked = true;
            Text500to999Clicked = true;


            Text5000AboveClicked = false;
            TextUnder499Clicked = false;
            Text1000to4999Clicked = false;


        } else {
            mRs500to999TextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            String unclickedPrice = "clear";
            PreferenceManager.getInstance(getContext()).setFilterPrice(unclickedPrice);
            Text500to999Clicked = false;
            mPriceIsChecked = false;

        }

    }


    void trendingImageClicked() {


        if (!TrendingClicked) {
            mTrendingImg.setImageResource(R.drawable.trending_active);
            mTrendingTextView.setTextColor(getResources().getColor(R.color.filter_select_text));
            mPopularTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mMostViewedTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));

            mMostViewedImg.setImageResource(R.drawable.view_inactive);
            mPopularImg.setImageResource(R.drawable.popular_inactive);
            mSortIsChecked = true;
            TrendingClicked = true;
            PopularClicked = false;
            MostViewedClicked = false;

        } else {
            mTrendingImg.setImageResource(R.drawable.trending_inactive);
            mTrendingTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));

            String unClicked = "clear";
            PreferenceManager.getInstance(getContext()).setFilterOptionSortBy(unClicked);
            TrendingClicked = false;
            mSortIsChecked = false;
        }
    }

    void popularImageClicked() {

        if (!PopularClicked) {
//            mCheckedIdSort = EventsFilter.FilterOptions.POPULAR;
            mPopularTextView.setTextColor(getResources().getColor(R.color.filter_select_text));
            mTrendingTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mMostViewedTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mPopularImg.setImageResource(R.drawable.popular_active);
            mTrendingImg.setImageResource(R.drawable.trending_inactive);
            mMostViewedImg.setImageResource(R.drawable.view_inactive);
            mSortIsChecked = true;
            PopularClicked = true;
            TrendingClicked = false;
            MostViewedClicked = false;


        } else {
            mPopularImg.setImageResource(R.drawable.popular_inactive);
            mPopularTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            String unClicked = "clear";
            PreferenceManager.getInstance(getContext()).setFilterOptionSortBy(unClicked);
            PopularClicked = false;
            mSortIsChecked = false;
        }


    }

    void mostViewedImageClicked() {


        if (!MostViewedClicked) {
            mMostViewedImg.setImageResource(R.drawable.view_active);
            mMostViewedTextView.setTextColor(getResources().getColor(R.color.filter_select_text));
            mTrendingTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mPopularTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mTrendingImg.setImageResource(R.drawable.trending_inactive);
            mPopularImg.setImageResource(R.drawable.popular_inactive);
            mSortIsChecked = true;
            MostViewedClicked = true;
            TrendingClicked = false;
            PopularClicked = false;
        } else {
            mMostViewedImg.setImageResource(R.drawable.view_inactive);
            mMostViewedTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            String unClicked = "clear";
            PreferenceManager.getInstance(getContext()).setFilterOptionSortBy(unClicked);
            MostViewedClicked = false;
            mSortIsChecked = false;
        }
    }

    void thisWeekImageClicked() {

        if (!thisWeekImageClicked) {
//            thisWeekImageClicked = false;
            mThisWeekImg.setImageResource(R.drawable.calendar_active);
            mThisWeekTextView.setTextColor(getResources().getColor(R.color.filter_select_text));
            mThisMonthTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mTodayTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mThisMonthImg.setImageResource(R.drawable.calendar_inactive);
            mTodayImg.setImageResource(R.drawable.calendar_inactive);

            mTimeIsChecked = true;
            thisWeekImageClicked = true;

            //....

            todayImageClicked = false;
            thisMonthImageClicked = false;
        } else {
            mThisWeekImg.setImageResource(R.drawable.calendar_inactive);
            mThisWeekTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            String unClicked = "clear";
            PreferenceManager.getInstance(getContext()).setFilterTimeSelected(unClicked);
            thisWeekImageClicked = false;
            mTimeIsChecked = false;

        }
    }

    void thisMonthImageClicked() {
        if (!thisMonthImageClicked) {
            mThisMonthImg.setImageResource(R.drawable.calendar_active);
            mThisMonthTextView.setTextColor(getResources().getColor(R.color.filter_select_text));
            mThisWeekTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mTodayTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));

            //.....
            thisWeekImageClicked = false;
            todayImageClicked = false;


            mTodayImg.setImageResource(R.drawable.calendar_inactive);
            mThisWeekImg.setImageResource(R.drawable.calendar_inactive);
            mTimeIsChecked = true;
            thisMonthImageClicked = true;
        } else {
            mThisMonthImg.setImageResource(R.drawable.calendar_inactive);
            mThisMonthTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            String unClicked = "clear";
            PreferenceManager.getInstance(getContext()).setFilterTimeSelected(unClicked);
            thisMonthImageClicked = false;
            mTimeIsChecked = false;
        }


    }

    void todayImageClicked() {
        if (!todayImageClicked) {


            mTodayImg.setImageResource(R.drawable.calendar_active);
            mTodayTextView.setTextColor(getResources().getColor(R.color.filter_select_text));
            mThisWeekTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mThisMonthTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));


            mThisWeekImg.setImageResource(R.drawable.calendar_inactive);
            mThisMonthImg.setImageResource(R.drawable.calendar_inactive);
//            mTodayImg.setTag();
            mTimeIsChecked = true;
//            buttonId = EventsFilter.FilterOptions.TODAY;

            //......

            todayImageClicked = true;

            thisWeekImageClicked = false;
            thisMonthImageClicked = false;


        } else {
            mTodayImg.setImageResource(R.drawable.calendar_inactive);
            mTodayTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            String unClicked = "clear";
            PreferenceManager.getInstance(getContext()).setFilterTimeSelected(unClicked);
            todayImageClicked = false;
            mTimeIsChecked = false;
        }
    }

    void text499Clicked() {

        if (!TextUnder499Clicked) {

            mUnderRs499TextView.setTextColor(getResources().getColor(R.color.filter_select_text));
            mUnderRs499TextView.setTypeface(null, Typeface.BOLD);
            mRs1000to4999TextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mRs5000nAboveTextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mRs500to999TextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            mPriceIsChecked = true;
            TextUnder499Clicked = true;

            Text5000AboveClicked = false;
            Text500to999Clicked = false;
            Text1000to4999Clicked = false;


        } else {
            mUnderRs499TextView.setTextColor(getResources().getColor(R.color.filter_unselect_text));
            String unclickedPrice = "clear";
            PreferenceManager.getInstance(getContext()).setFilterPrice(unclickedPrice);
            TextUnder499Clicked = false;
            mPriceIsChecked = false;
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String timeSelectedValue = PreferenceManager.getInstance(getActivity().getApplicationContext()).getFilterTimeSelected();
        String priceSelectedValue = PreferenceManager.getInstance(getActivity().getApplicationContext()).getFilterOptionPrice();
        String sortBySelectedValue = PreferenceManager.getInstance(getActivity().getApplicationContext()).getFilterOptionSortby();


        if (timeSelectedValue.equals("thismonth")) {

//            Toast.makeText(getActivity(), "ThisMonth", Toast.LENGTH_LONG).show();
            thisMonthImageClicked();


        }
        if (timeSelectedValue.equals("thisweek")) {
//            Toast.makeText(getActivity(), "ThisWeek", Toast.LENGTH_LONG).show();
            thisWeekImageClicked();
        }


        if (timeSelectedValue.equals("today")) {
//            Toast.makeText(getActivity(), "Today", Toast.LENGTH_LONG).show();
            todayImageClicked();
        }
        if (priceSelectedValue.equals("four")) {
            text499Clicked();
//           Toast.makeText(getActivity(), "499", Toast.LENGTH_LONG).show();
        }


        if (priceSelectedValue.equals("five")) {
//            text499Clicked();
//            TextUnder499Clicked();
//            Toast.makeText(getActivity(), "500to999", Toast.LENGTH_LONG).show();
            hundredFiveAboveClicked();


        }
        if (priceSelectedValue.equals("thousand")) {
//            Toast.makeText(getActivity(), "1000to5000", Toast.LENGTH_LONG).show();
            thousandOneAboveClicked();


        }

        if (priceSelectedValue.equals("fivethousand")) {
//          Toast.makeText(getActivity(), "5000to....", Toast.LENGTH_LONG).show();
            thousandFiveAboveClicked();
        }

        if (sortBySelectedValue.equals("trending")) {
//            Toast.makeText(getActivity(), "trending", Toast.LENGTH_LONG).show();
            trendingImageClicked();

        }

        if (sortBySelectedValue.equals("mostviewed")) {
//            Toast.makeText(getActivity(), "mostviewed", Toast.LENGTH_LONG).show();
            mostViewedImageClicked();


        }

        if (sortBySelectedValue.equals("popularity")) {
//          Toast.makeText(getActivity(), "popularity", Toast.LENGTH_LONG).show();
            popularImageClicked();
        }
    }


    @Override
    public void refresh() {
    }
}