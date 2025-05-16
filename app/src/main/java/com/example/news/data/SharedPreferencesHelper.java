package com.example.news.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private static final String PREF_NAME = "FastNewsPrefs";
    private static final String KEY_NOTIFICATION_STATUS = "notification_status";

    private SharedPreferences prefs;

    public SharedPreferencesHelper(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setNotificationStatus(int status) {
        prefs.edit().putInt(KEY_NOTIFICATION_STATUS, status).apply();
    }

    public int getNotificationStatus() {
        return prefs.getInt(KEY_NOTIFICATION_STATUS, 0); // Default: off (0)
    }
}