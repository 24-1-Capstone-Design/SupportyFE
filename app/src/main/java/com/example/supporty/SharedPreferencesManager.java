package com.example.supporty;

import android.content.Context;
import android.content.SharedPreferences;

import retrofit2.Callback;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_NICKNAME = "nickname";
    private static final String KEY_JOIN_DATE = "joinDate";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveUserInfo(Context context, String userId, String nickname, String joinDate) {
        SharedPreferences.Editor editor = getSharedPreferences((Context) context).edit();
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_NICKNAME, nickname);
        editor.putString(KEY_JOIN_DATE, joinDate);
        editor.apply();
    }

    public static String getUserId(Context context) {
        return getSharedPreferences(context).getString(KEY_USER_ID, "");
    }

    public static String getNickname(Context context) {
        return getSharedPreferences(context).getString(KEY_NICKNAME, "");
    }

    public static String getJoinDate(Context context) {
        return getSharedPreferences(context).getString(KEY_JOIN_DATE, "");
    }


}

