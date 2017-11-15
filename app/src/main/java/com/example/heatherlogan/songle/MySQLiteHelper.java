package com.example.heatherlogan.songle;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

/**
 * Created by heatherlogan on 10/11/2017.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "markers";

    public static final String ID = "marker_id";
    public static final String TITLE = "marker_title";
    public static final String SNIPPET = "marker_snippet";
    public static final String POSITION = "marker_position";

    private static final int D_VERSION = 1;
    private static final String DB_NAME = "markerlocations.db";

    private static final String DB_CREATE =
            "create table "+ TABLE_NAME + "("
            + ID + " integer primary key , "
            + TITLE + " text, "
            + SNIPPET + " text, "
            + POSITION + " text);";

    public MySQLiteHelper (Context context){

        super(context, DB_NAME, null, D_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL( "DROP TABLE IF EXISTS "  + TABLE_NAME);
        onCreate(db);
    }

}
