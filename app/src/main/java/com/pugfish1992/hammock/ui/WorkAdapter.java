package com.pugfish1992.hammock.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
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

    private final Context mContext;

    public WorkAdapter(List<Work> works, @NonNull ItemCardClickListener listener, @NonNull Context context) {
        super(works, listener);
        mContext = context;
    }

    @Override
    public WorkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_work_card, parent, false);
        final WorkHolder holder = new WorkHolder(view, mContext);
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
        holder.title.setText(work.getOverview().getTitle());
        holder.summary.setText(work.getOverview().getSummary());
        holder.commentAdapter.swapData(work.getComments());
        holder.completedMask.setVisibility(work.isCompleted() ? View.VISIBLE : View.GONE);
    }

    /* ------------------------------------------------------------------------------- *
     * VIEW HOLDER CLASS
     * ------------------------------------------------------------------------------- */

    static class WorkHolder extends RecyclerView.ViewHolder {

        final CardView baseCard;
        final TextView title;
        final TextView summary;
        final RecyclerView commentList;
        final ViewGroup completedMask;
        final MiniCommentAdapter commentAdapter;

        WorkHolder(View view, Context context) {
            super(view);
            baseCard = (CardView) view;
            title = view.findViewById(R.id.txt_title);
            summary = view.findViewById(R.id.txt_summary);
            completedMask = view.findViewById(R.id.completed_mask);

            commentAdapter = new MiniCommentAdapter(2, null);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            commentList = view.findViewById(R.id.recycler_comment_list);
            commentList.setLayoutManager(layoutManager);
            commentList.setAdapter(commentAdapter);

            DividerItemDecoration dividerItemDecoration =
                    new DividerItemDecoration(context, layoutManager.getOrientation());
            commentList.addItemDecoration(dividerItemDecoration);
        }
    }
}
