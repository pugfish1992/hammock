package com.pugfish1992.hammock.ui.binder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.pugfish1992.hammock.R;

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

    private ActionListener mActionListener;

    public CommentPosterBinder(View root) {
        mRoot = root.findViewById(R.id.poster_rl_root_view);
        mBodyText = root.findViewById(R.id.poster_txt_body_text);
        mTimeLabel = root.findViewById(R.id.poster_txt_time_label);
        mNavigationMessage = root.findViewById(R.id.poster_txt_navigation_message);

        mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActionListener != null) {
                    mActionListener.onPosterClick();
                }
            }
        });

        setShowNavigationMessage(false);
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public void setShowNavigationMessage(boolean showNavigationMessage) {
        if (showNavigationMessage) {
            mNavigationMessage.setVisibility(View.VISIBLE);
            mBodyText.setVisibility(View.GONE);
            mTimeLabel.setVisibility(View.GONE);
        } else {
            mNavigationMessage.setVisibility(View.GONE);
            mBodyText.setVisibility(View.VISIBLE);
            mTimeLabel.setVisibility(View.VISIBLE);
        }
    }

    public View getRootView() {
        return mRoot;
    }

    public void setBodyText(String text) {
        mBodyText.setText(text);
    }

    public void setTimeLabel(String label) {
        mTimeLabel.setText(label);
    }
}
