package com.pugfish1992.hammock.data;

import com.pugfish1992.hammock.ModelConstant;
import com.pugfish1992.javario.annotation.ModelSchema;
import com.pugfish1992.javario.annotation.PrimaryKey;
import com.pugfish1992.javario.datasource.DataSource;

/**
 * Created by daichi on 11/1/17.
 */

@ModelSchema("WorkData")
public class WorkSchema implements ModelConstant {
    @PrimaryKey
    final long id = INVALID_ID;
    final long id_Overview = INVALID_ID;
    final long id_ParentWork = INVALID_ID;
}
