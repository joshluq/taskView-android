package com.tektonlabs.taskview.ui.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.tektonlabs.taskview.R;
import com.tektonlabs.taskview.models.Project;
import com.tektonlabs.taskview.models.User;
import com.tektonlabs.taskview.ui.adapters.ProjectAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProjectsActivity extends BaseActivity {

    @BindView(R.id.rv_projects)
    RecyclerView rv_messages;

    private User currentUser;
    private Dialog newProjectDialog;

    private ProjectAdapter projectAdapter;

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
        ButterKnife.bind(this);
        setupAdapter();
        newProjectDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.projects_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                FirebaseAuth.getInstance().signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupAdapter(){
        rv_messages.setLayoutManager(new LinearLayoutManager(this));
        projectAdapter = new ProjectAdapter(this,firebaseManager.getProjectsReference());
        rv_messages.setAdapter(projectAdapter);
    }

    private void newProjectDialog(){
        newProjectDialog = new Dialog(this);
        newProjectDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        newProjectDialog.setContentView(R.layout.dialog_new_project);
        newProjectDialog.setCanceledOnTouchOutside(true);
        //noinspection ConstantConditions
        newProjectDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        Button btn_cancel = (Button) newProjectDialog.findViewById(R.id.btn_cancel);
        Button btn_done = (Button) newProjectDialog.findViewById(R.id.btn_done);
        final TextView et_project_name = (TextView) newProjectDialog.findViewById(R.id.et_project_name);
        final TextView et_project_description = (TextView) newProjectDialog.findViewById(R.id.et_project_description);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newProjectDialog.dismiss();
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et_project_name.getText().toString().isEmpty()){
                    createProject(et_project_name.getText().toString(),et_project_description.getText().toString());
                    newProjectDialog.dismiss();
                }
            }
        });
    }
    @OnClick(R.id.fab_new_project)
    public void newProject(){
        newProjectDialog.show();
    }

    private void createProject(String projectName, String description){
        Project project = new Project();
        project.setProjectName(projectName);
        project.setDescription(description);
        project.setOwner(currentUser);
        firebaseManager.getProjectsReference().push().setValue(project);
    }
}
