package com.example.heatherlogan.songle;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.app.Dialog;

import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.xmlpull.v1.XmlPullParserException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    private static final int ERROR_DIALOG_REQUEST = 9001;
    public static final String WIFI = "Wi-fi";
    public static final String URL = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkServices()) {
            openGame();
            loadPage();
        } else {
            System.out.print("No service");
        }
    }

    /*--------------------------------------------- Buttons ----------------------------------------------------------*/
    private void openGame() {
        Button newGameButton = findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoGame = new Intent(MainActivity.this, GameActivity.class);
                startActivity(gotoGame);
            }
        });
    }
    public void openScoreboard(View view) {
        Intent openScoreboard = new Intent(MainActivity.this, ScoreboardActivity.class);
        startActivity(openScoreboard);
    }

    public void openOptions(View view) {
        Intent openOptions = new Intent(MainActivity.this, OptionsActivity.class);
        startActivity(openOptions);
    }


    /* -------------Check for connection to google Play Services and Network Connection before entering game----------------------- */
    private boolean checkServices() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            return false;
        }
        return false;
    }

    private boolean isNetworkAvailable(){

        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = conMan.getActiveNetworkInfo();
        return activeInfo != null && activeInfo.isConnected();
    }

        /* ------------------------------------ XML -----------------------------------------
    -------------------------------------------------------------------------------------- */

    public void loadPage() {
        if (checkServices() && isNetworkAvailable()) {
            new DownloadXmlTask().execute(URL);
        } else {
           getResources().getString(R.string.connection_error);
        }
    }

    // passes feed URL, loadXml.. fetches and processes feed. returns result string
    private class DownloadXmlTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {

                return loadXmlFromNetwork(urls[0]);

            } catch (IOException e) {
                return getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                return getResources().getString(R.string.xml_error);
            }
        }
       @Override
        protected void onPostExecute(String result) {

           System.out.println(result + "---");
        }

    }

     private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {

            InputStream stream = null;

            XmlParser mXmlParser = new XmlParser();
            List<Song> songs = null;
            String number = null;
            String artist = null;
            String title = null;
            String link = null;

            StringBuilder result = new StringBuilder();

            try {

                stream = downloadUrl(urlString);
                songs = mXmlParser.parse(stream);

            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
            for (Song song : songs) {
                result.append(" \n");
                result.append(song.getNumber());
                result.append(" : " + song.getTitle() + " : " + song.getArtist() +" : " +  song.getLink() + "");

            }

         return result.toString();





     }
        //Given a string connection  of a url, sets up a string connection and gets an input stream.

        private InputStream downloadUrl(String urlString) throws IOException {

           // System.out.println("Download URL: "+ URL);
            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            return conn.getInputStream();

        }


}

