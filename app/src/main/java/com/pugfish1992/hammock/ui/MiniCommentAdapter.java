package com.pugfish1992.hammock.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.base.Preconditions;
import com.pugfish1992.hammock.R;
import com.pugfish1992.hammock.model.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daichi on 11/6/17.
 */

public class MiniCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_MINI_COMMENT = 1024;
    private static final int TYPE_VIEW_MORE_COMMENTS = 2048;

    private final int mMaxCommentsCount;
    @NonNull private final List<Comment> mComments;

    public MiniCommentAdapter(int maxCommentsCount, @Nullable List<Comment> comments) {
        mMaxCommentsCount = maxCommentsCount;
        if (comments == null) {
            mComments = new ArrayList<>();
        } else {
            mComments = comments;
        }
    }

    public void swapData(@NonNull List<Comment> comments) {
        mComments.clear();
        mComments.addAll(Preconditions.checkNotNull(comments));
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 &&
                mMaxCommentsCount < mComments.size()) {
            return TYPE_VIEW_MORE_COMMENTS;
        } else {
            return TYPE_MINI_COMMENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_VIEW_MORE_COMMENTS) {
            View view = inflater.inflate(R.layout.more_comments_view, parent, false);
            return new ViewMoreCommentsHolder(view);
        } else if (viewType == TYPE_MINI_COMMENT) {
            View view = inflater.inflate(R.layout.comment_view_mini, parent, false);
            return new MiniCommentHolder(view);
        } else {
            // Should not be reached
            throw new RuntimeException("illegal view type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int viewType = getItemViewType(position);
        if (viewType == TYPE_MINI_COMMENT) {
            Comment comment = mComments.get(position);
            MiniCommentHolder commentHolder = (MiniCommentHolder) holder;
            commentHolder.text.setText(comment.getText());
        }
    }

    @Override
    public int getItemCount() {
        return mMaxCommentsCount < mComments.size()
                ? mMaxCommentsCount + 1
                : mComments.size();
    }

    public int getCommentCount() {
        return mComments.size();
    }

    /* ----------------------------------- *
     * VIEW HOLDER FOR MINI COMMENT-CARD VIEW
     * ----------------------------------- */

    public static class MiniCommentHolder extends RecyclerView.ViewHolder {

        final TextView text;

        public MiniCommentHolder(View view) {
            super(view);
            text = view.findViewById(R.id.txt_text);
        }
    }

    /* ----------------------------------------- *
     * VIEW HOLDER FOR VIEW-MORE-COMMENTS VIEW
     * ----------------------------------------- */

    public static class ViewMoreCommentsHolder extends RecyclerView.ViewHolder {

        public ViewMoreCommentsHolder(View view) {
            super(view);
        }
    }
}
