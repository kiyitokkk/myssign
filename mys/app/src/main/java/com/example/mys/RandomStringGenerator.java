package com.example.mys;

import android.content.Context;
import android.content.SharedPreferences;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.UUID;
import java.security.SecureRandom;

public class RandomStringGenerator {
    private static final String CHARACTERS = "abcdef0123456789";
    private static final int LENGTH = 13;
    private static final String PREF_UUID = "deviceid";
    private static final String KEY_UUID = "x-rpc-device_id";
    private static final String KEY_RANDOM_STRING = "x-rpc-device_fp";
    public static String generateRandomString() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(LENGTH);

        for (int i = 0; i < LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
    public static String getUUID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_UUID, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_UUID, "null");
    }
    public static void saveUUID(String uuid, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_UUID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_UUID, uuid);
        editor.apply();
    }
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
    public static void saveRandomString(Context context, String randomString) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_UUID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_RANDOM_STRING, randomString);
        editor.apply();
    }
    public static String readRandomString(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_UUID, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_RANDOM_STRING, "null");
    }
}
