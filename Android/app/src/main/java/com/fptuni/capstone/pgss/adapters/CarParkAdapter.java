package com.fptuni.capstone.pgss.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
 * Created by TrungTNM on 3/12/2017.
 */

public class CarParkAdapter extends RecyclerView.Adapter<CarParkAdapter.ViewHolder> {

    private OnItemClickListener listener;
    private Context context;
    private List<CarPark> carParks;

    public CarParkAdapter(Context context, List<CarPark> carParks) {
        this.context = context;
        this.carParks = carParks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View carPark = inflater.inflate(R.layout.item_manager, parent, false);
        return new ViewHolder(carPark);
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

    private Context getContext() {
        return context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_manager_name)
        TextView tvName;
        @BindView(R.id.textview_manager_address)
        TextView tvAddress;
        @BindView(R.id.textview_manager_available_lot)
        TextView tvAvailableLot;

        public ViewHolder(final View itemView) {
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
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemLongClick(itemView, position);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }

        void bind(CarPark carPark) {
            tvName.setText(carPark.getName());
            tvAddress.setText(carPark.getAddress());
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

        private boolean isBetween(int x, int lower, int upper) {
            return lower <= x && x <= upper;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);

        void onItemLongClick(View itemView, int position);
    }
}
