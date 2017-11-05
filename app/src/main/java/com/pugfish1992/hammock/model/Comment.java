package com.pugfish1992.hammock.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.google.common.base.Preconditions.checkNotNull;

import com.pugfish1992.hammock.ModelConstant;
import com.pugfish1992.javario.CommentData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by daichi on 11/1/17.
 */

public class Comment implements ModelConstant {

    @NonNull private final CommentData mCommentData;

    public Comment(long id) {
        mCommentData = CommentData.findItem(id);
        if (mCommentData == null) {
            throw new IllegalArgumentException("invalid comment id");
        }
    }

    /* Intentional private */
    private Comment(@NonNull CommentData data) {
        mCommentData = data;
    }

    /**
     * GET
     * ---------- */

    public long getParentWorkId() {
        return mCommentData.id_ParentWork;
    }

    public long getId() {
        return mCommentData.id;
    }

    public String getText() {
        return mCommentData.text;
    }

    public Calendar getLastModifiedDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mCommentData.ts_LastModified);
        return calendar;
    }

    public Calendar getCreatedDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mCommentData.ts_CreatedAt);
        return calendar;
    }

    /**
     * SET
     * ---------- */

    private boolean mIsInBatchedUpdates = false;

    public void beginBatchedUpdates() {
        mIsInBatchedUpdates = true;
    }

    public void endBatchedUpdates() {
        mIsInBatchedUpdates = false;
        mCommentData.save();
    }

    public Comment setText(String text) {
        mCommentData.text = text;
        if (!mIsInBatchedUpdates) {
            mCommentData.save();
        }
        return this;
    }

    public Comment setLastModifiedDate(Calendar lastModifiedDate) {
        mCommentData.ts_LastModified = lastModifiedDate.getTimeInMillis();
        if (!mIsInBatchedUpdates) {
            mCommentData.save();
        }
        return this;
    }

    public Comment setCreatedDate(Calendar createdDate) {
        mCommentData.ts_CreatedAt = createdDate.getTimeInMillis();
        if (!mIsInBatchedUpdates) {
            mCommentData.save();
        }
        return this;
    }

    /**
     * MANAGE PARENT WORK
     * ---------- */

    /* Intentional package-private */
    void setParentWork(@NonNull Work work) {
        mCommentData.id_ParentWork = checkNotNull(work).getId();
        mCommentData.save();
    }

    /* Intentional package-private */
    void removeParentWork() {
        mCommentData.id_ParentWork = INVALID_ID;
        mCommentData.save();
    }

    public boolean hasParent() {
        return mCommentData.id_ParentWork != INVALID_ID;
    }

    /**
     * CLASS METHODS
     * ---------- */

    public static List<Comment> findCommentsOf(@NonNull Work work) {
        List<Comment> comments = new ArrayList<>();
        for (CommentData data : CommentData.listItems()) {
            if (data.id_ParentWork == work.getId()) {
                comments.add(new Comment(data.id));
            }
        }

        return comments;
    }

    public static int getCommentCountOf(@NonNull Work work) {
        List<CommentData> dataList = CommentData.listItems();
        if (dataList == null) return 0;

        Iterator<CommentData> iterator = dataList.iterator();
        final long parentId = work.getId();
        int count = 0;
        while (iterator.hasNext()) {
            if (iterator.next().id_ParentWork == parentId) {
                ++count;
            }
        }

        return count;
    }

    @Nullable
    public static Comment findById(long id) {
        CommentData data = CommentData.findItem(id);
        return (data != null) ? new Comment(data) : null;
    }

    public static void delete(@NonNull Comment comment) {
        checkNotNull(comment).mCommentData.delete();
    }
}
