package com.fptuni.capstone.pgss.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.models.Area;
import com.fptuni.capstone.pgss.models.CarPark;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by TrungTNM on 3/12/2017.
 */

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder> {

    private OnItemClickListener listener;
    private Context context;
    private List<Area> areas;

    public AreaAdapter(Context context, List<Area> areas) {
        this.context = context;
        this.areas = areas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_arealist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Area area = areas.get(position);
        holder.bind(area);
    }

    @Override
    public int getItemCount() {
        return areas.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private Context getContext() {
        return context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_arealist_name)
        TextView tvName;
        @BindView(R.id.textview_arealist_available_lot)
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

        void bind(Area area) {
            tvName.setText(area.getName());
            tvAvailableLot.setText(String.valueOf(area.getEmptyAmount()));
            tvAvailableLot.setTextColor(getAvailableColor(area.getEmptyAmount()));
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
