package com.example.heatherlogan.songle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.app.Dialog;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.InputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParserException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.MapView;


public class MainActivity extends AppCompatActivity {

    private static final int ERROR_DIALOG_REQUEST = 9001;
    public final String TAG = "MainActivity";
    public static final String WIFI = "Wi-fi";

    private RadioGroup difficultyoptions;
    int difficultyChoice;

    PlacemarkDatasource data;
    CollectedWordsDatasource word_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapsInitializer.initialize(getApplicationContext());

        System.out.println("is network available: " + isNetworkAvailable());

        checkServices();

        if (checkServices()) {
            resumeGame();
            newGame();

        } else {
            System.out.print("No service");
        }

        data = new PlacemarkDatasource(this);
        word_data = new CollectedWordsDatasource(this);

        try {
            data.open();
            word_data.open();

        } catch (Exception e) {
            Log.e(TAG, "DATABASE EXCEPTION");
        }

        openOptions();
    }

    /*--------------------------------------------- Buttons ----------------------------------------------------------*/
    private void resumeGame() {
        Button resumeGameButton = findViewById(R.id.resumeGameButton);
        resumeGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, GameActivity.class);
                startActivity(i);
            }
        });
    }

    private void newGame() {
        Button newGameButton = findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.new_game_dialog, null);

                Button yesButton = (Button) mView.findViewById(R.id.yesButton);
                Button noButton = (Button) mView.findViewById(R.id.noButton);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                yesButton.setOnClickListener(new View.OnClickListener() {

                    // Choosing to start a new game brings up dialog to choose difficulty level
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder m2Builder = new AlertDialog.Builder(MainActivity.this);
                        View m2View = getLayoutInflater().inflate(R.layout.select_difficulty_dialog, null);

                        difficultyoptions = (RadioGroup) m2View.findViewById(R.id.rg);
                        Button playButton7 = (Button) m2View.findViewById(R.id.playButton7);

                        m2Builder.setView(m2View);
                        final AlertDialog dialog2 = m2Builder.create();
                        dialog2.show();

                        difficultyoptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                                RadioButton radiob = (RadioButton) radioGroup.findViewById(checkedId);

                                switch (radiob.getId()) {
                                    case R.id.veryEasyRB:
                                        difficultyChoice = 5;
                                        break;
                                    case R.id.easyRB:
                                        difficultyChoice = 4;
                                        break;
                                    case R.id.mediumRB:
                                        difficultyChoice = 3;
                                        break;
                                    case R.id.hardRB:
                                        difficultyChoice = 2;
                                        break;
                                    case R.id.veryHardRB:
                                        difficultyChoice = 1;
                                        break;
                                }
                            }
                        });
                        //Enter button in Select Difficulty Level launches new game
                        playButton7.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                //clear previous marker and collected words database on new game
                                Log.i(TAG, "cleared database");
                                data.clearDatabase("markers");
                                word_data.clearDatabase("collected_words");

                                if (difficultyoptions.getCheckedRadioButtonId() == -1) {

                                    Toast.makeText(MainActivity.this, "Please Select Difficulty", Toast.LENGTH_LONG).show();

                                } else {

                                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                                    intent.putExtra("mapNo", difficultyChoice);
                                    startActivity(intent);

                                    dialog2.dismiss();

                                }
                            }
                        });
                        dialog.dismiss();
                    }
                });
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void openScoreboard(View view) {
        Intent openScoreboard = new Intent(MainActivity.this, ScoreboardActivity.class);
        startActivity(openScoreboard);
    }

    public void openOptions() {

        Button optionsBttn = (Button) findViewById(R.id.optionsButton);
        optionsBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openOptions = new Intent(MainActivity.this, OptionsActivity.class);
                startActivity(openOptions);
            }

        });

    }

    /* -------------Check for connection to google Play Services and Network Connection before entering game----------------------- */

    private boolean checkServices() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS) {

            System.out.println("Google API avalable");

            return true;

        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {

            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            System.out.println("Google API not avalable");
            return false;
        }
        return false;
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = conMan.getActiveNetworkInfo();
        return activeInfo != null && activeInfo.isConnected();
    }



}


