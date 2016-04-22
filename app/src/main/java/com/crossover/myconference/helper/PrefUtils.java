package com.crossover.myconference.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by user1 on 11/07/2014.
 */
public class PrefUtils {

    public static  final String PREF_WELCOME_DONE = "welcome_done";
    public static  final String ACCOUNT_NOTIFY_KEY = "account";
    public static  final String PERSON_KEY = "personal_key";
    public static  final String EMAIL_KEY = "email_key";
    public static  final String PHOTO_KEY = "photo_key";
    public static  final String ROLE_KEY = "role";


    public static boolean hasAccountNotification(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(ACCOUNT_NOTIFY_KEY, true); // by default we want vibes
    }

    public static void setFirstTime(Context context, Boolean key){
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(PREF_WELCOME_DONE, key).commit();
    }

    public static boolean isFirstTime(final Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_WELCOME_DONE, true);
    }

    public static void markWelcomeDone(final Context context){
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(PREF_WELCOME_DONE, true).commit();
    }

    public static void setPersonKey(Context context, String key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(PERSON_KEY, key).commit();
    }

    public static String getPersonKey(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PERSON_KEY, "0");
    }

    public static void setRole(Context context, String key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(ROLE_KEY, key).commit();
    }

    public static String getRole(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(ROLE_KEY, "0");
    }

    public static void setEmail(Context context, String key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(EMAIL_KEY, key).commit();
    }

    public static String getEmail(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(EMAIL_KEY, "0");
    }

    public static void setPhoto(Context context, Bitmap image){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(PHOTO_KEY, encodeTobase64(image)).commit();
    }

    public static String getPhoto(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PHOTO_KEY, "0");
    }


    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}
