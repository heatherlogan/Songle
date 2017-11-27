package com.example.heatherlogan.songle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class UnlockedSongsActivity extends AppCompatActivity {

    SongDatasource played_songs;
    private static final String TAG = "View played Songs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.unlocked_songs_activity);
        ListView mListView = (ListView) findViewById(R.id.unlockedSongsListview);

        played_songs = new SongDatasource(this);

        try {
            played_songs.open();
        } catch (Exception e ){
            Log.e(TAG, "DATABASE EXCEPTION");
        }

        List<Song> ps = played_songs.getPlayedSongs();

        ArrayList<Song> played_list = new ArrayList<>(ps);

        UnlockedSongAdapter adapter = new UnlockedSongAdapter(this, R.layout.unlocked_songs_adapter, played_list);
        mListView.setAdapter(adapter);


        //testing

        StringBuilder r = new StringBuilder();
        int count = 0;
        for (Song s : played_list ) {
            count ++;
            r.append(" \n");
            r.append(" : " + s.getNumber() + " : " + s.getTitle() +","+ s.getArtist());
        }
        System.out.println(r.toString());
        System.out.println("Number of played Songs: " + count);

        goBack();
    }

    private void goBack(){
        Button goBk = (Button) findViewById(R.id.gobackUnlockedSongs);
        goBk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnlockedSongsActivity.super.onBackPressed();
            }
        });
    }
}
