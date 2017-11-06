package com.pugfish1992.hammock.ui.binder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pugfish1992.hammock.R;

/**
 * This class depends on res/layout/comments_viewer.xml
 */

public class CommentsViewerBinder {

    public interface ActionListener {
        void onCloseButtonClick();
    }

    private final View mRootView;
    private final RecyclerView mCommentsList;
    private final TextView mCommentCount;

    private final ActionListener mActionListener;

    public CommentsViewerBinder(View root, ActionListener listener) {
        mActionListener = listener;
        mRootView = root.findViewById(R.id.comments_viewer_fl_root);
        mCommentsList = root.findViewById(R.id.comments_viewer_recycler_comments_list);
        mCommentCount = root.findViewById(R.id.comments_viewer_txt_comment_count);

        root.findViewById(R.id.comments_viewer_img_close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActionListener != null) {
                    mActionListener.onCloseButtonClick();
                }
            }
        });
    }

    public View getRootView() {
        return mRootView;
    }
}
