package com.example.clock0.Controller.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clock0.R;

/**
 * Created by Yassine Abou on 2/2/2021.
 */
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        finish();
    }
}
