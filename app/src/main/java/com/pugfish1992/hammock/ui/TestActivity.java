package com.pugfish1992.hammock.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.pugfish1992.hammock.R;
import com.pugfish1992.hammock.ui.binder.CommentListBottomSheetBinder;
import com.pugfish1992.hammock.ui.binder.WorkListBottomSheetBinder;

public class TestActivity extends AppCompatActivity {

    WorkListBottomSheetBinder mWorkListSheetBinder;
    CommentListBottomSheetBinder mCommentListSheetBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View root = findViewById(R.id.cl_root);
        mCommentListSheetBinder = new CommentListBottomSheetBinder(root);
        mCommentListSheetBinder.setSheetElevation(
                getResources().getDimensionPixelSize(R.dimen.comment_list_bottom_sheet_elevation));

        mWorkListSheetBinder = new WorkListBottomSheetBinder(root);
        mWorkListSheetBinder.setSheetElevation(
                getResources().getDimensionPixelSize(R.dimen.work_list_bottom_sheet_elevation));
        mWorkListSheetBinder.appendPeekHeight(mCommentListSheetBinder.getPeekHeight());
    }
}
