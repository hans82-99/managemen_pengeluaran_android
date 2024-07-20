package com.college.managerpengeluaran;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AkunAdapter extends RecyclerView.Adapter<AkunAdapter.AkunViewHolder> {

    private List<Account> accountList;
    private OnItemClickListener mListener;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onEditClick(int position);  // method buat edit
        void onDeleteClick(int position); // method buat delete
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public AkunAdapter(Context context, List<Account> accountList) {
        this.context = context;
        this.accountList = accountList;
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
        holder.initialBalance.setText(String.valueOf(currentItem.getInitialBalance()));

        holder.layarakun.setOnLongClickListener(v -> {
            if (mListener != null) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Pilih Aksi");
                alertDialogBuilder.setItems(new CharSequence[]{"Edit", "Hapus"}, (dialog, which) -> {
                    if (which == 0) { // Buat Edit
                        mListener.onEditClick(position);
                    } else if (which == 1) { // Buat Delete
                        mListener.onDeleteClick(position);
                    }
                });
                alertDialogBuilder.show();
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public static class AkunViewHolder extends RecyclerView.ViewHolder {
        CardView layarakun;
        public TextView accountName, description, initialBalance;

        public AkunViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            layarakun = itemView.findViewById(R.id.layarakun);
            accountName = itemView.findViewById(R.id.account_name);
            description = itemView.findViewById(R.id.account_desc);
            initialBalance = itemView.findViewById(R.id.initial_balance);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//buat klik ambik
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
