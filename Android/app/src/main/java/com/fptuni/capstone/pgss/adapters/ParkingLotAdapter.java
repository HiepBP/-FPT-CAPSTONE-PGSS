package com.fptuni.capstone.pgss.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.models.Area;
import com.fptuni.capstone.pgss.models.ParkingLot;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by TrungTNM on 3/12/2017.
 */

public class ParkingLotAdapter extends RecyclerView.Adapter<ParkingLotAdapter.ViewHolder> {

    private OnItemClickListener listener;
    private Context context;
    private List<ParkingLot> lots;

    public ParkingLotAdapter(Context context, List<ParkingLot> lots) {
        this.context = context;
        this.lots = lots;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_parkinglotlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ParkingLot lot = lots.get(position);
        holder.bind(lot);
    }

    @Override
    public int getItemCount() {
        return lots.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private Context getContext() {
        return context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_parkinglotlist_name)
        TextView tvName;

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
        }

        void bind(ParkingLot lot) {
            tvName.setText(lot.getName());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
}
