package com.medicalsurvey;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    Handler handler;

    Runnable runnable = () -> {
        startActivity(new Intent(SplashActivity.this, IntroSurveyActivity.class));
        finish();


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();

    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) handler.removeCallbacks(runnable);
    }
}
