package com.example.clock0.Controller.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clock0.R;
import com.example.clock0.Ui.MyService;
import com.example.clock0.databinding.ActivityRingBinding;

@SuppressWarnings("ALL")
public class RingActivity extends AppCompatActivity {


    ActivityRingBinding mRingBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRingBinding = ActivityRingBinding.inflate(getLayoutInflater());
        setContentView(mRingBinding.getRoot());

        Intent intent = new Intent(getBaseContext(), MyService.class);
       startService(intent);



        onShakeImage();

        cancelRing();



    }

    public void onShakeImage() {
        Animation shake;
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

        mRingBinding.bellImg.startAnimation(shake);// starts animation

    }

    private void cancelRing() {

        mRingBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MyService.class);
                stopService(intent);

                //remove all notification
                NotificationManager notifManager= (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notifManager.cancelAll();

                finish();

            }
        });
    }
}