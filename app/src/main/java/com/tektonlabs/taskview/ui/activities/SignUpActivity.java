package com.tektonlabs.taskview.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.tektonlabs.taskview.R;
import com.tektonlabs.taskview.managers.FirebaseManager;
import com.tektonlabs.taskview.managers.PreferencesManager;
import com.tektonlabs.taskview.models.User;
import com.tektonlabs.taskview.utils.Constants;
import com.tektonlabs.taskview.utils.UIHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    private final String TAG = BaseActivity.class.getSimpleName();

    @NotEmpty
    @BindView(R.id.et_username)
    TextView et_username;

    @NotEmpty
    @Email
    @BindView(R.id.et_email)
    TextView et_email;

    @NotEmpty
    @BindView(R.id.et_password)
    TextView et_password;

    @BindView(R.id.ll_sign_up_fields)
    View ll_sign_up_fields;

    @BindView(R.id.v_progress)
    View v_progress;

    private Validator validator;
    private PreferencesManager preferencesManager;
    private FirebaseManager firebaseManager;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    private boolean flag = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseManager = new FirebaseManager(this);
        auth = FirebaseAuth.getInstance();
        authListener();
        ButterKnife.bind(this);
        validator = new Validator(this);
        preferencesManager = PreferencesManager.getInstance(this);
        validateField();

    }

    @OnClick(R.id.btn_create)
    public void onCreateAccount() {
        validator.validate();
    }

    private void validateField(){
        Validator.ValidationListener validationListener = new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                createAccount();
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
//            TODO: Implement errors
            }
        };
        validator.setValidationListener(validationListener);
    }

    private void createAccount() {
        UIHelper.showProgress(SignUpActivity.this,v_progress,ll_sign_up_fields,true);
        auth.createUserWithEmailAndPassword(et_email.getText().toString(), et_password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        UIHelper.showProgress(SignUpActivity.this,v_progress,ll_sign_up_fields,false);
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Create Account failed.", Toast.LENGTH_SHORT).show();
                        }else{
                            if(firebaseManager.getFirebaseUser() != null){
                                firebaseManager.setOnSingleValueEventListener(singleValueEventListener);
                                firebaseManager.getUserInfo();
                            }
                        }
                    }
                });
    }

    FirebaseManager.OnSingleValueEventListener singleValueEventListener = new FirebaseManager.OnSingleValueEventListener() {
        @Override
        public void onSuccess() {
            if(flag){
                flag=false;
                startActivity(new Intent(SignUpActivity.this, ProjectsActivity.class));
                UIHelper.showProgress(SignUpActivity.this,v_progress,ll_sign_up_fields,false);
                finish();
            }
        }

        @Override
        public void onError() {

        }
    };

    private void authListener(){
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.e(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    firebaseManager.setFirebaseUser(user);
                    firebaseManager.getUsersReference().child(user.getUid()).setValue(new User(user.getUid(),et_username.getText().toString()));
                } else {
                    // User is signed out
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
