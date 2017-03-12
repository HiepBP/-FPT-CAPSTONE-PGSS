package com.fptuni.capstone.pgss;

import android.app.Application;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.fptuni.capstone.pgss.helpers.AppDatabaseHelper;
import com.fptuni.capstone.pgss.models.Account;
import com.fptuni.capstone.pgss.models.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

/**
 * Created by TrungTNM on 1/3/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
