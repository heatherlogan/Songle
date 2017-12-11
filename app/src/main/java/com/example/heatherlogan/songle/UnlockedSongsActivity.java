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

        setContentView(R.layout.activity_unlocked_songs);
        ListView mListView = findViewById(R.id.unlockedSongsListview);

        played_songs = new SongDatasource(this);

        try {
            Log.i(TAG, "Played songs database opened");
            played_songs.open();
        } catch (Exception e ){
            Log.e(TAG, "DATABASE EXCEPTION");
        }

        List<Song> ps = played_songs.getPlayedSongs();

        ArrayList<Song> played_list = new ArrayList<>(ps);

        UnlockedSongAdapter adapter = new UnlockedSongAdapter(this, R.layout.unlocked_songs_adapter, played_list);
        mListView.setAdapter(adapter);


        goBack();
    }

    private void goBack(){
        Button goBk = findViewById(R.id.gobackUnlockedSongs);
        goBk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnlockedSongsActivity.super.onBackPressed();
            }
        });
    }
}
