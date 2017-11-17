package com.example.heatherlogan.songle;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.Cursor;

import java.util.List;
import java.util.ArrayList;

/**
 * Code referenced from online tutorials by Javier C.
 * https://www.youtube.com/watch?v=s5phWxbMPH0&t=7s
 */

public class CollectedWordsDatasource {

    private MySQLiteHelper dbhelper;
    private SQLiteDatabase db;

    private String[] cols = {MySQLiteHelper.WORD, MySQLiteHelper.LINE_NUMBER, MySQLiteHelper.POS_NUMBER};

    public CollectedWordsDatasource(Context context){
        dbhelper = new MySQLiteHelper(context);
    }

    public void open(){
        db = dbhelper.getWritableDatabase();
    }
    public void close(){
        db.close();
    }

    public void addCollectedWord(WordInfo word){

        ContentValues cv = new ContentValues();

        cv.put(MySQLiteHelper.WORD, word.getWord());
        cv.put(MySQLiteHelper.LINE_NUMBER, word.getLine());
        cv.put(MySQLiteHelper.POS_NUMBER, word.getPos());

        db.insert(MySQLiteHelper.COLLECTEDWORDS_TABLE, null, cv);
    }

    public List<WordInfo> getCollectedWords(){

        List<WordInfo> collected_words = new ArrayList<>();

        Cursor cursor = db.query(MySQLiteHelper.COLLECTEDWORDS_TABLE, cols, null, null, null, null, null );

        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            WordInfo w = cursorToWord(cursor);
            collected_words.add(w);
            cursor.moveToNext();
        } cursor.close();

        return collected_words;
    }

    private WordInfo cursorToWord (Cursor cursor){

        WordInfo w = new WordInfo();

        w.setWord(cursor.getString(0));
        w.setLine(Integer.parseInt(cursor.getString(1)));
        w.setPos(Integer.parseInt(cursor.getString(2)));

        return w;
    }


    public void clearDatabase(String TABLE_NAME){
        String clearDB = "DELETE FROM " + TABLE_NAME;
        db.execSQL(clearDB);
    }


}
