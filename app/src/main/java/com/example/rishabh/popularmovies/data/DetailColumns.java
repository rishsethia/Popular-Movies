package com.example.rishabh.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Rishabh on 6/24/16.
 */
public interface DetailColumns {


    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(DataType.Type.TEXT)
    String KEY = "key";
    @DataType(DataType.Type.TEXT)
    String NAME = "name";

    @DataType(DataType.Type.TEXT)
    String CONTENT = "content";
    @DataType(DataType.Type.TEXT)
    String AUTHOR = "author";

    @DataType(DataType.Type.TEXT)
    String ID = "id";
}
