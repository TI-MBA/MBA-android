package com.gabia.mbaproject.infrastructure.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.gabia.mbaproject.model.User;
import com.google.gson.Gson;

public class UserDefaults implements UserDefaultsContract {

    private static final String USER_KEY = "userDataKey";
    private final SharedPreferences sharedPreferences;
    private final Gson gson = new Gson();

    public UserDefaults(Context context) {
        sharedPreferences = context.getSharedPreferences(
                "com.gabia.mbaproject.infrastructure.local", Context.MODE_PRIVATE);
    }

    @Override
    public void save(User user) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        String json = gson.toJson(user);
        prefsEditor.putString(USER_KEY, json);
        prefsEditor.apply();
    }

    @Override
    public User getCurrentUser() {
        String json = sharedPreferences.getString(USER_KEY, "");
        return gson.fromJson(json, User.class);
    }

    @Override
    public void deleteCurrentUser() {
        sharedPreferences.edit().remove(USER_KEY).apply();
    }
}
