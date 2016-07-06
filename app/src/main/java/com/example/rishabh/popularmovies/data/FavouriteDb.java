package com.example.rishabh.popularmovies.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Rishabh on 6/24/16.
 */

@Database(version = FavouriteDb.VERSION, fileName = FavouriteProvider.DB_FILE)
public final class FavouriteDb {

    private FavouriteDb() {
    }

    public static final int VERSION = 1;

    @Table(MovieColumns.class)
    public static final String MOVIES_TABLE_NAME = "movie";

    @Table(DetailColumns.class)
    public static final String DETAILS_TABLE_NAME = "details";


}
