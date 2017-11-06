package com.pugfish1992.hammock.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.common.base.Preconditions;
import com.pugfish1992.hammock.model.Work;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by daichi on 11/5/17.
 */

public abstract class WorkAdapterBase<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    @NonNull private final List<Work> mWorks;
    private final ItemCardClickListener mItemCardClickListener;

    WorkAdapterBase(List<Work> works, @NonNull ItemCardClickListener listener) {
        mWorks = (works != null) ? works : new ArrayList<Work>();
        mItemCardClickListener = Preconditions.checkNotNull(listener);
        this.setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return mWorks.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return mWorks.size();
    }

    public void add(@NonNull Work work) {
        mWorks.add(Preconditions.checkNotNull(work));
        notifyItemInserted(mWorks.size() - 1);
    }

    Work getAt(int position) {
        return mWorks.get(position);
    }

    public void swapData(@NonNull List<Work> works) {
        mWorks.clear();
        mWorks.addAll(Preconditions.checkNotNull(works));
        notifyDataSetChanged();
    }

    protected ItemCardClickListener getItemCardClickListener() {
        return mItemCardClickListener;
    }

    /* ------------------------------------------------------------------------------- *
     * INTERFACE FOR ITEM TOUCH EVENT
     * ------------------------------------------------------------------------------- */

    interface ItemCardClickListener {
        void onItemCardClick(int position);
    }
}
