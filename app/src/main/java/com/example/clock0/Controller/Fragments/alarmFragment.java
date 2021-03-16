package com.example.clock0.Controller.Fragments;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.clock0.R;
import com.example.clock0.Ui.AlterReceiver;

import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link alarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("ALL")
public class alarmFragment extends Fragment {
    private static final String CHANNEL_ID = "XXXX";
    private Button mButtonStart, mButtonStop;
    private TextView mTextAlarm;
    private AlarmManager mAlarmManager;
    private TimePickerDialog mTimePickerDialog;
    private Calendar mCalendar;
    private SharedPreferences mSharedPreferences;
    PendingIntent pendingIntent;
    private int alarmId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarm, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mButtonStart = getActivity().findViewById(R.id.activity_main_start_btn);
        mButtonStop = getActivity().findViewById(R.id.activity_main_stop_btn);
        mTextAlarm = getActivity().findViewById(R.id.activity_main_alarm_txt);
        mAlarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        mSharedPreferences = getContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(mSharedPreferences.getLong("alarm_time", mCalendar.getTimeInMillis()));


        updateTextAlarmTime();
        playBehaviourOfButtons();
        setTime();

    }

    @Override
    public void onResume() {
        super.onResume();

        // change style of buttons
        if (mSharedPreferences.getBoolean("alarm_running", false)) {
            mButtonStart.setVisibility(View.GONE);
            mButtonStop.setVisibility(View.VISIBLE);
        } else {
            mButtonStart.setVisibility(View.VISIBLE);
            mButtonStop.setVisibility(View.GONE);
        }
    }


    private void playBehaviourOfButtons() {
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonStart.setVisibility(View.GONE);
                mButtonStop.setVisibility(View.VISIBLE);

                startAlarm();
            }
        });
        mButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonStart.setVisibility(View.VISIBLE);
                mButtonStop.setVisibility(View.GONE);

                stopAlarm();
            }
        });
    }

    private void setTime() {
        mTextAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set new time for alarm


                if (!mSharedPreferences.getBoolean("alarm_running", false)) {
                    mTimePickerDialog = new TimePickerDialog(
                            getContext(),
                            R.style.MyTimePickerDialogStyle,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    mCalendar.set(Calendar.MINUTE, minute);

                                    updateTextAlarmTime();
                                }
                            },
                            mCalendar.get(Calendar.HOUR_OF_DAY),
                            mCalendar.get(Calendar.MINUTE),
                            true);

                    mTimePickerDialog.show();
                }

                else {
                    String toastText = String.format("There is already an alarm that is siten %n" +
                            "wait until the mission is done %n" +
                            "or stop the alarm");
                    Toast.makeText(getContext(), toastText, Toast.LENGTH_LONG).show();
                }
                }



        });
    }

    private void startAlarm() {

        Intent intent = new Intent(getContext(), AlterReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(getContext(), alarmId, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,  mCalendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, mCalendar.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // if alarm time has already passed, increment day by 1
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }

        mAlarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                alarmPendingIntent
        );

        mSharedPreferences.edit().putBoolean("alarm_running", true).apply();

     displayNotification();




    }

        private void stopAlarm () {
            // cancel the current alarm
            Intent intent = new Intent(getContext(), AlterReceiver.class);
            PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(getContext(), alarmId, intent, 0);
            mAlarmManager.cancel(alarmPendingIntent);
            String toastText = String.format("Alarm cancelled for %02d:%02d with id %d",  mCalendar.get(Calendar.HOUR_OF_DAY)
                    , mCalendar.get(Calendar.MINUTE), alarmId);
            Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT).show();

            mSharedPreferences.edit().putBoolean("alarm_running", false).apply();

            //remove all notification
            NotificationManager notifManager= (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notifManager.cancelAll();


        }

        private void updateTextAlarmTime () {
            mTextAlarm.setText(String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    mCalendar.get(Calendar.HOUR_OF_DAY),
                    mCalendar.get(Calendar.MINUTE)));
        }

        public void displayNotification() {

            String ntc = String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    mCalendar.get(Calendar.HOUR_OF_DAY),
                    mCalendar.get(Calendar.MINUTE));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Your channel name",
                        NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("my channel description");
                NotificationManager nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                nm.createNotificationChannel(channel);
            }
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext(), CHANNEL_ID);

            notificationBuilder.setSmallIcon(R.drawable.bell1)
                    .setContentTitle("Alarm")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Alarm was set to " + ntc));


            NotificationManagerCompat mnc = NotificationManagerCompat.from(getContext());
            mnc.notify(10, notificationBuilder.build());

        }



        }










