package com.college.managerpengeluaran;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class laporanAdapter extends RecyclerView.Adapter<laporanAdapter.ViewHolder>{
    private List<Transaction> transactions;
    private Context context;
    private List<modelakun> takeakun;
    private List<modelexpcategory> takecat;
    private List<modelinccategory> takeinc;
    //private static final String BASE_URL = "http://10.0.2.2:80/Expense_Manager/";
    //private static final String BASE_URL = "http://192.168.1.13/Expense_Manager/";
    private static final String BASE_URL = "https://mobilekuti2022.web.id/Expense_Manager/";

    public laporanAdapter(List<Transaction> transactions, Context context) {
        this.transactions = transactions;
        this.context = context;
    }

    @Override
    public laporanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tampilreport, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(laporanAdapter.ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        int index = position;

        holder.laykarreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog inidialog = new Dialog(context);
                inidialog.setContentView(R.layout.detailtransaksi);
                inidialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView title = inidialog.findViewById(R.id.txtnamapengeluaran);
                TextView jumlahpengeluaran = inidialog.findViewById(R.id.txtjumlahpengeluaran);
                TextView kategori = inidialog.findViewById(R.id.txtkategori);
                TextView description = inidialog.findViewById(R.id.txtdeskripsi);
                TextView date = inidialog.findViewById(R.id.txtdateandtime);
                ImageView gambar = inidialog.findViewById(R.id.txtgambar);
                TextView kuantitas = inidialog.findViewById(R.id.txtkuantitas);
                TextView paymentmethod = inidialog.findViewById(R.id.txtpaymentmethod);
                Button batal = inidialog.findViewById(R.id.btnbatal);
                //Button ubah = inidialog.findViewById(R.id.btnubah);

                String kombotanggal = transaction.getDate() + " | " + transaction.getDatetime();
                String tampungidtransaksi = String.valueOf(transaction.getId());

                title.setText(transaction.getTitle());
                jumlahpengeluaran.setText("Rp. " + String.valueOf(transaction.getAmount()));

                DatabaseHelper inidb = DatabaseHelper.getDB(context);
                takecat = inidb.AssistCat().getAllCat();
                takeinc = inidb.AssistInc().getAllCat();
                takeakun = inidb.AssistAkun().getAkun();

                if (transaction.isIncome()) {
                    boolean found = true;
                    for (int i = 0; i < takeinc.size(); i++) {
                        if (takeinc.get(i).getIncome_category_id() == transaction.getCategory()) {
                            kategori.setText(takeinc.get(i).getIncome_category_name());
                            found = true;
                            break;
                        }
                    }
                    if (!found || transaction.getCategory() == 0) {
                        kategori.setText("null");
                    }
                } else {
                    boolean found = false;
                    for (int i = 0; i < takecat.size(); i++) {
                        if (takecat.get(i).getId_expense() == transaction.getCategory()) {
                            kategori.setText(takecat.get(i).getExpense_category_name());
                            found = true;
                            break;
                        }
                    }
                    if (!found || transaction.getCategory() == 0) {
                        kategori.setText("null");
                    }
                }

                description.setText(transaction.getDescription());
                date.setText(kombotanggal);
                //gambar.setText(transaction.getImageDesc());

                // Bangun URL lengkap untuk gambar
                String imageUrl = BASE_URL + transaction.getImageDesc();
                // Load image using Picasso in the dialog
                Picasso.get()
                        .load(imageUrl)
                        //.placeholder(R.drawable.placeholder) // Add a placeholder image if needed
                        //.error(R.drawable.error) // Add an error image if needed
                        .into(gambar, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                Log.d("Picasso", "Image loaded successfully.");
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("Picasso", "Failed to load image: " + e.getMessage());
                            }
                        });

                if (transaction.isIncome()) {
                    kuantitas.setText("null");
                }else {
                    if (transaction instanceof modelekspense) {
                        modelekspense ekspense = (modelekspense) transaction;
                        kuantitas.setText(ekspense.getQuantity());
                    }
                }

                paymentmethod.setText(transaction.getPaymentMethod());

                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inidialog.dismiss();
                    }
                });

                gambar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialoggambar = new Dialog(context);
                        dialoggambar.setContentView(R.layout.detailtransaksigambar);
                        dialoggambar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        ImageView gambar = dialoggambar.findViewById(R.id.gambarfull);
                        Button btntutup = dialoggambar.findViewById(R.id.btntutup);
                        String imageUrl = BASE_URL + transaction.getImageDesc();
                        // Load image using Picasso in the dialog
                        Picasso.get()
                                .load(imageUrl)
                                //.placeholder(R.drawable.placeholder) // Add a placeholder image if needed
                                //.error(R.drawable.error) // Add an error image if needed
                                .into(gambar, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d("Picasso", "Image loaded successfully.");
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Log.e("Picasso", "Failed to load image: " + e.getMessage());
                                    }
                                });

                        btntutup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialoggambar.dismiss();
                            }
                        });
                        dialoggambar.show();
                    }
                });

                inidialog.show();
            }
        });

        String incomecolor = "#50B498";
        String expensecolor = "#EF5A6F";

        holder.title.setText(transaction.getTitle());
        holder.date.setText(transaction.getDate());
        holder.amount.setText("Rp. " + String.valueOf(transaction.getAmount()));
        holder.amount.setTextColor(transaction.isIncome() ? Color.parseColor(incomecolor) : Color.parseColor(expensecolor));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void updateData(List<Transaction> limitedTransactionList) {
        this.transactions = limitedTransactionList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView laykarreport;
        TextView title, date, amount;

        public ViewHolder(View itemView) {
            super(itemView);
            laykarreport = itemView.findViewById(R.id.laykarreport);
            title = itemView.findViewById(R.id.txtjudul);
            date = itemView.findViewById(R.id.txtdate);
            amount = itemView.findViewById(R.id.txtduit);
        }
    }
}
