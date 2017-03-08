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

        dumpDatabase();
    }

    private void dumpDatabase() {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Transaction t = new Transaction();
            t.setId(i * 3 + 1);
            t.setDate(new Date());
            t.setAmount(i * 9.5);
            t.setUsername("test1");
            t.setStatus("Testing");
            t.setCarParkId(i);
            transactions.add(t);
        }

        AppDatabaseHelper helper = AppDatabaseHelper.getInstance(this);
        for (Transaction transaction : transactions) {
            helper.addOrUpdateTransaction(transaction);
        }
        Account account = new Account();
        account.setUsername("test1");
        List<Transaction> test = helper.getAllTransactions(account);
        Log.d("Test DB", "Add " + String.valueOf(test.size()));
    }
}
