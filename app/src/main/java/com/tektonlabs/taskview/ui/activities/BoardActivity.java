package com.tektonlabs.taskview.ui.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tektonlabs.taskview.R;
import com.tektonlabs.taskview.models.Project;
import com.tektonlabs.taskview.models.Task;
import com.tektonlabs.taskview.ui.adapters.TaskAdapter;
import com.tektonlabs.taskview.utils.Constants;
import com.tektonlabs.taskview.utils.FirebaseConstants;
import com.tektonlabs.taskview.utils.UIHelper;
import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.DragItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BoardActivity extends BaseActivity {

    private final static int TASK_RESULT = 2;

    @BindView(R.id.board_view)
    BoardView mBoardView;

    TaskAdapter backlogAdapter;
    TaskAdapter todoAdapter;
    TaskAdapter wipAdapter;
    TaskAdapter codeReviewAdapter;
    TaskAdapter doneAdapter;
    TaskAdapter acceptedAdapter;

    ArrayList<Pair<Long, Task>> backlogList = new ArrayList<>();
    ArrayList<Pair<Long, Task>> todoList = new ArrayList<>();
    ArrayList<Pair<Long, Task>> wipList = new ArrayList<>();
    ArrayList<Pair<Long, Task>> codeReviewList = new ArrayList<>();
    ArrayList<Pair<Long, Task>> doneList = new ArrayList<>();
    ArrayList<Pair<Long, Task>> acceptedList = new ArrayList<>();

    Project project;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        project = (Project) getIntent().getExtras().get(Constants.EXTRA_PROJECT);
        if (project != null) {
            toolbar.setTitle(project.getProjectName());
            mBoardView.setSnapToColumnWhenDragging(true);
            mBoardView.setSnapDragItemToTouch(true);
            mBoardView.setCustomDragItem(new MyDragItem(this, R.layout.column_item));
            addColumnList();
            foo();
            mBoardView.setBoardListener(new BoardView.BoardListener() {
                @Override
                public void onItemDragStarted(int column, int row) {
                }

                @Override
                public void onItemChangedColumn(int oldColumn, int newColumn) {
                }

                @Override
                public void onItemDragEnded(int fromColumn, int fromRow, int toColumn, int toRow) {
                    if (fromColumn != toColumn || fromRow != toRow) {
                        switch (toColumn) {
                            case Constants.STATUS_BACKLOG:
                                backlogList.get(toRow).second.setStatus(Constants.STATUS_BACKLOG);
                                backlogList.get(toRow).second.setUpdatedAt(UIHelper.now());
                                firebaseManager.getProjectsReference().child(project.getId()).child(FirebaseConstants.CHILD_TASKS).child(backlogList.get(toRow).second.getId()).setValue(backlogList.get(toRow).second);
                                backlogList.remove(toRow);
                                backlogAdapter.setItemList(backlogList);
                                break;
                            case Constants.STATUS_TODO:
                                todoList.get(toRow).second.setStatus(Constants.STATUS_TODO);
                                todoList.get(toRow).second.setUpdatedAt(UIHelper.now());
                                firebaseManager.getProjectsReference().child(project.getId()).child(FirebaseConstants.CHILD_TASKS).child(todoList.get(toRow).second.getId()).setValue(todoList.get(toRow).second);
                                todoList.remove(toRow);
                                todoAdapter.setItemList(todoList);
                                break;
                            case Constants.STATUS_WIP:
                                wipList.get(toRow).second.setStatus(Constants.STATUS_WIP);
                                wipList.get(toRow).second.setUpdatedAt(UIHelper.now());
                                firebaseManager.getProjectsReference().child(project.getId()).child(FirebaseConstants.CHILD_TASKS).child(wipList.get(toRow).second.getId()).setValue(wipList.get(toRow).second);
                                wipList.remove(toRow);
                                wipAdapter.setItemList(wipList);
                                break;
                            case Constants.STATUS_CODE_REVIEW:
                                codeReviewList.get(toRow).second.setStatus(Constants.STATUS_CODE_REVIEW);
                                codeReviewList.get(toRow).second.setUpdatedAt(UIHelper.now());
                                firebaseManager.getProjectsReference().child(project.getId()).child(FirebaseConstants.CHILD_TASKS).child(codeReviewList.get(toRow).second.getId()).setValue(codeReviewList.get(toRow).second);
                                codeReviewList.remove(toRow);
                                codeReviewAdapter.setItemList(codeReviewList);
                                break;
                            case Constants.STATUS_DONE:
                                doneList.get(toRow).second.setStatus(Constants.STATUS_DONE);
                                backlogList.get(toRow).second.setUpdatedAt(UIHelper.now());
                                firebaseManager.getProjectsReference().child(project.getId()).child(FirebaseConstants.CHILD_TASKS).child(doneList.get(toRow).second.getId()).setValue(doneList.get(toRow).second);
                                doneList.remove(toRow);
                                doneAdapter.setItemList(doneList);
                                break;
                            case Constants.STATUS_ACCEPTED:
                                acceptedList.get(toRow).second.setStatus(Constants.STATUS_ACCEPTED);
                                acceptedList.get(toRow).second.setUpdatedAt(UIHelper.now());
                                firebaseManager.getProjectsReference().child(project.getId()).child(FirebaseConstants.CHILD_TASKS).child(acceptedList.get(toRow).second.getId()).setValue(acceptedList.get(toRow).second);
                                acceptedList.remove(toRow);
                                acceptedAdapter.setItemList(acceptedList);
                                break;
                        }
                    }
                }
            });
        }

    }

    public void foo() {

        firebaseManager.getProjectsReference().child(project.getId()).child(FirebaseConstants.CHILD_TASKS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clearData();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Task task = snapshot.getValue(Task.class);
                    task.setId(snapshot.getKey());
                    switch (task.getStatus()) {
                        case Constants.STATUS_BACKLOG:
                            backlogList.add(new Pair<>(getCreatedItems(), task));
                            backlogAdapter.setItemList(backlogList);
                            break;
                        case Constants.STATUS_TODO:
                            todoList.add(new Pair<>(getCreatedItems(), task));
                            todoAdapter.setItemList(todoList);
                            break;
                        case Constants.STATUS_WIP:
                            wipList.add(new Pair<>(getCreatedItems(), task));
                            wipAdapter.setItemList(wipList);
                            break;
                        case Constants.STATUS_CODE_REVIEW:
                            codeReviewList.add(new Pair<>(getCreatedItems(), task));
                            codeReviewAdapter.setItemList(codeReviewList);
                            break;
                        case Constants.STATUS_DONE:
                            doneList.add(new Pair<>(getCreatedItems(), task));
                            doneAdapter.setItemList(doneList);
                            break;
                        case Constants.STATUS_ACCEPTED:
                            acceptedList.add(new Pair<>(getCreatedItems(), task));
                            acceptedAdapter.setItemList(acceptedList);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void clearData() {

        backlogList.clear();
        backlogAdapter.setItemList(backlogList);

        todoList.clear();
        todoAdapter.setItemList(todoList);

        wipList.clear();
        wipAdapter.setItemList(wipList);

        codeReviewList.clear();
        codeReviewAdapter.setItemList(codeReviewList);

        doneList.clear();
        doneAdapter.setItemList(doneList);

        acceptedList.clear();
        acceptedAdapter.setItemList(acceptedList);

    }

    private void addColumnList() {

        backlogAdapter = new TaskAdapter(this, backlogList, R.layout.column_item, R.id.item_layout, true);
        todoAdapter = new TaskAdapter(this, todoList, R.layout.column_item, R.id.item_layout, true);
        wipAdapter = new TaskAdapter(this, wipList, R.layout.column_item, R.id.item_layout, true);
        codeReviewAdapter = new TaskAdapter(this, codeReviewList, R.layout.column_item, R.id.item_layout, true);
        doneAdapter = new TaskAdapter(this, doneList, R.layout.column_item, R.id.item_layout, true);
        acceptedAdapter = new TaskAdapter(this, acceptedList, R.layout.column_item, R.id.item_layout, true);

        View header = View.inflate(this, R.layout.column_header, null);
        ((TextView) header.findViewById(R.id.tv_column_name)).setText(getString(R.string.str_backlog));
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoardActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, TASK_RESULT);
            }
        });

        mBoardView.addColumnList(backlogAdapter, buildHeader(getString(R.string.str_backlog)), false);
        mBoardView.addColumnList(todoAdapter, buildHeader(getString(R.string.str_todo)), false);
        mBoardView.addColumnList(wipAdapter, buildHeader(getString(R.string.str_wip)), false);
        mBoardView.addColumnList(codeReviewAdapter, buildHeader(getString(R.string.str_code_review)), false);
        mBoardView.addColumnList(doneAdapter, buildHeader(getString(R.string.str_done)), false);
        mBoardView.addColumnList(acceptedAdapter, buildHeader(getString(R.string.str_accepted)), false);

    }

    private View buildHeader(String columnName) {
        View header = View.inflate(this, R.layout.column_header, null);
        ((TextView) header.findViewById(R.id.tv_column_name)).setText(columnName);
        if (columnName.equals(getString(R.string.str_backlog))) {
            ImageView iv_add_task = (ImageView) header.findViewById(R.id.iv_add_task);
            iv_add_task.setVisibility(View.VISIBLE);
            iv_add_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BoardActivity.this, AddTaskActivity.class);
                    startActivityForResult(intent, TASK_RESULT);
                }
            });
        }
        return header;
    }

    private static class MyDragItem extends DragItem {

        Context context;

        MyDragItem(Context context, int layoutId) {
            super(context, layoutId);
            this.context = context;
        }

        @Override
        public void onBindDragView(View clickedView, View dragView) {
            CharSequence text = ((TextView) clickedView.findViewById(R.id.title)).getText();
            CharSequence text1 = ((TextView) clickedView.findViewById(R.id.responsible)).getText();
            CharSequence text2 = ((TextView) clickedView.findViewById(R.id.time)).getText();
            ((TextView) dragView.findViewById(R.id.title)).setText(text);
            ((TextView) dragView.findViewById(R.id.responsible)).setText(text1);
            ((TextView) dragView.findViewById(R.id.time)).setText(text2);
            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));
            CardView clickedCard = ((CardView) clickedView.findViewById(R.id.card));

            dragCard.setMaxCardElevation(40);
            dragCard.setCardElevation(clickedCard.getCardElevation());
            dragCard.setForeground(ContextCompat.getDrawable(context, R.drawable.card_view_drag_foreground));
        }

        @Override
        public void onMeasureDragView(View clickedView, View dragView) {
            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));
            CardView clickedCard = ((CardView) clickedView.findViewById(R.id.card));
            int widthDiff = dragCard.getPaddingLeft() - clickedCard.getPaddingLeft() + dragCard.getPaddingRight() -
                    clickedCard.getPaddingRight();
            int heightDiff = dragCard.getPaddingTop() - clickedCard.getPaddingTop() + dragCard.getPaddingBottom() -
                    clickedCard.getPaddingBottom();
            int width = clickedView.getMeasuredWidth() + widthDiff;
            int height = clickedView.getMeasuredHeight() + heightDiff;
            dragView.setLayoutParams(new FrameLayout.LayoutParams(width, height));

            int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            dragView.measure(widthSpec, heightSpec);
        }

        @Override
        public void onStartDragAnimation(View dragView) {
            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));
            ObjectAnimator anim = ObjectAnimator.ofFloat(dragCard, "CardElevation", dragCard.getCardElevation(), 40);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(ANIMATION_DURATION);
            anim.start();
        }

        @Override
        public void onEndDragAnimation(View dragView) {
            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));
            ObjectAnimator anim = ObjectAnimator.ofFloat(dragCard, "CardElevation", dragCard.getCardElevation(), 6);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(ANIMATION_DURATION);
            anim.start();
        }
    }

    private long getCreatedItems() {
        return (long) (backlogAdapter.getItemCount() + todoAdapter.getItemCount() + wipAdapter.getItemCount() + codeReviewAdapter.getItemCount() + doneAdapter.getItemCount() + acceptedAdapter.getItemCount());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TASK_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                Task newTask = (Task) data.getSerializableExtra("task");
                if(data.getBooleanExtra("edited",false)){
                    firebaseManager.getProjectsReference().child(project.getId()).child(FirebaseConstants.CHILD_TASKS).child(newTask.getId()).setValue(newTask);
                }else{
                    firebaseManager.getProjectsReference().child(project.getId()).child(FirebaseConstants.CHILD_TASKS).push().setValue(newTask);
                }
            }
        }
    }
}
