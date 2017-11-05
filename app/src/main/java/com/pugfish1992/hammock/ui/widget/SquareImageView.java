package com.pugfish1992.hammock.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by daichi on 11/6/17.
 */

public class SquareImageView extends AppCompatImageView {

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // pass width spec for *both* width & height to get a square tile
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
