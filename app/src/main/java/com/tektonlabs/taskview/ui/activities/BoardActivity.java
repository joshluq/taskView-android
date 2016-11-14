package com.tektonlabs.taskview.ui.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tektonlabs.taskview.R;
import com.tektonlabs.taskview.models.Task;
import com.tektonlabs.taskview.ui.adapters.TaskAdapter;
import com.tektonlabs.taskview.utils.FirebaseArray;
import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.DragItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BoardActivity extends BaseActivity {

    @BindView(R.id.board_view)
    BoardView mBoardView;

    FirebaseArray snapshots;
    private static int sCreatedItems = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        ButterKnife.bind(this);
        mBoardView.setSnapToColumnWhenDragging(true);
        mBoardView.setSnapDragItemToTouch(true);
        mBoardView.setCustomDragItem(new MyDragItem(this, R.layout.column_item));
        addColumnList();
    }

    private void addColumnList() {
        ArrayList<Pair<Long, Task>> backlogList = new ArrayList<>();
        ArrayList<Pair<Long, Task>> todoList = new ArrayList<>();
        ArrayList<Pair<Long, Task>> wipList = new ArrayList<>();
        ArrayList<Pair<Long, Task>> codeReviewList = new ArrayList<>();
        ArrayList<Pair<Long, Task>> doneList = new ArrayList<>();
        ArrayList<Pair<Long, Task>> acceptedList = new ArrayList<>();

        TaskAdapter backlogAdapter = new TaskAdapter(backlogList, R.layout.column_item, R.id.item_layout, true);
        TaskAdapter todoAdapter = new TaskAdapter(todoList, R.layout.column_item, R.id.item_layout, true);
        TaskAdapter wipAdapter = new TaskAdapter(wipList, R.layout.column_item, R.id.item_layout, true);
        TaskAdapter codeReviewAdapter = new TaskAdapter(codeReviewList, R.layout.column_item, R.id.item_layout, true);
        TaskAdapter doneAdapter = new TaskAdapter(doneList, R.layout.column_item, R.id.item_layout, true);
        TaskAdapter acceptedAdapter = new TaskAdapter(acceptedList, R.layout.column_item, R.id.item_layout, true);

        View header = View.inflate(this, R.layout.column_header, null);
        ((TextView) header.findViewById(R.id.tv_column_name)).setText(getString(R.string.str_backlog));
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = sCreatedItems++;
                Task task = new Task();
                task.setTaskName( "Test " + id);
                Pair item = new Pair<>(id, task);
                mBoardView.addItem(0, 0, item, true);
                Intent intent = new Intent(BoardActivity.this,AddTaskActivity.class);
                startActivityForResult(intent, Activity.RESULT_OK);
            }
        });

        mBoardView.addColumnList(backlogAdapter, buildHeader(getString(R.string.str_backlog)), false);
        mBoardView.addColumnList(todoAdapter, buildHeader(getString(R.string.str_todo)), false);
        mBoardView.addColumnList(wipAdapter, buildHeader(getString(R.string.str_wip)), false);
        mBoardView.addColumnList(codeReviewAdapter, buildHeader(getString(R.string.str_code_review)), false);
        mBoardView.addColumnList(doneAdapter, buildHeader(getString(R.string.str_done)), false);
        mBoardView.addColumnList(acceptedAdapter, buildHeader(getString(R.string.str_accepted)), false);

    }

    private View buildHeader(String columnName){
        View header = View.inflate(this, R.layout.column_header, null);
        ((TextView) header.findViewById(R.id.tv_column_name)).setText(columnName);
        if(columnName.equals(getString(R.string.str_backlog))){
            ImageView iv_add_task = (ImageView) header.findViewById(R.id.iv_add_task);
            iv_add_task.setVisibility(View.VISIBLE);
            iv_add_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BoardActivity.this,AddTaskActivity.class);
                    startActivityForResult(intent, Activity.RESULT_OK);
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
            CharSequence text = ((TextView) clickedView.findViewById(R.id.text)).getText();
            ((TextView) dragView.findViewById(R.id.text)).setText(text);
            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));
            CardView clickedCard = ((CardView) clickedView.findViewById(R.id.card));

            dragCard.setMaxCardElevation(40);
            dragCard.setCardElevation(clickedCard.getCardElevation());
            dragCard.setForeground(ContextCompat.getDrawable(context,R.drawable.card_view_drag_foreground));
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
}
