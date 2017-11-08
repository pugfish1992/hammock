package com.pugfish1992.hammock.ui.binder;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.IntDef;
import android.support.annotation.Px;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pugfish1992.hammock.R;
import com.pugfish1992.hammock.util.ResUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This class depends on res/layout/comment_poster.xml
 */

public class CommentPosterBinder {

    // UI
    private final ViewGroup mRoot;
    private final TextView mBodyText;
    private final TextView mTimeLabel;
    private final TextView mNavigationMessage;
    private final ViewGroup mCommentCounter;
    private final TextView mCommentCountLabel;
    private final ImageView mIcon;

    @PosterMode private int mCurrentPosterMode = POSTER_MODE_INVALID;

    public CommentPosterBinder(View root, Context context) {
        mRoot = root.findViewById(R.id.poster_rl_root_view);
        mBodyText = mRoot.findViewById(R.id.poster_txt_body_text);
        mTimeLabel = mRoot.findViewById(R.id.poster_txt_time_label);
        mNavigationMessage = mRoot.findViewById(R.id.poster_txt_navigation_message);
        mIcon = mRoot.findViewById(R.id.comment_poster_img_icon);
        mCommentCounter = mRoot.findViewById(R.id.comment_poster_ll_comment_counter_layout);
        mCommentCountLabel = mRoot.findViewById(R.id.comment_poster_txt_comment_count);

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
            transition.addTarget(mTimeLabel);
            transition.addTarget(mNavigationMessage);
            transition.addTarget(mCommentCounter);
            TransitionManager.beginDelayedTransition(mRoot);
        }

        switch (mode) {
            case POSTER_MODE_VIEWER:
                mBodyText.setVisibility(View.VISIBLE);
                mTimeLabel.setVisibility(View.VISIBLE);
                mNavigationMessage.setVisibility(View.GONE);
                mCommentCounter.setVisibility(View.GONE);
                break;

            case POSTER_MODE_NAVIGATION_MESS:
                mBodyText.setVisibility(View.GONE);
                mTimeLabel.setVisibility(View.GONE);
                mNavigationMessage.setVisibility(View.VISIBLE);
                mCommentCounter.setVisibility(View.GONE);
                break;

            case POSTER_MODE_COUNTER:
                mBodyText.setVisibility(View.GONE);
                mTimeLabel.setVisibility(View.GONE);
                mNavigationMessage.setVisibility(View.GONE);
                mCommentCounter.setVisibility(View.VISIBLE);
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

    public void setTimeLabel(String label) {
        mTimeLabel.setText(label);
    }

    public void setCommentCountLabel(int count) {
        mCommentCountLabel.setText(String.valueOf(count));
    }
}
