package com.pugfish1992.hammock.ui.binder;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pugfish1992.hammock.R;
import com.pugfish1992.hammock.model.Comment;
import com.pugfish1992.hammock.ui.CommentAdapter;
import com.pugfish1992.hammock.ui.utils.ViewUtils;
import com.pugfish1992.hammock.ui.decoration.SpacerItemDecoration;

import java.util.List;

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

    public CommentsViewerBinder(View root, Context context, ActionListener listener, @Nullable List<Comment> comments) {
        mActionListener = listener;
        mRootView = root.findViewById(R.id.comments_viewer_fl_root);

        mCommentCount = root.findViewById(R.id.comments_viewer_txt_comment_count);
        mCommentCount.setText(String.valueOf(comments != null ? comments.size() : 0));

        root.findViewById(R.id.comments_viewer_img_close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActionListener != null) {
                    mActionListener.onCloseButtonClick();
                }
            }
        });

        mCommentsList = root.findViewById(R.id.comments_viewer_recycler_comments_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mCommentsList.setLayoutManager(layoutManager);
        mCommentsList.setAdapter(new CommentAdapter(comments));

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
//        mCommentsList.addItemDecoration();

        SpacerItemDecoration spacerItemDecoration = new SpacerItemDecoration();
        final int itemTopOffset = ViewUtils.getActionBarSize(context) +
                context.getResources().getDimensionPixelSize(R.dimen.comments_viewer_extra_start_offset);
        spacerItemDecoration.setTopSpace(itemTopOffset);
        mCommentsList.addItemDecoration(spacerItemDecoration);
    }

    public View getRootView() {
        return mRootView;
    }
}
