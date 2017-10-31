package com.example.heatherlogan.songle;

// Adapted from XmlParser, which was referenced from --

import android.util.Xml;

import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;

public class KmlParser {

    public static final String ns = null;

    public List<Placemark> parseKml(InputStream in) throws XmlPullParserException, IOException {

        try {
            XmlPullParser kParser = Xml.newPullParser();

            kParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            kParser.setInput(in, null);
            kParser.nextTag();
            return readKmlFeed(kParser);

            } finally {

            in.close();
        }
    }

    private List<Placemark> readKmlFeed(XmlPullParser parser) throws XmlPullParserException, IOException {

        List<Placemark> placemarks = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "kml");

        parser.nextTag();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("Placemark")) {
                placemarks.add(readPlacemark(parser));
            } else {
                skip(parser);
            }
        } return placemarks;
    }


    private Placemark readPlacemark(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "Placemark");


        String wordLocation = null;
        String description = null;
        String styleUrl = null;
        LatLng coordinates = null;


        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String pm = parser.getName();
            if (pm.equals("name")) {
                wordLocation = readName(parser);
            } else if (pm.equals("description")) {
                description = readDescription(parser);
            } else if (pm.equals("styleUrl")) {
                styleUrl = readStyle(parser);
            } else if (pm.equals("Point")) {
                coordinates = readCoords(parser);
            } else {
                skip(parser);
            }
        }

        String lineNum = wordLocation.substring(0, wordLocation.indexOf(":"));
        String wordNum = wordLocation.substring(wordLocation.indexOf(":") + 1, wordLocation.length());


        return new Placemark(wordLocation, description, styleUrl, coordinates);
    }


    private String readName(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return name;
    }

    private String readDescription(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }
    private String readStyle(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "styleUrl");
        String styleUrl = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "styleUrl");
        return styleUrl;
    }

    private LatLng readCoords(XmlPullParser parser) throws IOException, XmlPullParserException {
        LatLng coordinates;

        parser.require(XmlPullParser.START_TAG, ns, "Point");
        parser.nextTag();

        String[] coordStr = readText(parser).split(",");
        coordinates = new LatLng(Double.parseDouble(coordStr[1]), Double.parseDouble(coordStr[0]));

        parser.require(XmlPullParser.END_TAG, ns, "coordinates");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, "Point");
        return coordinates;

    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }




}