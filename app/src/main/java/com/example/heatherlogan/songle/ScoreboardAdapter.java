package com.example.heatherlogan.songle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ScoreboardAdapter extends ArrayAdapter<User> {

    private static final String TAG = "Scoreboard Adapter";
    private Context mContext;
    private int mResource;

    public ScoreboardAdapter(Context context, int resource, ArrayList<User> users) {

        super(context, resource, users);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String name = getItem(position).getUserName();
        String level = getItem(position).getUserLevel();
        int time = getItem(position).getUserTime();
        int steps = getItem(position).getUserSteps();

        User u = new User(name, level, time, steps);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView tvLevel = (TextView) convertView.findViewById(R.id.levelTextView);
        TextView tvTime = (TextView) convertView.findViewById(R.id.timeTextView);
        TextView tvSteps = (TextView) convertView.findViewById(R.id.stepsTextView);

        tvName.setText(name);
        tvLevel.setText(level);
        tvTime.setText(formatTime(time));
        tvSteps.setText(Integer.toString(steps));

        return convertView;

    }

    private static String formatTime(int time){

            int hours =   (int) ((time / (1000*60*60)));
            int minutes = (int) ((time / (1000*60) % 60));
            int seconds = (int) ((time / 1000) % 60);

            StringBuilder formattedTime = new StringBuilder();

            if (hours != 0) {
                if (hours < 10) {
                    formattedTime.append("0");
                    formattedTime.append(hours);
                    formattedTime.append(":");
                } else {
                    formattedTime.append(hours);
                    formattedTime.append(":");
                }
            } else {
                formattedTime.append("00:");
            }
            if (minutes != 0) {

                if (minutes < 10) {
                    formattedTime.append("0");
                    formattedTime.append(minutes);
                    formattedTime.append(":");
                } else {
                    formattedTime.append(minutes);
                    formattedTime.append(":");
                }
            } else {
                formattedTime.append("00:");
            }
            if (seconds != 0) {
                if (seconds < 10) {
                    formattedTime.append("0");
                    formattedTime.append(seconds);
                } else {
                    formattedTime.append(seconds);
                }
            } else {
                formattedTime.append("00");
            }

        return formattedTime.toString();
        }


}
