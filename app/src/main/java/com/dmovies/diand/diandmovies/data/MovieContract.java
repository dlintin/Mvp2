package com.dmovies.diand.diandmovies.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by USER on 28/05/2017.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.dmovies.diand.diandmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";
    public static final class MovieEntry implements BaseColumns {
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_FAVORITES)
            .build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_FAVORITES;
        // Table name
    public static final String TABLE_NAME = "movies";
    public static final String ID = "id";
    public static final String ORIGINAL_TITLE = "original_title";
    public static final String OVERVIEW = "overview";
    public static final String VOTE = "vote_average";
    public static final String RELEASE = "release_date";
    public static final String POSTER = "poster_path";
    public static final String original_language = "original_language";
        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
}
}
