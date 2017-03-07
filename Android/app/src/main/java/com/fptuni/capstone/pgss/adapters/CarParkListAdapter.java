package com.fptuni.capstone.pgss.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.models.CarPark;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by TrungTNM on 3/7/2017.
 */

public class CarParkListAdapter extends RecyclerView.Adapter<CarParkListAdapter.ViewHolder> {

    private List<CarPark> carParks;
    private Context context;

    public CarParkListAdapter(Context context, List<CarPark> carParks) {
        this.carParks = carParks;
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View carParkView = inflater.inflate(R.layout.item_car_park_list, parent, false);

        return new ViewHolder(carParkView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CarPark carPark = carParks.get(position);

        holder.bind(carPark);
    }

    @Override
    public int getItemCount() {
        return carParks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_carparklist_name)
        TextView tvName;
        @BindView(R.id.textview_carparklist_address)
        TextView tvAddress;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(CarPark carPark) {
            tvName.setText(carPark.getName());
            tvAddress.setText(carPark.getAddress());
        }
    }
}
