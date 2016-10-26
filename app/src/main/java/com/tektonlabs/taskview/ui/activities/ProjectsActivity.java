package com.tektonlabs.taskview.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.google.gson.Gson;
import com.tektonlabs.taskview.R;
import com.tektonlabs.taskview.models.User;

public class ProjectsActivity extends BaseActivity {

    private User currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        currentUser = new Gson().fromJson(preferencesManager.getPreferenceUser(),User.class);
        if(currentUser!=null){
            toolbar.setTitle(currentUser.getUsername());
        }
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.projects_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
