package com.example.heatherlogan.songle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class ViewCollectedWords extends AppCompatActivity {

    CollectedWordsDatasource word_data;
    private static final String TAG = "View Collected Words";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_collected_words);
        ListView mListView = findViewById(R.id.collectedwordslistview);

        word_data = new CollectedWordsDatasource(this);

        try {
            word_data.open();
            Log.i(TAG, "Words database opened");
        } catch (Exception e ){
            Log.e(TAG, "DATABASE EXCEPTION");
        }

        List<WordInfo> collected_words = word_data.getCollectedWords();

        ArrayList<WordInfo> arraylist_words = new ArrayList<WordInfo>(collected_words);

        Collections.sort(arraylist_words, WordInfo.WordComparator);

        WordListAdapter adapter = new WordListAdapter(this, R.layout.collected_words_adapter, arraylist_words);
        mListView.setAdapter(adapter);

        goBack();
    }

    private void goBack(){
        Button goBk = findViewById(R.id.gobackColWords);
        goBk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewCollectedWords.super.onBackPressed();
            }
        });
    }
}
