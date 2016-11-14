package com.tektonlabs.taskview.ui.adapters;

import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tektonlabs.taskview.R;
import com.tektonlabs.taskview.models.Task;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

public class TaskAdapter extends DragItemAdapter<Pair<Long, Task>, TaskAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;
    private ArrayList<Pair<Long, Task>>  tasks;

    public TaskAdapter(ArrayList<Pair<Long, Task>>  tasks, int mLayoutId, int mGrabHandleId, boolean mDragOnLongPress) {
        this.mLayoutId = mLayoutId;
        this.mGrabHandleId = mGrabHandleId;
        this.mDragOnLongPress = mDragOnLongPress;
        this.tasks = tasks;
        setHasStableIds(true);
        setItemList(tasks);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        String text = mItemList.get(position).second.getTaskName();
        holder.mText.setText(text);
        holder.itemView.setTag(text);
    }

    @Override
    public long getItemId(int position) {
        return mItemList.get(position).first;
    }

    public class ViewHolder extends DragItemAdapter.ViewHolder {
        TextView mText;

        public ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);
            mText = (TextView) itemView.findViewById(R.id.text);
        }

        @Override
        public void onItemClicked(View view) {
            Toast.makeText(view.getContext(), "Item clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onItemLongClicked(View view) {
            Toast.makeText(view.getContext(), "Item long clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}
