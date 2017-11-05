package com.pugfish1992.hammock.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pugfish1992.hammock.R;
import com.pugfish1992.hammock.model.Work;

import java.util.List;

/**
 * Created by daichi on 11/6/17.
 */

public class WorkAdapter extends WorkAdapterBase<WorkAdapter.WorkHolder> {

    public WorkAdapter(List<Work> works, @NonNull ItemCardClickListener listener) {
        super(works, listener);
    }

    @Override
    public WorkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_work_card, parent, false);
        final WorkHolder holder = new WorkHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItemCardClickListener().onItemCardClick(holder.getAdapterPosition());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(WorkHolder holder, int position) {
        Work work = getAt(position);
        holder.name.setText(work.getOverview().getTitle());
        holder.summary.setText(work.getOverview().getSummary());
        holder.commentCount.setText(work.getCommentCount() + " comments");
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
}
