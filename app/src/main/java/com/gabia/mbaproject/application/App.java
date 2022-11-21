package com.gabia.mbaproject.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.gabia.mbaproject.infrastructure.local.UserDefaults;
import com.gabia.mbaproject.model.User;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public static User getCurrentUser(Context context) {
        return new UserDefaults(context).getCurrentUser();
    }
}
