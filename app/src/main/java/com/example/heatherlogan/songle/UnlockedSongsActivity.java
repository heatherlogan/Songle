package com.example.heatherlogan.songle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class UnlockedSongsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.unlocked_songs_activity);
        ListView mListView = (ListView) findViewById(R.id.unlockedSongsListview);

        //TOY DATA

        Song s1 = new Song("Perfect Day", "Lou Reed", null, null);
        Song s2 = new Song("Smells Like Teen Spirit", "Nirvana", null, null);
        Song s3 = new Song("Life on Mars", "David Bowie", null, null);

        ArrayList<Song> unlocked = new ArrayList<>();

        unlocked.add(s1);
        unlocked.add(s2);
        unlocked.add(s3);

        UnlockedSongAdapter adapter = new UnlockedSongAdapter(this, R.layout.unlocked_songs_adapter, unlocked);
        mListView.setAdapter(adapter);

    }
}
