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

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by TrungTNM on 3/7/2017.
 */

public class CarParkListAdapter extends RecyclerView.Adapter<CarParkListAdapter.ViewHolder> {

    private List<CarPark> carParks;
    private Context context;
    private OnItemClickListener listener;

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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_carparklist_name)
        TextView tvName;
        @BindView(R.id.textview_carparklist_address)
        TextView tvAddress;
        @BindView(R.id.textview_carparklist_distance)
        TextView tvDistance;
        @BindView(R.id.textview_carparklist_available_lot)
        TextView tvAvailableLot;

        ViewHolder(final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }

        void bind(CarPark carPark) {
            tvName.setText(carPark.getName());
            tvAddress.setText(carPark.getAddress());
            tvDistance.setText(getDistanceString(carPark.getAwayDistance(), carPark.getFromTarget()));
            tvAvailableLot.setText(String.valueOf(carPark.getAvailableLot()));
            tvAvailableLot.setTextColor(getAvailableColor(carPark.getAvailableLot()));
        }

        private int getAvailableColor(int availableLot) {
            int id;
            if (isBetween(availableLot, 0, 0)) {
                id = R.color.colorShortAvailable;
            } else if (isBetween(availableLot, 1, 10)) {
                id = R.color.colorAverageAvailable;
            } else {
                id = R.color.colorPlentifulAvailable;
            }

            Context context = getContext();
            return context.getResources().getColor(id);
        }

        private String getDistanceString(double distance, String target) {
            Context context = getContext();
            DecimalFormat distanceInKmFormat = new DecimalFormat("#.##");
            return distanceInKmFormat.format(distance / 1000) +
                    context.getString(R.string.carparklist_text_distance) + " " +
                    target;
        }

        private boolean isBetween(int x, int lower, int upper) {
            return lower <= x && x <= upper;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
}
