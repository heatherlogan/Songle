package com.example.heatherlogan.songle;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ListView;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;

public class GameActivity extends AppCompatActivity {


    public static final String TAG = "Game Activity";
    public static final String URL = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.txt";
    public static String kmlURL;
   // public static final String kmlURL = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/15/map5.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_menu);

        Intent intent = getIntent();
        int mapNum = intent.getIntExtra("mapNo", 0);

        System.out.println("map number: " + mapNum);

        kmlURL = generateKmlUrl(mapNum);

        System.out.println(kmlURL);

        String lyricUrl = generateLyricUrl();


        new DownloadXmlTask().execute(URL);
        new DownloadKmlTask().execute(kmlURL);


        openMap();

        openCollectedWords();

        guessSong();

        openGetHint();

        giveUp();
    }

        /* ----------------------------------------------- BUTTONS ----------------------------------------------*/

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

                        // SHOW CORRECT SONG DIALOG REMOVE LATER
                        AlertDialog.Builder m4Builder = new AlertDialog.Builder(GameActivity.this);
                        View m4View = getLayoutInflater().inflate(R.layout.correct_song_dialog, null);

                        Button continue3 = (Button) m4View.findViewById(R.id.continue3);
                        continue3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                AlertDialog.Builder m5Builder = new AlertDialog.Builder(GameActivity.this);
                                View m5View = getLayoutInflater().inflate(R.layout.incorrect_guess_dialog, null);

                                m5Builder.setView(m5View);
                                AlertDialog dialog5 = m5Builder.create();
                                dialog5.show();
                            }
                        });

                        m4Builder.setView(m4View);
                        AlertDialog dialog4 = m4Builder.create();
                        dialog4.show();

                    }
                });
                Button mExit = (Button) mView.findViewById(R.id.gobackButton);
                mExit.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick (View view){
                        // replace later with close

                        AlertDialog.Builder m2Builder = new AlertDialog.Builder(GameActivity.this);
                        View m2View = getLayoutInflater().inflate(R.layout.enter_name_dialog, null);

                        m2Builder.setView(m2View);
                        AlertDialog dialog2 = m2Builder.create();
                        dialog2.show();
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

    private void openGetHint(){

        Button getHintBttn = findViewById(R.id.getHintBttn);
        getHintBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.request_hint, null);

                Button getHintYes = mView.findViewById(R.id.getHintYes);
                getHintYes.setOnClickListener(new View.OnClickListener(){
                    //reveal song info when gave up
                    @Override
                    public void onClick (View view) {
                        AlertDialog.Builder mBuild = new AlertDialog.Builder(GameActivity.this);
                        View mV = getLayoutInflater().inflate(R.layout.show_hint_dialog, null);

                        mBuild.setView(mV);
                        final AlertDialog dialog = mBuild.create();
                        dialog.show();
                    }
                });
                Button getHintNo = (Button) mView.findViewById(R.id.getHintNo);
                getHintNo.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick (View view){
                        AlertDialog.Builder mBuild2 = new AlertDialog.Builder(GameActivity.this);
                        View mV2 = getLayoutInflater().inflate(R.layout.no_hint_dialog, null);

                        mBuild2.setView(mV2);
                        final AlertDialog dialog = mBuild2.create();
                        dialog.show();
                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });



    }

    private void giveUp(){
        Button give_up_button = findViewById(R.id.give_up_button);
        give_up_button.setOnClickListener(new View.OnClickListener(){

            // ask if user wants to quit when give up button is clicked
            @Override
            public void onClick(View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.give_up_dialog, null);

                Button yesGiveUp = mView.findViewById(R.id.yesGiveUp);
                yesGiveUp.setOnClickListener(new View.OnClickListener(){
                    //reveal song info when gave up
                    @Override
                    public void onClick (View view) {
                        AlertDialog.Builder m2Builder = new AlertDialog.Builder(GameActivity.this);
                        View m2View = getLayoutInflater().inflate(R.layout.on_quit_dialog, null);

                        // ADD BUTTONS FOR NEW GAME AND EXIT

                        m2Builder.setView(m2View);
                        final AlertDialog dialog2 = m2Builder.create();
                        dialog2.show();
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



        /* ------------------------------------------ GET URLS ----------------------------------------------*/

        //replace with number of songs in database
        int randomNum = ThreadLocalRandom.current().nextInt(1, 15 + 1);


        public String generateKmlUrl (int difficulty){

            String songNo;

            if (randomNum < 10){

                songNo = "0" + randomNum;

            } else {
                songNo = "" + randomNum;
            }

            return "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/"
                    + songNo + "/map" + difficulty + ".txt";
        }


    // should be pass to mapActivity?
        public String generateLyricUrl(){

            String songNo;

            if (randomNum < 10){

                songNo = "0" + randomNum;

            } else {
                songNo = "" + randomNum;
            }
            return "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/"+ randomNum +"/words.txt";

        }




       /* ----------------------------------------------- XML ----------------------------------------------*/

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
           // System.out.println(result + "---");
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

        System.out.println("Download URL: "+ URL);
        URL url = new URL(urlString);


        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        conn.connect();
        return conn.getInputStream();

    }

    /* ----------------------------------------------- KML ----------------------------------------------*/
    private class DownloadKmlTask extends AsyncTask<String, Void, List<Placemark>> {

        @Override
        protected List<Placemark> doInBackground(String... urls) {

            try {
                System.out.println("do in background working");

                return loadKmlFromNetwork(urls[0]);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "io exception");
                return null;

            } catch (XmlPullParserException e) {
                e.printStackTrace();
                Log.e(TAG, "XML Error");
                return null;
            }
        }
        @Override
        protected void onPostExecute(List<Placemark> result) {

         //   MapActivity m = new MapActivity();
         //   m.addMarkers(result);

        }

        private List<Placemark> loadKmlFromNetwork(String url) throws IOException, XmlPullParserException {

            InputStream stream = null;
            KmlParser kParser = new KmlParser();
            List<Placemark> placemarks = null;

            Log.i(TAG, "beginning load kml from Network ");
            try {
                Log.i(TAG, "try in load kml from Network");

                stream = IOUtils.toInputStream(downloadUrl(url), "UTF-8");
                placemarks = kParser.parseKml(IOUtils.toString(stream, "utf-8"));

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i(TAG, "Markers downloaded");

           /* Testing purposes */
            StringBuilder result = new StringBuilder();

            for (Placemark placemark : placemarks ) {
                result.append(" \n");
                result.append(placemark.getName());
                result.append(" : " + placemark.getDescription() + " : " + placemark.getCoordinates());
                result.append(": " + placemark.getStyleUrl());
            }

            System.out.println(result.toString());

            return placemarks;
        }


        private String downloadUrl(String urlString) throws IOException {

            System.out.println("Download KML: " + urlString);

            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();

            InputStream stream = conn.getInputStream();

            return IOUtils.toString(stream, "utf-8");
        }

    }




}
