package com.fptuni.capstone.pgss.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDButton;
import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.adapters.CarParkAdapter;
import com.fptuni.capstone.pgss.helpers.AccountHelper;
import com.fptuni.capstone.pgss.interfaces.CarParkClient;
import com.fptuni.capstone.pgss.models.Account;
import com.fptuni.capstone.pgss.models.CarPark;
import com.fptuni.capstone.pgss.network.CarParkPackage;
import com.fptuni.capstone.pgss.network.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_progress)
    ProgressBar toolbarProgress;
    @BindView(R.id.drawerlayout_manager)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigationview_manager)
    NavigationView navigationView;
    @BindView(R.id.recyclerview_manager_car_park_list)
    RecyclerView rvCarParkList;

    // Car Park Dialog
    private MaterialDialog dialog;
    private EditText etName;
    private EditText etPhone;
    private EditText etEmail;
    private EditText etAddress;
    private EditText etDescription;
    private CarPark focusedCarPark;
    private MDButton btnPositive;

    private ActionBarDrawerToggle drawerToggle;
    private CarParkAdapter adapter;
    private List<CarPark> carParks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        initiateFields();
        initiateViews();
        getCarParkData();
        setTitle(R.string.manager_title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void initiateFields() {
        carParks = new ArrayList<>();
        adapter = new CarParkAdapter(this, carParks);
        adapter.setOnItemClickListener(new CarParkAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                CarPark carPark = carParks.get(position);
                Intent intent = AreaListActivity.createIntent(ManagerActivity.this, carPark.getId(),
                        carPark.getName());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View itemView, int position) {
                focusedCarPark = carParks.get(position);
                etName.setText(focusedCarPark.getName());
                etPhone.setText(focusedCarPark.getPhone());
                etEmail.setText(focusedCarPark.getEmail());
                etAddress.setText(focusedCarPark.getAddress());
                etDescription.setText(focusedCarPark.getDescription());

                btnPositive.setEnabled(false);

                dialog.show();
            }
        });
    }

    private void initiateViews() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onDrawerItemClick(item);
                return true;
            }
        });
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

        rvCarParkList.setAdapter(adapter);
        rvCarParkList.setLayoutManager(new LinearLayoutManager(this));

        // dialog
        dialog = new MaterialDialog.Builder(this)
                .title(R.string.dialogcarpark_title)
                .customView(R.layout.dialog_car_park, true)
                .positiveText(R.string.dialogcarpark_positive_text)
                .negativeText(R.string.dialogcarpark_negative_text)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        focusedCarPark.setName(etName.getText().toString());
                        focusedCarPark.setPhone(etPhone.getText().toString());
                        focusedCarPark.setEmail(etEmail.getText().toString());
                        focusedCarPark.setDescription(etDescription.getText().toString());
                        updateCarPark();
                    }
                })
                .build();
        View customView = dialog.getCustomView();
        btnPositive = dialog.getActionButton(DialogAction.POSITIVE);
        TextWatcher textWatcher = new TextWatcher() {
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
        };
        etName = (EditText) customView.findViewById(R.id.edittext_dialogcarpark_name);
        etName.addTextChangedListener(textWatcher);
        etPhone = (EditText) customView.findViewById(R.id.edittext_dialogcarpark_phone);
        etPhone.addTextChangedListener(textWatcher);
        etEmail = (EditText) customView.findViewById(R.id.edittext_dialogcarpark_email);
        etEmail.addTextChangedListener(textWatcher);
        etAddress = (EditText) customView.findViewById(R.id.edittext_dialogcarpark_address);
        etAddress.addTextChangedListener(textWatcher);
        etDescription = (EditText) customView.findViewById(R.id.edittext_dialogcarpark_description);
        etDescription.addTextChangedListener(textWatcher);
    }

    private void onDrawerItemClick(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_manager_log_out:
                intent = new Intent(this, LoginActivity.class);
                AccountHelper.clear(this);
                startActivity(intent);
                finish();
                break;
            default:
        }
        drawerLayout.closeDrawers();
    }


    private void getCarParkData() {
        int oldSize = carParks.size();
        carParks.clear();
        adapter.notifyItemRangeRemoved(0, oldSize);
        if (!toolbarProgress.isShown()) {
            toolbarProgress.setVisibility(View.VISIBLE);
        }
        Account account = AccountHelper.get(this);
        CarParkClient client = ServiceGenerator.createService(CarParkClient.class);
        Call<CarParkPackage> call = client.getCarParkByUsername(account.getUsername());
        call.enqueue(new Callback<CarParkPackage>() {
            @Override
            public void onResponse(Call<CarParkPackage> call, Response<CarParkPackage> response) {
                if (toolbarProgress.isShown()) {
                    toolbarProgress.setVisibility(View.INVISIBLE);
                }
                CarParkPackage result = response.body();
                if (result.isSuccess()) {
                    carParks.addAll(result.getResult());
                    adapter.notifyItemRangeInserted(0, carParks.size());
                }
            }

            @Override
            public void onFailure(Call<CarParkPackage> call, Throwable t) {
                getCarParkData();
            }
        });
    }

    private void updateCarPark() {
        if (!toolbarProgress.isShown()) {
            toolbarProgress.setVisibility(View.VISIBLE);
        }
        Account account = AccountHelper.get(this);
        CarParkClient client = ServiceGenerator.createService(CarParkClient.class);
        Call<CarParkPackage> call = client.update(focusedCarPark);
        call.enqueue(new Callback<CarParkPackage>() {
            @Override
            public void onResponse(Call<CarParkPackage> call, Response<CarParkPackage> response) {
                if (toolbarProgress.isShown()) {
                    toolbarProgress.setVisibility(View.INVISIBLE);
                }
                CarParkPackage result = response.body();
                if (result.isSuccess()) {
                    getCarParkData();
                }
            }

            @Override
            public void onFailure(Call<CarParkPackage> call, Throwable t) {
                updateCarPark();
            }
        });
    }
}
