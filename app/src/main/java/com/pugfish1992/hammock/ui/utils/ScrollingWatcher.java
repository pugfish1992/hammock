package com.pugfish1992.hammock.ui.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by daichi on 11/8/17.
 */

public class ScrollingWatcher extends RecyclerView.OnScrollListener {

    public interface Callback {
        void onScrolled(ScrollingWatcher scrollingWatcher);
    }

    private Callback mCallback;
    private int mScrollOffsetX = 0;
    private int mScrollOffsetY = 0;

    /**
     * @param host scroll offsets (x,y) must be 0 at this time.
     */
    public ScrollingWatcher(@NonNull RecyclerView host) {
        host.addOnScrollListener(this);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        mScrollOffsetX += dx;
        mScrollOffsetY += dy;

        if (mCallback != null) {
            mCallback.onScrolled(this);
        }
    }

    public int getScrollOffsetX() {
        return mScrollOffsetX;
    }

    public int getScrollOffsetY() {
        return mScrollOffsetY;
    }
}
