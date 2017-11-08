package com.pugfish1992.hammock.ui.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Px;

/**
 * Created by daichi on 11/7/17.
 */

public class ViewUtils {

    /* Intentional private */
    private ViewUtils() {}

    @Px
    public static int getActionBarSize(Context context) {
        TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarSize;
    }
}
