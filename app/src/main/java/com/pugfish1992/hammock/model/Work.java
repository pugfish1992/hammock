package com.pugfish1992.hammock.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;
import android.util.Log;

import static com.google.common.base.Preconditions.checkNotNull;

import com.pugfish1992.hammock.ModelConstant;
import com.pugfish1992.javario.WorkData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by daichi on 11/1/17.
 */

public class Work implements ModelConstant {

    private static final int INVALID_CACHED_COUNT = -1;
    
    @NonNull final private WorkData mWorkData;
    @NonNull final private Overview mOverview;
    @Nullable private LongSparseArray<Work> mSubWorks;
    @Nullable private LongSparseArray<Comment> mComments;
    private int mSubWorkCountCache = INVALID_CACHED_COUNT;
    private int mCommentCountCache = INVALID_CACHED_COUNT;

    public Work(long id) {
        mWorkData = WorkData.findItem(id);
        if (mWorkData == null) {
            throw new RuntimeException("invalid id");
        }

        mOverview = new Overview(mWorkData.id_Overview);
    }

    /* Intentional private */
    private Work(@NonNull WorkData data) {
        mWorkData = checkNotNull(data);
        mOverview = new Overview(mWorkData.id_Overview);
    }

    public long getId() {
        return mWorkData.id;
    }

    public Overview getOverview() {
        return mOverview;
    }

    /**
     * SUB-WORKS
     * ---------- */

    public void obtainSubWorks(boolean forceInvalidate) {
        obtainSubWorks(false, forceInvalidate);
    }

    public void obtainSubWorksRecursively(boolean forceInvalidate) {
        obtainSubWorks(true, forceInvalidate);
    }

    private void obtainSubWorks(boolean obtainRecursively, boolean forceInvalidate) {
        if (mSubWorks != null && !forceInvalidate) return;

        if (mSubWorks != null) {
            clearSubWorks();
        }

        mSubWorks = new LongSparseArray<>();
        List<WorkData> dataList = WorkData.listItems();
        if (dataList == null) return;

        for (WorkData data : dataList) {
            if (data.id_ParentWork == this.getId()) {
                Work work = new Work(data);
                mSubWorks.put(work.getId(), work);
                if (obtainRecursively) {
                    work.obtainSubWorksRecursively(forceInvalidate);
                }
            }
        }
        mSubWorkCountCache = mSubWorks.size();
    }

    public void clearSubWorks() {
        if (mSubWorks != null) {
            mSubWorks.clear();
        }
        mSubWorks = null;
    }

    public void attachSubWork(@NonNull Work work) {
        // Prevent cross-reference
        if (work.hasParentWork()) {
            throw new IllegalStateException(
                    "specified work already has a parent");
        }

        if (mSubWorks != null) {
            mSubWorks.put(work.mWorkData.id, work);
        }

        work.mWorkData.id_ParentWork = this.mWorkData.id;
        this.mWorkData.save();
        work.mWorkData.save();

        if (0 <= mSubWorkCountCache) {
            ++mSubWorkCountCache;
        }
    }

    public void dettachSubWork(@NonNull Work work) {
        if (!this.isParentOf(work)) return;

        if (mSubWorks != null) {
            mSubWorks.remove(work.mWorkData.id);
        }

        work.mWorkData.id_ParentWork = INVALID_ID;
        work.mWorkData.save();
        this.mWorkData.save();

        --mSubWorkCountCache;
    }

    public boolean isParentOf(@NonNull Work child) {
        return child.mWorkData.id_ParentWork == mWorkData.id;
    }

    public boolean hasParentWork() {
        return hasParentWork(mWorkData);
    }

    public List<Work> getSubWorks() {
        if (mSubWorks == null) {
            this.obtainSubWorks(false);
        }
        ArrayList<Work> subWorks = new ArrayList<>(mSubWorks.size());
        for (int i = 0; i < mSubWorks.size(); ++i) {
            subWorks.add(mSubWorks.get(mSubWorks.keyAt(i)));
        }
        return subWorks;
    }

