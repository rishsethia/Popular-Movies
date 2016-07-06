package com.example.rishabh.popularmovies.data;

import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Rishabh on 6/24/16.
 */
public interface MovieColumns {


    @DataType(DataType.Type.TEXT)
    @NotNull
    String TITLE = "original_title";

    @DataType(DataType.Type.TEXT)
    String OVERVIEW = "overview";

    @DataType(DataType.Type.REAL)
    String VOTE = "vote_average";

    @DataType(DataType.Type.TEXT)
    String DATE = "release_date";

    @DataType(DataType.Type.TEXT)
    String PATH = "poster_path";
    // Conflict Resolution
    @DataType(DataType.Type.TEXT)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    String ID = "id";

}
