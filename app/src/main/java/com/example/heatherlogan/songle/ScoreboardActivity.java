package com.example.heatherlogan.songle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreboardActivity extends AppCompatActivity {

    ScoreboardDatasource scoreboard_data;
    private static final String TAG = "Scoreboard Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard);
        ListView mListView = findViewById(R.id.scoreboardListView);

        scoreboard_data = new ScoreboardDatasource(this);

        try {
            scoreboard_data.open();
        } catch (Exception e){
            Log.e(TAG, "DATABASE EXCEPTION");
        }


        /* Displays in order of fastest time */

        List<User> scoreboard_users = scoreboard_data.getScoreboard();

        ArrayList<User> scoreboard_array = new ArrayList<User>(scoreboard_users);

        Collections.sort(scoreboard_array, User.UserComparator);

        // add comparator
        ScoreboardAdapter adapter = new ScoreboardAdapter(
                this, R.layout.scoreboard_adapter_layout, scoreboard_array);
        mListView.setAdapter(adapter);

        goBack();
    }


    @Override
    public void onBackPressed(){

        /* Override back button to prevent user from undoing the completion of the game
        * After being directed to the scoreboard.*/

        Intent i = new Intent(ScoreboardActivity.this, MainActivity.class);
        startActivity(i);
    }

    private void goBack() {

        Button goback = findViewById(R.id.gobackScoreboard);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (ScoreboardActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }

}
