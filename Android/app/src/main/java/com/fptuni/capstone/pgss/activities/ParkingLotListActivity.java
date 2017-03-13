package com.fptuni.capstone.pgss.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.adapters.ParkingLotAdapter;
import com.fptuni.capstone.pgss.interfaces.ParkingLotClient;
import com.fptuni.capstone.pgss.models.ParkingLot;
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

    public static final String EXTRA_AREA_ID = "areaId";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview_parkinglotlist)
    RecyclerView rvParkingLot;

    // Parking Lot Dialog
    private MaterialDialog dialog;
    private EditText etName;
    private ParkingLot focusedLot;

    private int areaId;
    private List<ParkingLot> lots;
    private ParkingLotAdapter adapter;

    public static Intent createIntent(Context context, int areaId) {
        Intent intent = new Intent(context, ParkingLotListActivity.class);
        intent.putExtra(EXTRA_AREA_ID, areaId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_lot_list);

        initiateFields();
        initiateViews();
        getParkingLotData();
    }

    private void initiateFields() {
        areaId = getIntent().getIntExtra(EXTRA_AREA_ID, -1);
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
                dialog.show();
            }
        });
    }

    private void initiateViews() {
        ButterKnife.bind(this);
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
                        focusedLot.setName(etName.getText().toString());
                        updateLot();
                    }
                })
                .build();
        View customView = dialog.getCustomView();
        etName = (EditText) customView.findViewById(R.id.edittext_dialogparkinglot_name);
    }

    private void getParkingLotData() {
        ParkingLotClient client = ServiceGenerator.createService(ParkingLotClient.class);
        if (areaId < 0) {
            return;
        }
        Call<ParkingLotPackage> call = client.getParkingLotByAreaId(areaId);
        call.enqueue(new Callback<ParkingLotPackage>() {
            @Override
            public void onResponse(Call<ParkingLotPackage> call, Response<ParkingLotPackage> response) {
                ParkingLotPackage result = response.body();
                if (result.isSuccess()) {
                    lots.addAll(result.getResult());
                    adapter.notifyItemRangeInserted(0, lots.size());
                }
            }

            @Override
            public void onFailure(Call<ParkingLotPackage> call, Throwable t) {

            }
        });
    }

    private void updateLot() {
        //TODO: update lot
        Toast.makeText(this, focusedLot.getName(), Toast.LENGTH_SHORT).show();
    }
}
