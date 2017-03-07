package com.fptuni.capstone.pgss.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.helpers.AppDatabaseHelper;
import com.fptuni.capstone.pgss.models.Account;
import com.fptuni.capstone.pgss.models.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
    }
}
