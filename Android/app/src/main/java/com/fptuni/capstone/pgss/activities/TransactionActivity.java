package com.fptuni.capstone.pgss.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.adapters.TransactionListAdapter;
import com.fptuni.capstone.pgss.helpers.AccountHelper;
import com.fptuni.capstone.pgss.helpers.AppDatabaseHelper;
import com.fptuni.capstone.pgss.helpers.InternetHelper;
import com.fptuni.capstone.pgss.interfaces.TransactionClient;
import com.fptuni.capstone.pgss.models.Account;
import com.fptuni.capstone.pgss.models.Transaction;
import com.fptuni.capstone.pgss.network.ServiceGenerator;
import com.fptuni.capstone.pgss.network.TransactionPackage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        if (InternetHelper.isConnected(this)) {
            TransactionClient client = ServiceGenerator.createService(TransactionClient.class);
            final Account account = AccountHelper.get(this);
            Call<TransactionPackage> call = client.getTransactionByUsername(account.getUsername());
            call.enqueue(new Callback<TransactionPackage>() {
                @Override
                public void onResponse(Call<TransactionPackage> call, Response<TransactionPackage> response) {
                    TransactionPackage result = response.body();
                    if (result.isSuccess()) {
                        transactions.addAll(result.getResult());
                        adapter.notifyItemRangeInserted(0, transactions.size());
                        AppDatabaseHelper dbHelper = AppDatabaseHelper.getInstance(TransactionActivity.this);
                        for (Transaction transaction : transactions) {
                            transaction.setUsername(account.getUsername());
                            dbHelper.addOrUpdateTransaction(transaction);
                        }
                    }
                }

                @Override
                public void onFailure(Call<TransactionPackage> call, Throwable t) {
                }
            });
        } else {
            AppDatabaseHelper dbHelper = AppDatabaseHelper.getInstance(this);
            Account account = AccountHelper.get(this);
            transactions.addAll(dbHelper.getAllTransactions(account));
            adapter.notifyItemRangeInserted(0, transactions.size());
        }

    }

    private void initiateViews() {
        ButterKnife.bind(this);

        rvTransactionList.setAdapter(adapter);
        rvTransactionList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initiateFields() {
        transactions = new ArrayList<>();
        adapter = new TransactionListAdapter(this, transactions);
        adapter.setOnItemClickListener(new TransactionListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Transaction transaction = transactions.get(position);
                //TODO: only pending or reserved transaction can be click
                SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a, MMM d, ''yy");
                String content = "Reservation of lot " +
                        transaction.getLot().getName() + " at " +
                        "ADDRESS" + " when " +
                        dateFormat.format(transaction.getDate());

                new MaterialDialog.Builder(TransactionActivity.this)
                        .content(content)
                        .positiveText(R.string.transaction_dialog_button_check_in)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //TODO: update transaction to finished
                                Toast.makeText(TransactionActivity.this, "Check In clicked",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //TODO: update transaction to cancel
                                Toast.makeText(TransactionActivity.this, "Cancel clicked",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .negativeText(R.string.transaction_dialog_button_cancel)
                        .show();
            }
        });
    }
}
