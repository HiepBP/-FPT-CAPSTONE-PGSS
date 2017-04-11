package com.fptuni.capstone.pgss.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.models.Transaction;
import com.fptuni.capstone.pgss.models.TransactionStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Body;

/**
 * Created by TrungTNM on 3/9/2017.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> transactions;
    private Context context;
    private OnItemClickListener listener;

    public TransactionAdapter(Context context, List<Transaction> transactions) {
        this.transactions = transactions;
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View transactionView = inflater.inflate(R.layout.item_transaction, parent, false);

        return new ViewHolder(transactionView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        holder.bind(transaction);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private static final String DATE_FORMAT = "EEEE, dd MMMM, ''yy";

        @BindView(R.id.textview_transaction_address)
        TextView tvAddress;
        @BindView(R.id.textview_transaction_date)
        TextView tvDate;
        @BindView(R.id.textview_transaction_status)
        TextView tvStatus;
        @BindView(R.id.textview_transaction_amount)
        TextView tvAmount;
        @BindView(R.id.textview_transaction_lot_name)
        TextView tvLotName;
        @BindView(R.id.textview_transaction_pin)
        TextView tvPin;

        ViewHolder(final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int positon = getAdapterPosition();
                        if (positon != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, positon);
                        }
                    }
                }
            });
        }

        void bind(Transaction transaction) {
            tvAddress.setText(transaction.getCarPark().getAddress());
            tvDate.setText(getFormatedDate(transaction.getDate()));
            tvStatus.setText(TransactionStatus.getById(transaction.getStatus()).getName());
            tvAmount.setText(Html.fromHtml(getAmountText(transaction.getAmount())));
            tvLotName.setText(Html.fromHtml(getLotNameText(transaction.getLot().getName())));
            tvPin.setText(transaction.getTransactionCode());
        }

        private String getLotNameText(String lotName) {
            return " " + lotName;
        }

        private String getAmountText(double amount) {
            return " " + String.valueOf(amount);
        }

        private String getFormatedDate(Date date) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, new Locale("vi", "VN"));
            return dateFormat.format(date);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
}
