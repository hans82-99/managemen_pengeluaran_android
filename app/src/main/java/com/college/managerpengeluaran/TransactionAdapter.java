package com.college.managerpengeluaran;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transaction> transactions;
    private Context context;

    public TransactionAdapter(List<Transaction> transactions, Context context) {
        this.transactions = transactions;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.title.setText(transaction.getTitle());
        holder.description.setText(transaction.getDescription());
        holder.date.setText(transaction.getDate());
        holder.amount.setText(String.valueOf(transaction.getAmount()));
        holder.amount.setTextColor(transaction.isIncome() ? Color.GREEN : Color.RED);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, date, amount;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.transaction_title);
            description = itemView.findViewById(R.id.transaction_description);
            date = itemView.findViewById(R.id.transaction_date);
            amount = itemView.findViewById(R.id.transaction_amount);
        }
    }
}
