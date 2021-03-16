package com.example.clock0.Ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

public class AlterReceiver extends BroadcastReceiver {
    private static final String TAG = "AlterReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            String toastText = String.format("Alarm Reboot");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
        }
        else {


            startAlarmService(context, intent);

            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    "pref",
                    Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("alarm_running", false).apply();


        }
    }

    private void startAlarmService(Context context, Intent intent) {
        Intent intentService = new Intent(context, MyService.class);
      //  intentService.putExtra(TITLE, intent.getStringExtra(TITLE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }

    }


}