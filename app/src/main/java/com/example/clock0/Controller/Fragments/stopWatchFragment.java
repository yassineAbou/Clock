package com.example.clock0.Controller.Fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.clock0.Controller.Adapter.LapsRecyclerViewAdapter;
import com.example.clock0.Model.Lap;
import com.example.clock0.R;
import com.example.clock0.Ui.ViewAnimation;
import com.example.clock0.databinding.FragmentStopWatchBinding;


@SuppressWarnings("ALL")
public class stopWatchFragment extends Fragment {

    LapsRecyclerViewAdapter mAdapter;
    FragmentStopWatchBinding stopWatch;
    private static boolean isRunning = false;
    private static long isPauseOffSet;

    public stopWatchFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        stopWatch = FragmentStopWatchBinding.inflate(inflater,container,false);

        return stopWatch.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        playBehaviourOfButtons();
        initialArrayListOfLaps();

    }



    private void playBehaviourOfButtons() {

        stopWatch.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               //start the stopWatch
               stopWatch.start.setVisibility(View.GONE);
               stopWatch.resume.setVisibility(View.VISIBLE);
               stopWatch.resume.setTextColor(getResources().getColor(R.color.red));
               stopWatch.resume.setText("stop");
               stopWatch.lap.setVisibility(View.VISIBLE);
               stopWatch.lap.setText("lap");
                ViewAnimation.divergeTwoViewHorizontal(
                        getContext(),
                        stopWatch.resume,
                        stopWatch.lap);

               
               startStopWatch();

            }
        });

        stopWatch.resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stopWatch.resume.getText().toString().equals("resume")) {
                    //resume stopwatch
                    stopWatch.resume.setText("stop");
                    stopWatch.resume.setTextColor(getResources().getColor(R.color.red));
                    stopWatch.lap.setText("lap");

                    startStopWatch();

                }
                else {
                    //pause stopwatch
                    stopWatch.resume.setText("resume");
                    stopWatch.resume.setTextColor(getResources().getColor(R.color.gray1));
                    stopWatch.lap.setText("reset");

                    pauseStopWatch();
                }
            }
        });

        stopWatch.lap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stopWatch.lap.getText().toString().equals("lap")) {
                    // add the list under TextView with animation at the first time
                    if (mAdapter.getItemCount() == 0) {
                        stopWatch.recyclerview.setVisibility(View.VISIBLE);
                        ViewAnimation.moveToTheTopVertical(
                                getContext(),
                                stopWatch.chronometer,
                                stopWatch.recyclerview
                        );
                    }

                     createNewLap();
                }

                else {
                   //reset stopWatch
                    if (mAdapter.getItemCount() > 0) {
                        ViewAnimation.fadeView(getContext(), stopWatch.recyclerview);
                    }
                    stopWatch.start.setVisibility(View.VISIBLE);
                    stopWatch.resume.setTextColor(getResources().getColor(R.color.gray1));
                    stopWatch.resume.setVisibility(View.GONE);
                    stopWatch.lap.setVisibility(View.GONE);
                    stopWatch.recyclerview.setVisibility(View.GONE);

                    ViewAnimation.comeCloserTwoViewHorizontal(
                            getContext(),
                            stopWatch.resume,
                            stopWatch.lap);

                    resetStopWatch();
                }
            }
        });
    }


    private void startStopWatch() {
           if (!isRunning) {
        stopWatch.chronometer.setBase(SystemClock.elapsedRealtime() - isPauseOffSet);
        stopWatch.chronometer.start();
        isRunning = true;
          }

    }

    private void pauseStopWatch() {
        if(isRunning) {
            stopWatch.chronometer.stop();
         isPauseOffSet = SystemClock.elapsedRealtime() - stopWatch.chronometer.getBase();
          isRunning = false;
        }
    }

    private void resetStopWatch() {
        stopWatch.chronometer.stop();
        stopWatch.chronometer.setBase(SystemClock.elapsedRealtime());
        isPauseOffSet = 0;
        isRunning = false;

        mAdapter.deleteAllLaps();
    }

    private void initialArrayListOfLaps() {
        mAdapter = new LapsRecyclerViewAdapter();
        LinearLayoutManager lm = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                true);

        lm.setStackFromEnd(true);
        stopWatch.recyclerview.setLayoutManager(lm);
        stopWatch.recyclerview.setAdapter(mAdapter);
    }

    private void createNewLap() {
        mAdapter.addNewLap(
                new Lap(SystemClock.elapsedRealtime() - stopWatch.chronometer.getBase())
        );

        if (mAdapter.getItemCount() > 0) {
            stopWatch.recyclerview.smoothScrollToPosition(mAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       stopWatch = null;
    }
}