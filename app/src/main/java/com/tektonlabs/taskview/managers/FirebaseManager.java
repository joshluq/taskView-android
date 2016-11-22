package com.tektonlabs.taskview.managers;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tektonlabs.taskview.models.User;
import com.tektonlabs.taskview.utils.FirebaseConstants;


public class FirebaseManager {
    private FirebaseUser firebaseUser;
    private DatabaseReference usersReference;
    private DatabaseReference projectsReference;
    private PreferencesManager preferencesManager;

    public FirebaseManager(Context context) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        preferencesManager = PreferencesManager.getInstance(context);
        usersReference = FirebaseDatabase.getInstance().getReference(FirebaseConstants.REF_DATA).child(FirebaseConstants.CHILD_USERS);
        projectsReference = FirebaseDatabase.getInstance().getReference(FirebaseConstants.REF_DATA).child(FirebaseConstants.CHILD_PROJECTS);
    }

    public DatabaseReference getProjectsReference() {
        return projectsReference;
    }

    public DatabaseReference getUsersReference() {
        return usersReference;
    }


    public void getUserInfo(){
        if (firebaseUser!=null){
            usersReference.child(firebaseUser.getUid()).addValueEventListener(userValueEventListener);
        }
    }

    //Firebase Event Listeners
    private ValueEventListener userValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            User user = dataSnapshot.getValue(User.class);
            if(user != null){
                preferencesManager.setPreferenceUser(user);
                singleValueEventListener.onSuccess();
            }else{
                singleValueEventListener.onError();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    //Interfaces
    public interface OnSingleValueEventListener{
        void onSuccess();
        void onError();
    }

    private OnSingleValueEventListener singleValueEventListener;

    public void setOnSingleValueEventListener(OnSingleValueEventListener listener) {
        singleValueEventListener = listener;
    }

    //Getter and Setter
    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }
}
