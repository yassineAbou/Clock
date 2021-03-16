package com.example.clock0.Controller.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.clock0.R;
import com.example.clock0.Ui.MyService;
import com.example.clock0.Ui.ViewAnimation;
import com.example.clock0.databinding.FragmentTimerBinding;

import java.util.Locale;


@SuppressWarnings("ALL")
public class timerFragment extends Fragment   {


    private static final String CHANNEL_ID = "FFFFF";
    private static FragmentTimerBinding timer;
     private boolean sRunning = false;
     public static final String ARG_TIME = "time";
     private static final String TAG = "timerFragment";
     private static long  sStartTime = 60000; // 1 munite by default
     private static long sTimeLeft =sStartTime;
     private CountDownTimer mCountDownTimer = new CountDownTimer(sTimeLeft, 1000) {
         @Override
         public void onTick(long millisUntilFinished) {
             sTimeLeft = millisUntilFinished;
             updateTimerText();
         }

         @Override
         public void onFinish() {
             updateTimerText();
         }
     };

    public timerFragment() {
        // Required empty public constructor
    }



    public static timerFragment newInstance(long time) {
        timerFragment fragment = new timerFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_TIME, time);
         sStartTime = sTimeLeft = time;
        fragment.setArguments(args);
        updateTimerText();
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null)
        {
     //    timeInMill = getArguments().getLong(ARG_TIME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        timer = FragmentTimerBinding.inflate(inflater, container, false);
        View view = timer.getRoot();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        playBehaviourOfButtons();
        setTime();
        updateTimerText();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       timer = null;
    }

    private void playBehaviourOfButtons() {
        timer.buttonStartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //start timer
               timer.buttonStartTimer.setVisibility(View.GONE);
               timer.buttonStopResumeTimer.setVisibility(View.VISIBLE);
               timer.buttonResetTimer.setVisibility(View.VISIBLE);
                ViewAnimation.divergeTwoViewHorizontal(
                        getContext(),
                        timer.buttonStopResumeTimer,
                        timer.buttonResetTimer);

                startTimer();
            }
        });
        timer.buttonStopResumeTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer.buttonStopResumeTimer.getText().toString().equals("resume")) {
                    //resume Timer
                    timer.buttonStopResumeTimer.setText("stop");
                    timer.buttonStopResumeTimer.setTextColor(getResources().getColor(R.color.red));

                    startTimer();
                }
                else{
                    //pause timer
                    timer.buttonStopResumeTimer.setText("resume");
                    timer.buttonStopResumeTimer.setTextColor(getResources().getColor(R.color.gray1));

                    pauseTimer();
                }
            }
        });
        timer.buttonResetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset timer
                ViewAnimation.divergeTwoViewHorizontal(
                        getContext(),
                        timer.buttonStopResumeTimer,
                        timer.buttonResetTimer);
                timer.buttonStopResumeTimer.setVisibility(View.GONE);
                timer.buttonStopResumeTimer.setText("stop");
                timer.buttonStopResumeTimer.setTextColor(getResources().getColor(R.color.red));
                timer.buttonResetTimer.setVisibility(View.GONE);
                timer.buttonStartTimer.setVisibility(View.VISIBLE);

                resetTimer();
            }
        });
    }


    // set new time for timer via the custom dialog
    private void setTime() {
        timer.timerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!sRunning && timer.buttonStartTimer.getVisibility() == View.VISIBLE) {

                    TimerDialogFragment dialog = new TimerDialogFragment();
                    dialog.setTargetFragment(timerFragment.this, 1);
                    dialog.show(getFragmentManager(), "MyCustomDialog");
                }
            }
        });
    }

    private void startTimer() {
        if (!sRunning) {
           timer.progressBarTimer.setMax((int) sStartTime);
           timer.progressBarTimer.setProgress((int) sTimeLeft);

           mCountDownTimer = new CountDownTimer(sTimeLeft, 1000) {
               @Override
               public void onTick(long millisUntilFinished) {

                   timer.progressBarTimer.setProgress((int) millisUntilFinished);

                     sTimeLeft = millisUntilFinished;
                     updateTimerText();
               }

               @Override
               public void onFinish() {
                   sTimeLeft = sStartTime = 60000;
                   sRunning = false;
                   timer.progressBarTimer.setMax((int) sStartTime);
                   timer.progressBarTimer.setProgress((int) sStartTime);

                   ViewAnimation.divergeTwoViewHorizontal(
                           getContext(),
                           timer.buttonStopResumeTimer,
                           timer.buttonResetTimer);
                   timer.buttonStopResumeTimer.setVisibility(View.GONE);
                   timer.buttonStopResumeTimer.setText("stop");
                   timer.buttonStopResumeTimer.setTextColor(getResources().getColor(R.color.red));
                   timer.buttonResetTimer.setVisibility(View.GONE);
                   timer.buttonStartTimer.setVisibility(View.VISIBLE);
                   updateTimerText();
                   
                   showNotificationAboutExpired();
               }
           }.start();
        }

          sRunning = true;
    }


    private void pauseTimer() {
        if (sRunning) {
            mCountDownTimer.cancel();
            sRunning = false;
        }
    }

    private void resetTimer() {
        mCountDownTimer.cancel();
        sTimeLeft = sStartTime = 60000;
        sRunning = false;
        updateTimerText();
        timer.progressBarTimer.setMax((int) sStartTime);
        timer.progressBarTimer.setProgress((int) sStartTime);

    }


    private static void updateTimerText() {
        int seconds = (int) (sTimeLeft / 1000) % 60 ;
        int minutes = (int) ((sTimeLeft / (1000*60)) % 60);
        int hours   = (int) ((sTimeLeft / (1000*60*60)) % 24);

        String TimeLeftFormatted = String.format(
                Locale.getDefault(),
                "%02d:%02d:%02d",
                hours,
                minutes,
                seconds);

        timer.timerText.setText(TimeLeftFormatted);
    }

    //....................
    //when timer is expired show a notification with a sound
    //...................

    private void showNotificationAboutExpired() {
        getActivity().startService(new Intent(getContext(), MyService.class));
    }


}