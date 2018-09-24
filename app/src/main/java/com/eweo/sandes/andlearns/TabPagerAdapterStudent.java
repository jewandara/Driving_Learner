package com.eweo.sandes.andlearns;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapterStudent extends FragmentStatePagerAdapter {

    int tabCount;

    public TabPagerAdapterStudent(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabStudentProfile tabSt1 = new TabStudentProfile();
                return tabSt1;
            case 1:
                TabStudentSchedule tabSt2 = new TabStudentSchedule();
                return tabSt2;
            case 2:
                TabStudentProgress tabSt3 = new TabStudentProgress();
                return tabSt3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

