package com.pugfish1992.hammock.ui.binder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.transition.AutoTransition;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

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

    private final ViewGroup mRoot;
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
        mPreviewerRoot.setVisibility(View.VISIBLE);
        mMainViewerRoot.setVisibility(View.GONE);
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
    }

    public void swapComments(@NonNull List<Comment> comments) {
        mBriefViewerBinder.swapComments(comments);
    }
    
    private void showMainViewerWithAnim() {
        final View commentPoster = mPosterBinder.getRootView();
        int cx = commentPoster.getLeft() + commentPoster.getWidth() / 2;
        int cy = commentPoster.getTop() + commentPoster.getHeight() / 2;
        int startRad = 0;
        int endRad = (int) Math.hypot(mMainViewerRoot.getWidth(), mMainViewerRoot.getHeight());
        Animator revealAnim = ViewAnimationUtils.createCircularReveal(mMainViewerRoot, cx, cy, startRad, endRad);
        revealAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mPreviewerRoot.setVisibility(View.GONE);
            }
        });
        mMainViewerRoot.setVisibility(View.VISIBLE);
        revealAnim.start();
    }
    
    private void hideMainViewerWithAnim() {
        final View commentPoster = mPosterBinder.getRootView();
        int cx = commentPoster.getLeft() + commentPoster.getWidth() / 2;
        int cy = commentPoster.getTop() + commentPoster.getHeight() / 2;
        int startRad = (int) Math.hypot(mMainViewerRoot.getWidth(), mMainViewerRoot.getHeight());
        int endRad = 0;
        Animator closeAnim = ViewAnimationUtils.createCircularReveal(mMainViewerRoot, cx, cy, startRad, endRad);
        closeAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mMainViewerRoot.setVisibility(View.GONE);
            }
        });
        mPreviewerRoot.setVisibility(View.VISIBLE);
        closeAnim.start();
    }

    private void showOrHideNavigationMessageOnPosterWithAnim(boolean showMessage) {
        Transition transition = new AutoTransition();
        transition.setDuration(200);
        TransitionManager.beginDelayedTransition(mRoot, transition);
        mPosterBinder.setShowNavigationMessage(showMessage);
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
        switch (state) {
            case EXPANDED:
                showMainViewerWithAnim();
                break;

            case EXPANDED_BUT_COULD_COLLAPSED:
                hideMainViewerWithAnim();
                break;

            case SLIDING_BETWEEN_COLLAPSED_EXPANDED:
                showOrHideNavigationMessageOnPosterWithAnim(true);
                break;

            case WILL_COLLAPSED_FROM_TOP:
                showOrHideNavigationMessageOnPosterWithAnim(false);
                break;
        }
    }
}
