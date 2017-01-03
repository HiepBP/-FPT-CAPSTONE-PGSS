package com.fptuni.capstone.pgss;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by TrungTNM on 1/3/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
