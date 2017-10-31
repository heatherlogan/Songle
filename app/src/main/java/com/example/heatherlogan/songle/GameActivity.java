package com.example.heatherlogan.songle;

import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_menu);

        openMap();

        openCollectedWords();

        guessSong();

        giveUp();
    }

    private void openMap(){
        Button view_map = findViewById(R.id.view_map);
        view_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent gotomap = new Intent(GameActivity.this, MapActivity.class);
                startActivity(gotomap);
            }
        });
    }

    private void guessSong(){
        Button guess_song_bttn = findViewById(R.id.guess_song_bttn);
        guess_song_bttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.guess_dialog, null);
                final EditText mGuess = (EditText) mView.findViewById(R.id.songGuessEnter);

                Button mEnter = (Button) mView.findViewById(R.id.enterButton);
                mEnter.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick (View view){
                        // If statement to see if correct
                        //if not, show Toast.
                        //if correct, check scoreboard & enter name
                        Intent gotoscore = new Intent(GameActivity.this, ScoreboardActivity.class);
                        startActivity(gotoscore);
                    }
                });
                Button mExit = (Button) mView.findViewById(R.id.gobackButton);
                mExit.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick (View view){
                        // replace later with close
                        Intent goBack = new Intent(GameActivity.this, GameActivity.class);
                        startActivity(goBack);
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();


            }
        });

    }

    private void openCollectedWords(){
        Button view_collected_words = findViewById(R.id.view_collected_words);
        view_collected_words.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotocollectedwords = new Intent(GameActivity.this, ViewCollectedWords.class);
                startActivity(gotocollectedwords);
            }
        });
    }


    private void giveUp(){
        Button give_up_button = findViewById(R.id.give_up_button);
        give_up_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.give_up_dialog, null);

                Button yesGiveUp = mView.findViewById(R.id.yesGiveUp);
                yesGiveUp.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick (View view){

                        Toast.makeText(GameActivity.this,
                                "You have Given up!\nThe song was SONGNAME by ARTISTNAmE",
                                Toast.LENGTH_LONG).show();

                        Intent giveupgame = new Intent(GameActivity.this, MainActivity.class);
                        startActivity(giveupgame);
                    }
                });
                Button keepPlaying = (Button) mView.findViewById(R.id.keepPlaying);
                keepPlaying.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick (View view){
                        // replace later with close
                        Intent dontGiveUp = new Intent(GameActivity.this, GameActivity.class);
                        startActivity(dontGiveUp);
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

    }
}
