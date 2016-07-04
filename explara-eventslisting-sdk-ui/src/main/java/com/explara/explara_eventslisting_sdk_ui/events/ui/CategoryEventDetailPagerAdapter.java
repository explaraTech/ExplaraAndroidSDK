package com.explara.explara_eventslisting_sdk_ui.events.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.explara.explara_eventslisting_sdk.events.dto.CollectionEventsDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anudeep on 24/10/15.
 */
public class CategoryEventDetailPagerAdapter extends FragmentStatePagerAdapter implements EventsDetailPagerFragment.GetFragmentListener {

    private String mCategoryName;
    private int mEventPosition;
    Map<Integer, EventDetailFragment> fragmentsMap = new HashMap<>();
    private List<CollectionEventsDto.Events> eventsList;

    public CategoryEventDetailPagerAdapter(FragmentManager fm, String categoryName, int position, List<CollectionEventsDto.Events> eventsList) {
        super(fm);
        mCategoryName = categoryName;
        mEventPosition = position;
        this.eventsList = eventsList;
    }

    @Override
    public Fragment getItem(int position) {
        EventDetailFragment eventDetailFragment = EventDetailFragment.getInstance(eventsList.get(position).id);
        fragmentsMap.put(position, eventDetailFragment);
        return eventDetailFragment;
    }

    @Override
    public int getCount() {
        return eventsList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        fragmentsMap.remove(position);
    }

    @Override
    public EventDetailFragment getFragment(int pos) {

        return fragmentsMap.get(pos);
    }
}
