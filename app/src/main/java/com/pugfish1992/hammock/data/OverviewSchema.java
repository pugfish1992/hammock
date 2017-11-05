package com.pugfish1992.hammock.data;

import com.pugfish1992.hammock.ModelConstant;
import com.pugfish1992.javario.annotation.ModelSchema;
import com.pugfish1992.javario.annotation.PrimaryKey;

/**
 * Created by daichi on 11/4/17.
 */

@ModelSchema("OverviewData")
public class OverviewSchema implements ModelConstant {
    @PrimaryKey
    long id = INVALID_ID;
    String title;
    String summary;
}
