package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static data.MovieContract.MovieEntry;

/**
 * Created by USER on 28/05/2017.
 */

public class MovieDbhelper extends SQLiteOpenHelper{

public static final String DATABASE_NAME = "movies.db";
private static final int DATABASE_VERSION = 2;
public MovieDbhelper(Context context){super(context, DATABASE_NAME, null, DATABASE_VERSION);}


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    final String SQL_CREATE_TABLE =
            "CREATE TABLE "+ MovieEntry.TABLE_NAME +" ("+
                    MovieEntry._ID                           +" INTEGER PRIMARY KEY AUTOINCREMENT,   "+
                    MovieEntry.ID                            +" INTEGER NOT NULL,                    "+
                    MovieEntry.ORIGINAL_TITLE                +" TEXT NOT NULL,                       "+
                    MovieEntry.OVERVIEW                      +" TEXT NOT NULL,                       "+
                    MovieEntry.original_language             +" TEXT NOT NULL,                       "+
                    MovieEntry.VOTE                          +" TEXT NOT NULL,                       "+
                    MovieEntry.RELEASE                       +" TEXT NOT NULL,                       "+
                    MovieEntry.POSTER                        +" TEXT NOT NULL,                       "+
                    "UNIQUE ("+ MovieEntry.ID + ")ON CONFLICT REPLACE);";
    sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
onCreate(sqLiteDatabase);
    }
}
