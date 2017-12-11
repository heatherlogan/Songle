package com.example.heatherlogan.songle;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class WordListAdapter extends ArrayAdapter<WordInfo>{

    private static final String TAG = "WordListAdapter";
    private Context mContext;
    private int mResource;

    public WordListAdapter(Context context, int resource, ArrayList<WordInfo> words){

        super(context, resource, words);
        mContext = context;
        mResource = resource;
    }
    @NonNull
    @Override
    public View getView (int position, View convertView, ViewGroup parent){

        String word = getItem(position).getWord();
        int lineNo = getItem(position).getLine();
        int positionNo = getItem(position).getPos();

        WordInfo sw = new WordInfo(word, lineNo, positionNo);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvWord = convertView.findViewById(R.id.wordTextView);
        TextView tvLine = convertView.findViewById(R.id.lineTextView);
        TextView tvPosit = convertView.findViewById(R.id.positionTextView);

        tvWord.setText(word);
        tvLine.setText(Integer.toString(lineNo));
        tvPosit.setText(Integer.toString(positionNo));

        return convertView;

    }





}
