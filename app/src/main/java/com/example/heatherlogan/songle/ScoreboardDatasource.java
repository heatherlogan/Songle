package com.example.heatherlogan.songle;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.Cursor;

import java.util.List;
import java.util.ArrayList;

public class ScoreboardDatasource {

    private MySQLiteHelper2 dbhelper;
    private SQLiteDatabase db;

    private String[] cols = {MySQLiteHelper2.NAME, MySQLiteHelper2.LEVEL, MySQLiteHelper2.TIME } ;

    public ScoreboardDatasource(Context context) {
        dbhelper = new MySQLiteHelper2(context);
    }

    public void open(){
        db = dbhelper.getWritableDatabase();
    }
    public void close(){
        db.close();
    }

    public void addToScoreboard(User user){

        ContentValues cv = new ContentValues();

        cv.put(MySQLiteHelper2.NAME, user.getUserName());
        cv.put(MySQLiteHelper2.LEVEL, user.getUserLevel());
        cv.put(MySQLiteHelper2.TIME,  user.getUserTime());

        db.insert(MySQLiteHelper2.SCOREBOARD_TABLE, null, cv);

    }



    public List<User> getScoreboard() {

        List<User> scoreboard_users = new ArrayList<>();

        Cursor cursor = db.query(MySQLiteHelper2.SCOREBOARD_TABLE, cols, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            User u = cursorToUser(cursor);
            scoreboard_users.add(u);
            cursor.moveToNext();
        } cursor.close();

        return scoreboard_users;
    }

    private User cursorToUser(Cursor cursor){

        User u = new User();

        u.setUserName(cursor.getString(0));
        u.setUserLevel(cursor.getString(1));
        u.setUserTime(Integer.parseInt(cursor.getString(2)));

        return u;
    }

    public void clearDatabase (String TABLE_NAME){
        String clearDB = "DELETE FROM " + TABLE_NAME;
        db.execSQL(clearDB);
    }

}
