package com.systech.farha.datereminderapp.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.systech.farha.datereminderapp.activity.AuthActivity.LoginActivity;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences preferences;
    Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "Login_Pref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_ID = "id";
    public static final String KEY_EMAIL = "email";

    public SessionManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void storeLoginSession(String name, String userName){

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, name);
        editor.putString(KEY_EMAIL, userName);
        editor.commit();
    }

    public HashMap<String, String> getLoginDetails(){

        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_ID, preferences.getString(KEY_ID, null));
        user.put(KEY_EMAIL, preferences.getString(KEY_EMAIL, null));
        return user;
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){

            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
//        else {
//            Intent i = new Intent(context, MainActivity.class);
//            context.startActivity(i);
//        }
    }

    public void logoutUser(){

        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return preferences.getBoolean(IS_LOGIN, false);
    }
}
