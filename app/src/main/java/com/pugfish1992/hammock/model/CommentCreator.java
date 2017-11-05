package com.pugfish1992.hammock.model;

import android.support.annotation.NonNull;

import com.pugfish1992.javario.CommentData;

import java.util.Calendar;

/**
 * Created by daichi on 11/3/17.
 */

public class CommentCreator {

    private CommentData mCommentData;

    public CommentCreator() {
        mCommentData = new CommentData();
    }

    public CommentCreator text(String text) {
        mCommentData.text = text;
        return this;
    }

    public CommentCreator lastModifiedDate(@NonNull Calendar lastModifiedDate) {
        mCommentData.ts_LastModified = lastModifiedDate.getTimeInMillis();
        return this;
    }

    public CommentCreator createdDate(@NonNull Calendar createdDate) {
        mCommentData.ts_CreatedAt = createdDate.getTimeInMillis();
        return this;
    }

    public Comment save() {
        if (!mCommentData.save()) {
            throw new RuntimeException("failed to save a new comment data");
        }
        Comment comment = new Comment(mCommentData.id);
        mCommentData = new CommentData();
        return comment;
    }
}
