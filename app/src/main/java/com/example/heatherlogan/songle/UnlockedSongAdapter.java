package com.example.heatherlogan.songle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UnlockedSongAdapter extends ArrayAdapter<Song> {

    private static final String TAG = "UnlockedSongAdapter";
    private Context mContext;
    private int mResource;

    public UnlockedSongAdapter(Context context, int resource, ArrayList<Song> songs){

        super(context, resource, songs);
        mContext = context;
        mResource = resource;
    }
    @NonNull
    @Override
    public View getView (int position, View convertView, ViewGroup parent){

        /* using name of song in place of number for the purpose of displaying
        * on unlocked songs activity */

        String name = getItem(position).getTitle();
        String artist = getItem(position).getArtist();

        // Song song = new Song(name, artist, null, null);

        LayoutInflater inflater = LayoutInflater.from(mContext);

        convertView = inflater.inflate(mResource, parent, false);

        TextView nameTV = convertView.findViewById(R.id.songTextView);
        TextView artistTV = convertView.findViewById(R.id.artistTextView);

        nameTV.setText(name);
        artistTV.setText(artist);

        return convertView;
    }



}
