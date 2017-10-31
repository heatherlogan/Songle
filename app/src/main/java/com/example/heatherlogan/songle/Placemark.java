package com.example.heatherlogan.songle;

import android.support.design.widget.CoordinatorLayout;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by heatherlogan on 27/10/2017.
 */

public class Placemark {

    private String name;
    private String description;
    private String styleUrl;
    private LatLng coordinates; // change later


    public Placemark(String name, String description, String styleUrl, LatLng coordinates) {
        this.name = name;
        this.description = description;
        this.styleUrl = styleUrl;
        this.coordinates = coordinates;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStyleUrl() {
        return styleUrl;
    }

    public void setStyleUrl(String styleUrl) {
        this.styleUrl = styleUrl;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

/*
    public static class Point {
        private LatLng coordinates;

        public Point (LatLng coordinates){
            this.coordinates = coordinates;
        }

        public LatLng getCoordinates(){
            return coordinates;
        }

        public void setCoordinates(LatLng coordinates){
            this.coordinates = coordinates;
        }
    }*/
}