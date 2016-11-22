package com.tektonlabs.taskview.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tektonlabs.taskview.R;
import com.tektonlabs.taskview.ui.adapters.UserAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchUsersActivity extends BaseActivity {

    @BindView(R.id.rv_users)
    RecyclerView rv_users;
    @BindView(R.id.et_search)
    TextView et_search;

    String search="";

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
        userAdapter = new UserAdapter(this,firebaseManager.getUsersReference());
        rv_users.setAdapter(userAdapter);
    }

    @OnClick(R.id.btn_search)
    public void search(){
        if(!et_search.getText().toString().isEmpty()){
            if(!search.equals(et_search.getText().toString())){
                userAdapter = new UserAdapter(this,firebaseManager.getUsersReference().orderByChild("username").startAt(et_search.getText().toString().toLowerCase()));
                rv_users.setAdapter(userAdapter);
                search= et_search.getText().toString();
            }

        }else{
            userAdapter = new UserAdapter(this,firebaseManager.getUsersReference());
            rv_users.setAdapter(userAdapter);
        }
    }
}
