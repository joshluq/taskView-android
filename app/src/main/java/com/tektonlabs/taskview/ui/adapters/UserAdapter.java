package com.tektonlabs.taskview.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.tektonlabs.taskview.R;
import com.tektonlabs.taskview.models.User;



public class UserAdapter extends FirebaseRecyclerAdapter<User, UserAdapter.UserHolder> {

    public UserAdapter(Query ref) {
        super(User.class,R.layout.row_users, UserHolder.class, ref);
    }

    @Override
    protected void populateViewHolder(UserHolder viewHolder, User model, int position) {
        viewHolder.tv_username.setText(model.getUsername());
    }

    public static class UserHolder extends RecyclerView.ViewHolder{
        TextView tv_username;
        LinearLayout ll_row_container;

        public UserHolder(View itemView) {
            super(itemView);
            tv_username = (TextView)itemView.findViewById(R.id.tv_username);
        }
    }
}
