package com.example.heatherlogan.songle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by heatherlogan on 21/11/2017.
 */

public class MySQLiteHelper2 extends SQLiteOpenHelper {

    public static final String SCOREBOARD_TABLE = "scoreboard_table" ;

    public static final String ID = "scoreboard_id";
    public static final String NAME = "user_name";
    public static final String LEVEL = "user_level";
    public static final String TIME = "user_time";
   // public static final String STEPS = "user_steps";

    private static final int D_VERSION = 1;
    private static final String DB_NAME = "scoreboard_info.db";

    private static final String DB_CREATE_SCOREBOARD =
            "create table "+ SCOREBOARD_TABLE + "("
                    + ID + " integer primary key , "
                    + NAME + " text, "
                    + LEVEL + " text, "
                    + TIME + " text);";

    public MySQLiteHelper2 (Context context){
        super(context, DB_NAME, null, D_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(DB_CREATE_SCOREBOARD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL( "DROP TABLE IF EXISTS "  + SCOREBOARD_TABLE);
        onCreate(db);
    }

}
