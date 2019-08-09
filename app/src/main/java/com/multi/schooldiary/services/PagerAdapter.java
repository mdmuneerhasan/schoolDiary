package com.multi.schooldiary.services;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> list;
    String[] days;
    public PagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list=list;
        days=new String[]{"My Shop","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    }

    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return days[position];
    }
}
