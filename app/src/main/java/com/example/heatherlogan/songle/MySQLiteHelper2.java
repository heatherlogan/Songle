package com.example.heatherlogan.songle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by heatherlogan on 21/11/2017.
 */

public class MySQLiteHelper2 extends SQLiteOpenHelper {

    // for scoreboard table

    public static final String SCOREBOARD_TABLE = "scoreboard_table" ;
    public static final String ID = "scoreboard_id";
    public static final String NAME = "user_name";
    public static final String LEVEL = "user_level";
    public static final String TIME = "user_time";
    public static final String STEPS = "user_steps";

    public static final String UNPLAYED_SONGS_TABLE = "unplayed_songs";
    public static final String ID3 = "unplayed_song_id";
    public static final String UNPLAYED_SONG_NUMB = "unplayed_song_number";
    public static final String UNPLAYED_SONG_ARTIST = "unplayed_song_artist";
    public static final String UNPLAYED_SONG_TITLE = "unplayed_song_title";


    public static final String PLAYED_SONGS_TABLE = "played_songs";
    public static final String ID4 = "played_song_id";
    public static final String PLAYED_SONG_NUMB = "played_song_number";
    public static final String PLAYED_SONG_ARTIST = "played_song_artist";
    public static final String PLAYED_SONG_TITLE = "played_song_title";

    private static final int D_VERSION = 5;
    private static final String DB_NAME = "scoreboard_info.db";

    private static final String DB_CREATE_SCOREBOARD =
            "create table "+ SCOREBOARD_TABLE + "("
                    + ID + " integer primary key , "
                    + NAME + " text, "
                    + LEVEL + " text, "
                    + TIME + " text, "
                    + STEPS + " text);";


    private static final String DB_CREATE_UNPLAYED_SONGS =
            "create table "+ UNPLAYED_SONGS_TABLE + "("
                    + ID3 + " integer primary key , "
                    + UNPLAYED_SONG_NUMB + " text, "
                    + UNPLAYED_SONG_ARTIST + " text, "
                    + UNPLAYED_SONG_TITLE + " text);";


    private static final String DB_CREATE_PLAYED_SONGS =
            "create table "+ PLAYED_SONGS_TABLE + "("
                    + ID4 + " integer primary key , "
                    + PLAYED_SONG_NUMB + " text, "
                    + PLAYED_SONG_ARTIST + " text, "
                    + PLAYED_SONG_TITLE + " text);";


    public MySQLiteHelper2 (Context context){
        super(context, DB_NAME, null, D_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(DB_CREATE_SCOREBOARD);
        db.execSQL(DB_CREATE_UNPLAYED_SONGS);
        db.execSQL(DB_CREATE_PLAYED_SONGS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL( "DROP TABLE IF EXISTS "  + SCOREBOARD_TABLE);
        db.execSQL( "DROP TABLE IF EXISTS "  + UNPLAYED_SONGS_TABLE);
        db.execSQL( "DROP TABLE IF EXISTS "  + PLAYED_SONGS_TABLE);
        onCreate(db);
    }


}
