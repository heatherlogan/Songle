package com.example.heatherlogan.songle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ScoreboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard);
        ListView mListView = (ListView) findViewById(R.id.scoreboardListView);

        //Song word objects

        User u1 = new User("John", "Medium", "00:16", 830 );
        User u2 = new User("Karen", "Very Easy", "00:24", 2001 );
        User u3 = new User("Liz", "Hard", "00:40", 1330 );
        User u4 = new User("Robert", "Easy", "00:52", 1137 );
        User u5 = new User("Greg", "Easy", "01:02", 1204 );
        User u6 = new User("Hannah", "Medium", "01:10", 1501 );
        User u7 = new User("Katie", "Very", "01:21", 1529);
        User u8 = new User("Jack", "Hard", "01:36", 1703);
        User u9 = new User("Adam", "Medium", "01:40", 1739);
        User u10 = new User("Susan", "Very Hard", "01:44", 1899);

        ArrayList<User> users = new ArrayList<>();

        users.add(u1);
        users.add(u2);
        users.add(u3);
        users.add(u4);
        users.add(u5);
        users.add(u6);
        users.add(u7);
        users.add(u8);
        users.add(u9);
        users.add(u10);

        UserListAdapter adapter = new UserListAdapter(this, R.layout.scoreboard_adapter_layout, users);
        mListView.setAdapter(adapter);
    }
}
