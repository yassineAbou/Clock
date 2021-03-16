package com.example.clock0.Model;

import androidx.fragment.app.Fragment;

/**
 * Created by Yassine Abou on 2/7/2021.
 */
public class Tab {

    String mTabName;
    Fragment mFragment;

    public Tab(String tabName, Fragment fragment) {
        mTabName = tabName;
        mFragment = fragment;
    }

    public String getTabName() {
        return mTabName;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public void setTabName(String tabName) {
        mTabName = tabName;
    }

    public void setFragment(Fragment fragment) {
        mFragment = fragment;
    }
}
