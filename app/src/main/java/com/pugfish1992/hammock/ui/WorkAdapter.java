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
import com.pugfish1992.hammock.model.Work;

import java.util.Collections;
import java.util.List;

/**
 * Created by daichi on 11/5/17.
 */

class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.WorkHolder> {

    @NonNull private final List<Work> mWorks;
    private final ItemCardClickListener mClickListener;

    WorkAdapter(List<Work> works, @NonNull ItemCardClickListener listener) {
        mWorks = (works != null) ? works : Collections.<Work>emptyList();
        mClickListener = Preconditions.checkNotNull(listener);
        this.setHasStableIds(true);
    }

    @Override
    public WorkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_work_card, parent, false);
        final WorkHolder holder = new WorkHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemCardClick(holder.getAdapterPosition());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(WorkHolder holder, int position) {
        Work work = mWorks.get(position);
        holder.name.setText(work.getOverview().getTitle());
        holder.summary.setText(work.getOverview().getSummary());
        holder.commentCount.setText(work.getCommentCount() + " comments");
    }

    @Override
    public int getItemCount() {
        return mWorks.size();
    }

    void add(@NonNull Work work) {
        mWorks.add(Preconditions.checkNotNull(work));
        notifyItemInserted(mWorks.size() - 1);
    }

    Work getAt(int position) {
        return mWorks.get(position);
    }

    void swapData(@NonNull List<Work> works) {
        mWorks.clear();
        mWorks.addAll(Preconditions.checkNotNull(works));
        notifyDataSetChanged();
    }

    /* ------------------------------------------------------------------------------- *
     * VIEW HOLDER CLASS
     * ------------------------------------------------------------------------------- */

    static class WorkHolder extends RecyclerView.ViewHolder {

        final TextView name;
        final TextView summary;
        final TextView commentCount;

        WorkHolder(View view) {
            super(view);
            name = view.findViewById(R.id.txt_name);
            summary = view.findViewById(R.id.txt_summary);
            commentCount = view.findViewById(R.id.txt_comment_count);
        }
    }

    /* ------------------------------------------------------------------------------- *
     * INTERFACE FOR ITEM TOUCH EVENT
     * ------------------------------------------------------------------------------- */

    interface ItemCardClickListener {
        void onItemCardClick(int position);
    }
}
