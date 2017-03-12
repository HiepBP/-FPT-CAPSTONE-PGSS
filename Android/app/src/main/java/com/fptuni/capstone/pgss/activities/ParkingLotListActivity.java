package com.fptuni.capstone.pgss.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

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
    }

    private void initiateViews() {
        ButterKnife.bind(this);
        rvParkingLot.setAdapter(adapter);
        rvParkingLot.setLayoutManager(new LinearLayoutManager(this));
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


}
