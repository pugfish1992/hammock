package com.pugfish1992.hammock.data;

import com.pugfish1992.hammock.ModelConstant;
import com.pugfish1992.javario.annotation.ModelSchema;
import com.pugfish1992.javario.annotation.PrimaryKey;

/**
 * Created by daichi on 11/1/17.
 */

@ModelSchema("CommentData")
public class CommentSchema implements ModelConstant {
    @PrimaryKey
    final long id = INVALID_ID;
    final long id_ParentWork = INVALID_ID;
    String text;
    long ts_LastModified;
    long ts_CreatedAt;
}
