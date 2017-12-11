package com.example.heatherlogan.songle;

import android.support.design.widget.CoordinatorLayout;

import com.google.android.gms.maps.model.LatLng;

/**
 * Placemark object
 */

public class Placemark {

    private String name;
    private String description;
    private String styleUrl;
    private String coordinates;

    public Placemark(){

    }

    public Placemark(String name, String description, String styleUrl, String coordinates) {
        this.name = name;
        this.description = description;
        this.styleUrl = styleUrl;
        this.coordinates = coordinates;

    }

    public Placemark(String name, String description, String coordinates) {
        this.name = name;
        this.description = description;
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

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

}