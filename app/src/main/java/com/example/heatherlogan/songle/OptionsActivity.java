package com.example.heatherlogan.songle;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_layout);

        openHowToPlay();
        openUnlockedSongs();
    }
    private void openHowToPlay(){
        Button howtoplayBttn = findViewById(R.id.howtoplayBttn);
        howtoplayBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(OptionsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.how_to_play, null);

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        } );
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



}
