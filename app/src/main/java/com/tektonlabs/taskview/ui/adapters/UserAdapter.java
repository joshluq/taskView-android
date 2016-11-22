package com.tektonlabs.taskview.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.tektonlabs.taskview.R;
import com.tektonlabs.taskview.models.User;
import com.tektonlabs.taskview.ui.activities.SearchUsersActivity;


public class UserAdapter extends FirebaseRecyclerAdapter<User, UserAdapter.UserHolder> {

    private SearchUsersActivity activity;

    public UserAdapter(SearchUsersActivity activity, Query ref) {
        super(User.class,R.layout.row_users, UserHolder.class, ref);
        this.activity = activity;
    }

    @Override
    protected void populateViewHolder(UserHolder viewHolder, final User model, int position) {
        viewHolder.tv_username.setText(model.getUsername());
        viewHolder.tv_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("responsible",model);
                activity.setResult(Activity.RESULT_OK,returnIntent);
                activity.finish();
            }
        });
    }

    public static class UserHolder extends RecyclerView.ViewHolder{
        TextView tv_username;

        public UserHolder(View itemView) {
            super(itemView);
            tv_username = (TextView)itemView.findViewById(R.id.tv_username);
        }
    }
}
