package com.pugfish1992.hammock.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.pugfish1992.hammock.R;
import com.pugfish1992.hammock.model.Comment;
import com.pugfish1992.hammock.model.Work;
import com.pugfish1992.hammock.ui.binder.CommentListBottomSheetBinder;
import com.pugfish1992.hammock.ui.binder.WorkListBottomSheetBinder;
import com.pugfish1992.hammock.util.ResUtils;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    WorkListBottomSheetBinder mWorkListSheetBinder;
    CommentListBottomSheetBinder mCommentListSheetBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        List<Comment> comments = new ArrayList<>();
        long id = 1;
        while (Comment.findById(id) != null) {
            comments.add(Comment.findById(id));
            ++id;
        }

        View root = findViewById(R.id.cl_root);

        mCommentListSheetBinder = new CommentListBottomSheetBinder(root, this, comments);
        mCommentListSheetBinder.setSheetElevation(ResUtils.getPx(this, R.dimen.comment_list_bs_elevation));

        mWorkListSheetBinder = new WorkListBottomSheetBinder(root, this, Work.getRootWorks());
        mWorkListSheetBinder.setSheetElevation(ResUtils.getPx(this, R.dimen.work_list_bs_elevation));
        mWorkListSheetBinder.appendPeekHeight(mCommentListSheetBinder.getPeekHeight());
        mWorkListSheetBinder.setExtraBottomContentOffset(mCommentListSheetBinder.getPeekHeight(), this);
    }
}
