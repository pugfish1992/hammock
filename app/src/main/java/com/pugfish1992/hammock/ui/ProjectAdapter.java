package com.pugfish1992.hammock.ui;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pugfish1992.hammock.R;
import com.pugfish1992.hammock.model.Work;
import com.pugfish1992.hammock.util.SampleThumbLoader;

import java.util.List;

/**
 * Created by daichi on 11/6/17.
 */

public class ProjectAdapter extends WorkAdapterBase<ProjectAdapter.ProjectHolder> {

    private static final int TYPE_RIGHT_SIDE_THUMB_CARD = 1024;
    private static final int TYPE_LEFT_SIDE_THUMB_CARD = 2048;
    private static final int TYPE_LARGE_SIZE_CARD = 4096;

    private final SampleThumbLoader mSampleThumbLoader;

    public ProjectAdapter(List<Work> works, @NonNull ItemCardClickListener listener, Context context) {
        super(works, listener);
        mSampleThumbLoader = new SampleThumbLoader(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_LARGE_SIZE_CARD;
        } else {
            return (position % 2 == 0)
                    ? TYPE_LEFT_SIDE_THUMB_CARD
                    : TYPE_RIGHT_SIDE_THUMB_CARD;
        }
    }

    @Override
    public ProjectHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        @LayoutRes int layoutId;
        switch (viewType) {
            case TYPE_RIGHT_SIDE_THUMB_CARD:
                layoutId = R.layout.item_project_card_right_side_thumb;
                break;
            case TYPE_LEFT_SIDE_THUMB_CARD:
                layoutId = R.layout.item_project_card_left_side_thumb;
                break;
            case TYPE_LARGE_SIZE_CARD:
                layoutId = R.layout.item_project_card_large;
                break;
            default:
                // Should not be reached
                throw new IllegalArgumentException();
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        final ProjectHolder holder = new ProjectHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItemCardClickListener().onItemCardClick(holder.getAdapterPosition());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ProjectHolder holder, int position) {
        Work work = getAt(position);
        holder.name.setText(work.getOverview().getTitle());
        holder.summary.setText(work.getOverview().getSummary());
        holder.thumb.setImageBitmap(mSampleThumbLoader.get());
    }

    /* ----------------------------- *
     * VIEW HOLDER
     * ----------------------------- */

    public static class ProjectHolder extends RecyclerView.ViewHolder {

        final TextView name;
        final TextView summary;
        final ImageView thumb;

        public ProjectHolder(View view) {
            super(view);
            name = view.findViewById(R.id.txt_project_name);
            summary = view.findViewById(R.id.txt_project_summary);
            thumb = view.findViewById(R.id.img_project_thumb);
        }
    }
}
