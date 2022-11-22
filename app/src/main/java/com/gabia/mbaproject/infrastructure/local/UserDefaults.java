package com.gabia.mbaproject.infrastructure.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.gabia.mbaproject.model.Member;
import com.google.gson.Gson;

public class UserDefaults implements UserDefaultsContract {

    private static final String USER_KEY = "userDataKey";
    private final SharedPreferences sharedPreferences;
    private final Gson gson = new Gson();

    private UserDefaults(Context context) {
        sharedPreferences = context.getSharedPreferences(
                "com.gabia.mbaproject.infrastructure.local", Context.MODE_PRIVATE);
    }

    public static UserDefaults getInstance(Context context) {
        return new UserDefaults(context);
    }

    @Override
    public void save(Member member) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        String json = gson.toJson(member);
        prefsEditor.putString(USER_KEY, json);
        prefsEditor.apply();
    }

    @Override
    public Member getCurrentUser() {
        String json = sharedPreferences.getString(USER_KEY, "");
        return gson.fromJson(json, Member.class);
    }

    @Override
    public void deleteCurrentUser() {
        sharedPreferences.edit().remove(USER_KEY).apply();
    }
}
