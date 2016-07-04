package com.explara.explara_ticketing_sdk_ui.attendee.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.explara.explara_ticketing_sdk.attendee.dto.AttendeeDetailsResponseDto;
import com.explara.explara_ticketing_sdk_ui.common.TicketingBaseFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anudeep on 04/09/15.
 */
public class AttendeePagerAdapter extends FragmentStatePagerAdapter {


    private AttendeeDetailsResponseDto mAttendeeDetailsResponseDto;
    private Map<Integer, AttendeeFieldItemsFragment> mAttendeeFormFragmentMap = new HashMap<>();

    public AttendeePagerAdapter(FragmentManager fm, AttendeeDetailsResponseDto attendeeDetailsResponseDto) {
        super(fm);
        mAttendeeDetailsResponseDto = attendeeDetailsResponseDto;
    }

    @Override
    public Fragment getItem(int position) {
        AttendeeFieldItemsFragment attendeeFieldItemsFragment = AttendeeFieldItemsFragment.newInstance(position);
        mAttendeeFormFragmentMap.put(position, attendeeFieldItemsFragment);
        return attendeeFieldItemsFragment;
    }

    @Override
    public int getCount() {
        return mAttendeeDetailsResponseDto.attendeeform.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mAttendeeFormFragmentMap.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position + 1 + "";
    }

    public TicketingBaseFragment getFragment(int position) {
        return mAttendeeFormFragmentMap.get(position);
    }

    public void cleanUp() {
        if (mAttendeeFormFragmentMap != null) {
            mAttendeeFormFragmentMap.clear();
            mAttendeeFormFragmentMap = null;
        }
    }
}
