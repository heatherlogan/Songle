package com.example.heatherlogan.songle;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.apache.commons.io.IOUtils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;

public class KmlParser {

    public final String TAG = "KML_PARSER";

    private static final String ns = null;

    public List<Placemark> parseKml(String in) throws XmlPullParserException, IOException {

        InputStream stream = IOUtils.toInputStream(in, "UTF-8");

        try {
            XmlPullParser kParser = Xml.newPullParser();
            kParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            kParser.setInput(stream, null);
            kParser.nextTag();

            Log.i(TAG, "Parsing KML");

            return readKmlFeed(kParser);

            } finally {

            stream.close();
        }
    }

    public List<Placemark> readKmlFeed(XmlPullParser parser) throws XmlPullParserException, IOException {

        /* Reads the KML feed and returns a list of the placemarks*/

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
        String coordinates = null;

        /* Identifies relevant tags and sends to appropriate method*/

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


    private String readCoords(XmlPullParser parser) throws IOException, XmlPullParserException{

        parser.require(XmlPullParser.START_TAG, ns, "Point");
        parser.nextTag();

        String coordinates = readText(parser);

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
