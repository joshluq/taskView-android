package com.tektonlabs.taskview.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.tektonlabs.taskview.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_sign_up)
    public void signUp() {
        startActivity(new Intent(this,SignUpActivity.class));
    }
}
