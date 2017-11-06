package com.pugfish1992.hammock.ui.decoration;

import android.graphics.Rect;
import android.support.annotation.Px;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by daichi on 11/7/17.
 */

public class SpacerItemDecoration extends RecyclerView.ItemDecoration {

    @Px private int mTopSpace = 0;
    @Px private int mBottomSpace = 0;
    @Px private int mLeftSpace = 0;
    @Px private int mRightSpace = 0;
    @Px private int mSpaceBetweenItems = 0;

    public void setTopSpace(@Px int topSpace) {
        mTopSpace = topSpace;
    }

    public void setBottomSpace(@Px int bottomSpace) {
        mBottomSpace = bottomSpace;
    }

    public void setLeftSpace(@Px int leftSpace) {
        mLeftSpace = leftSpace;
    }

    public void setRightSpace(@Px int rightSpace) {
        mRightSpace = rightSpace;
    }

    public void setSpaceBetweenItems(@Px int spaceBetweenItems) {
        mSpaceBetweenItems = spaceBetweenItems;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        final int itemPos = parent.getChildAdapterPosition(view);

        outRect.left = mLeftSpace;
        outRect.right = mRightSpace;

        if (itemPos == 0) {
            outRect.top = mTopSpace;
        }

        if (itemPos + 1 == parent.getAdapter().getItemCount()) {
            outRect.bottom = mBottomSpace;
        } else {
            outRect.bottom = mSpaceBetweenItems;
        }
    }
}
