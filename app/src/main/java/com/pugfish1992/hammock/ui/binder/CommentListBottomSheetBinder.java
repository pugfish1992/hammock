package com.pugfish1992.hammock.ui.binder;

import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;

import com.pugfish1992.hammock.R;

/**
 * This class depends res/layout/comment_list_bottom_sheet.xml
 */

public class CommentListBottomSheetBinder {

    private ViewGroup mRoot;

    private BottomSheetBehavior mSheetBehavior;

    public CommentListBottomSheetBinder(View root) {
        mRoot = root.findViewById(R.id.comment_list_bs_fl_root);
        mSheetBehavior = BottomSheetBehavior.from(mRoot);
    }

    public int getPeekHeight() {
        return mSheetBehavior.getPeekHeight();
    }

    public void setSheetElevation(int elevationPx) {
        ViewCompat.setElevation(mRoot, elevationPx);
    }
}
