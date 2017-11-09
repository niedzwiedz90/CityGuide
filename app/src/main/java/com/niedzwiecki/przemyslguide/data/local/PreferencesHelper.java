package com.niedzwiecki.przemyslguide.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "android_boilerplate_pref_file";

    private final SharedPreferences mPref;
    private static PreferencesHelper instance;

    public PreferencesHelper(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        mPref.edit().clear().apply();
    }

    public boolean contains(PreferencesKeys key) {
        return mPref.contains(key.name());
    }

    public String getAuthenticationHeader(PreferencesKeys key) {
        return mPref.getString(key.name(), null);
    }

    public void setAuthenticationHeader(PreferencesKeys key, String loginHeader) {
        mPref.edit().putString(key.name(), loginHeader).apply();
    }

    public void clearAuthenticationHeader(PreferencesKeys key) {
        mPref.edit().remove(key.name()).apply();
    }

    public static PreferencesHelper getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesHelper(context);
        }

        return instance;
    }
}
