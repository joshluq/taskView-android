package com.tektonlabs.taskview.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.mobsandgeeks.saripaar.annotation.Password;
import com.tektonlabs.taskview.R;
import com.tektonlabs.taskview.managers.FirebaseManager;
import com.tektonlabs.taskview.utils.UIHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity  extends AppCompatActivity {

    private final String TAG = SignInActivity.class.getSimpleName();

    @NotEmpty(message = "Complete el campo")
    @Email(message = "email invalido")
    @BindView(R.id.et_email)
    TextView et_email;

    @Password
    @BindView(R.id.et_password)
    TextView et_password;

    @BindView(R.id.ll_sign_in_fields)
    View ll_sign_in_fields;

    @BindView(R.id.v_progress)
    View v_progress;

    private Validator validator;
    private FirebaseManager firebaseManager;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    private boolean flag = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        firebaseManager = new FirebaseManager(this);
        auth = FirebaseAuth.getInstance();
        authListener();
        ButterKnife.bind(this);
        validator = new Validator(this);
        validateField();
    }

    @OnClick(R.id.btn_sign_up)
    public void onSignUp() {
        startActivity(new Intent(this,SignUpActivity.class));
    }

    @OnClick(R.id.btn_sign_in)
    public void onSignIn() {
        validator.validate();
    }

    private void validateField(){
        Validator.ValidationListener validationListener = new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                signIn();
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {

                for (ValidationError error : errors) {
                    View view = error.getView();
                    String message = error.getCollatedErrorMessage(SignInActivity.this);

                    if (view instanceof EditText) {
                        ((EditText) view).setError(message);
                    } else {
                        Toast.makeText(SignInActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
        validator.setValidationListener(validationListener);
    }

    private void signIn(){
        UIHelper.showProgress(SignInActivity.this,v_progress,ll_sign_in_fields,true);
        auth.signInWithEmailAndPassword(et_email.getText().toString(), et_password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.e(TAG, "signInWithEmail: "+ task.isSuccessful());
                }else{
                    Log.e(TAG, "signInWithEmail", task.getException());
                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    FirebaseManager.OnSingleValueEventListener singleValueEventListener = new FirebaseManager.OnSingleValueEventListener() {
        @Override
        public void onSuccess() {
            if(flag){
                flag=false;
                UIHelper.showProgress(SignInActivity.this,v_progress,ll_sign_in_fields,false);
                startActivity(new Intent(SignInActivity.this, ProjectsActivity.class));
                finish();
            }
        }
        @Override
        public void onError() {
            UIHelper.showProgress(SignInActivity.this,v_progress,ll_sign_in_fields,false);
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
                    firebaseManager.setOnSingleValueEventListener(singleValueEventListener);
                    firebaseManager.getUserInfo();
                    firebaseManager.setFirebaseUser(user);
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
