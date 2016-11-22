package com.tektonlabs.taskview.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.tektonlabs.taskview.R;
import com.tektonlabs.taskview.models.Project;
import com.tektonlabs.taskview.models.User;
import com.tektonlabs.taskview.ui.activities.BoardActivity;
import com.tektonlabs.taskview.utils.Constants;

public class ProjectAdapter  extends FirebaseRecyclerAdapter<Project, ProjectAdapter.ProjectHolder> {

    private User currentUser;
    private Context context;

    public ProjectAdapter(Context context, Query ref) {
        super(Project.class,R.layout.row_projects, ProjectHolder.class, ref);
        this.context = context;
    }

    @Override
    protected void populateViewHolder(ProjectHolder viewHolder, final Project model, final int position) {

            viewHolder.tv_title.setText(model.getProjectName());
            viewHolder.tv_description.setText(model.getDescription());
            viewHolder.tv_owner.setText(model.getOwner().getUsername());
            viewHolder.ll_row_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, BoardActivity.class);
                    model.setId(getRef(position).getKey());
                    intent.putExtra(Constants.EXTRA_PROJECT,model);
                    context.startActivity(intent);
                }
            });


    }

    public static class ProjectHolder extends RecyclerView.ViewHolder{
        TextView tv_title;
        TextView tv_description;
        TextView tv_owner;
        LinearLayout ll_row_container;

        public ProjectHolder(View itemView) {
            super(itemView);
            tv_title = (TextView)itemView.findViewById(R.id.tv_title);
            tv_description = (TextView)itemView.findViewById(R.id.tv_description);
            tv_owner = (TextView)itemView.findViewById(R.id.tv_owner);
            ll_row_container = (LinearLayout) itemView.findViewById(R.id.ll_row_container);
        }
    }
}
