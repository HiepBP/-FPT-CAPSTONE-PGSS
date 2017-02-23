package com.fptuni.capstone.pgss.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fptuni.capstone.pgss.models.Account;
import com.google.gson.Gson;

/**
 * Created by TrungTNM on 2/22/2017.
 */

public class AccountHelper {

    private static String ACCOUNT_KEY = "account";

    private AccountHelper() {

    }

    public static void save(Context context, Account account) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(account);
        editor.putString(ACCOUNT_KEY, json);
        editor.apply();
    }

    public static Account get(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(ACCOUNT_KEY, null);
        if (json == null) {
            return null;
        }
        return gson.fromJson(json, Account.class);
    }

    public static void clear(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(ACCOUNT_KEY);
        editor.apply();
    }
}
