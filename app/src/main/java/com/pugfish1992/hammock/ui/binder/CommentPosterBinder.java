package com.pugfish1992.hammock.ui.binder;

import android.graphics.Point;
import android.support.annotation.IntDef;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pugfish1992.hammock.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This class depends on res/layout/comment_poster.xml
 */

public class CommentPosterBinder {

    public interface ActionListener {
        void onPosterClick();
    }

    // UI
    private final View mRoot;
    private final TextView mBodyText;
    private final TextView mTimeLabel;
    private final TextView mNavigationMessage;
    private final ViewGroup mCommentCounter;
    private final TextView mCommentCountLabel;
    private final ImageView mIcon;

    private ActionListener mActionListener;

    public CommentPosterBinder(View root) {
        mRoot = root.findViewById(R.id.poster_rl_root_view);
        mBodyText = mRoot.findViewById(R.id.poster_txt_body_text);
        mTimeLabel = mRoot.findViewById(R.id.poster_txt_time_label);
        mNavigationMessage = mRoot.findViewById(R.id.poster_txt_navigation_message);
        mIcon = mRoot.findViewById(R.id.comment_poster_img_icon);
        mCommentCounter = mRoot.findViewById(R.id.comment_poster_ll_comment_counter_layout);
        mCommentCountLabel = mRoot.findViewById(R.id.comment_poster_txt_comment_count);

        mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActionListener != null) {
                    mActionListener.onPosterClick();
                }
            }
        });

        setPosterMode(POSTER_MODE_VIEWER);
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({POSTER_MODE_VIEWER, POSTER_MODE_NAVIGATION_MESS, POSTER_MODE_COUNTER})
    @interface PosterMode{}
    public static final int POSTER_MODE_VIEWER = 0;
    public static final int POSTER_MODE_NAVIGATION_MESS = 1;
    public static final int POSTER_MODE_COUNTER = 2;

    public void setPosterMode(@PosterMode int mode) {
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
