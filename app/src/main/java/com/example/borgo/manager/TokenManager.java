package com.example.borgo.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TokenManager {
    private static final String PREF_NAME = "AuthPrefs";
    private static final String KEY_AUTH_TOKEN = "authToken";
    private static final String KEY_USER_ID = "userId";
    private static final String TAG = "TokenManager";

    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    public static synchronized void init(Context context) {
        if (prefs == null) {
            prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            editor = prefs.edit();
            Log.d(TAG, "TokenManager initialized");
        }
    }

    // Token methods
    public static void saveToken(String token) {
        Log.d(TAG, "Saving token: " + (token != null ? "'" + token + "'" : "null"));
        Log.d(TAG, "Token length: " + (token != null ? token.length() : 0));
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.apply();
    }

    public static String getToken() {
        String token = prefs.getString(KEY_AUTH_TOKEN, null);
        Log.d(TAG, "Retrieving token: " + (token != null ? "'" + token + "'" : "null"));
        Log.d(TAG, "Token length: " + (token != null ? token.length() : 0));
        return token;
    }

    // User ID methods
    public static void saveUserId(int userId) {
        Log.d(TAG, "Saving userId: " + userId);
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    public static int getUserId() {
        int userId = prefs.getInt(KEY_USER_ID, -1);
        Log.d(TAG, "Retrieving userId: " + userId);
        return userId;
    }

    public static void clearAll() {
        Log.d(TAG, "Clearing all auth data");
        editor.remove(KEY_AUTH_TOKEN);
        editor.remove(KEY_USER_ID);
        editor.apply();
    }
}