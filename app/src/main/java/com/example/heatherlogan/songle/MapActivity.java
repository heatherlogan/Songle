package com.example.heatherlogan.songle;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
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

import org.xmlpull.v1.XmlPullParserException;


public class MapActivity
        extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap gMap;
    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;
    private static final String TAG = "MapsActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private Marker currentLocationMarker;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    public static final String URL = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/09/map3.txt";
   //  public static final String testUrl2 = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/13/map2.txt";
   // public static final String testUrl3 = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/03/map2.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        System.out.print("HEY");

        downloadMap();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
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

        try {
            gMap.setMyLocationEnabled(true);

        } catch (SecurityException se) {
            System.out.println("Security exception thrown[onMapReady]");
        }
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    private void moveCamera(LatLng latLgn, float zoom) {
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLgn, zoom));
    }

    protected void createLocationRequest() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

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

            gMap.addMarker(new MarkerOptions().position(currentLo).title("Your Location"));

            moveCamera(currentLo, 15.0f);

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            Log.e(TAG, "Permission not granted");
        }
    }

    @Override
    public void onLocationChanged(Location current) {

       if (current == null ) {
            System.out.print("Null"); //Put Toast Here
        } else {
           mLastLocation = current;

            System.out.println("[onLocationChanged] Lat / long now (" +
                    String.valueOf(current.getLatitude()) + "," + String.valueOf(current.getLongitude()) + ")");

            LatLng lastLocationCoords = new LatLng(current.getLatitude(), current.getLongitude());
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocationCoords, 15.0f));

            if (currentLocationMarker != null ){
                currentLocationMarker.remove();
            }
            currentLocationMarker = gMap.addMarker(new MarkerOptions()
                    .position(lastLocationCoords)
                    .title("Your Location"));
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



    /*------------------------------------------ Downloading/Parsing KML File -----------------------------------------  */
    /* private String getKmlUrl(){

    String mapNumber = ... getDifficulty

    String songNumber = getNextSong..

        return "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + songNumber + "/" + mapNumber + ".txt"

        }*/



    public void downloadMap() {

        new DownloadKmlTask().execute(URL);

    }

    private class DownloadKmlTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String ... urls){
            try {

                return loadKmlFromNetwork(urls[0]);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "io exception");
                return "Connection Error";

            } catch (XmlPullParserException e) {
                e.printStackTrace();
                Log.e(TAG, "XML Error");
                return "XML PP Exception";
            }

        }
        @Override
        protected void onPostExecute(String result){

            System.out.println(result + "/n");
        }
    }

    private String loadKmlFromNetwork(String url) throws IOException, XmlPullParserException {

        InputStream stream = null;
        KmlParser kParser = new KmlParser();
        List<Placemark> placemarks = null;

        String name = null;
        String description = null;
        String styleUrl = null;
        LatLng coordinates = null;

        try {
            stream = downloadUrl(url);
            placemarks = kParser.parseKml(stream);

        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        StringBuilder result = new StringBuilder();

        for (Placemark placemark : placemarks){
            result.append("Name: " + name + "/n");
            result.append("Description: " + description + "/n");
            result.append("Styleurl: " + styleUrl + "/n");
            result.append("Point: " + coordinates + "/n");

        }
            // System.out.println(result.toString());
        return result.toString();
    }


    private InputStream downloadUrl(String urlString) throws IOException {

        System.out.println("Download KML: " + URL);
        URL url = new URL(urlString);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        conn.connect();
        return conn.getInputStream();

    }




    /*---------------------------------------------------- Markers ----------------------------------------------------- */



    // Loop through KML info and create markers.
    Marker marker;

    private void addMarkers(String name, String description, String styleUrl, LatLng coordinates){

        MarkerOptions options = new MarkerOptions()
                .title(name)
                .position(coordinates)
                .snippet(description);
        // .icon <- styleURL

        marker = gMap.addMarker(options);
    }


}

