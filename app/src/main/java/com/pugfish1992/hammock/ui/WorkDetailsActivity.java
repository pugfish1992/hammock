package com.pugfish1992.hammock.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.common.base.Preconditions;
import com.pugfish1992.hammock.R;
import com.pugfish1992.hammock.model.Comment;
import com.pugfish1992.hammock.model.CommentCreator;
import com.pugfish1992.hammock.model.Work;
import com.pugfish1992.hammock.ui.binder.CommentsViewerBottomSheetBinder;

import java.util.Calendar;

public class WorkDetailsActivity extends AppCompatActivity
        implements
        WorkAdapterBase.ItemCardClickListener,
        NewWorkDialog.OnCreateNewWorkListener,
        TextInputDialog.TextInputListener {

    public static final String KEY_TARGET_WORK_ID = "WorkDetailsActivity:targetWorkId";

    private Work mTargetWork;
    private WorkAdapterBase mSubWorkAdapter;

    // UI
    private BottomSheetBehavior mSheetBehavior;

    public static Bundle makeExtras(@NonNull Work targetWork) {
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_TARGET_WORK_ID, Preconditions.checkNotNull(targetWork).getId());
        return bundle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(KEY_TARGET_WORK_ID)) {
            long id = extras.getLong(KEY_TARGET_WORK_ID);
            mTargetWork = new Work(id);

        } else {
            throw new IllegalStateException(
                    "pass the id of a target work to this activity");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(mTargetWork.getOverview().getTitle());

        FloatingActionButton fabNewSubWork = (FloatingActionButton) findViewById(R.id.fab_new_sub_work);
        fabNewSubWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddNewWork();
            }
        });

        FloatingActionButton fabNewComment = (FloatingActionButton) findViewById(R.id.fab_new_comment);
        fabNewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddNewComment();
            }
        });

        mSubWorkAdapter = new WorkAdapter(mTargetWork.getSubWorks(), this, this);

        RecyclerView workList = (RecyclerView) findViewById(R.id.recycler_view);
        workList.setLayoutManager(new LinearLayoutManager(this));
        workList.setAdapter(mSubWorkAdapter);

        // # Bottom Sheet

        CommentsViewerBottomSheetBinder bottomSheetBinder =
                new CommentsViewerBottomSheetBinder(findViewById(R.id.cl_root), this, mTargetWork.getComments());
        final int sheetElevation = getResources().getDimensionPixelSize(R.dimen.work_details_activity_bottom_sheet_elevation);
        bottomSheetBinder.setSheetElevation(sheetElevation);
    }

    private void onAddNewWork() {
        NewWorkDialog dialog = new NewWorkDialog();
        dialog.show(getSupportFragmentManager(), null);
    }

    private void onAddNewComment() {
        TextInputDialog dialog = new TextInputDialog.Builder()
                .title("Add Comment")
                .editTextHint("Comment")
                .cancelButtonTitle("discard")
                .okButtonTitle("save")
                .build();

        dialog.show(getSupportFragmentManager(), null);
    }

    /**
     * INTERFACE IMPL -> WorkAdapterBase.ItemCardClickListener
     * ---------- */

    @Override
    public void onItemCardClick(int position) {
        Work subWork = mSubWorkAdapter.getAt(position);
        Bundle extras = WorkDetailsActivity.makeExtras(subWork);
        Intent intent = new Intent(this, WorkDetailsActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
    }

    /**
     * INTERFACE IMPL -> NewWorkDialog.OnCreateNewWorkListener
     * ---------- */

    @Override
    public void onNewWorkCreated(@NonNull Work work) {
        mTargetWork.attachSubWork(work);
        mSubWorkAdapter.add(work);
    }

    /**
     * INTERFACE IMPL -> TextInputDialog.TextInputListener
     * ---------- */

    @Override
    public void onTextInput(@Nullable String text) {
        if (text != null) {
            Calendar date = Calendar.getInstance();
            Comment comment = new CommentCreator()
                    .text(text)
                    .lastModifiedDate(date)
                    .createdDate(date)
                    .save();

            mTargetWork.attachComment(comment);
        }
    }
}
