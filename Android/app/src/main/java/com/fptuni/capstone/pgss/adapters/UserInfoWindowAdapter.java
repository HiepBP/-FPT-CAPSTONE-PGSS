package com.fptuni.capstone.pgss.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.models.CarPark;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by TrungTNM on 2/26/2017.
 */

public class UserInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private View contentView;
    @BindView(R.id.textview_user_car_park_name)
    TextView tvCarParkName;
    @BindView(R.id.textview_user_car_park_address)
    TextView tvCarParkAddress;
    @BindView(R.id.textview_user_car_park_away)
    TextView tvAwayDistance;

    public UserInfoWindowAdapter(LayoutInflater inflater) {
        contentView = inflater.inflate(R.layout.infowindow_user, null);
        ButterKnife.bind(this, contentView);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        CarPark carPark = (CarPark) marker.getTag();
        tvCarParkName.setText(carPark.getName());
        tvCarParkAddress.setText(carPark.getAddress());
        tvAwayDistance.setText(getDistanceString(carPark.getAwayDistance(), carPark.getFromTarget()));
        return contentView;
    }

    private String getDistanceString(double distance, String target) {
        DecimalFormat distanceInKmFormat = new DecimalFormat("#.##");
        String text = "Cách [t] bán kính [r] kms";
        return text.replace("[r]", distanceInKmFormat.format(distance / 1000))
                .replace("[t]", target);
    }
}
