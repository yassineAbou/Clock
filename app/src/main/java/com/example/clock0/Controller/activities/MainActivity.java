package com.example.clock0.Controller.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.clock0.Controller.Adapter.PagerAdapter;
import com.example.clock0.Controller.Fragments.TimerDialogFragment;
import com.example.clock0.Controller.Fragments.alarmFragment;
import com.example.clock0.Controller.Fragments.stopWatchFragment;
import com.example.clock0.Controller.Fragments.timerFragment;
import com.example.clock0.Model.Tab;
import com.example.clock0.R;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements TimerDialogFragment.OkButtonClickedListener {
    TabLayout mTableLayout;
    ViewPager mViewPager;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createTabs();
    }

    private void createTabs() {
        mTableLayout = findViewById(R.id.activity_main_tabs_TabLayout);
        mViewPager = findViewById(R.id.activity_main_pages_ViewPager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

        adapter.addTab(new Tab("Alarm", new alarmFragment()));
        adapter.addTab(new Tab("Stopwatch", new stopWatchFragment()));
        adapter.addTab(new Tab("Timer", new timerFragment()));

        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(adapter);
        mTableLayout.setupWithViewPager(mViewPager);


    }


    //receive the time from TimerDialogFragment and send it to timerFragment
    @Override
    public void okButtonClickedListener(long fullTime) {
        timerFragment fragment = timerFragment.newInstance(fullTime);

    }
}