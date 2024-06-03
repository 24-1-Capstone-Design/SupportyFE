package com.example.supporty;

import android.content.Context;
import android.content.SharedPreferences;

import retrofit2.Callback;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_PASSWD = "passwd";
    private static final String KEY_NICKNAME = "nickname";
    private static final String KEY_JOIN_DATE = "joinDate";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn"; // 로그인 상태 저장용

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

    //로그인 시 다음에도 똑같은 아이디로 저장될 수 있게...
    public static void saveLoginInfo(Context context, String userId, String passwd) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_PASSWD, passwd);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public static String getPasswd(Context context) {
        return getSharedPreferences(context).getString(KEY_PASSWD, "");
    }

    //자동 로그인을 위한 함수
    public static boolean isLoggedIn(Context context) {
        return getSharedPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false);
    }

    //로그아웃 시 저장되었던 정보들 삭제
    public static void Logout(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_IS_LOGGED_IN);
        editor.apply();
    }

}

