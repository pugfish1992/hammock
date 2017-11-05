package com.pugfish1992.hammock.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;
import com.pugfish1992.hammock.ModelConstant;
import com.pugfish1992.javario.OverviewData;

/**
 * Created by daichi on 11/4/17.
 */

public class Overview implements ModelConstant {

    @NonNull private final OverviewData mOverviewData;

    public Overview(long id) {
        mOverviewData = OverviewData.findItem(id);
        if (mOverviewData == null) {
            throw new IllegalArgumentException("invalid id");
        }
    }

    /* Intentional private */
    private Overview(@NonNull OverviewData data) {
        mOverviewData = data;
    }

    private boolean mIsInBatchedUpdates = false;

    public void beginBatchedUpdates() {
        mIsInBatchedUpdates = true;
    }

    public void endBatchedUpdates() {
        mIsInBatchedUpdates = false;
        mOverviewData.save();
    }

    public Overview setTitle(String title) {
        mOverviewData.title = title;
        if (!mIsInBatchedUpdates) {
            mOverviewData.save();
        }
        return this;
    }

    public Overview setSummary(String summary) {
        mOverviewData.summary = summary;
        if (!mIsInBatchedUpdates) {
            mOverviewData.save();
        }
        return this;
    }

    public long getId() {
        return mOverviewData.id;
    }

    public String getTitle() {
        return mOverviewData.title;
    }

    public String getSummary() {
        return mOverviewData.summary;
    }

    /**
     * CLASS METHODS
     * ---------- */

    @Nullable
    public static Overview findById(long id) {
        OverviewData data = OverviewData.findItem(id);
        return (data != null) ? new Overview(data) : null;
    }

    public static void delete(@NonNull Overview overview) {
        OverviewData.deleteItem(checkNotNull(overview).mOverviewData);
    }
}
