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
import com.fptuni.capstone.pgss.adapters.AreaAdapter;
import com.fptuni.capstone.pgss.interfaces.AreaClient;
import com.fptuni.capstone.pgss.models.Area;
import com.fptuni.capstone.pgss.network.AreaPackage;
import com.fptuni.capstone.pgss.network.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AreaListActivity extends AppCompatActivity {

    private static final String EXTRA_CAR_PARK_ID = "carParkId";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview_arealist)
    RecyclerView rvArea;

    // Area Dialog
    private MaterialDialog dialog;
    private EditText etName;
    private Area focusedArea;

    private int carParkId;
    private List<Area> areas;
    private AreaAdapter adapter;

    public static Intent createIntent(Context context, int carParkId) {
        Intent intent = new Intent(context, AreaListActivity.class);
        intent.putExtra(EXTRA_CAR_PARK_ID, carParkId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_list);

        initiateFields();
        initiateViews();
        getAreaData();
    }

    private void initiateFields() {
        carParkId = getIntent().getIntExtra(EXTRA_CAR_PARK_ID, -1);
        areas = new ArrayList<>();
        adapter = new AreaAdapter(this, areas);
        adapter.setOnItemClickListener(new AreaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Area area = areas.get(position);
                Intent intent = ParkingLotListActivity.createIntent(AreaListActivity.this, area.getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View itemView, int position) {
                focusedArea = areas.get(position);
                etName.setText(focusedArea.getName());
                dialog.show();
            }
        });
    }

    private void initiateViews() {
        ButterKnife.bind(this);
        rvArea.setAdapter(adapter);
        rvArea.setLayoutManager(new LinearLayoutManager(this));

        // Custom dialog
        dialog = new MaterialDialog.Builder(this)
                .title(R.string.dialogarea_title)
                .positiveText(R.string.dialogarea_positive_text)
                .negativeText(R.string.dialogcarpark_negative_text)
                .customView(R.layout.dialog_area, true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        focusedArea.setName(etName.getText().toString());
                        updateArea();
                    }
                })
                .build();
        View customView = dialog.getCustomView();
        etName = (EditText) customView.findViewById(R.id.edittext_dialogarea_name);
    }

    private void getAreaData() {
        AreaClient client = ServiceGenerator.createService(AreaClient.class);
        if (carParkId < 0) {
            return;
        }
        Call<AreaPackage> call = client.getAreaByCarParkId(carParkId);
        call.enqueue(new Callback<AreaPackage>() {
            @Override
            public void onResponse(Call<AreaPackage> call, Response<AreaPackage> response) {
                AreaPackage result = response.body();
                if (result.isSuccess()) {
                    areas.addAll(result.getResult());
                    adapter.notifyItemRangeInserted(0, areas.size());
                }
            }

            @Override
            public void onFailure(Call<AreaPackage> call, Throwable t) {

            }
        });
    }

    private void updateArea() {
        //TODO: update area
        Toast.makeText(this, focusedArea.getName(), Toast.LENGTH_SHORT).show();
    }
}
