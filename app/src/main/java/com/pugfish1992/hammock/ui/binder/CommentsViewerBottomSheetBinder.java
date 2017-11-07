package com.pugfish1992.hammock.ui.binder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.pugfish1992.hammock.R;
import com.pugfish1992.hammock.model.Comment;
import com.pugfish1992.hammock.ui.BottomSheetCallbackHelper;

import java.util.List;

/**
 * This class depends on res/layout/comments_viewer_bottom_sheet.xml
 */

public class CommentsViewerBottomSheetBinder
        implements
        CommentsViewerBinder.ActionListener,
        CommentPosterBinder.ActionListener,
        BottomSheetCallbackHelper.StateChangeListener {

    private final View mRoot;
    // comment-poster and brief-comments-viewer
    private final View mPreviewerRoot;
    private final View mMainViewerRoot;
    private final CommentPosterBinder mPosterBinder;
    private final BriefCommentsViewerBinder mBriefViewerBinder;
    private final CommentsViewerBinder mViewerBinder;

    private final BottomSheetBehavior mSheetBehavior;

    public CommentsViewerBottomSheetBinder(View root, Context context, @Nullable List<Comment> comments) {
        mPosterBinder = new CommentPosterBinder(root);
        mPosterBinder.setActionListener(this);

        mBriefViewerBinder = new BriefCommentsViewerBinder(root);
        mViewerBinder = new CommentsViewerBinder(root, context, this, comments);
        mRoot = root.findViewById(R.id.comments_sheet_fl_root);
        mPreviewerRoot = root.findViewById(R.id.comments_sheet_preview_content_root);
        mMainViewerRoot = mViewerBinder.getRootView();

        mSheetBehavior = BottomSheetBehavior.from(mRoot);
        BottomSheetCallbackHelper sheetCallbackHelper = new BottomSheetCallbackHelper();
        sheetCallbackHelper.setStateChangeListener(this);
        sheetCallbackHelper.attachToHost(mSheetBehavior);

        // default status
        mBriefViewerBinder.setup(context, comments);
        setIsExpanded(false);
    }

    public View getRootView() {
        return mRoot;
    }

    /**
     *
     * @param elevationDip in dip
     */
    public void setSheetElevation(int elevationDip, Context context) {
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, elevationDip,
                context.getResources().getDisplayMetrics());
        ViewCompat.setElevation(mRoot, px);
    }

    /**
     *
     * @param elevationPx in px
     */
    public void setSheetElevation(float elevationPx) {
        ViewCompat.setElevation(mRoot, elevationPx);
    }

    public void setIsExpanded(boolean isExpanded) {
        mSheetBehavior.setState(
                isExpanded
                ? BottomSheetBehavior.STATE_EXPANDED
                : BottomSheetBehavior.STATE_COLLAPSED);

        if (isExpanded) {
            mSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            mPreviewerRoot.setVisibility(View.GONE);
            mMainViewerRoot.setVisibility(View.VISIBLE);
        } else {
            mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            mMainViewerRoot.setVisibility(View.GONE);
            mPreviewerRoot.setVisibility(View.VISIBLE);
        }
    }

    public void swapComments(@NonNull List<Comment> comments) {
        mBriefViewerBinder.swapComments(comments);
    }

    /**
     * INTERFACE IMPL -> CommentsViewerBinder.ActionListener
     * ---------- */

    @Override
    public void onCloseButtonClick() {
        this.setIsExpanded(false);
    }

    /**
     * INTERFACE IMPL -> CommentPosterBinder.ActionListener
     * ---------- */

    @Override
    public void onPosterClick() {
        this.setIsExpanded(true);
    }

    /**
     * INTERFACE IMPL -> BottomSheetCallbackHelper.StateChangeListener
     * ---------- */

    @Override
    public void onSheetStateChanged(@NonNull BottomSheetCallbackHelper.SheetState state) {
        Log.d("mylog", "[changed] " + state.name());
    }
}
