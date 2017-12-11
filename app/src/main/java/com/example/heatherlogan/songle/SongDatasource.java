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

public class SongDatasource {

    private MySQLiteHelper2 dbhelper;
    private SQLiteDatabase db;

    private String[] cols_unplayed = {
            MySQLiteHelper2.UNPLAYED_SONG_NUMB,
            MySQLiteHelper2.UNPLAYED_SONG_ARTIST,
            MySQLiteHelper2.UNPLAYED_SONG_TITLE};
    private String[] cols_played = {
            MySQLiteHelper2.PLAYED_SONG_NUMB,
            MySQLiteHelper2.PLAYED_SONG_ARTIST,
            MySQLiteHelper2.PLAYED_SONG_TITLE};

    public SongDatasource(Context context){
        dbhelper = new MySQLiteHelper2(context);
    }

    public void open(){
        db = dbhelper.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    // Add songs to databases

    public void addUnplayedSong(Song song){

        ContentValues cv = new ContentValues();

        cv.put(MySQLiteHelper2.UNPLAYED_SONG_NUMB, song.getNumber());
        cv.put(MySQLiteHelper2.UNPLAYED_SONG_ARTIST, song.getArtist());
        cv.put(MySQLiteHelper2.UNPLAYED_SONG_TITLE, song.getTitle());

        db.insert(MySQLiteHelper2.UNPLAYED_SONGS_TABLE, null, cv);
    }

    public void addPlayedSong(Song song){

        ContentValues cv = new ContentValues();

        cv.put(MySQLiteHelper2.PLAYED_SONG_NUMB, song.getNumber());
        cv.put(MySQLiteHelper2.PLAYED_SONG_ARTIST, song.getArtist());
        cv.put(MySQLiteHelper2.PLAYED_SONG_TITLE, song.getTitle());

        db.insert(MySQLiteHelper2.PLAYED_SONGS_TABLE, null, cv);
    }

    public void removeUnplayedSong(String number){

        /* remove songs from databases */

        db.delete(MySQLiteHelper2.UNPLAYED_SONGS_TABLE, "unplayed_song_number = ?", new String[] { number });
    }

    /* retrieve database in list form */

    public List<Song> getUnplayedSongs(){

        List<Song> unplayed_songs = new ArrayList<>();

        Cursor cursor = db.query(MySQLiteHelper2.UNPLAYED_SONGS_TABLE, cols_unplayed, null, null, null, null, null );

        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            Song s = cursorToSong(cursor);
            unplayed_songs.add(s);
            cursor.moveToNext();
        } cursor.close();

        return unplayed_songs;
    }

    public List<Song> getPlayedSongs(){

        List<Song> played_songs = new ArrayList<>();

        Cursor cursor = db.query(MySQLiteHelper2.PLAYED_SONGS_TABLE, cols_played, null, null, null, null, null );

        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            Song s = cursorToSong(cursor);
            played_songs.add(s);
            cursor.moveToNext();
        } cursor.close();

        return played_songs;
    }

    private Song cursorToSong (Cursor cursor){

        Song s = new Song();

        s.setNumber(cursor.getString(0));
        s.setArtist(cursor.getString(1));
        s.setTitle(cursor.getString(2));

        return s;
    }

    public boolean songExistsInPlayed(String number){

        /* Check if song exists in played Songs database */

        Cursor cursor = null;
        String query = "SELECT played_song_number FROM " +MySQLiteHelper2.PLAYED_SONGS_TABLE+ " WHERE played_song_number=" + number;
        cursor = db.rawQuery(query, null);

        return cursor.getCount() > 0;
    }

    public void clearDatabase(String TABLE_NAME){
        String clearDB = "DELETE FROM " + TABLE_NAME;
        db.execSQL(clearDB);
    }


}