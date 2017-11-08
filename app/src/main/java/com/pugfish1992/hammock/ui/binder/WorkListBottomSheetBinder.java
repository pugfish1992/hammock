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
import com.pugfish1992.hammock.model.Work;
import com.pugfish1992.hammock.ui.WorkAdapter;
import com.pugfish1992.hammock.ui.decoration.SpacerItemDecoration;
import com.pugfish1992.hammock.ui.utils.BottomSheetCallbackHelper;
import com.pugfish1992.hammock.ui.utils.ScrollingWatcher;
import com.pugfish1992.hammock.util.ResUtils;

import java.util.List;

/**
 * This class depends res/layout/work_list_bottom_sheet.xml
 */

public class WorkListBottomSheetBinder 
        implements 
        BottomSheetCallbackHelper.StateChangeListener, 
        WorkAdapter.ItemCardClickListener {

    private final ViewGroup mRoot;
    private final View mScrim;
    private final WorkPosterBinder mPosterBinder;
    
    private final BottomSheetBehavior mSheetBehavior;
    private final WorkAdapter mWorkAdapter;
    private final SpacerItemDecoration mSpacerItemDecoration;

    public WorkListBottomSheetBinder(View root, Context context, @Nullable List<Work> works) {
        mRoot = root.findViewById(R.id.work_list_bs_fl_root);

        mScrim = mRoot.findViewById(R.id.work_list_bs_view_scrim);
        mScrim.setVisibility(View.INVISIBLE);

        mPosterBinder = new WorkPosterBinder(mRoot);
        mPosterBinder.setWorkCountLabel(works != null ? works.size() : 0);

        mSheetBehavior = BottomSheetBehavior.from(mRoot);
        BottomSheetCallbackHelper sheetCallbackHelper = new BottomSheetCallbackHelper();
        sheetCallbackHelper.setStateChangeListener(this);
        sheetCallbackHelper.attachToHost(mSheetBehavior);

        mWorkAdapter = new WorkAdapter(works, this, context);
                
        RecyclerView workList = mRoot.findViewById(R.id.work_list_bs_recycler_work_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        workList.setLayoutManager(layoutManager);
        workList.setAdapter(mWorkAdapter);

        ResUtils resUtils = new ResUtils(context);
        final int sideSpace = resUtils.getPx(R.dimen.work_list_bs_list_side_offset);
        mSpacerItemDecoration = new SpacerItemDecoration();
        mSpacerItemDecoration.setTopSpace(resUtils.getPx(R.dimen.work_poster_height));
        mSpacerItemDecoration.setLeftSpace(sideSpace);
        mSpacerItemDecoration.setRightSpace(sideSpace);
        mSpacerItemDecoration.setBottomSpace(resUtils.getPx(R.dimen.work_list_bs_list_bottom_offset));
        mSpacerItemDecoration.setSpaceBetweenItems(resUtils.getPx(R.dimen.work_list_bs_cards_offset));
        workList.addItemDecoration(mSpacerItemDecoration);

        // Fade out WorkPoster along with scrolling
        ScrollingWatcher scrollingWatcher = new ScrollingWatcher(workList);
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

    public void appendPeekHeight(int extraPeekHeight) {
        int peekHeight = mSheetBehavior.getPeekHeight();
        mSheetBehavior.setPeekHeight(peekHeight + extraPeekHeight);
    }

    public void setExtraBottomContentOffset(int extraOffset, Context context) {
        mSpacerItemDecoration.setBottomSpace(
                ResUtils.getPx(context, R.dimen.work_list_bs_list_bottom_offset) + extraOffset);
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
                mPosterBinder.setPosterModeWithAnim(WorkPosterBinder.POSTER_MODE_COUNTER);
                break;

            case EXPANDED_BUT_COULD_COLLAPSED:
                hideScrimWithAnim();
                mPosterBinder.setPosterModeWithAnim(WorkPosterBinder.POSTER_MODE_NAVIGATION_MESS);
                break;

            case COLLAPSED_BUT_COULD_EXPANDED:
                mPosterBinder.setPosterModeWithAnim(WorkPosterBinder.POSTER_MODE_NAVIGATION_MESS);
                break;

            case COLLAPSED:
            case WILL_COLLAPSED_FROM_TOP:
                mPosterBinder.setPosterModeWithAnim(WorkPosterBinder.POSTER_MODE_VIEWER);
                break;
        }
    }
    
    /**
     * INTERFACE IMPL -> WorkAdapter.ItemCardClickListener
     * ---------- */

    @Override
    public void onItemCardClick(int position) {
        
    }
}

