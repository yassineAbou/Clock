package com.example.clock0.Controller.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.clock0.Model.Tab;

import java.util.ArrayList;

/**
 * Created by Yassine Abou on 2/7/2021.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Tab> tabs = new ArrayList<>();

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public void addTab(Tab tab) {
        tabs.add(tab);
    }


    @Override
    public Fragment getItem(int position) {
        return tabs.get(position).getFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTabName();
    }

    @Override
    public int getCount() {
        return tabs.size();
    }
}
