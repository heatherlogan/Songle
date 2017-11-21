package com.example.heatherlogan.songle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserListAdapter extends ArrayAdapter<User> {

    private static final String TAG = "UserListAdapter";
    private Context mContext;
    private int mResource;

    public UserListAdapter(Context context, int resource, ArrayList<User> users) {

        super(context, resource, users);
        mContext = context;
        mResource = resource;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String name = getItem(position).getUserName();
        String level = getItem(position).getUserLevel();
        long time = getItem(position).getUserTime();
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
        tvTime.setText(Long.toString(time));
        tvSteps.setText(Integer.toString(steps));

        return convertView;

    }
}
