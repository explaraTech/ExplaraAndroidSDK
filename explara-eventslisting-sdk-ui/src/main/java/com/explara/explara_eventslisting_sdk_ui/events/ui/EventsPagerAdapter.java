package com.explara.explara_eventslisting_sdk_ui.events.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.explara.explara_eventslisting_sdk.events.EventsManger;
import com.explara.explara_eventslisting_sdk_ui.common.BaseFragmentWithBottomSheet;
import com.explara_core.utils.ConstantKeys;
import com.explara_core.utils.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anudeep on 04/09/15.
 */
public class EventsPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = EventsPagerAdapter.class.getSimpleName();
    private int mCollectionPosition;
    private String mCategoryName;
    private String mCityName;
    private String collectionId;
    private String mSourcePage;
    private Map<Integer, CollectionEventFragment> collectionEventFragmentMap = new HashMap<>();
    private Context mContext;

    public EventsPagerAdapter(FragmentManager fm, int collectionPosition, String sourcePage, Context context) {
        super(fm);
        this.mContext = context;
        mCollectionPosition = collectionPosition;
        mSourcePage = sourcePage;
        if (ConstantKeys.SOURCE_PAGE.FROM_NOTIFICATION_SCREEN.equals(mSourcePage)) {
            // Here mCollectionPosition is collection Id
            collectionId = Integer.toString(mCollectionPosition);
        } else {
            EventsManger.getInstance().mAppCallBackListener.getSelectedCollectionIdFromPosition(mCollectionPosition);
            collectionId = EventsManger.getInstance().mSelectedSessionId;
            // Here mCollectionPosition is actual collection position
            //collectionId = CollectionsManager.newInstance().homeScreenDto.collections.get(mCollectionPosition).id;
        }
    }

    public EventsPagerAdapter(FragmentManager fm, String categoryName, String cityName, Context context) {
        super(fm);
        mCategoryName = categoryName;
        mCityName = cityName;
        this.mContext = context;
    }

    public EventsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        CollectionEventFragment collectionEventFragment;
        if (Constants.EXPLARA_ONLY) {
            if (TextUtils.isEmpty(mCategoryName)) {
                collectionEventFragment = CollectionEventFragment.getInstance(mCollectionPosition, position, CollectionEventFragment.From.EVENT, mSourcePage);
            } else {
                collectionEventFragment = CollectionEventFragment.getInstance(mCategoryName, position, CollectionEventFragment.From.CATEGORY, mCityName, mSourcePage);
            }
        } else {
            collectionEventFragment = CollectionEventFragment.getInstance(position, CollectionEventFragment.From.ORG, null);
        }
        collectionEventFragmentMap.put(position, collectionEventFragment);
        return collectionEventFragment;
    }

    @Override
    public int getCount() {
        if (Constants.EXPLARA_ONLY) {
            if (TextUtils.isEmpty(mCategoryName)) {
                return EventsManger.getInstance().getEventsCount(collectionId);
            } else {
                return EventsManger.getInstance().categoriesResponseDtoMap.get(mCategoryName).getSubCategory().length;
            }
        } else {
            return 2;// upcoming and past
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        collectionEventFragmentMap.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (Constants.EXPLARA_ONLY) {
            if (TextUtils.isEmpty(mCategoryName)) {
                return EventsManger.getInstance().mCollectionEventsDtoMap.get(collectionId).collections.collectionCategories.get(position);
            } else {
                String category = EventsManger.getInstance().categoriesResponseDtoMap.get(mCategoryName).getSubCategory()[position];
                //  googleAnalyticsSendScreenName(category);
                return category;
            }
        } else {
            return EventsManger.getInstance().mOrganizerEventsDto.allEvents.get(position).listType;
        }

    }

    public BaseFragmentWithBottomSheet getFragment(int position) {
        return collectionEventFragmentMap.get(position);
    }

    public void cleanUp() {
        if (collectionEventFragmentMap != null) {
            collectionEventFragmentMap.clear();
            collectionEventFragmentMap = null;
        }
    }
}
