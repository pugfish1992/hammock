package com.pugfish1992.hammock.ui.binder;

import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;

import com.pugfish1992.hammock.R;

/**
 * This class depends res/layout/work_list_bottom_sheet.xml
 */

public class WorkListBottomSheetBinder {

    private ViewGroup mRoot;

    private BottomSheetBehavior mSheetBehavior;

    public WorkListBottomSheetBinder(View root) {
        mRoot = root.findViewById(R.id.work_list_bs_fl_root);
        mSheetBehavior = BottomSheetBehavior.from(mRoot);
    }

    public void appendPeekHeight(int extraPeekHeight) {
        int peekHeight = mSheetBehavior.getPeekHeight();
        mSheetBehavior.setPeekHeight(peekHeight + extraPeekHeight);
    }

    public void setSheetElevation(int elevationPx) {
        ViewCompat.setElevation(mRoot, elevationPx);
    }
}

