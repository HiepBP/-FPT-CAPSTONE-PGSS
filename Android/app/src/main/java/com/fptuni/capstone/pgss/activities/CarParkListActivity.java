package com.fptuni.capstone.pgss.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.adapters.CarParkListAdapter;
import com.fptuni.capstone.pgss.interfaces.CarParkClient;
import com.fptuni.capstone.pgss.models.CarPark;
import com.fptuni.capstone.pgss.network.CarParkPackage;
import com.fptuni.capstone.pgss.network.GetCoordinatePackage;
import com.fptuni.capstone.pgss.network.ServiceGenerator;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarParkListActivity extends AppCompatActivity {

    private static final String EXTRA_CURRENT_LOCATION_LAT = "currentLocationLat";
    private static final String EXTRA_CURRENT_LOCATION_LON = "currentLocationLon";
    private static final String EXTRA_FROM_TARGET = "fromTarget";

    @BindView(R.id.recyclerview_carparklist_list)
    RecyclerView rvCarParkList;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private LatLng currentPostion;
    private CarParkListAdapter adapter;
    private List<CarPark> carParks;
    private String target;

    private int numberOfCar;

    public static Intent createIntent(Context context, @NonNull LatLng currentLocation, String target) {
        Intent intent = new Intent(context, CarParkListActivity.class);
        intent.putExtra(EXTRA_CURRENT_LOCATION_LAT, currentLocation.latitude);
        intent.putExtra(EXTRA_CURRENT_LOCATION_LON, currentLocation.longitude);
        intent.putExtra(EXTRA_FROM_TARGET, target);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_park_list);

        initiateFields();
        initiateViews();
        getCarParkData(currentPostion.latitude, currentPostion.longitude);
    }

    private void initiateViews() {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvCarParkList.setAdapter(adapter);
        rvCarParkList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initiateFields() {
        double lat = getIntent().getDoubleExtra(EXTRA_CURRENT_LOCATION_LAT, 0);
        double lon = getIntent().getDoubleExtra(EXTRA_CURRENT_LOCATION_LON, 0);
        currentPostion = new LatLng(lat, lon);
        target = getIntent().getStringExtra(EXTRA_FROM_TARGET);

        numberOfCar = 10;
        carParks = new ArrayList<>();
        adapter = new CarParkListAdapter(this, carParks);
        adapter.setOnItemClickListener(new CarParkListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                CarPark carPark = carParks.get(position);
                Intent intent = CarParkDetailActivity.createIntent(CarParkListActivity.this, carPark);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getCarParkData(double lat, double lon) {
        CarParkClient client = ServiceGenerator.createService(CarParkClient.class);
        Call<GetCoordinatePackage> call = client.getCoordinateNearestCarPark(lat, lon, numberOfCar);
        call.enqueue(new Callback<GetCoordinatePackage>() {
            @Override
            public void onResponse(Call<GetCoordinatePackage> call, Response<GetCoordinatePackage> response) {
                List<CarParkPackage> result = response.body().getResult();
                int curSize = carParks.size();
                carParks.clear();
                adapter.notifyItemRangeRemoved(0, curSize);
                for (CarParkPackage data : result) {
                    CarPark carPark = data.getCarPark();
                    carPark.setAvailableLot(data.getAvailableLot());
                    carPark.setAwayDistance(data.getDistance());
                    carPark.setFromTarget(target);
                    carParks.add(carPark);
                }
                adapter.notifyItemRangeInserted(0, carParks.size());
            }

            @Override
            public void onFailure(Call<GetCoordinatePackage> call, Throwable t) {

            }
        });
    }
}
