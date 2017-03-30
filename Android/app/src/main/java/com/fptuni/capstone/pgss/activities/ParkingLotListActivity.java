package com.fptuni.capstone.pgss.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDButton;
import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.adapters.ParkingLotAdapter;
import com.fptuni.capstone.pgss.interfaces.ParkingLotClient;
import com.fptuni.capstone.pgss.models.ParkingLot;
import com.fptuni.capstone.pgss.models.ParkingLotStatus;
import com.fptuni.capstone.pgss.network.ParkingLotPackage;
import com.fptuni.capstone.pgss.network.ServiceGenerator;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingLotListActivity extends AppCompatActivity {

    private static final String EXTRA_AREA_ID = "areaId";
    private static final String EXTRA_AREA_NAME = "areaName";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_progress)
    ProgressBar toolbarProgress;
    @BindView(R.id.recyclerview_parkinglotlist)
    RecyclerView rvParkingLot;

    // Parking Lot Dialog
    private MaterialDialog dialog;
    private EditText etName;
    private TextView tvStatus;
    private ParkingLot focusedLot;
    private SwitchCompat scActive;
    private MDButton btnPositive;

    private int areaId;
    private String areaName;
    private List<ParkingLot> lots;
    private ParkingLotAdapter adapter;

    public static Intent createIntent(Context context, int areaId, String areaName) {
        Intent intent = new Intent(context, ParkingLotListActivity.class);
        intent.putExtra(EXTRA_AREA_ID, areaId);
        intent.putExtra(EXTRA_AREA_NAME, areaName);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_lot_list);

        initiateFields();
        initiateViews();
        getParkingLotData();
        String title = getResources().getString(R.string.parkinglot_title) + " " + areaName;
        setTitle(title);
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

    private void initiateFields() {
        areaId = getIntent().getIntExtra(EXTRA_AREA_ID, -1);
        areaName = getIntent().getStringExtra(EXTRA_AREA_NAME);

        lots = new ArrayList<>();
        adapter = new ParkingLotAdapter(this, lots);
        adapter.setOnItemClickListener(new ParkingLotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                // blank
            }

            @Override
            public void onItemLongClick(View itemView, int position) {
                focusedLot = lots.get(position);
                etName.setText(focusedLot.getName());
                btnPositive.setEnabled(false);
                if (focusedLot.getStatus() == ParkingLotStatus.Nonavailable.getId() ||
                        focusedLot.getStatus() == ParkingLotStatus.Reserved.getId()) {
                    tvStatus.setVisibility(View.VISIBLE);
                    tvStatus.setText(ParkingLotStatus.getById(focusedLot.getStatus()).getName());
                    scActive.setEnabled(false);
                } else {
                    tvStatus.setVisibility(View.INVISIBLE);
                    scActive.setEnabled(true);
                    if (focusedLot.getStatus() == ParkingLotStatus.Active.getId()) {
                        scActive.setChecked(true);
                    } else {
                        scActive.setChecked(false);
                    }
                }
                dialog.show();
            }
        });
    }

    private void initiateViews() {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvParkingLot.setAdapter(adapter);
        rvParkingLot.setLayoutManager(new LinearLayoutManager(this));

        // dialog
        dialog = new MaterialDialog.Builder(this)
                .title(R.string.dialogparkinglot_title)
                .customView(R.layout.dialog_parking_lot, true)
                .positiveText(R.string.dialogparkinglot_positive_text)
                .negativeText(R.string.dialogcarpark_negative_text)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ParkingLot lot = new ParkingLot(focusedLot);
                        lot.setName(etName.getText().toString());
                        if (scActive.isEnabled()) {
                            if (scActive.isChecked()) {
                                lot.setStatus(1);
                            } else {
                                lot.setStatus(0);
                            }
                        }
                        updateLot(lot);
                    }
                })
                .build();
        View customView = dialog.getCustomView();
        etName = (EditText) customView.findViewById(R.id.edittext_dialogparkinglot_name);
        tvStatus = (TextView) customView.findViewById(R.id.textview_dialogparkinglot_status);
        scActive = (SwitchCompat) customView.findViewById(R.id.switchcompat_dialogparkinglot_active);
        btnPositive = dialog.getActionButton(DialogAction.POSITIVE);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnPositive.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        scActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnPositive.setEnabled(true);
            }
        });
    }

    private void getParkingLotData() {
        int oldSize = lots.size();
        lots.clear();
        adapter.notifyItemRangeRemoved(0, oldSize);
        if (!toolbarProgress.isShown()) {
            toolbarProgress.setVisibility(View.VISIBLE);
        }
        ParkingLotClient client = ServiceGenerator.createService(ParkingLotClient.class);
        if (areaId < 0) {
            return;
        }
        Call<ParkingLotPackage> call = client.getParkingLotByAreaId(areaId);
        call.enqueue(new Callback<ParkingLotPackage>() {
            @Override
            public void onResponse(Call<ParkingLotPackage> call, Response<ParkingLotPackage> response) {
                if (toolbarProgress.isShown()) {
                    toolbarProgress.setVisibility(View.INVISIBLE);
                }
                ParkingLotPackage result = response.body();
                if (result.isSuccess()) {
                    lots.addAll(result.getResult());
                    adapter.notifyItemRangeInserted(0, lots.size());
                }
            }

            @Override
            public void onFailure(Call<ParkingLotPackage> call, Throwable t) {
                getParkingLotData();
            }
        });
    }

    private void updateLot(ParkingLot newLot) {
        if (!newLot.getName().equals(focusedLot.getName())) {
            updateLotName(newLot);
        }
        if (newLot.getStatus() != focusedLot.getStatus()) {
            updateLotStatus(newLot);
        }
    }

    private void updateLotStatus(final ParkingLot lot) {
        if (!toolbarProgress.isShown()) {
            toolbarProgress.setVisibility(View.VISIBLE);
        }
        ParkingLotClient client = ServiceGenerator.createService(ParkingLotClient.class);
        if (areaId < 0) {
            return;
        }
        Call<ParkingLotPackage> call = client.updateStatus(lot);
        call.enqueue(new Callback<ParkingLotPackage>() {
            @Override
            public void onResponse(Call<ParkingLotPackage> call, final Response<ParkingLotPackage> response) {
                if (toolbarProgress.isShown()) {
                    toolbarProgress.setVisibility(View.INVISIBLE);
                }
                final ParkingLotPackage result = response.body();
                if (result.isSuccess()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ParkingLotListActivity.this, result.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    getParkingLotData();
                }
            }

            @Override
            public void onFailure(Call<ParkingLotPackage> call, Throwable t) {
                updateLotStatus(lot);
            }
        });
    }

    private void updateLotName(final ParkingLot lot) {
        if (!toolbarProgress.isShown()) {
            toolbarProgress.setVisibility(View.VISIBLE);
        }
        ParkingLotClient client = ServiceGenerator.createService(ParkingLotClient.class);
        if (areaId < 0) {
            return;
        }
        Call<ParkingLotPackage> call = client.updateName(lot);
        call.enqueue(new Callback<ParkingLotPackage>() {
            @Override
            public void onResponse(Call<ParkingLotPackage> call, final Response<ParkingLotPackage> response) {
                if (toolbarProgress.isShown()) {
                    toolbarProgress.setVisibility(View.INVISIBLE);
                }
                final ParkingLotPackage result = response.body();
                if (result.isSuccess()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ParkingLotListActivity.this, result.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    getParkingLotData();
                }
            }

            @Override
            public void onFailure(Call<ParkingLotPackage> call, Throwable t) {
                updateLotName(lot);
            }
        });
    }
}
