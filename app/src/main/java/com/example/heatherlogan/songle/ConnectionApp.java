package com.example.heatherlogan.songle;

import android.app.Application;

/**
 * Created by heatherlogan on 09/12/2017.
 */

public class ConnectionApp extends Application {

    private static ConnectionApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized ConnectionApp getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
