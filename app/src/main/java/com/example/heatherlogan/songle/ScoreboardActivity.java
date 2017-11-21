package com.example.heatherlogan.songle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ScoreboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard);
        ListView mListView = (ListView) findViewById(R.id.scoreboardListView);

        //Song word objects

        User u1 = new User("John", "Medium", 1313 , 830 );
        User u2 = new User("Karen", "Very Easy", 3443, 2001 );

        ArrayList<User> users = new ArrayList<>();

        users.add(u1);
        users.add(u2);

        UserListAdapter adapter = new UserListAdapter(this, R.layout.scoreboard_adapter_layout, users);
        mListView.setAdapter(adapter);

        goBack();
    }


    private void goBack() {

        Button goback = (Button) findViewById(R.id.gobackScoreboard);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ScoreboardActivity.super.onBackPressed();
            }
        });

    }

}
