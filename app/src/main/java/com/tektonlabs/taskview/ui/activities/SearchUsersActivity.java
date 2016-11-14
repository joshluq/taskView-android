package com.tektonlabs.taskview.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tektonlabs.taskview.R;
import com.tektonlabs.taskview.ui.adapters.UserAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchUsersActivity extends BaseActivity {
    @BindView(R.id.rv_users)
    RecyclerView rv_users;

    private UserAdapter userAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);
        ButterKnife.bind(this);
        setupAdapter();
    }


    private void setupAdapter(){
        rv_users.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(firebaseManager.getUsersReference());
        rv_users.setAdapter(userAdapter);
    }
}
