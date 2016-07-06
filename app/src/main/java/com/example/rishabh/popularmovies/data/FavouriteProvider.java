package com.example.rishabh.popularmovies.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Rishabh on 6/24/16.
 */

@ContentProvider(authority = FavouriteProvider.AUTHORITY, database = FavouriteDb.class)
public class FavouriteProvider {
    public static final String AUTHORITY = "com.example.rishabh.popularmovies.data.FavouriteProvider";
    public static final String DB_FILE = "mydb.sqlite";


    interface Path {
        String MOVIE = "movies";
        String DETAIL = "details";
    }

    @TableEndpoint(table = FavouriteDb.MOVIES_TABLE_NAME)
    public static class MovieLists {

        @ContentUri(path = Path.MOVIE,
                type = "vnd.android.cursor.dir/movielist",
                defaultSort = MovieColumns.TITLE + " ASC"
        )
        public static final Uri URI_MOVIE_LISTS = Uri.parse("content://" + AUTHORITY + "/" + Path.MOVIE);


    }

    @TableEndpoint(table = FavouriteDb.DETAILS_TABLE_NAME)
    public static class Detail {

        @InexactContentUri(
                path = Path.DETAIL + "/#",
                type = "vnd.android.cursor.dir/details",
                whereColumn = DetailColumns.ID,
                name = "ID",
                pathSegment = 1

        )
        public static Uri withId(String id) {
            return Uri.parse("content://" + AUTHORITY + "/" + Path.DETAIL + "/" + id);
        }
    }
}
