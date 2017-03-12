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
import android.view.MenuItem;
import android.view.View;

import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.adapters.CarParkAdapter;
import com.fptuni.capstone.pgss.adapters.CarParkAdvanceAdapter;
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
    @BindView(R.id.drawerlayout_manager)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigationview_manager)
    NavigationView navigationView;
    @BindView(R.id.recyclerview_manager_car_park_list)
    RecyclerView rvCarParkList;

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
                Intent intent = AreaListActivity.createIntent(ManagerActivity.this, carPark.getId());
                startActivity(intent);
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
        Account account = AccountHelper.get(this);
        CarParkClient client = ServiceGenerator.createService(CarParkClient.class);
        Call<CarParkPackage> call = client.getCarParkByUsername(account.getUsername());
        call.enqueue(new Callback<CarParkPackage>() {
            @Override
            public void onResponse(Call<CarParkPackage> call, Response<CarParkPackage> response) {
                CarParkPackage result = response.body();
                if (result.isSuccess()) {
                    carParks.addAll(result.getResult());
                    adapter.notifyItemRangeInserted(0, carParks.size());
                }
            }

            @Override
            public void onFailure(Call<CarParkPackage> call, Throwable t) {

            }
        });
    }
}
