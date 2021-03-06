package com.tektonlabs.taskview.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tektonlabs.taskview.managers.FirebaseManager;
import com.tektonlabs.taskview.managers.PreferencesManager;

public class BaseActivity extends AppCompatActivity {
    protected FirebaseManager firebaseManager;
    protected FirebaseAuth.AuthStateListener authListener;
    protected FirebaseAuth auth;
    protected PreferencesManager preferencesManager;

    private final String TAG = BaseActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesManager = PreferencesManager.getInstance(this);
        firebaseManager = new FirebaseManager(this);
        auth = FirebaseAuth.getInstance();
        authListener();

    }

    private void authListener(){
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.e(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    preferencesManager.logOut();
                    startActivity(new Intent(getApplicationContext(),SplashActivity.class));
                    finish();
                    Log.e(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }


}
