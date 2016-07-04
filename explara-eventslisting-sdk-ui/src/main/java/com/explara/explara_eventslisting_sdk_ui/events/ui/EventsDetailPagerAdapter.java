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
 * Created by anudeep on 15/09/15.
 */
public class EventsDetailPagerAdapter extends FragmentStatePagerAdapter implements EventsDetailPagerFragment.GetFragmentListener {

    private List<CollectionEventsDto.Events> eventsList;

    Map<Integer, EventDetailFragment> fragmentsMap = new HashMap<>();

    public EventsDetailPagerAdapter(FragmentManager fm, List<CollectionEventsDto.Events> eventsList) {
        super(fm);
        this.eventsList = eventsList;
    }


    @Override
    public Fragment getItem(int position) {
        EventDetailFragment eventDetailFragment = EventDetailFragment.getInstance(eventsList.get(position).id);

//        EventDetailFragment eventDetailFragment = EventDetailFragment.newInstance(CollectionsManager.newInstance().getCollectionEvent(mCollectionPos).getCollectionEvents().get(count).getEvents().get(position).getId());
        fragmentsMap.put(position, eventDetailFragment);
        return eventDetailFragment;
    }


    @Override
    public int getCount() {
        return eventsList.size();
//        return CollectionsManager.newInstance().getCollectionEvent(mCollectionPos).getCollectionEvents().get(count).getEvents().size();
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
