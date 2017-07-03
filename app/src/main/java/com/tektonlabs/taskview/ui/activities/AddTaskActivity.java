package com.tektonlabs.taskview.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.tektonlabs.taskview.R;
import com.tektonlabs.taskview.models.Task;
import com.tektonlabs.taskview.models.User;
import com.tektonlabs.taskview.utils.Constants;
import com.tektonlabs.taskview.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTaskActivity extends BaseActivity {

    private final static int USER_RESULT = 1;

    private User responsible;

    @NotEmpty
    @BindView(R.id.et_task_title)
    EditText et_task_title;

    @NotEmpty
    @BindView(R.id.et_task_description)
    EditText et_task_description;

    @NotEmpty
    @BindView(R.id.et_responsible)
    EditText et_responsible;

    @NotEmpty
    @BindView(R.id.et_time)
    EditText et_time;

    @BindView(R.id.tv_created_at)
    TextView tv_created_at;

    @BindView(R.id.tv_updated_at)
    TextView tv_updated_at;

    @BindView(R.id.v_edit_view)
    View v_edit_view;

    private Validator validator;

    private Task task;

    private boolean edited;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);
        validator = new Validator(this);
        validateField();
        Bundle bundle =  getIntent().getExtras();
        if(bundle!=null){
            task = (Task)bundle.get(Constants.EXTRA_TASK);
            et_task_title.setText(task != null ? task.getTaskName() : "");
            et_task_description.setText(task != null ? task.getDescription() : "");
            et_responsible.setText(task != null ? task.getUsers().get(0).getUsername() : "");
            et_time.setText(task != null ? String.valueOf(task.getTime()) : "");
            tv_created_at.setText(task != null ? String.format("Created at: %s", task.getCreatedAt()) : "");
            tv_updated_at.setText(task != null ? String.format("Updated at: %s", task.getUpdatedAt()) : "");
            v_edit_view.setVisibility(View.VISIBLE);
            edited=true;
        }
    }

    private void validateField(){
        Validator.ValidationListener validationListener = new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                int time = Integer.parseInt(et_time.getText().toString());
                if(time >0){
                    Task newTask = new Task();
                    newTask.setTaskName(et_task_title.getText().toString());
                    newTask.setDescription(et_task_description.getText().toString());
                    newTask.setTime(time);
                    if(responsible !=null){
                        ArrayList<User> users = new ArrayList<>();
                        users.add(responsible);
                        newTask.setUsers(users);
                    }else {
                        newTask.setUsers(task.getUsers());
                    }
                    if(edited){
                        newTask.setId(task.getId());
                        newTask.setStatus(task.getStatus());
                        newTask.setUpdatedAt(UIHelper.now());
                    }else{
                        newTask.setStatus(Constants.STATUS_BACKLOG);
                        newTask.setCreatedAt(UIHelper.now());
                    }
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("task",newTask);
                    returnIntent.putExtra("edited",edited);
                    AddTaskActivity.this.setResult(Activity.RESULT_OK,returnIntent);
                    AddTaskActivity.this.finish();
                }else{
                    Toast.makeText(AddTaskActivity.this,"El numero tiene que se mayor a cero", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                Toast.makeText(AddTaskActivity.this,"Por favor el complete todos los campos", Toast.LENGTH_SHORT).show();
            }
        };
        validator.setValidationListener(validationListener);
    }

    @OnClick(R.id.et_responsible)
    public void selectResponsible(){
        Intent intent = new Intent(this,SearchUsersActivity.class);
        startActivityForResult(intent, USER_RESULT);
    }

    @OnClick(R.id.btn_cancel)
    public void cancel(){
        finish();
    }

    @OnClick(R.id.btn_done)
    public void done(){
        validator.validate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == USER_RESULT) {
            if(resultCode == Activity.RESULT_OK){
                responsible = (User)data.getSerializableExtra("responsible");
                et_responsible.setText(responsible.getUsername());
            }
        }
    }
}
