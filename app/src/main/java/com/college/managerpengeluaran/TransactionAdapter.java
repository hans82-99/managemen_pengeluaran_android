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
        View view = LayoutInflater.from(context).inflate(R.layout.tampilhistory, parent, false);
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

    public void updateData(List<Transaction> limitedTransactionList) {
        this.transactions = limitedTransactionList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, date, amount;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtjudul);
            description = itemView.findViewById(R.id.txtdeskripsi);
            date = itemView.findViewById(R.id.txtdate);
            amount = itemView.findViewById(R.id.txtduit);
        }
    }
}
