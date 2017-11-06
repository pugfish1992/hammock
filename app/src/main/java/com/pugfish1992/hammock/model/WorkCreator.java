package com.pugfish1992.hammock.model;

import android.support.annotation.NonNull;

import com.pugfish1992.javario.WorkData;

/**
 * Created by daichi on 11/3/17.
 */

public class WorkCreator {

    private WorkData mWorkData;

    public WorkCreator() {
        mWorkData = new WorkData();
    }

    public WorkCreator overview(@NonNull Overview overview) {
        mWorkData.id_Overview = overview.getId();
        return this;
    }

    public WorkCreator isCompleted(boolean isCompleted) {
        mWorkData.isCompleted = isCompleted;
        return this;
    }

    public Work save() {
        if (mWorkData.id_Overview == Comment.INVALID_ID) {
            throw new IllegalStateException(
                    "call overview method() before call this method");
        }

        if (!mWorkData.save()) {
            throw new RuntimeException(
                    "failed to save a new work data");
        }

        Work work = new Work(mWorkData.id);
        mWorkData = new WorkData();
        return work;
    }
}
