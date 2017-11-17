package com.example.heatherlogan.songle;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

/**
 * Created by heatherlogan on 10/11/2017.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String MARKERS_TABLE = "markers";
    public static final String COLLECTEDWORDS_TABLE = "collected_words";

    public static final String ID = "marker_id";
    public static final String TITLE = "marker_title";
    public static final String SNIPPET = "marker_snippet";
    public static final String POSITION = "marker_position";

    public static final String ID2 = "word_id";
    public static final String WORD = "song_word";
    public static final String LINE_NUMBER = "word_line_no";
    public static final String POS_NUMBER = "word_pos_no";

    private static final int D_VERSION = 1;
    private static final String DB_NAME = "songle_info.db";

    private static final String DB_CREATE_MARKERS =
            "create table "+ MARKERS_TABLE + "("
                    + ID + " integer primary key , "
                    + TITLE + " text, "
                    + SNIPPET + " text, "
                    + POSITION + " text);";


    private static final String DB_CREATE_COLLECTED_WORDS =
            "create table "+ COLLECTEDWORDS_TABLE + "("
                    + ID2 + " integer primary key , "
                    + WORD + " text, "
                    + LINE_NUMBER + " integer, "
                    + POS_NUMBER + " integer);";


    public MySQLiteHelper (Context context){
        super(context, DB_NAME, null, D_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(DB_CREATE_MARKERS);
        db.execSQL(DB_CREATE_COLLECTED_WORDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL( "DROP TABLE IF EXISTS "  + MARKERS_TABLE);
        db.execSQL( "DROP TABLE IF EXISTS "  + COLLECTEDWORDS_TABLE);
        onCreate(db);
    }

}
