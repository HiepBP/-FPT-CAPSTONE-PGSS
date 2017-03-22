package com.fptuni.capstone.pgss.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.adapters.TransactionAdapter;
import com.fptuni.capstone.pgss.helpers.AccountHelper;
import com.fptuni.capstone.pgss.helpers.AppDatabaseHelper;
import com.fptuni.capstone.pgss.helpers.InternetHelper;
import com.fptuni.capstone.pgss.helpers.PubNubHelper;
import com.fptuni.capstone.pgss.interfaces.TransactionClient;
import com.fptuni.capstone.pgss.models.Account;
import com.fptuni.capstone.pgss.models.Transaction;
import com.fptuni.capstone.pgss.models.TransactionStatus;
import com.fptuni.capstone.pgss.network.CommandPackage;
import com.fptuni.capstone.pgss.network.ServiceGenerator;
import com.fptuni.capstone.pgss.network.TransactionPackage;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionActivity extends AppCompatActivity {

    private List<Transaction> transactions;
    private TransactionAdapter adapter;

    @BindView(R.id.recyclerview_transaction_list)
    RecyclerView rvTransactionList;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_progress)
    ProgressBar toolbarProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        initiateFields();
        initiateViews();
        getTransactionData();
        setTitle(R.string.transaction_title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initiateViews() {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvTransactionList.setAdapter(adapter);
        rvTransactionList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initiateFields() {
        transactions = new ArrayList<>();
        adapter = new TransactionAdapter(this, transactions);
        adapter.setOnItemClickListener(new TransactionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, final int position) {
                final Transaction transaction = transactions.get(position);
                if (transaction.getStatus() == TransactionStatus.Finished.getId() ||
                        transaction.getStatus() == TransactionStatus.Canceled.getId()) {
                    return;
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a, MMM d, ''yy");
                String content = "Reservation of lot " +
                        transaction.getLot().getName() + " at " +
                        transaction.getCarPark().getAddress() + " until " +
                        dateFormat.format(transaction.getEndTime());

                new MaterialDialog.Builder(TransactionActivity.this)
                        .content(content)
                        .positiveText(R.string.transaction_dialog_button_positive)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                checkInTransaction(transaction, position);
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                cancelTransaction(transaction, position);
                            }
                        })
                        .negativeText(R.string.transaction_dialog_button_negative)
                        .onNeutral(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })
                        .show();
            }
        });
    }

    private void cancelTransaction(final Transaction transaction, final int position) {
        PubNub pubNub = PubNubHelper.getPubNub();
        Account account = AccountHelper.get(this);
        CommandPackage commandPackage = new CommandPackage();
        commandPackage.setTransactionId(transaction.getId());
        commandPackage.setCarParkId(transaction.getCarParkId());
        commandPackage.setLotId(transaction.getLotId());
        commandPackage.setUsername(account.getUsername());
        commandPackage.setCommand(CommandPackage.COMMAND_CANCEL);
        pubNub.publish()
                .channel(PubNubHelper.CHANNEL_USER)
                .message(commandPackage)
                .usePOST(true)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        if (status.isError()) {
                            Toast.makeText(TransactionActivity.this,
                                    R.string.carparkdetail_reserve_dialog_failed, Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(TransactionActivity.this,
                                    R.string.carparkdetail_reserve_dialog_successful, Toast.LENGTH_SHORT)
                                    .show();
                            transaction.setStatus(TransactionStatus.Canceled.getId());
                            adapter.notifyItemChanged(position);
                        }
                    }
                });
    }

    private void checkInTransaction(final Transaction transaction, final int position) {
        PubNub pubNub = PubNubHelper.getPubNub();
        Account account = AccountHelper.get(this);
        CommandPackage commandPackage = new CommandPackage();
        commandPackage.setTransactionId(transaction.getId());
        commandPackage.setCarParkId(transaction.getCarParkId());
        commandPackage.setLotId(transaction.getLotId());
        commandPackage.setUsername(account.getUsername());
        commandPackage.setCommand(CommandPackage.COMMAND_CHECK_IN);
        pubNub.publish()
                .channel(PubNubHelper.CHANNEL_USER)
                .message(commandPackage)
                .usePOST(true)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        if (status.isError()) {
                            Toast.makeText(TransactionActivity.this,
                                    R.string.carparkdetail_reserve_dialog_failed, Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(TransactionActivity.this,
                                    R.string.carparkdetail_reserve_dialog_successful, Toast.LENGTH_SHORT)
                                    .show();
                            transaction.setStatus(TransactionStatus.Finished.getId());
                            adapter.notifyItemChanged(position);
                        }
                    }
                });
    }

    private void getTransactionData() {
        int oldSize = transactions.size();
        transactions.clear();
        adapter.notifyItemRangeRemoved(0, oldSize);
        if (InternetHelper.isConnected(this)) {
            if (!toolbarProgress.isShown()) {
                toolbarProgress.setVisibility(View.VISIBLE);
            }
            TransactionClient client = ServiceGenerator.createService(TransactionClient.class);
            final Account account = AccountHelper.get(this);
            Call<TransactionPackage> call = client.getTransactionByUsername(account.getUsername());
            call.enqueue(new Callback<TransactionPackage>() {
                @Override
                public void onResponse(Call<TransactionPackage> call, Response<TransactionPackage> response) {
                    toolbarProgress.setVisibility(View.INVISIBLE);
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
                    getTransactionData();
                }
            });
        } else {
            AppDatabaseHelper dbHelper = AppDatabaseHelper.getInstance(this);
            Account account = AccountHelper.get(this);
            transactions.addAll(dbHelper.getAllTransactions(account));
            adapter.notifyItemRangeInserted(0, transactions.size());
        }

    }
}
