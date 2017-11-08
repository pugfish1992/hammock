package com.pugfish1992.hammock.ui.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pugfish1992.hammock.R;
import com.pugfish1992.hammock.model.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daichi on 11/7/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    @NonNull private final List<Comment> mComments;

    public CommentAdapter(@Nullable List<Comment> comments) {
        if (comments != null) {
            mComments = comments;
        } else {
            mComments = new ArrayList<>();
        }
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    /* ----------------------------- *
     * VIEW HOLDER
     * ----------------------------- */

    public static class CommentHolder extends RecyclerView.ViewHolder {

        public CommentHolder(View view) {
            super(view);
        }
    }
}
