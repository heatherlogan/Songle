package com.example.heatherlogan.songle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import android.app.Dialog;

public class MainActivity extends AppCompatActivity {

    // cited from Mitch Tabian youtube tutorial on google maps api setup
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // if google play services are working correctly we can enter game
        if (checkServices()){
            openGame();
        }
    }

    private void openGame(){
        Button newGameButton = findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent gotoGame = new Intent(MainActivity.this, GameActivity.class);
                startActivity(gotoGame);
            }
        });
    }

    // Google API Setup from Mitch Tabian. Checks if Google Play Services are working correctly
    public boolean checkServices() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {

            return false;


        } return false;
    }
}

