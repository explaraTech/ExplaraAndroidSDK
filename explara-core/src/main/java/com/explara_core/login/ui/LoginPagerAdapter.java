package com.explara_core.login.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.explara_core.common.LoginBaseFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ananthasooraj on 9/4/15.
 */
public class LoginPagerAdapter extends FragmentStatePagerAdapter {

    private String[] title = {"SIGN UP", "LOGIN"};
    public Map<Integer, LoginBaseFragment> mLoginFragmentMap = new HashMap();

    public LoginPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.title[position];
    }

    @Override
    public Fragment getItem(int position) {
        LoginBaseFragment baseFragment;
        switch (position) {
            case 0:
                baseFragment = new SignUpTabFragment();
                mLoginFragmentMap.put(position, baseFragment);
                return baseFragment;

            case 1:
                baseFragment = new LoginTabFragment();
                mLoginFragmentMap.put(position, baseFragment);
                return baseFragment;
            default:
                return null;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mLoginFragmentMap.remove(position);
    }

    @Override
    public int getCount() {
        return this.title.length;
    }

    public LoginBaseFragment getLoginFragment(int position){
        return mLoginFragmentMap.get(position);
    }
}
