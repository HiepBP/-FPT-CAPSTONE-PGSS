package com.fptuni.capstone.pgss.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.adapters.TransactionListAdapter;
import com.fptuni.capstone.pgss.helpers.AccountHelper;
import com.fptuni.capstone.pgss.helpers.AppDatabaseHelper;
import com.fptuni.capstone.pgss.models.Account;
import com.fptuni.capstone.pgss.models.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionActivity extends AppCompatActivity {

    private List<Transaction> transactions;
    private TransactionListAdapter adapter;

    @BindView(R.id.recyclerview_transaction_list)
    RecyclerView rvTransactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        initiateFields();
        initiateViews();
        getTransactionData();
    }

    private void getTransactionData() {
        AppDatabaseHelper dbHelper = AppDatabaseHelper.getInstance(this);
        Account account = AccountHelper.get(this);
        transactions.addAll(dbHelper.getAllTransactions(account));
        adapter.notifyItemRangeInserted(0, transactions.size());
        Log.d("Test DB", String.valueOf(transactions.size()));
    }

    private void initiateViews() {
        ButterKnife.bind(this);

        rvTransactionList.setAdapter(adapter);
        rvTransactionList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initiateFields() {
        transactions = new ArrayList<>();
        adapter = new TransactionListAdapter(this, transactions);
    }


}
