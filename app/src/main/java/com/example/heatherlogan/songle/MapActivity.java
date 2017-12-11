package com.example.heatherlogan.songle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.graphics.Color;

import java.text.NumberFormat;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.util.List;
import java.io.BufferedReader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Circle;

import org.xmlpull.v1.XmlPullParserException;
import org.apache.commons.io.IOUtils;

public class MapActivity
        extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap gMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    PlacemarkDatasource data;
    CollectedWordsDatasource word_data;

    private static final String TAG = "Maps Activity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final float DEFAULT_ZOOM = 20.0f;

    private static Snackbar snackbar;


    private Marker currentLocationMarker;
    private Circle collectableRadius;

    private ProgressBar progressbar;
    private TextView colwords_tv;
    private int numberofmarkers = 0;
    private int numbercollectedmarkers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        progressbar = findViewById(R.id.progressBar);
        colwords_tv = findViewById(R.id.colwords_tv);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        data = new PlacemarkDatasource(this);
        word_data = new CollectedWordsDatasource(this);

        try {
            data.open();
            word_data.open();

        } catch (Exception e ){
            Log.e(TAG, "DATABASE EXCEPTION");
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPreferences.edit();
        String lyricURL = mPreferences.getString("lyricUrl_key","");

        Log.i(TAG, "Got lyric URL from Shared Preferences");

        // buttons

        openCollectedWords();




        snackbar = Snackbar.make(findViewById(R.id.layout1),
                "Your current location cannot be found.\nPlease check location services.",
                Snackbar.LENGTH_INDEFINITE);
        TextView snackbarTV = (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);

        snackbar.getView().setBackgroundColor(Color.DKGRAY);
        snackbarTV.setTextColor(Color.WHITE);
        snackbarTV.setTextSize(20);

    }

    @Override
    protected void onResume(){
        super.onResume();

        /* On resume, retrieve the number of collected markers from shared preferences */

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ediot = mPreferences.edit();

        numbercollectedmarkers = mPreferences.getInt("numbercollectedmarkers", 0);
        progressbar.setProgress(numbercollectedmarkers);


    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        /* On stop, add to shared preferences to keep data from being lost when map activity is left */

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt("numbercollectedmarkers", numbercollectedmarkers );
        editor.apply();
        Log.i(TAG, "put collected words into shared pref ");
    }


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

          savedInstanceState.putInt("numcollectedmarkers", numbercollectedmarkers);
          savedInstanceState.putInt("numtotalmarkers", numberofmarkers);

        Log.i("Instance State", "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        if((savedInstanceState !=null) && savedInstanceState.containsKey("numcollectedmarkers")
                && savedInstanceState.containsKey("numtotalmarkers")) {

            int colmarkers = savedInstanceState.getInt("numcollectedmarkers");
            int totalmarkers = savedInstanceState.getInt("numtotalmarkers");

            System.out.println("col markers: " + colmarkers);

            String s = "Collected Words: " + colmarkers + "/" + totalmarkers;
            colwords_tv.setText(s);
            progressbar.setMax(totalmarkers);
            progressbar.setProgress(colmarkers);

        }
        Log.i("Instance state", "onRestoreInstanceState");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap = googleMap;

        Log.i(TAG, "Adding Markers");
        addMarkers();

        String s = "Collected Words " + numbercollectedmarkers + "/" + numberofmarkers;
        colwords_tv.setText(s);

        try {
            gMap.setMyLocationEnabled(true);

        } catch (SecurityException se) {
            se.printStackTrace();
            Log.e(TAG, "Security exception thrown[onMapReady]");
        }
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    // Helper to move camera
    private void moveCamera(LatLng latLgn, float zoom) {

        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLgn, zoom));

    }

    protected void createLocationRequest() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Start Location Updates
        int permissionCheck = ContextCompat.checkSelfPermission(this, FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }


    @VisibleForTesting
    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            createLocationRequest();

        } catch (java.lang.IllegalStateException ise) {
            System.out.println("IllegalStateException thrown [onConnected]");
            Log.e(TAG, "Illegal state exception");
        }

        if (ContextCompat.checkSelfPermission(this,
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            LatLng currentLo;

            if (mLastLocation!=null) {

                double lat = mLastLocation.getLatitude();
                double lon = mLastLocation.getLongitude();

                currentLo = new LatLng(lat, lon);

                currentLocationMarker = gMap.addMarker(new MarkerOptions().position(currentLo).title("Your Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

                collectableRadius = gMap.addCircle(new CircleOptions().center(currentLo)
                        .radius(3).strokeColor(Color.CYAN));

                moveCamera(currentLo, DEFAULT_ZOOM);

                setOnClickMarker();



            } else {
                /* Notify player that location is not available*/

                    snackbar.show();

                LatLng defaultLocation = new LatLng(55.944899, -3.188864);

                moveCamera(defaultLocation, 17);

            }

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            Log.e(TAG, "Permission not granted");
        }
    }

    @VisibleForTesting
    @Override
    public void onLocationChanged(Location current) {

        snackbar.dismiss();

        if (current != null) {

            mLastLocation = current;

            System.out.println("[onLocationChanged] Lat / long now (" +
                    String.valueOf(current.getLatitude()) + "," + String.valueOf(current.getLongitude()) + ")");

            LatLng lastLocationCoords = new LatLng(current.getLatitude(), current.getLongitude());

           // gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocationCoords, DEFAULT_ZOOM));

            moveCamera(lastLocationCoords, DEFAULT_ZOOM);

            if (currentLocationMarker != null) {
                currentLocationMarker.remove();

            }
            if (collectableRadius != null){
                collectableRadius.remove();
            }

            currentLocationMarker = gMap.addMarker(new MarkerOptions()
                    .position(lastLocationCoords)
                    .title("Your Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

            collectableRadius = gMap.addCircle(new CircleOptions()
                    .center(lastLocationCoords)
                    .radius(3).strokeColor(Color.CYAN));

            setOnClickMarker();

        } else {

                snackbar.show();

        }

    }

    @Override
    public void onConnectionSuspended(int flat) {
        System.out.println(">>>>>onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        System.out.println(">>>>onConnectionFailed");
    }

    /*---------------------------------------------------- Markers ----------------------------------------------------- */

     protected void addMarkers() {

         List<Placemark> m = data.getMarkers();

         for (int i = 0 ; i < m.size() ; i++){

             numberofmarkers ++;

             String coords = m.get(i).getCoordinates();
             String removeZero = coords.substring(0, coords.length()-2);
             String[] slatlng =  removeZero.split(",");

             try {

                 NumberFormat nf = NumberFormat.getInstance();
                 double lat = nf.parse(slatlng[1]).doubleValue();
                 double lon = nf.parse(slatlng[0]).doubleValue();

                 LatLng latlng = new LatLng(lat, lon);

                    switch (m.get(i).getDescription()) {

                        case "unclassified":
                            gMap.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .title(m.get(i).getName())
                                    .snippet(m.get(i).getDescription())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.unclassifiedmarker)));
                            break;

                        case "boring":
                            gMap.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .title(m.get(i).getName())
                                    .snippet(m.get(i).getDescription())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.boringmarker)));
                            break;

                        case "notboring":
                            gMap.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .title(m.get(i).getName())
                                    .snippet(m.get(i).getDescription())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.notboringmarker)));
                            break;
                        case "interesting":

                            gMap.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .title(m.get(i).getName())
                                    .snippet(m.get(i).getDescription())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.interestingmarker)));
                            break;
                        case "veryinteresting":

                            gMap.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .title(m.get(i).getName())
                                    .snippet(m.get(i).getDescription())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.veryinterestingmarker)));
                            break;
                    }

                 } catch (ParseException e) {
                 e.printStackTrace();
                 }
            }

         Log.i(TAG, "Added " + numberofmarkers + " markers");
         progressbar.setMax(numberofmarkers);
     }

    // when clicked, marker is remove marker from map and database, corresponding word displayed and added to Collected words database.
     private void setOnClickMarker() {

        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker m) {

                    // if user location is not available then markers are not clickable

                    float[] distance = new float[2];

                    Location.distanceBetween(m.getPosition().latitude, m.getPosition().longitude,
                            collectableRadius.getCenter().latitude, collectableRadius.getCenter().longitude, distance);

                    // checks whether clicked marker is within the radius.

                    if (distance[0] > collectableRadius.getRadius()) {

                        Snackbar cantCollectSnack = Snackbar.make(findViewById(R.id.layout1), "You are too far away to collect this word!", Snackbar.LENGTH_LONG);
                        TextView cantCollectSnackTV = (cantCollectSnack.getView()).findViewById(android.support.design.R.id.snackbar_text);

                        cantCollectSnack.getView().setBackgroundColor(Color.DKGRAY);
                        cantCollectSnackTV.setTextColor(Color.WHITE);
                        cantCollectSnackTV.setTextSize(25);
                        cantCollectSnack.show();

                    } else {
                        if (m.getId().equals(currentLocationMarker.getId())) {
                            return false;
                        } else {
                            collectMarker(m);
                        }
                    }
                return true;
            }
        });

    }

    public void collectMarker(Marker m){

        numbercollectedmarkers += 1;

        String statsstring = "Collected Words: " + numbercollectedmarkers + "/" + numberofmarkers;
        colwords_tv.setText(statsstring);

        progressbar.setProgress(numbercollectedmarkers);

        // for matching marker to word in lyrics

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(MapActivity.this);
        SharedPreferences.Editor editor = mPreferences.edit();
        String lyricURL = mPreferences.getString("lyricUrl_key", "");

        String position = m.getTitle();
        TaskParameters params = new TaskParameters(lyricURL, position);

        new FindWordInLyrics().execute(params);

        m.remove();
        data.deleteMarker(new Placemark(m.getTitle(), m.getSnippet(), m.getPosition().longitude + "," + m.getPosition().latitude + ",0"));

        // for testing
        List<Placemark> pms = data.getMarkers();
        StringBuilder result = new StringBuilder();

        int count = 0;
        for (Placemark placemark : pms) {
            count++;
            // result.append(" \n");
            // result.append(" : " + plac emark.getName() + " : " + placemark.getCoordinates());
        }
        System.out.println("Number of markers: " + count);

    }

    /*------------------------------------------ Lyric Parsing ---------------------------------------------*/

    // An object holding information word and position which is used to invoke an add to collected words list

    private static class TaskParameters {

        String url;
        String position;

        TaskParameters(String url, String position) {
            this.url = url;
            this.position = position;
        }
    }

    private class FindWordInLyrics extends AsyncTask<TaskParameters, Void, WordInfo> {

        @Override
        protected WordInfo doInBackground(TaskParameters... params) {

            try {
                String url = params[0].url;
                String position = params[0].position;

                return getWord(url, position);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(WordInfo result) {

            // displays found word on a timed dialog

            AlertDialog.Builder m2Builder = new AlertDialog.Builder(MapActivity.this);
            View m2View = getLayoutInflater().inflate(R.layout.found_word_dialog, null);

                TextView tv = m2View.findViewById(R.id.wordFoundTV);
                tv.setText(result.getWord());

            m2Builder.setView(m2View);
            final AlertDialog dialog2 = m2Builder.create();
            dialog2.show();

            //Dialog times out after 2 seconds

            final Timer t = new Timer();
            t.schedule(new TimerTask() {
                public void run() {
                    dialog2.dismiss();
                    t.cancel();
                }
            }, 2000);

            // add to database of collected words

            addToCollectedWords(result);
        }

        private WordInfo getWord(String url, String position) throws IOException {

            InputStream is = null;
            String foundWord = "";


            SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(MapActivity.this);
            SharedPreferences.Editor editor = mPreferences.edit();
            String lyricURL = mPreferences.getString("lyricUrl_key","");

            Log.i(TAG, "got lyricURL from SharedPref" + lyricURL);

            String[] parts = position.split(":");
            int lineNo = Integer.parseInt(parts[0]);
            int posNo = Integer.parseInt(parts[1]);

            StringBuilder l = new StringBuilder();

            //download lyric url
            try {
                is = downloadLyricUrl(lyricURL);
                int counter = 0;

                System.out.println(lyricURL);

                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                String word;

                //get nth line
                while ((line = br.readLine()) != null) {
                    counter++;
                    if (counter == lineNo) {
                        //remove leading whitespaces and numbers from lines
                        String line2 = line.replaceAll("[0-9]", "").trim();

                        //retrieve nth word of line
                        String[] wordArray = line2.split("\\s+");
                        foundWord = wordArray[posNo - 1];
                    }
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            WordInfo result = new WordInfo(foundWord, lineNo, posNo);

            String w = result.getWord();
            int lz = result.getLine();
            int p = result.getPos();
            Log.i(TAG, "word: " + w + " line: " + lz + " position: " + p);

            return result;
        }

    }
    private InputStream downloadLyricUrl(String urlString) throws IOException {

        URL url = new URL(urlString);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        conn.connect();
        return conn.getInputStream();
    }
    // adds words to collected words list and displays them in list view

    public void addToCollectedWords(WordInfo object){

        word_data.addCollectedWord(object);
    }

    /*--------------------------------------Buttons---------------------------------------*/

    private void openCollectedWords(){
        Button view_collected_words = findViewById(R.id.viewWordsFromMap);
        view_collected_words.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotocollectedwords = new Intent(MapActivity.this, ViewCollectedWords.class);
                startActivity(gotocollectedwords);
            }
        });
    }


}



