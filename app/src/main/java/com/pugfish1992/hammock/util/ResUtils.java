package com.pugfish1992.hammock.util;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Px;

/**
 * Created by daichi on 11/8/17.
 */

public class ResUtils {

    @NonNull private final Context mContext;

    public ResUtils(@NonNull Context context) {
        mContext = context;
    }

    @Px
    public int getPx(@DimenRes int id) {
        return getPx(mContext, id);
    }


    @Px
    public static int getPx(Context context, @DimenRes int id) {
        return context.getResources().getDimensionPixelSize(id);
    }
}
