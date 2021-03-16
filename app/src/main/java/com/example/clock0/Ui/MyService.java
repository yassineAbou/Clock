package com.example.clock0.Ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.clock0.R;

@SuppressWarnings("ALL")
public class MyService extends Service {

    private static final String CHANNEL_ID = "XXXX";
    private static final String ACTION_STOP_SERVICE = "STOP";
    int count;
    MediaPlayer mp;

    private static final String TAG = "MyService";
    public MyService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mp = MediaPlayer.create(this, R.raw.angry_ring_ring_v);
        Log.d(TAG, "onCreate() called");


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //remove all notification
        NotificationManager notifManager= (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();

        if (ACTION_STOP_SERVICE.equals(intent.getAction())) {
            stopSelf();
        }


        getNotification();

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (count < Integer.MAX_VALUE) {
                    mp.start();
                    count++;
                }
            }
        });

        mp.start();

      startForeground(10, getNotification());


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mp.isPlaying()) {
            mp.stop();
            mp.release();
        }
        Log.d(TAG, "service destroyed");

        //remove all notification
        NotificationManager notifManager= (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();



    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Notification getNotification() {



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Your channel name",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("my channel description");
            NotificationManager nm = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
            nm.createNotificationChannel(channel);
        }

        Intent stopSelf = new Intent(this, MyService.class);
        stopSelf.setAction(ACTION_STOP_SERVICE);

        PendingIntent pStopSelf = PendingIntent
                .getService(this, 0, stopSelf
                        ,PendingIntent.FLAG_CANCEL_CURRENT);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getBaseContext(), CHANNEL_ID);

        notificationBuilder.setSmallIcon(R.drawable.bell1)
                .setContentTitle("Ring Ring")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Ring Ring..Ring Ring "))
                .addAction(R.drawable.bell1,"Close", pStopSelf);



        NotificationManagerCompat mnc = NotificationManagerCompat.from(getBaseContext());
        mnc.notify(10, notificationBuilder.build());

        return notificationBuilder.build();

    }
}