package com.tektonlabs.taskview.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tektonlabs.taskview.models.User;

public class PreferencesManager {

    private static final String PREFERENCES_NAME = "task";
    private static final String PREFERENCE_USER = "current_user";

    private static PreferencesManager self;
    private final SharedPreferences mPreferences;

    private PreferencesManager(Context context) {
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static PreferencesManager getInstance(Context context) {
        if (self == null) {
            self = new PreferencesManager(context);
        }
        return self;
    }

    public void setPreferenceUser(User user) {
        String json = new Gson().toJson(user);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(PREFERENCE_USER, json);
        editor.apply();
    }

    public String getPreferenceUser() {
        return mPreferences.getString(PREFERENCE_USER, "");
    }

    public void logOut() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(PREFERENCE_USER, null);
        editor.apply();
    }

}
