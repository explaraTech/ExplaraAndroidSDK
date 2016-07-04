package com.explara.explara_ticketing_sdk_ui.tickets.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.explara.explara_ticketing_sdk.tickets.TicketsManager;

import java.util.HashMap;

/**
 * Created by anudeep on 04/09/15.
 */
public class MultidatePagerAdapter extends FragmentStatePagerAdapter {

    private String mEventId;
    private int mSelectedMonthPos;

    private HashMap<Integer, PackagesByDateFragment> mFragmentsMap = new HashMap<>();

    public MultidatePagerAdapter(FragmentManager fm, String eventId, int selectedMonthPos) {
        super(fm);
        mEventId = eventId;
        mSelectedMonthPos = selectedMonthPos;
    }

    @Override
    public Fragment getItem(int position) {
        PackagesByDateFragment packagesByDateFragment = PackagesByDateFragment.newInstance(position, mEventId, mSelectedMonthPos);
        mFragmentsMap.put(position, packagesByDateFragment);
        return packagesByDateFragment;
    }

    @Override
    public int getCount() {
        return TicketsManager.getInstance().getTicketDatesCount(mSelectedMonthPos);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mFragmentsMap.remove(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public PackagesByDateFragment getFragment(int pos) {
        return mFragmentsMap.get(pos);
    }

    public void cleanUp() {
        if (mFragmentsMap != null) {
            mFragmentsMap.clear();
            mFragmentsMap = null;
        }
    }
}
