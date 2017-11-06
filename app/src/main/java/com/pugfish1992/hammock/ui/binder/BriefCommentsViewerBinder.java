package com.pugfish1992.hammock.ui.binder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pugfish1992.hammock.R;
import com.pugfish1992.hammock.model.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * This class depends on res/layout/comments_brief_viewer.xml
 */

public class BriefCommentsViewerBinder {

    private RecyclerView mRoot;
    private BriefCommentAdapter mAdapter;

    public BriefCommentsViewerBinder(View root) {
        mRoot = root.findViewById(R.id.comments_brief_viewer_recycler_root);
    }

    public RecyclerView getRootView() {
        return mRoot;
    }

    public void setup(Context context, @Nullable List<Comment> comments) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRoot.setLayoutManager(layoutManager);
        if (comments != null) {
            mAdapter = new BriefCommentAdapter(comments);
        } else {
            mAdapter = new BriefCommentAdapter(new ArrayList<Comment>(0));
        }
        mRoot.setAdapter(mAdapter);
        mRoot.addItemDecoration(new DividerItemDecoration(context, layoutManager.getOrientation()));
    }

    public void swapComments(@Nullable List<Comment> comments) {
        if (comments != null) {
            mAdapter.swapData(comments);
        } else {
            mAdapter.swapData(new ArrayList<Comment>(0));
        }
    }

    /* ------------------------------------- *
     * ADAPTER
     * ------------------------------------- */

    private static class BriefCommentAdapter extends RecyclerView.Adapter<BriefCommentHolder> {

        @NonNull private final List<Comment> mComments;

        BriefCommentAdapter(@NonNull List<Comment> comments) {
            mComments = comments;
            this.setHasStableIds(true);
        }

        void swapData(@NonNull List<Comment> comments) {
            mComments.clear();
            mComments.addAll(comments);
            notifyDataSetChanged();
        }

        @Override
        public BriefCommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment_brief, parent, false);
            return new BriefCommentHolder(view);
        }

        @Override
        public void onBindViewHolder(BriefCommentHolder holder, int position) {
            Comment comment = mComments.get(position);
            holder.bodyText.setText(comment.getText());
        }

        @Override
        public int getItemCount() {
            return mComments.size();
        }
    }

    /* ------------------------------------- *
     * VIEW HOLDER
     * ------------------------------------- */

    private static class BriefCommentHolder extends RecyclerView.ViewHolder {

        final TextView timeLabel;
        final TextView bodyText;

        BriefCommentHolder(View view) {
            super(view);
            timeLabel = view.findViewById(R.id.comment_brief_txt_time_label);
            bodyText = view.findViewById(R.id.comment_brief_txt_body_text);
        }
    }
}
