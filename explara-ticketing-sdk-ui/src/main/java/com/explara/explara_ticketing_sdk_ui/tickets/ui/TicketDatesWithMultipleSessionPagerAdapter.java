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
public class TicketDatesWithMultipleSessionPagerAdapter extends FragmentStatePagerAdapter {

    private String mEventId;
    private String mCurrency;
    private int mSelectedMonthPos;

    private HashMap<Integer, TicketDetailsByDateMultipleSessionFragment> mFragmentsMap = new HashMap<>();

    public TicketDatesWithMultipleSessionPagerAdapter(FragmentManager fm, String eventId, int selectedMonthPos, String mCurrency) {
        super(fm);
        mEventId = eventId;
        this.mCurrency = mCurrency;
        mSelectedMonthPos = selectedMonthPos;
    }

    @Override
    public Fragment getItem(int position) {
        TicketDetailsByDateMultipleSessionFragment ticketDetailsByDateMultipleSessionFragment = TicketDetailsByDateMultipleSessionFragment.getInstance(position, mEventId, mSelectedMonthPos, mCurrency);
        mFragmentsMap.put(position, ticketDetailsByDateMultipleSessionFragment);
        return ticketDetailsByDateMultipleSessionFragment;
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

    public TicketDetailsByDateMultipleSessionFragment getFragment(int pos) {
        return mFragmentsMap.get(pos);
    }

    public void cleanUp() {
        if (mFragmentsMap != null) {
            mFragmentsMap.clear();
            mFragmentsMap = null;
        }
    }
}
