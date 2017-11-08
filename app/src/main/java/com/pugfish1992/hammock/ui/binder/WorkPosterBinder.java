package com.pugfish1992.hammock.ui.binder;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.IntDef;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pugfish1992.hammock.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This class depends on res/layout/work_poster.xml
 */

public class WorkPosterBinder {

    // UI
    private final ViewGroup mRoot;
    private final TextView mBodyText;
    private final TextView mNavigationMessage;
    private final ViewGroup mWorkCounter;
    private final TextView mWorkCountLabel;
    private final ImageView mIcon;

    @PosterMode private int mCurrentPosterMode = POSTER_MODE_INVALID;

    public WorkPosterBinder(View root) {
        mRoot = root.findViewById(R.id.work_poster_rl_root);
        mBodyText = mRoot.findViewById(R.id.work_poster_txt_body_text);
        mNavigationMessage = mRoot.findViewById(R.id.works_poster_txt_navigation_message);
        mIcon = mRoot.findViewById(R.id.work_poster_img_icon);
        mWorkCounter = mRoot.findViewById(R.id.work_poster_ll_work_counter_layout);
        mWorkCountLabel = mRoot.findViewById(R.id.work_poster_txt_work_count);

        setPosterMode(POSTER_MODE_VIEWER);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({POSTER_MODE_INVALID, POSTER_MODE_VIEWER, POSTER_MODE_NAVIGATION_MESS, POSTER_MODE_COUNTER})
    @interface PosterMode{}
    private static final int POSTER_MODE_INVALID = -1;
    public static final int POSTER_MODE_VIEWER = 0;
    public static final int POSTER_MODE_NAVIGATION_MESS = 1;
    public static final int POSTER_MODE_COUNTER = 2;

    public void setPosterMode(@PosterMode int mode) {
        setPosterMode(mode, false);
    }

    public void setPosterModeWithAnim(@PosterMode int mode) {
        setPosterMode(mode, true);
    }

    public void setPosterMode(@PosterMode int mode, boolean animate) {
        if (mCurrentPosterMode == mode) return;

        if (animate) {
            AutoTransition transition = new AutoTransition();
            transition.setDuration(250);
            transition.addTarget(mBodyText);
            transition.addTarget(mNavigationMessage);
            transition.addTarget(mWorkCounter);
            TransitionManager.beginDelayedTransition(mRoot);
        }

        switch (mode) {
            case POSTER_MODE_VIEWER:
                mBodyText.setVisibility(View.VISIBLE);
                mNavigationMessage.setVisibility(View.GONE);
                mWorkCounter.setVisibility(View.GONE);
                break;

            case POSTER_MODE_NAVIGATION_MESS:
                mBodyText.setVisibility(View.GONE);
                mNavigationMessage.setVisibility(View.VISIBLE);
                mWorkCounter.setVisibility(View.GONE);
                break;

            case POSTER_MODE_COUNTER:
                mBodyText.setVisibility(View.GONE);
                mNavigationMessage.setVisibility(View.GONE);
                mWorkCounter.setVisibility(View.VISIBLE);
                break;
        }

        mCurrentPosterMode = mode;
    }


    public View getRootView() {
        return mRoot;
    }

    public Point getCenterOfIcon() {
        Point point = new Point();
        point.x = mIcon.getLeft() + mIcon.getWidth() / 2;
        point.y = mIcon.getTop() + mIcon.getHeight() / 2;
        return point;
    }

    @PosterMode
    public int getMode() {
        return mCurrentPosterMode;
    }

    public void setBodyText(String text) {
        mBodyText.setText(text);
    }

    public void setWorkCountLabel(int count) {
        mWorkCountLabel.setText(String.valueOf(count));
    }
}
