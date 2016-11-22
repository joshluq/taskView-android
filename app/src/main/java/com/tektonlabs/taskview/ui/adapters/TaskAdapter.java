package com.tektonlabs.taskview.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tektonlabs.taskview.R;
import com.tektonlabs.taskview.models.Task;
import com.tektonlabs.taskview.ui.activities.AddTaskActivity;
import com.tektonlabs.taskview.utils.Constants;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

public class TaskAdapter extends DragItemAdapter<Pair<Long, Task>, TaskAdapter.ViewHolder> {

    private final static int TASK_RESULT = 2;
    private int mLayoutId;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;
    private Context context;
    private Activity activity;

    private Task task;

    public TaskAdapter(Activity activity, ArrayList<Pair<Long, Task>>  tasks, int mLayoutId, int mGrabHandleId, boolean mDragOnLongPress) {
        this.mLayoutId = mLayoutId;
        this.mGrabHandleId = mGrabHandleId;
        this.mDragOnLongPress = mDragOnLongPress;
        this.activity = activity;
        setHasStableIds(true);
        setItemList(tasks);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int p) {
        super.onBindViewHolder(holder, p);
        final int position = holder.getAdapterPosition();
        task =  mItemList.get(position).second;
        String title = task.getTaskName();
        String time = task.getTime()+ " Hours";
        String responsible = task.getUsers().get(0).getUsername();
        holder.itemView.setTag(title);
        holder.title.setText(title);
        holder.responsible.setText(time);
        holder.time.setText(responsible);

        switch (task.getStatus()) {
            case Constants.STATUS_BACKLOG:
                holder.background.setCardBackgroundColor(ContextCompat.getColor(context,R.color.backlog));
                break;
            case Constants.STATUS_TODO:
                holder.background.setCardBackgroundColor(ContextCompat.getColor(context,R.color.todo));
                break;
            case Constants.STATUS_WIP:
                holder.background.setCardBackgroundColor(ContextCompat.getColor(context,R.color.wip));
                break;
            case Constants.STATUS_CODE_REVIEW:
                holder.background.setCardBackgroundColor(ContextCompat.getColor(context,R.color.codereview));
                break;
            case Constants.STATUS_DONE:
                holder.background.setCardBackgroundColor(ContextCompat.getColor(context,R.color.done));
                break;
            case Constants.STATUS_ACCEPTED:
                holder.background.setCardBackgroundColor(ContextCompat.getColor(context,R.color.accepted));
                break;
        }

    }

    @Override
    public long getItemId(int position) {
        return mItemList.get(position).first;
    }

    public class ViewHolder extends DragItemAdapter.ViewHolder {
        TextView title;
        TextView responsible;
        TextView time;
        CardView background;

        public ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);
            title = (TextView) itemView.findViewById(R.id.title);
            responsible = (TextView) itemView.findViewById(R.id.responsible);
            time = (TextView) itemView.findViewById(R.id.time);
            background = (CardView) itemView.findViewById(R.id.card);
        }

        @Override
        public void onItemClicked(View view) {
            Intent intent = new Intent(context, AddTaskActivity.class);
            intent.putExtra(Constants.EXTRA_TASK,task);
            activity.startActivityForResult(intent,TASK_RESULT);
        }

        @Override
        public boolean onItemLongClicked(View view) {

            return true;
        }
    }
}
