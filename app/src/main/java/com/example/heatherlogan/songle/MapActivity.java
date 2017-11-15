package com.example.heatherlogan.songle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.Map;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.util.List;
import java.io.BufferedReader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import com.google.android.gms.tasks.Task;

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

    private static final String TAG = "Maps Activity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private Marker currentLocationMarker;

    public static final String lyricURL = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/15/words.txt";

    public ArrayList<WordInfo> collectedWords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        data = new PlacemarkDatasource(this);

        try {
            data.open();

        } catch (Exception e ){
            Log.e(TAG, "DATABASE EXCEPTION");
        }


       data.addMarker(new Placemark("7:3", "notboring", "-3.188857391507885,55.94380480880355"));

        List<Placemark> m = data.getMarkers();

        for (int i = 0 ; i < m.size() ; i++){

             gMap.addMarker(new MarkerOptions()
                            .title(m.get(i).getName())
                            .snippet(m.get(i).getDescription())
                            .position(stringToLatLng(m.get(i).getCoordinates())));
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public LatLng stringToLatLng(String str){

        String pos =  str.substring(0, str.length()-2);
        String [] posSplit = pos.split(",");

        double longitude = Double.valueOf(posSplit[0]);
        double latitude = Double.valueOf(posSplit[1]);

        LatLng realPosition = new LatLng(latitude, longitude);
        return realPosition;
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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap = googleMap;
       // setOnCickMarker();

        try {
            gMap.setMyLocationEnabled(true);

        } catch (SecurityException se) {
            System.out.println("Security exception thrown[onMapReady]");
        }
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    // Helper to move camera
    private void moveCamera(LatLng latLgn, float zoom) {
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLgn, zoom));
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

            double lat = mLastLocation.getLatitude();
            double lon = mLastLocation.getLongitude();

            LatLng currentLo = new LatLng(lat, lon);

            gMap.addMarker(new MarkerOptions().position(currentLo).title("Your Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

            moveCamera(currentLo, 17.0f);

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            Log.e(TAG, "Permission not granted");
        }
    }

    @Override
    public void onLocationChanged(Location current) {

        if (current == null) {
            System.out.print("Null"); //Put Toast Here
        } else {
            mLastLocation = current;

            System.out.println("[onLocationChanged] Lat / long now (" +
                    String.valueOf(current.getLatitude()) + "," + String.valueOf(current.getLongitude()) + ")");

            LatLng lastLocationCoords = new LatLng(current.getLatitude(), current.getLongitude());
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocationCoords, 15.0f));

            if (currentLocationMarker != null) {
                currentLocationMarker.remove();
            }
            currentLocationMarker = gMap.addMarker(new MarkerOptions()
                    .position(lastLocationCoords)
                    .title("Your Location")); //Edit marker options here
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






     /*   protected void addMarkers(List<Placemark> placemarks) {
            Log.v(TAG, "Adding Markers to map");

            for (Placemark placemark : placemarks) {

                switch (placemark.getStyleUrl()) {

                    case "#unclassified":

                        gMap.addMarker(new MarkerOptions()
                                .position(placemark.getCoordinates())
                                .title(placemark.getName())
                                .snippet(placemark.getDescription()));
                        break;

                    case "#boring":

                        gMap.addMarker(new MarkerOptions()
                                .position(placemark.getCoordinates())
                                .title(placemark.getName())
                                .snippet(placemark.getDescription())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.boringmarker)));
                        break;

                    case "#notboring":

                        gMap.addMarker(new MarkerOptions()
                                .position(placemark.getCoordinates())
                                .title(placemark.getName())
                                .snippet(placemark.getDescription())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.notboringmarker)));
                        break;

                    case "#interesting":

                        gMap.addMarker(new MarkerOptions()
                                .position(placemark.getCoordinates())
                                .title(placemark.getName())
                                .snippet(placemark.getDescription())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.interestingmarker)));
                        break;

                    case "#veryinteresting":

                        gMap.addMarker(new MarkerOptions()
                                .position(placemark.getCoordinates())
                                .title(placemark.getName())
                                .snippet(placemark.getDescription())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.veryinterestingmarker)));
                        break;
                }
            }
        }

    // when marker is clicked
    private void setOnCickMarker() {

        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker m) {

                // add radius to collect
                // add to database

                String position = m.getTitle();
                TaskParameters params = new TaskParameters(lyricURL, position);
                new FindWordInLyrics().execute(params);

                m.remove();

                return false;
            }
        });
    }*/

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

                TextView tv = (TextView) m2View.findViewById(R.id.wordFoundTV);
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

            // add to arraylist of collected words
            addToCollectedWords(result);

        }

        private WordInfo getWord(String url, String position) throws IOException {

            InputStream is = null;
            String foundWord = null;

            String[] parts = position.split(":");
            int lineNo = Integer.parseInt(parts[0]);
            int posNo = Integer.parseInt(parts[1]);

            System.out.println("line: " + lineNo + "position: " + posNo);

            StringBuilder l = new StringBuilder();

            //download lyric url
            try {
                is = downloadLyricUrl(url);
                int counter = 0;

                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                String word;

                //get nth line
                while ((line = br.readLine()) != null) {
                    counter++;
                    if (counter == lineNo) {
                        System.out.println("Line: " + line);

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
            System.out.println("word: " + w + "\nline: " + lz + "\npos: " + p);

            return result;
        }
    }
    private InputStream downloadLyricUrl(String urlString) throws IOException {

        System.out.println("Download Lyric URL: " + lyricURL);
        URL url = new URL(urlString);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        conn.connect();
        return conn.getInputStream();

    }


    /*--------------------------------------Add to Collected Words---------------------------------------*/

    // adds words to collected words list and displays them in list view

    public void addToCollectedWords(WordInfo object){

        collectedWords.add(object);

        System.out.println();
        for (WordInfo word : collectedWords){
            System.out.println(word.getWord() + " " + word.getLine() + ":" + word.getPos());
        }

    }


}



