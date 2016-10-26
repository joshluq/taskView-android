package com.tektonlabs.taskview.ui.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tektonlabs.taskview.R;
import com.tektonlabs.taskview.managers.PreferencesManager;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                startApplication();
            }
        }, SPLASH_TIME_OUT);
    }

    private void startApplication(){
        Intent intent;
        if (PreferencesManager.getInstance(this).getPreferenceUser().isEmpty()) {
            intent = new Intent(this, SignInActivity.class);
        }else{
            intent = new Intent(this, ProjectsActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
