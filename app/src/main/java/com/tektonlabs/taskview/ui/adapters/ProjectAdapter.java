package com.tektonlabs.taskview.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.google.gson.Gson;
import com.tektonlabs.taskview.R;
import com.tektonlabs.taskview.managers.PreferencesManager;
import com.tektonlabs.taskview.models.Project;
import com.tektonlabs.taskview.models.User;


public class ProjectAdapter  extends FirebaseRecyclerAdapter<Project, ProjectAdapter.ProjectHolder> {

    private User currentUser;

    public ProjectAdapter(Context context, Query ref) {
        super(Project.class,R.layout.row_projects, ProjectHolder.class, ref);
        currentUser = new Gson().fromJson(PreferencesManager.getInstance(context).getPreferenceUser(),User.class);
    }

    @Override
    protected void populateViewHolder(ProjectHolder viewHolder, Project model, int position) {
        if(currentUser.getId().equals(model.getOwner().getId())){
            viewHolder.tv_title.setText(model.getProjectName());
            viewHolder.tv_description.setText(model.getDescription());
            viewHolder.tv_owner.setText(model.getOwner().getUsername());
        }

    }

    public static class ProjectHolder extends RecyclerView.ViewHolder{
        TextView tv_title;
        TextView tv_description;
        TextView tv_owner;

        public ProjectHolder(View itemView) {
            super(itemView);
            tv_title = (TextView)itemView.findViewById(R.id.tv_title);
            tv_description = (TextView)itemView.findViewById(R.id.tv_description);
            tv_owner = (TextView)itemView.findViewById(R.id.tv_owner);
        }
    }
}
