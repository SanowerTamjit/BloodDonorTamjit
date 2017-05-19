package com.santam.blooddonortamjit;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Samad Talukder on 1/30/2017.
 */

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME="logindata";
    private static final String KEY_NAME="NAME";
    //    private static final String KEY_USER_EMAIL="email";
    private static final String PHONE_EMAIL="PHN_EMAIL";
    private static final String IMAGE="IMAGEURL";

    private SharedPrefManager(Context context)
    {
        mCtx=context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(String id,String Name,String URL){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString(PHONE_EMAIL,id);
        editor.putString(KEY_NAME,Name);
        editor.putString(IMAGE,URL);

//        editor.putString(KEY_USERNAME,id);
//        editor.putString(KEY_USER_EMAIL,email);

        editor.apply();
        return true;
    }
    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        if (sharedPreferences.getString(PHONE_EMAIL,null)!=null){
            return true;
        }
        return false;
    }

    public boolean LogOut(){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public String getUserName(){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(PHONE_EMAIL,null);
    }
}
