package com.fptuni.capstone.pgss.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.models.CarPark;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by TrungTNM on 2/26/2017.
 */

public class UserInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private View contentView;
    private TextView tvCarParkName;
    private TextView tvCarParkAddress;

    public UserInfoWindowAdapter(LayoutInflater inflater) {
        contentView = inflater.inflate(R.layout.infowindow_user, null);
        tvCarParkName = (TextView) contentView.findViewById(R.id.textview_user_car_park_name);
        tvCarParkAddress = (TextView) contentView.findViewById(R.id.textview_user_car_park_address);
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
        return contentView;
    }
}
