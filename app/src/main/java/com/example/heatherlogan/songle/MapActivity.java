package com.example.heatherlogan.songle;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";
    private static final int PERMISSION_REQ_CODE = 1234;

    private Boolean permissionGranted = false;
    private GoogleMap gmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getLocationPermission();
    }

    private void getLocationPermission() {
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQ_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQ_CODE);
        }
    }

    public void permissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] results){
        permissionGranted = false;

        switch(requestCode){
            case PERMISSION_REQ_CODE : {
                if(results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED){
                  for (int i = 0 ; i < results.length ; i++){
                      if (results[i] != PackageManager.PERMISSION_GRANTED){
                          permissionGranted = false;
                          return;
                      }
                  }
                    permissionGranted = true;
                    initialiseMap();
                }
        }
    }
    }

    private void initialiseMap(){
        SupportMapFragment mapF = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapF.getMapAsync(MapActivity.this);
            }

    @Override
    public void onMapReady(GoogleMap googleMap){
        gmap = googleMap;
    }




}
