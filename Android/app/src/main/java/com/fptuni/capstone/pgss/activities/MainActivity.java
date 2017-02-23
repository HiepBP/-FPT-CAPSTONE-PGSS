package com.fptuni.capstone.pgss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.helpers.AccountHelper;
import com.fptuni.capstone.pgss.models.Account;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO: splash screen

        Account account = AccountHelper.get(this);
        Intent intent;
        if (account == null) {
            intent = new Intent(this, LoginActivity.class);
        } else {
            // TODO: load activity based on account role
            intent = new Intent(this, MapsActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