    public int getSubWorkCount() {
        if (mSubWorks != null) {
            return mSubWorks.size();
        }
        if (0 <= mSubWorkCountCache) {
            return mSubWorkCountCache;
        }

        List<WorkData> dataList = WorkData.listItems();
        if (dataList == null) return 0;

        Iterator<WorkData> iterator = dataList.iterator();
        final long parentId = this.getId();
        int count = 0;
        while (iterator.hasNext()) {
            if (iterator.next().id_ParentWork == parentId) {
                ++count;
            }
        }

        mSubWorkCountCache = count;
        return count;
    }

    public void clearSubWorkCountCache() {
        mSubWorkCountCache = INVALID_CACHED_COUNT;
    }

    /**
     * COMMENTS
     * ---------- */

    public void obtainComments(boolean forceInvalidate) {
        if (mComments != null && !forceInvalidate) return;

        if (mComments != null) {
            clearComments();
        }

        mComments = new LongSparseArray<>();
        for (Comment comment : Comment.findCommentsOf(this)) {
            mComments.put(comment.getId(), comment);
        }
        mCommentCountCache = mComments.size();
    }

    public void clearComments() {
        if (mComments != null) {
            mComments.clear();
        }
        mComments = null;
    }

    public void attachComment(@NonNull Comment comment) {
        if (checkNotNull(comment).hasParent()) {
            throw new IllegalStateException(
                    "specified comment already has a parent work");
        }

        comment.setParentWork(this);
        if (mComments != null) {
            mComments.put(comment.getId(), comment);
        }

        if (0 <= mCommentCountCache) {
            ++mCommentCountCache;
        }
    }

    public void dettachComment(@NonNull Comment comment) {
        if (!this.isParentOf(comment)) {
            throw new IllegalArgumentException(
                    "specified comment has a parent work, " +
                            "but it is different from this work");
        }

        comment.removeParentWork();
        if (mComments != null) {
            mComments.remove(comment.getId());
        }

        --mCommentCountCache;
    }

    public boolean isParentOf(@NonNull Comment comment) {
        return checkNotNull(comment).getParentWorkId() == this.getId();
    }

    public List<Comment> getComments() {
        if (mComments == null) {
            this.obtainComments(false);
        }
        ArrayList<Comment> comments = new ArrayList<>();
        for (int i = 0; i < mComments.size(); ++i) {
            long key = mComments.keyAt(i);
            comments.add(mComments.get(key));
        }
        return comments;
    }

    public int getCommentCount() {
        if (mComments != null) {
            return mComments.size();
        } else if (0 <= mCommentCountCache) {
            return mCommentCountCache;
        } else {
            return Comment.getCommentCountOf(this);
        }
    }

    public void clearCommentCountCache() {
        mCommentCountCache = INVALID_CACHED_COUNT;
    }

    /**
     * CLASS METHODS
     * ---------- */

    public static void delete(@NonNull Work work) {
        checkNotNull(work).mWorkData.delete();
        Overview.delete(work.getOverview());
        for (Work subWork : work.getSubWorks()) {
            Work.delete(subWork);
        }
        for (Comment comment : work.getComments()) {
            Comment.delete(comment);
        }
    }

    private static boolean hasParentWork(@NonNull WorkData data) {
        return data.id_ParentWork != INVALID_ID;
    }

    @NonNull
    public static List<Work> getRootWorks() {
        List<WorkData> dataList = WorkData.listItems();
        if (dataList == null) return Collections.emptyList();
        List<Work> works = new ArrayList<>();
        for (WorkData data : dataList) {
            if (!hasParentWork(data)) {
                works.add(new Work(data));
            }
        }
        return works;
    }

    public static Work findById(long id) {
        WorkData data = WorkData.findItem(id);
        return (data != null) ? new Work(data) : null;
    }
}
