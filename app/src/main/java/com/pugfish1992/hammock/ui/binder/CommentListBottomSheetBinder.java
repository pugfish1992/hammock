package com.pugfish1992.hammock.ui.binder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import com.pugfish1992.hammock.R;
import com.pugfish1992.hammock.model.Comment;
import com.pugfish1992.hammock.ui.utils.BottomSheetCallbackHelper;
import com.pugfish1992.hammock.ui.CommentAdapter;
import com.pugfish1992.hammock.ui.decoration.SpacerItemDecoration;
import com.pugfish1992.hammock.ui.utils.ScrollingWatcher;
import com.pugfish1992.hammock.util.ResUtils;

import java.util.List;

/**
 * This class depends res/layout/comment_list_bottom_sheet.xml
 */

public class CommentListBottomSheetBinder
        implements BottomSheetCallbackHelper.StateChangeListener {

    private final ViewGroup mRoot;
    private final View mScrim;
    private final CommentPosterBinder mPosterBinder;

    private final BottomSheetBehavior mSheetBehavior;
    private final CommentAdapter mCommentAdapter;

    public CommentListBottomSheetBinder(View root, Context context, @Nullable List<Comment> comments) {
        mRoot = root.findViewById(R.id.comment_list_bs_fl_root);

        mScrim = mRoot.findViewById(R.id.comment_list_bs_view_scrim);
        mScrim.setVisibility(View.INVISIBLE);

        mPosterBinder = new CommentPosterBinder(mRoot, context);
        mPosterBinder.setCommentCountLabel(comments != null ? comments.size() : 0);

        mSheetBehavior = BottomSheetBehavior.from(mRoot);
        BottomSheetCallbackHelper sheetCallbackHelper = new BottomSheetCallbackHelper();
        sheetCallbackHelper.setStateChangeListener(this);
        sheetCallbackHelper.attachToHost(mSheetBehavior);

        mCommentAdapter = new CommentAdapter(comments);

        RecyclerView commentList = mRoot.findViewById(R.id.comment_list_bs_recycle_comment_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        commentList.setLayoutManager(layoutManager);
        commentList.setAdapter(mCommentAdapter);

        ResUtils resUtils = new ResUtils(context);
        final int sideSpace = resUtils.getPx(R.dimen.comment_list_bs_list_side_offset);
        SpacerItemDecoration spacerItemDecoration = new SpacerItemDecoration();
        spacerItemDecoration.setTopSpace(resUtils.getPx(R.dimen.comment_poster_height));
        spacerItemDecoration.setLeftSpace(sideSpace);
        spacerItemDecoration.setRightSpace(sideSpace);
        spacerItemDecoration.setBottomSpace(resUtils.getPx(R.dimen.comment_list_bs_list_bottom_offset));
        spacerItemDecoration.setSpaceBetweenItems(resUtils.getPx(R.dimen.comment_list_bs_cards_offset));
        commentList.addItemDecoration(spacerItemDecoration);

        // Fade out CommentPoster along with scrolling
        ScrollingWatcher scrollingWatcher = new ScrollingWatcher(commentList);
        scrollingWatcher.setCallback(new ScrollingWatcher.Callback() {
            @Override
            public void onScrolled(ScrollingWatcher scrollingWatcher) {
                int posterHeight = mPosterBinder.getRootView().getHeight();
                int diff = posterHeight - scrollingWatcher.getScrollOffsetY();

                if (diff < 0) {
                    mPosterBinder.getRootView().setVisibility(View.INVISIBLE);
                    return;
                }

                mPosterBinder.getRootView().setVisibility(View.VISIBLE);
                float alpha = Math.abs((float) diff / posterHeight);
                mPosterBinder.getRootView().setAlpha(alpha);
            }
        });
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

    /**
     * INTERFACE IMPL -> BottomSheetCallbackHelper.StateChangeListener
     * ---------- */

    @Override
    public void onSheetStateChanged(@NonNull BottomSheetCallbackHelper.SheetState state) {
        switch (state) {
            case EXPANDED:
                showScrimWithAnim();
                mPosterBinder.setPosterModeWithAnim(CommentPosterBinder.POSTER_MODE_COUNTER);
                break;

            case EXPANDED_BUT_COULD_COLLAPSED:
                hideScrimWithAnim();
                mPosterBinder.setPosterModeWithAnim(CommentPosterBinder.POSTER_MODE_NAVIGATION_MESS);
                break;

            case COLLAPSED_BUT_COULD_EXPANDED:
                mPosterBinder.setPosterModeWithAnim(CommentPosterBinder.POSTER_MODE_NAVIGATION_MESS);
                break;

            case COLLAPSED:
            case WILL_COLLAPSED_FROM_TOP:
                mPosterBinder.setPosterModeWithAnim(CommentPosterBinder.POSTER_MODE_VIEWER);
                break;
        }
    }
}
