package com.tektonlabs.taskview.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tektonlabs.taskview.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTaskActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.et_responsible)
    public void selectResponsible(){
        Intent intent = new Intent(this,SearchUsersActivity.class);
        startActivityForResult(intent, Activity.RESULT_OK);
    }

    @OnClick(R.id.btn_cancel)
    public void cancel(){
        finish();
    }

    @OnClick(R.id.btn_done)
    public void done(){

    }
}
