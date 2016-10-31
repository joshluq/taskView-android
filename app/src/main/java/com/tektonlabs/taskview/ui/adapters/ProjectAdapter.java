package com.tektonlabs.taskview.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.tektonlabs.taskview.R;
import com.tektonlabs.taskview.models.Project;


public class ProjectAdapter  extends FirebaseRecyclerAdapter<Project, ProjectAdapter.ProjectHolder> {

    public ProjectAdapter( Query ref) {
        super(Project.class,R.layout.row_projects, ProjectHolder.class, ref);
    }

    @Override
    protected void populateViewHolder(ProjectHolder viewHolder, Project model, int position) {
        viewHolder.tv_title.setText(model.getProjectName());
        viewHolder.tv_description.setText(model.getDescription());
        viewHolder.tv_owner.setText(model.getOwner().getUsername());
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
