package com.tektonlabs.taskview.ui.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.tektonlabs.taskview.R;
import com.tektonlabs.taskview.managers.PreferencesManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 4000;
    @BindView(R.id.iv_logo)
    ImageView iv_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotare_animation);
        iv_logo.startAnimation(animation);
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
