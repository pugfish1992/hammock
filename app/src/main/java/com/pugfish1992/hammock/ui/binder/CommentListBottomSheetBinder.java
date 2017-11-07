package com.pugfish1992.hammock.ui.binder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.transition.AutoTransition;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import com.pugfish1992.hammock.R;
import com.pugfish1992.hammock.ui.BottomSheetCallbackHelper;

/**
 * This class depends res/layout/comment_list_bottom_sheet.xml
 */

public class CommentListBottomSheetBinder
        implements BottomSheetCallbackHelper.StateChangeListener {

    private final ViewGroup mRoot;
    private final View mScrim;
    private final CommentPosterBinder mPosterBinder;

    private final BottomSheetBehavior mSheetBehavior;

    public CommentListBottomSheetBinder(View root) {
        mRoot = root.findViewById(R.id.comment_list_bs_fl_root);

        mScrim = mRoot.findViewById(R.id.comment_list_bs_view_scrim);
        mScrim.setVisibility(View.INVISIBLE);

        mPosterBinder = new CommentPosterBinder(mRoot);

        mSheetBehavior = BottomSheetBehavior.from(mRoot);
        BottomSheetCallbackHelper sheetCallbackHelper = new BottomSheetCallbackHelper();
        sheetCallbackHelper.setStateChangeListener(this);
        sheetCallbackHelper.attachToHost(mSheetBehavior);
    }

    public int getPeekHeight() {
        return mSheetBehavior.getPeekHeight();
    }

    public void setSheetElevation(int elevationPx) {
        ViewCompat.setElevation(mRoot, elevationPx);
    }

    private void showScrimWithAnim() {
        if (mScrim.isShown()) return;
        Point center = mPosterBinder.getCenterOfIcon();
        int startRad = 0;
        int endRad = (int) Math.hypot(mScrim.getWidth(), mScrim.getHeight());
        Animator revealAnim = ViewAnimationUtils.createCircularReveal(mScrim, center.x, center.y, startRad, endRad);
        mScrim.setVisibility(View.VISIBLE);
        revealAnim.start();
    }

    private void hideScrimWithAnim() {
        if (!mScrim.isShown()) return;
        Point center = mPosterBinder.getCenterOfIcon();
        int startRad = (int) Math.hypot(mScrim.getWidth(), mScrim.getHeight());
        int endRad = 0;
        Animator closeAnim = ViewAnimationUtils.createCircularReveal(mScrim, center.x, center.y, startRad, endRad);
        closeAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mScrim.setVisibility(View.GONE);
            }
        });
        closeAnim.start();
    }

    private void changePosterModeWithAnim(@CommentPosterBinder.PosterMode int mode) {
        Transition transition = new AutoTransition();
        transition.setDuration(250);
        TransitionManager.beginDelayedTransition(mRoot, transition);
        mPosterBinder.setPosterMode(mode);
    }

    /**
     * INTERFACE IMPL -> BottomSheetCallbackHelper.StateChangeListener
     * ---------- */

    @Override
    public void onSheetStateChanged(@NonNull BottomSheetCallbackHelper.SheetState state) {
        switch (state) {
            case EXPANDED:
                showScrimWithAnim();
                changePosterModeWithAnim(CommentPosterBinder.POSTER_MODE_COUNTER);
                break;

            case EXPANDED_BUT_COULD_COLLAPSED:
                hideScrimWithAnim();
                changePosterModeWithAnim(CommentPosterBinder.POSTER_MODE_NAVIGATION_MESS);
                break;

            case SLIDING_BETWEEN_COLLAPSED_EXPANDED:
                changePosterModeWithAnim(CommentPosterBinder.POSTER_MODE_NAVIGATION_MESS);
                break;

            case WILL_COLLAPSED_FROM_TOP:
                changePosterModeWithAnim(CommentPosterBinder.POSTER_MODE_VIEWER);
                break;
        }
    }
}
