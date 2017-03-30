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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDButton;
import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.adapters.AreaAdapter;
import com.fptuni.capstone.pgss.interfaces.AreaClient;
import com.fptuni.capstone.pgss.models.Area;
import com.fptuni.capstone.pgss.models.AreaStatus;
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
    private static final String EXTRA_CAR_PARK_NAME = "carParkName";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_progress)
    ProgressBar toolbarProgress;
    @BindView(R.id.recyclerview_arealist)
    RecyclerView rvArea;

    // Area Dialog
    private MaterialDialog detailDialog;
    private EditText etName;
    private SwitchCompat scActive;
    private Area focusedArea;
    private MDButton btnPositive;

    private int carParkId;
    private String carParkName;
    private List<Area> areas;
    private AreaAdapter adapter;

    public static Intent createIntent(Context context, int carParkId, String carParkName) {
        Intent intent = new Intent(context, AreaListActivity.class);
        intent.putExtra(EXTRA_CAR_PARK_ID, carParkId);
        intent.putExtra(EXTRA_CAR_PARK_NAME, carParkName);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_list);

        initiateFields();
        initiateViews();
        getAreaData();
        String title = getResources().getString(R.string.area_title) + " " + carParkName;
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
        carParkId = getIntent().getIntExtra(EXTRA_CAR_PARK_ID, -1);
        carParkName = getIntent().getStringExtra(EXTRA_CAR_PARK_NAME);

        areas = new ArrayList<>();
        adapter = new AreaAdapter(this, areas);
        adapter.setOnItemClickListener(new AreaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Area area = areas.get(position);
                Intent intent = ParkingLotListActivity.createIntent(AreaListActivity.this, area.getId(),
                        area.getName());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View itemView, int position) {
                focusedArea = areas.get(position);
                etName.setText(focusedArea.getName());
                btnPositive.setEnabled(false);
                if (focusedArea.isUpdateAvailable()) {
                    scActive.setEnabled(true);
                    if (focusedArea.getStatus() == AreaStatus.Active.getId()) {
                        scActive.setChecked(true);
                    } else if (focusedArea.getStatus() == AreaStatus.Deactive.getId()) {
                        scActive.setChecked(false);
                    }
                } else {
                    scActive.setEnabled(false);
                }
                detailDialog.show();
            }
        });
    }

    private void initiateViews() {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvArea.setAdapter(adapter);
        rvArea.setLayoutManager(new LinearLayoutManager(this));

        // Custom detailDialog
        detailDialog = new MaterialDialog.Builder(this)
                .title(R.string.dialogarea_title)
                .positiveText(R.string.dialogarea_positive_text)
                .negativeText(R.string.dialogcarpark_negative_text)
                .customView(R.layout.dialog_area, true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Area area = new Area(focusedArea);
                        area.setName(etName.getText().toString());
                        if (scActive.isEnabled()) {
                            if (scActive.isChecked()) {
                                area.setStatus(AreaStatus.Active.getId());
                            } else {
                                area.setStatus(AreaStatus.Deactive.getId());
                            }
                        }
                        updateArea(area);
                    }
                })
                .build();
        View customView = detailDialog.getCustomView();
        etName = (EditText) customView.findViewById(R.id.edittext_dialogarea_name);
        scActive = (SwitchCompat) customView.findViewById(R.id.switchcompat_dialogarea_active);
        btnPositive = detailDialog.getActionButton(DialogAction.POSITIVE);
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

    private void getAreaData() {
        int oldSize = areas.size();
        areas.clear();
        adapter.notifyItemRangeRemoved(0, oldSize);
        if (!toolbarProgress.isShown()) {
            toolbarProgress.setVisibility(View.VISIBLE);
        }
        AreaClient client = ServiceGenerator.createService(AreaClient.class);
        if (carParkId < 0) {
            return;
        }
        Call<AreaPackage> call = client.getAreaByCarParkId(carParkId);
        call.enqueue(new Callback<AreaPackage>() {
            @Override
            public void onResponse(Call<AreaPackage> call, Response<AreaPackage> response) {
                if (toolbarProgress.isShown()) {
                    toolbarProgress.setVisibility(View.INVISIBLE);
                }
                AreaPackage result = response.body();
                if (result.isSuccess()) {
                    areas.addAll(result.getResult());
                    adapter.notifyItemRangeInserted(0, areas.size());
                }
            }

            @Override
            public void onFailure(Call<AreaPackage> call, Throwable t) {
                getAreaData();
            }
        });
    }

    private void updateArea(Area area) {
        if (!area.getName().equals(focusedArea.getName())) {
            updateAreaName(area);
        }
        if (area.getStatus() != focusedArea.getStatus()) {
            updateAreaStatus(area);
        }
    }

    private void updateAreaStatus(final Area area) {
        AreaClient client = ServiceGenerator.createService(AreaClient.class);
        if (carParkId < 0) {
            return;
        }
        Call<AreaPackage> call = client.updateStatus(area);
        call.enqueue(new Callback<AreaPackage>() {
            @Override
            public void onResponse(Call<AreaPackage> call, final Response<AreaPackage> response) {
                final AreaPackage result = response.body();
                if (result.isSuccess()) {
                    getAreaData();
                }
            }

            @Override
            public void onFailure(Call<AreaPackage> call, Throwable t) {
                updateAreaStatus(area);
            }
        });
    }

    private void updateAreaName(final Area area) {
        AreaClient client = ServiceGenerator.createService(AreaClient.class);
        if (carParkId < 0) {
            return;
        }
        Call<AreaPackage> call = client.updateName(area);
        call.enqueue(new Callback<AreaPackage>() {
            @Override
            public void onResponse(Call<AreaPackage> call, final Response<AreaPackage> response) {
                final AreaPackage result = response.body();
                if (result.isSuccess()) {
                    getAreaData();
                }
            }

            @Override
            public void onFailure(Call<AreaPackage> call, Throwable t) {
                updateAreaName(area);
            }
        });
    }
}
