package com.example.heatherlogan.songle;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OptionsActivity extends AppCompatActivity {

    public static final String TAG = "Options Activity";

    SongDatasource song_data;
    ScoreboardDatasource scoreboard_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        song_data = new SongDatasource(this);
        scoreboard_data = new ScoreboardDatasource(this);

        try {
            song_data.open();
            scoreboard_data.open();
        } catch (Exception e){
            Log.e(TAG, "DATABASE EXCEPTION");
        }

        /* Buttons */

        openHowToPlay();
        openUnlockedSongs();
        resetGame();
        goBack();
    }

    private void openHowToPlay(){

        /* Simple how to play dialog dismissed by clicking anywhere on dialog */

        Button howtoplayBttn = findViewById(R.id.howtoplayBttn);
        howtoplayBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(OptionsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.how_to_play_dialog, null);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                LinearLayout layout = mView.findViewById(R.id.howtolayout);
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void openUnlockedSongs() {
        Button viewUnlockedBttn = findViewById(R.id.viewUnlockedBttn);
        viewUnlockedBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoUnlockedSongs = new Intent(OptionsActivity.this, UnlockedSongsActivity.class);
                startActivity(gotoUnlockedSongs);
            }
        });
    }

    private void resetGame(){

        /* User can reset game which clears played songs and scoreboard table. */

        Button resetGame = findViewById(R.id.resetGame);
        resetGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(OptionsActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.on_reset_dialog, null);

                Button yesBttn = mView.findViewById(R.id.resetYes);
                Button noBttn = mView.findViewById(R.id.resetNo);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                yesBttn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        Log.i(TAG, "reset scoreboard and played songs");

                        song_data.clearDatabase("played_songs");
                        scoreboard_data.clearDatabase("scoreboard_table");

                        dialog.dismiss();

                        Snackbar snackbar = Snackbar.make(findViewById(R.id.linearlayout), "Game has been reset", Snackbar.LENGTH_LONG);
                        TextView snackbarTV = (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);

                        snackbar.getView().setBackgroundColor(Color.DKGRAY);
                        snackbarTV.setTextColor(Color.WHITE);
                        snackbarTV.setTextSize(20);
                        snackbar.show();

                    }
                });

                noBttn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });

            }
        });



    }

    private void goBack(){

            Button goback = findViewById(R.id.goBackOptions);
            goback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent (OptionsActivity.this, MainActivity.class);
                    startActivity(i);
                }
            });

        }



}
