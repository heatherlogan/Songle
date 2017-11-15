package com.example.heatherlogan.songle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import android.widget.ListView;

public class ViewCollectedWords extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_collected_words);
        ListView mListView = (ListView) findViewById(R.id.collectedwordslistview);

        WordListAdapter adapter = new WordListAdapter(this, R.layout.collected_words_adapter, null);
        mListView.setAdapter(adapter);
    }
}
