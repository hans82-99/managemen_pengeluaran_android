package com.college.managerpengeluaran;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AkunAdapter extends RecyclerView.Adapter<AkunAdapter.AkunViewHolder> {

    private List<Account> accountList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public AkunAdapter(List<Account> accountList, OnItemClickListener listener) {
        this.accountList = accountList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public AkunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_akun, parent, false);
        return new AkunViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AkunViewHolder holder, int position) {
        Account currentItem = accountList.get(position);

        holder.accountName.setText(currentItem.getAccountName());
        holder.description.setText(currentItem.getDescription());
        //holder.initialBalance.setText(currentItem.getInitialBalance());
        holder.initialBalance.setText(String.valueOf(currentItem.getInitialBalance()));
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public static class AkunViewHolder extends RecyclerView.ViewHolder {
        public TextView accountName,description,initialBalance;

        public AkunViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            accountName = itemView.findViewById(R.id.account_name);
            description = itemView.findViewById(R.id.account_desc);
            initialBalance = itemView.findViewById(R.id.initial_balance);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
