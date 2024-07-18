package com.college.managerpengeluaran;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class expcatAdapter extends RecyclerView.Adapter<expcatAdapter.ViewHolder> {
    private List<modelexpcategory> expcatList;
    private Context context;

    public expcatAdapter(List<modelexpcategory> catlist, crudkategori crudkategori) {
        this.expcatList = catlist;
        this.context = crudkategori;
    }

    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tampilkategori, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder (ViewHolder holder, int position) {
        modelexpcategory cat = expcatList.get(position);
        int index = position;
        holder.laykarkategori.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alertini = new AlertDialog.Builder(context);
                alertini.setIcon(R.drawable.tongsampah);
                alertini.setTitle("Hapus Kategori?");
                alertini.setMessage("Apakah anda ingin menghapus kategori ini?");
                alertini.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int nampungid = expcatList.get(index).getId_expense();
                        new useforcrud(expcatAdapter.class, this, 0).execute(String.valueOf(nampungid));
                        expcatList.remove(index);
                        notifyItemRemoved(index);
                    }
                });
                alertini.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertini.show();
                return true;
            }
        });

        holder.namakategoriini.setText(cat.getExpense_category_name());
    }

    @Override
    public int getItemCount() {
        return expcatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView laykarkategori;
        TextView namakategoriini;

        public ViewHolder(View itemView) {
            super(itemView);
            laykarkategori = itemView.findViewById(R.id.laykarkategori);
            namakategoriini = itemView.findViewById(R.id.namakategoriini);
        }
    }
}

