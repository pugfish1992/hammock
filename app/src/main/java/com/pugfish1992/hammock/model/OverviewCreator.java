package com.pugfish1992.hammock.model;

import com.pugfish1992.javario.OverviewData;

/**
 * Created by daichi on 11/4/17.
 */

public class OverviewCreator {

    private OverviewData mOverviewData;

    public OverviewCreator() {
        mOverviewData = new OverviewData();
    }

    public OverviewCreator title(String title) {
        mOverviewData.title = title;
        return this;
    }

    public OverviewCreator summary(String summary) {
        mOverviewData.summary = summary;
        return this;
    }

    public Overview save() {
        if (!mOverviewData.save()) {
            throw new RuntimeException(
                    "failed to save a new overview data");
        }
        Overview overview = new Overview(mOverviewData.id);
        mOverviewData = new OverviewData();
        return overview;
    }
}
