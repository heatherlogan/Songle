package com.example.heatherlogan.songle;

import com.google.android.gms.maps.model.LatLng;

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

public class PlacemarkDatasource {

    private MySQLiteHelper dbhelper;
    private SQLiteDatabase db;

    private String[] cols = {MySQLiteHelper.TITLE, MySQLiteHelper.SNIPPET, MySQLiteHelper.POSITION};

    public PlacemarkDatasource(Context context){
        dbhelper = new MySQLiteHelper(context);
    }

    public void open(){
        db = dbhelper.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    public void addMarker(Placemark placemark){

        ContentValues cv = new ContentValues();

        cv.put(MySQLiteHelper.TITLE, placemark.getName());
        cv.put(MySQLiteHelper.SNIPPET, placemark.getDescription());
        cv.put(MySQLiteHelper.POSITION, placemark.getCoordinates());

        db.insert(MySQLiteHelper.MARKERS_TABLE, null, cv);
    }

    public List<Placemark> getMarkers(){

        List<Placemark> markers = new ArrayList<>();

        Cursor cursor = db.query(MySQLiteHelper.MARKERS_TABLE, cols, null, null, null, null, null );

        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            Placemark p = cursorToMarker(cursor);
            markers.add(p);
            cursor.moveToNext();
        } cursor.close();

        return markers;
    }


    private Placemark cursorToMarker (Cursor cursor){

        Placemark p = new Placemark();

        p.setName(cursor.getString(0));
        p.setDescription(cursor.getString(1));
        p.setCoordinates(cursor.getString(2));
        return p;

    }

    public void deleteMarker(Placemark p){
        db.delete(MySQLiteHelper.MARKERS_TABLE, MySQLiteHelper.POSITION + " = '" + p.getCoordinates()  + "'", null) ;
    }

    public void clearDatabase(String TABLE_NAME){
        String clearDB = "DELETE FROM " + TABLE_NAME;
        db.execSQL(clearDB);
    }


}
