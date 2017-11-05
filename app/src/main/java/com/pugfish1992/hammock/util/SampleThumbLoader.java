package com.pugfish1992.hammock.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daichi on 11/6/17.
 */

public class SampleThumbLoader {

    private final List<Bitmap> mBitmapCache;

    public SampleThumbLoader(Context context) {
        String[] sourceNames = new String[] {
                "sample1.png",
                "sample2.jpg",
                "sample3.jpg",
                "sample4.png",
                "sample5.jpg",
                "sample6.png"
        };

        mBitmapCache = new ArrayList<>();
        try {
            for (String sourceName : sourceNames) {
                InputStream istream =
                        context.getResources().getAssets().open(sourceName);
                Bitmap bitmap = BitmapFactory.decodeStream(istream);
                mBitmapCache.add(bitmap);
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public Bitmap get() {
        int index = (int) (mBitmapCache.size() * Math.random());
        return mBitmapCache.get(index);
    }
}
